import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Translator {
	LogWriter writer;
	OutputFormatter format;
	DirectiveFinder finder;
	boolean movementBegun = false;
	BufferedReader file = null;
	Board board;
	DirectiveHandler handler;
	boolean interactionMode = false;

	public Translator(String fileName, boolean containedFile) {
		writer = new LogWriter();
		writer.writeToFile("Process: Log file Initialized.");
		if (containedFile) {
			if (initializeReader(fileName)) {
				writer.writeToFile("Process: Sucessfully opened file [" + fileName + "]");

			} else {
				writer.writeToFile(
						"Error: There was a problem with the file you entered. Reverting to Interaction Mode.");
				interactionMode = true;
			}
		} else {
			writer.writeToFile("Process: You entered no filepath. The program will now revert to Interaction Mode.");
			interactionMode = true;
		}
		format = new OutputFormatter();
		finder = new DirectiveFinder();
		handler = new DirectiveHandler();
		board = new Board(writer);
	}

	public void translate() {
		if (!interactionMode) {
			translateFile();
		} else {
			interactionMode();
		}
	}

	private void interactionMode(){
		setUpBoard();
		board.writeBoard();
	}
	
	private void setUpBoard(){
		BufferedReader initializer;
		try {
			FileInputStream inputStream = new FileInputStream("src/BoardInitialization.chess");
			initializer = new BufferedReader(new InputStreamReader(inputStream));
			while(initializer.ready()){
				processPlacement(initializer.readLine().trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private void translateFile() {
		try {
			while (file.ready()) {
				String currentLine = getCurrentLine().trim();
				if (finder.containsComment(currentLine)) {
					currentLine = finder.removeComment(currentLine).trim();
				}
				if (currentLine.trim().length() > 0) {
					if (finder.isPlacement(currentLine)) {
						processPlacement(currentLine);
					} else if (finder.isMovement(currentLine)) {
						processMovement(currentLine);
					} else if (finder.containsCastle(currentLine)) {
						processCastling(currentLine);

					} else {
						writer.writeToFile(format.getIncorrect(currentLine));
					}
				}
			}
			board.writeBoard();
			shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean initializeReader(String fileName) {
		FileInputStream inputStream;
		boolean successful = true;
		try {
			inputStream = new FileInputStream(fileName);
			file = new BufferedReader(new InputStreamReader(inputStream));
		} catch (FileNotFoundException e) {
			successful = false;
		}
		return successful;
	}

	private String getCurrentLine() {
		String currentLine = null;
		try {
			currentLine = file.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return currentLine;
	}

	private void processPlacement(String currentLine) {
		if (movementBegun) {
			writer.writeToFile("Warning: Skipping [" + currentLine + "]. Movement has begun.");
		} else {
			String placement = finder.getPlacementDirective(currentLine);
			board.addNewPiece(placement);
			String placement1 = "Placement: Adding [" + placement + "] " + format.formatPlacement(placement);
			writer.writeToFile(placement1);
		}
	}

	private void processMovement(String currentLine) throws Exception {
		if (!movementBegun) {
			movementBegun = true;
		}
		ArrayList<String> movements = finder.getMovementDirectives(currentLine);
		boolean movement1Valid = board.movePiece(movements.get(0), true);
		if (movement1Valid) {
			writer.writeToFile(format.formatMovement(movements.get(0), true));
			board.writeBoard();
		} else {
			writeFirstMovementError(movements.get(0));
		}
		boolean movement2Valid = board.movePiece(movements.get(1), false);
		if (movement2Valid) {
			writer.writeToFile(format.formatMovement(movements.get(1), false));
			board.writeBoard();
		} else {
			writeSecondMovementError(movements.get(1));
		}
	}

	private void processCastling(String currentLine) throws Exception {
		ArrayList<String> lineAction = finder.getLineAction(currentLine);
		if (lineAction.get(0) != null && lineAction.get(1) != null) {
			if (finder.containsSingleMovement(currentLine)) {
				if (lineAction.size() == 2) {
					if (finder.isCastle(lineAction.get(0))) {
						if (board.isValidCastle(lineAction.get(0), true)) {
							board.castle(true, lineAction.get(0));
							writer.writeToFile(format.formatCastle(lineAction.get(0), true));
						} else {
							writer.writeToFile("This castle is impossible at this time.");
						}
					} else {
						if (board.movePiece(lineAction.get(0), true)) {
							writer.writeToFile(format.formatMovement(lineAction.get(0), true));
						} else {
							writeFirstMovementError(lineAction.get(0));
						}
					}
					if (finder.isCastle(lineAction.get(1))) {
						if (board.isValidCastle(lineAction.get(1), false)) {
							board.castle(false, lineAction.get(1));
							writer.writeToFile(format.formatCastle(lineAction.get(1), false));
						} else {
							writer.writeToFile("This castle is impossible at this time.");
						}
					} else {
						if (board.movePiece(lineAction.get(1), false)) {
							writer.writeToFile(format.formatMovement(lineAction.get(1), false));
						} else {
							writeSecondMovementError(lineAction.get(1));
						}
					}
				}
			} else {
				if (board.isValidCastle(lineAction.get(0), true)) {
					board.castle(true, lineAction.get(0));
					writer.writeToFile(format.formatCastle(lineAction.get(0), true));
				} else {
					writer.writeToFile("This castle is impossible at this time.");
				}
				if (board.isValidCastle(lineAction.get(1), false)) {
					board.castle(false, lineAction.get(1));
					writer.writeToFile(format.formatCastle(lineAction.get(1), false));
				} else {
					writer.writeToFile("This castle is impossible at this time.");
				}
			}
		} else {
			writer.writeToFile(format.getIncorrect(currentLine));
		}
	}

	public void shutdown() {
		try {
			writer.writeToFile("Process: Closing Files.");
			file.close();
			writer.closeLogFile();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void writeFirstMovementError(String movement) {
		Position pos1 = new Position(handler.getInitialRank(movement, true), handler.getInitialFile(movement, true));
		Position pos2 = new Position(handler.getSecondaryRank(movement), handler.getSecondaryFile(movement));
		String s = format.formatInvalidMovement(board, pos1, pos2, true, movement,
				handler.getPieceChar(movement, true));
		writer.writeToFile(s);
	}

	private void writeSecondMovementError(String movement) {
		Position pos1 = new Position(handler.getInitialRank(movement, true), handler.getInitialFile(movement, true));
		Position pos2 = new Position(handler.getSecondaryRank(movement), handler.getSecondaryFile(movement));
		String s = format.formatInvalidMovement(board, pos1, pos2, false, movement,
				handler.getPieceChar(movement, false));
		writer.writeToFile(s);
	}

}
