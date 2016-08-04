import java.util.ArrayList;

public class Board {
	private final static int BOARD_SIZE = 8;
	private Piece[][] board;
	LogWriter writer;
	DirectiveHandler handler;

	public Board(LogWriter writer) {
		this.writer = writer;
		board = new Piece[BOARD_SIZE][BOARD_SIZE];
		handler = new DirectiveHandler();
	}

	public void addNewPiece(String placement) {
		Position position = new Position(handler.getInitialRank(placement, false),
				handler.getInitialFile(placement, false));
		boolean isWhite = handler.isWhite(placement);
		char piece = handler.getPieceChar(placement, isWhite);
		board[position.getRank()][position.getFile()] = handler.getPiece(piece, position, isWhite);
	}

	public boolean movePiece(String placement, boolean isWhite) {
		boolean sucessfulMove = false;
		char piece = handler.getPieceChar(placement, isWhite);
		Position position1 = new Position(handler.getInitialRank(placement, true),
				handler.getInitialFile(placement, true));
		Position position2 = new Position(handler.getSecondaryRank(placement), handler.getSecondaryFile(placement));

		if (board[position1.getRank()][position1.getFile()] != null) {
			if (isValid(position1, position2, isWhite, placement, piece)) {
				Piece p = getPiece(position1);
				if(isValidPieceMovement(handler.isCapture(placement), p, position2)){
				board[position1.getRank()][position1.getFile()] = null;
				p.setHasMoved();
				p.setCurrentPosition(position2);
				board[position2.getRank()][position2.getFile()] = p;
				sucessfulMove = true;
				}
			}
		}
		return sucessfulMove;
	}

	public void castle(boolean isWhite, String castle) {
		boolean isKingSide = handler.isKingSide(castle);
		Rook rook = getRook(isWhite, isKingSide);
		King king = getKing(isWhite);
		moveKingForCastle(king, isWhite, isKingSide);
		moveRookForCastle(rook, isWhite, isKingSide);

	}

	public boolean isValidCastle(String castle, boolean isWhite) {
		boolean valid = false;
		boolean isKingSide = handler.isKingSide(castle);
		Position rookPos = getRookPosition(isWhite, isKingSide);
		Position kingPos = getKingPosition(isWhite);
		Piece king = board[kingPos.getRank()][kingPos.getFile()];
		Piece rook = board[rookPos.getRank()][rookPos.getFile()];
		if ((rook != null && king != null) && (rook.getType() == PieceType.ROOK && king.getType() == PieceType.KING)) {
			if (!king.hasMoved() || !rook.hasMoved()) {
				if (!middleGroundOccupied(kingPos, rookPos, isKingSide)) {
					valid = true;
				}
			}
		}
		return valid;
	}

	public void writeBoard() {
		for (int i = BOARD_SIZE - 1; i >= 0; --i) {
			String boardString = "";
			for (int j = 0; j < BOARD_SIZE; ++j) {
				if (j == 0) {
					boardString += (i + 1) + "|" + getPieceStringForBoard(i, j);
				} else {
					boardString += "|" + getPieceStringForBoard(i, j);
				}
				if (j == 7) {
					boardString += "|";
					writer.writeToFile(boardString);
				}
			}
		}
		writer.writeToFile("  A B C D E F G H ");
	}

	private String getPieceStringForBoard(int y, int x) {
		Piece p = board[y][x];
		return (p == null ? " " : (p.isWhite() ? "" + p.getType().getWhiteType() : "" + p.getType().getBlackType()));
	}

	public boolean isCapture(String directive) {
		return directive.contains("x");
	}

	public boolean isOccupied(Position position) {
		return board[position.getRank()][position.getFile()] != null;
	}

	private boolean isValid(Position position1, Position position2, boolean isWhiteTurn, String placement, char piece) {
		boolean valid = (isCorrectPiece(piece, position1, isWhiteTurn));
		if (isOccupied(position2) && valid) {
			valid = (isCapture(placement) && !isPlayerPiece(isWhiteTurn, position2));
		} else if (!isOccupied(position2) && !isCapture(placement) && valid) {
			valid = true;
		} else {
			valid = false;
		}
		return valid;
	}

	public boolean isPlayerPiece(boolean isWhiteTurn, Position position) {
		return ((board[position.getRank()][position.getFile()].isWhite() && isWhiteTurn)
				|| (!board[position.getRank()][position.getFile()].isWhite() && !isWhiteTurn));
	}

	public boolean isCorrectPiece(char piece, Position position, boolean isWhiteTurn) {
		char p = board[position.getRank()][position.getFile()].getType().getWhiteType();
		boolean isCorrect = ((board[position.getRank()][position.getFile()].isWhite() && isWhiteTurn)
				|| (!board[position.getRank()][position.getFile()].isWhite() && !isWhiteTurn));
		return (piece == p && isCorrect);
	}

	private Position getRookPosition(boolean isWhite, boolean isKingSide) {
		Position rookPos;
		if (isWhite) {
			rookPos = (isKingSide ? new Position(0, 7) : new Position(0, 0));
		} else {
			rookPos = (isKingSide ? new Position(7, 7) : new Position(7, 0));
		}
		return rookPos;
	}

	private Position getKingPosition(boolean isWhite) {
		return (isWhite ? new Position(0, 4) : new Position(7, 4));
	}

	private Rook getRook(boolean isWhite, boolean isKingSide) {
		Position rookPos;
		if (isWhite) {
			rookPos = (isKingSide ? new Position(0, 7) : new Position(0, 0));
		} else {
			rookPos = (isKingSide ? new Position(7, 7) : new Position(7, 0));
		}
		Rook rook = (Rook) board[rookPos.getRank()][rookPos.getFile()];
		board[rookPos.getRank()][rookPos.getFile()] = null;
		return rook;
	}

	private King getKing(boolean isWhite) {
		Position kingPos = (isWhite ? new Position(0, 4) : new Position(7, 4));
		King king = (King) board[kingPos.getRank()][kingPos.getFile()];
		board[kingPos.getRank()][kingPos.getFile()] = null;
		return king;
	}

	private boolean middleGroundOccupied(Position kingPos, Position rookPos, boolean isKingSide) {
		boolean occupied = false;
		if (isKingSide) {
			for (int i = kingPos.getFile() + 1; i < rookPos.getFile(); ++i) {
				if (board[kingPos.getRank()][i] != null) {
					occupied = true;
				}
			}
		} else {
			for (int i = kingPos.getFile() - 1; i > rookPos.getFile(); --i) {
				if (board[kingPos.getRank()][i] != null) {
					occupied = true;
				}
			}
		}
		return occupied;
	}

	private void moveKingForCastle(King king, boolean isWhite, boolean isKingSide) {
		Position newKing = (isKingSide ? king.getKingSide() : king.getQueenSide());
		king.setCurrentPosition(newKing);
		king.setHasMoved();
		board[newKing.getRank()][newKing.getFile()] = king;
	}

	private void moveRookForCastle(Rook rook, boolean isWhite, boolean isKingSide) {
		Position newRook = (isKingSide ? rook.getKingSide() : rook.getQueenSide());
		rook.setCurrentPosition(newRook);
		rook.setHasMoved();
		board[newRook.getRank()][newRook.getFile()] = rook;
	}

	public Piece getPiece(Position p) {
		return board[p.getRank()][p.getFile()];
	}

	private boolean isValidPieceMovement(boolean isCapture, Piece p, Position p2) {
		ArrayList<Position> possiblePositions = p.getMovement(board, isCapture);
		boolean found = false;
		for (Position pos : possiblePositions) {
			if (pos.equals(p2)) {
				found = true;
			}
		}
		return found;
	}
}
