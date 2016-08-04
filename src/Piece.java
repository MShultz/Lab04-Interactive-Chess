import java.util.ArrayList;

public abstract class Piece {
	private boolean hasMoved;
	private PieceType type;
	private boolean isWhite;
	private Position currentPosition;
	
	public Piece(PieceType type, boolean isWhite, Position currentPosition){
		this.type = type;
		this.isWhite = isWhite;
		this.currentPosition = currentPosition;
		hasMoved = false;
	}
	public boolean hasMoved() {
		return hasMoved;
	}
	public PieceType getType() {
		return type;
	}
	public boolean isWhite() {
		return isWhite;
	}
	public Position getCurrentPosition(){
		return currentPosition;
	}
	public void setCurrentPosition(Position p){
		this.currentPosition = p;
	}
	public void setHasMoved(){
		hasMoved = true;
	}
	@Override
	public String toString() {
		return "" + (isWhite? type.getWhiteType(): type.getBlackType());
	}
	
	public abstract ArrayList<Position> getMovement(Piece[][] board, boolean isCapture);	
}
