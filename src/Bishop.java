import java.util.ArrayList;

public class Bishop extends Piece {

	public Bishop(PieceType type, boolean isWhite, Position p) {
		super(type, isWhite, p);
	}

	@Override
	public ArrayList<Position> getMovement(Piece[][] board, boolean isCapture) {
		ArrayList<Position> allPossible = new ArrayList<Position>();
		allPossible.addAll(getPossibleMovements(board, isCapture, -1, 1));
		allPossible.addAll(getPossibleMovements(board, isCapture, -1, -1)); 
		allPossible.addAll(getPossibleMovements(board, isCapture, 1, 1)); 
		allPossible.addAll(getPossibleMovements(board, isCapture, 1, -1)); 
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
				if (isCapture && board[newPos.getRank()][newPos.getFile()].isWhite() != this.isWhite()) {
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
