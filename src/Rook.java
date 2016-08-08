import java.util.ArrayList;

public class Rook extends Piece {

	public Rook(PieceType type, boolean isWhite, Position p) {
		super(type, isWhite, p);
	}

	public Position getQueenSide() {
		Position location;
		if (isWhite()) {
			location = new Position(0, 3);
		} else {
			location = new Position(7, 3);
		}
		return location;
	}

	public Position getKingSide() {
		Position location;
		if (isWhite()) {
			location = new Position(0, 5);
		} else {
			location = new Position(7, 5);
		}
		return location;
	}

	@Override
	public ArrayList<Position> getMovement(Piece[][] board, boolean isCapture) {
		ArrayList<Position> allPossible = new ArrayList<Position>();
		allPossible.addAll(getPossibleMovements(board, isCapture, 1, 0)); //north
		allPossible.addAll(getPossibleMovements(board, isCapture, -1, 0)); //south
		allPossible.addAll(getPossibleMovements(board, isCapture, 0, 1)); //west
		allPossible.addAll(getPossibleMovements(board, isCapture, 0, -1)); //east
		return allPossible;
	}

	private ArrayList<Position> getPossibleMovements(Piece[][] board, boolean isCapture, int rankMultiplier,
			int fileMultiplier) {
		Position p = this.getCurrentPosition();
		boolean endFound = false;
		ArrayList<Position> positions = new ArrayList<Position>();
		Position newPos = new Position(p.getRank() + (1 * rankMultiplier), p.getFile() + (1 * fileMultiplier));
		while (newPos.isValid() && !endFound) {
			if (board[newPos.getRank()][newPos.getFile()] != null) {
				if (isCapture && this.isWhite() != board[newPos.getRank()][newPos.getFile()].isWhite()) {
					positions.add(newPos);
				}
				endFound = true;
			} else if (board[newPos.getRank()][newPos.getFile()] == null) {
				positions.add(newPos);
			}
			newPos = new Position(newPos.getRank()+(1 * rankMultiplier), newPos.getFile() + (1 * fileMultiplier));
		}
		return positions;
	}
}
