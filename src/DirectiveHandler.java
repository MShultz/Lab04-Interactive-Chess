import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DirectiveHandler {
	Pattern piece = Pattern.compile("[KQRNB]");
	private final static int CHAR_CONVERT_NUM = 48;

	public Piece getPiece(char piece, Position p, boolean isWhite) {
		Piece newPiece;
		switch (piece) {
		case 'K':
			newPiece = new King(PieceType.KING, isWhite, p);
			break;
		case 'Q':
			newPiece = new Queen(PieceType.QUEEN, isWhite, p);
			break;
		case 'R':
			newPiece = new Rook(PieceType.ROOK, isWhite, p);
			break;
		case 'B':
			newPiece = new Bishop(PieceType.BISHOP, isWhite, p);
			break;
		case 'N':
			newPiece = new Knight(PieceType.KNIGHT, isWhite, p);
			break;
		case 'P':
		default:
			newPiece = new Pawn(PieceType.PAWN, isWhite, p);
			break;
		}
		return newPiece;
	}

	public int getInitialRank(String directive, boolean isMovement) {
		Matcher match = piece.matcher(directive);
		String rank = "";
		int pawnLoc = 1;
		if (!isMovement) {
			rank += directive.charAt(directive.length() - 1);
		} else if (!match.find()) {
			rank += directive.charAt(pawnLoc);
		} else {
			rank += directive.charAt(pawnLoc + 1);
		}
		return Integer.parseInt(rank) - 1;
	}

	public int getSecondaryRank(String directive) {
		Matcher match = piece.matcher(directive);
		String rank = "";
		int pawnLoc = 4;
		if (!match.find()) {
			rank += directive.charAt(pawnLoc);
		} else {
			rank += directive.charAt(pawnLoc + 1);
		}
		return Integer.parseInt(rank) -1;
	}

	public int getInitialFile(String directive, boolean isMovement) {
		Matcher match = piece.matcher(directive);
		char file;
		int pawnLoc = 0;
		if (!isMovement) {
			file = directive.charAt(directive.length() - 2);
		} else if (!match.find()) {
			file = directive.charAt(pawnLoc);
		} else {
			file = directive.charAt(pawnLoc + 1);
		}
		return Character.getNumericValue(file - CHAR_CONVERT_NUM)-1;
	}

	public int getSecondaryFile(String directive) {
		Matcher match = piece.matcher(directive);
		char file;
		int pawnLoc = 3;
		if (!match.find()) {
			file = directive.charAt(pawnLoc);
		} else {
			file = directive.charAt(pawnLoc + 1);
		}
		return Character.getNumericValue(file - CHAR_CONVERT_NUM)-1;
	}

	public boolean isCapture(String directive) {
		return directive.contains("x");
	}

	public boolean isWhite(String directive) {
		return directive.contains("l");
	}

	public char getPieceChar(String directive, boolean isWhiteTurn) {
		char pieceChar;
		Matcher match = piece.matcher(directive);
		if (match.find()) {
			pieceChar = directive.charAt(0);
		} else {
			pieceChar = 'P';
		}
		return pieceChar;
	}

	public boolean isKingSide(String castle){
		return (castle.trim().equals("O-O-O")? false:true);
	}
}
