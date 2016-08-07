import java.util.ArrayList;

public class Pawn extends Piece {

	public Pawn(PieceType type, boolean isWhite, Position p) {
		super(type, isWhite, p);
	}

	public ArrayList<Position> getMovement(Piece[][] board, boolean isCapture) {
		ArrayList<Position> movements = getPossibleMovements(board);
		movements.addAll(getPossibleCaptures(board));
		return movements;

	}

	private ArrayList<Position> getPossibleCaptures(Piece[][] board) {
		ArrayList<Position> captures = new ArrayList<Position>();
		int multiplier = (this.isWhite() ? 1 : -1);
		Position p = this.getCurrentPosition();
		if (p.getFile() - 1 >= 0 && p.getRank() + 1 < 8 && p.getRank() - 1 >= 0)
			if (board[p.getRank() + (1 * multiplier)][p.getFile() - 1] != null) {
				if (board[p.getRank() + (1 * multiplier)][p.getFile() - 1].isWhite() != this.isWhite())
					captures.add(new Position(p.getRank() + (1 * multiplier), p.getFile() - 1));
			}
		if (p.getFile() + 1 < 8 && p.getRank() + 1 < 8 && p.getRank() - 1 >= 0)
			if (board[p.getRank() + (1 * multiplier)][p.getFile() + 1] != null) {
				if (board[p.getRank() + (1 * multiplier)][p.getFile() + 1].isWhite() != this.isWhite())
					captures.add(new Position(p.getRank() + (1 * multiplier), p.getFile() + 1));
			}
		return captures;
	}

	private ArrayList<Position> getPossibleMovements(Piece[][] board) {
		ArrayList<Position> movements = new ArrayList<Position>();
		Position p = this.getCurrentPosition();
		int multiplier = (this.isWhite() ? 1 : -1);
		if (board[p.getRank() + (1 * multiplier)][p.getFile()] == null) {
			movements.add(new Position(p.getRank() + (1 * multiplier), p.getFile()));
			if (!hasMoved() && board[p.getRank() + (2 * multiplier)][p.getFile()] == null) {
				movements.add(new Position(p.getRank() + (2 * multiplier), p.getFile()));
			}
		}
		return movements;
	}

}
