import java.util.ArrayList;

public class Knight extends Piece {
	private Position[] changeInPosition = { new Position(2, 1), new Position(2, -1), new Position(-2, 1),
			new Position(-2, -1), new Position(-1, 2), new Position(1, 2), new Position(-1, -2), new Position(1, -2) };

	public Knight(PieceType type, boolean isWhite, Position p) {
		super(type, isWhite, p);
	}

	@Override
	public ArrayList<Position> getMovement(Piece[][] board, boolean isCapture) {
		Position p = this.getCurrentPosition();
		ArrayList<Position> possiblePositions = new ArrayList<Position>();
		for (Position pos : changeInPosition) {
			Position newPos = new Position(p.getRank() + pos.getRank(), p.getFile() + pos.getFile());
			if (newPos.isValid()) {
				if ((isCapture && board[newPos.getRank()][newPos.getFile()] != null
						&& this.isWhite() != board[newPos.getRank()][newPos.getFile()].isWhite())
						|| (board[newPos.getRank()][newPos.getFile()] == null)) {
					possiblePositions.add(newPos);
				}
			}
		}
		return possiblePositions;
	}

}
