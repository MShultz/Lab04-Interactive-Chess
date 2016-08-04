import java.util.ArrayList;

public class Queen extends Piece {

	public Queen(PieceType type, boolean isWhite, Position p) {
		super(type, isWhite, p);
	}

	@Override
	public ArrayList<Position> getMovement(Piece[][] board, boolean isCapture) {
		ArrayList<Position> allPossible = new ArrayList<Position>();
		Rook r = new Rook(PieceType.ROOK, this.isWhite(), this.getCurrentPosition());
		Bishop b = new Bishop(PieceType.BISHOP, this.isWhite(), this.getCurrentPosition());
		allPossible.addAll(r.getMovement(board, isCapture));
		allPossible.addAll(b.getMovement(board, isCapture));
		return allPossible;
	}

}
