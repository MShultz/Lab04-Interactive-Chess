import java.util.ArrayList;
import java.util.Scanner;

public class UserInterface {
	private final static int CHAR_CONVERT_NUM = 65;
	Scanner scan = new Scanner(System.in);

	public void inform(boolean isWhite) {
		System.out.println("Choose your movement, " + (isWhite ? "white: " : "black: "));
	}

	private void promptForPiece(ArrayList<Piece> pieces) {
		System.out.println("0. Exit");
		int count = 1;
		for (Piece p : pieces) {
			System.out.println(count + ". " + getPieceString(p));
			++count;
		}
	}

	private void promptForMove(ArrayList<Position> possibleMoves) {
		System.out.println("0. Exit");
		System.out.println("1. Go Back");
		int count = 2;
		for (Position p : possibleMoves) {
			if(p.getFile() == -1)
				System.out.println(count + ". castle Queen Side");
			else if(p.getFile() == 8)
				System.out.println(count + ". Castle King side");
			else
			System.out.println(count + ". " + getMoveString(p));
			++count;
		}
	}

	public void ensureQuit() {
		System.out.println("Are you sure?");
		System.out.println("0. No");
		System.out.println("1. Yes");

	}

	private int getChoice(int range) {
		boolean valid = false;
		int num = -1;
		while (!valid) {
			System.out.print("Input the number of your choice: ");
			String s = scan.nextLine().replaceAll("[^\\d]", "");
			if (s.length() > 0)
				num = Integer.parseInt(s);
			if (num >= 0 && num <= range) {
				valid = true;
			} else {
				System.out.println("That is not a valid input.");
			}
		}
		return num;
	}

	private void ensurePiece(Piece p) {
		System.out.println("Was this your choice? " + getPieceString(p));
		System.out.println("0. No");
		System.out.println("1. Yes");

	}

	private void ensureMove(Position p) {
		if(p.file == -1)
			System.out.println("Was this your choice? Castling queen side");
		else if(p.file == 8)
			System.out.println("Was this your choice? Castling king side");
		else
		System.out.println("Was this your choice? " + getMoveString(p));
		System.out.println("0. No");
		System.out.println("1. Yes");
	}

	private String getPieceString(Piece p) {
		Position current = p.getCurrentPosition();
		char fileLetter = getFileLetter(current.getFile());
		String piece = (p.isWhite() ? "White " : "Black ") + p.getType().toString().toLowerCase();
		return piece + " on " + fileLetter + (current.getRank() + 1);
	}

	private String getMoveString(Position p) {
		return ("" + getFileLetter(p.getFile()) + (p.getRank()+1));
	}

	public char getFileLetter(int file) {
		int i = ((file) + CHAR_CONVERT_NUM);
		return (char) i;
	}

	public int determinePiece(ArrayList<Piece> pieces) {
		boolean correctPiece = false;
		int piece;
		do {
			promptForPiece(pieces);
			piece = getChoice(pieces.size());
			if (piece == 0) {
				ensureQuit();
				int choice = getChoice(1);
				if (choice == 1)
					correctPiece = true;
			} else {
				ensurePiece(pieces.get(piece - 1));
				int choice = getChoice(1);
				if (choice == 1) {
					correctPiece = true;
				}
			}
		} while (!correctPiece);
		return piece;
	}

	public int determineMove(ArrayList<Position> moves) {
		boolean correctMove = false;
		int move;
		do {
			promptForMove(moves);
			move = getChoice(moves.size()+1);
			if (move == 0) {
				ensureQuit();
				int choice = getChoice(1);
				if (choice == 1)
					correctMove = true;
			}else if(move == 1){
				correctMove = true;
			}
			else {
				ensureMove(moves.get(move - 2));
				int choice = getChoice(1);
				if (choice == 1) {
					correctMove = true;
				}
			}
		} while (!correctMove);
		return move;
	}

}
