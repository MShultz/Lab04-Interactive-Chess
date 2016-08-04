
public enum PieceType {
KING('K', 'k'), QUEEN('Q', 'q'), ROOK('R', 'r'), KNIGHT('N', 'n'), BISHOP('B', 'b'), PAWN('P', 'p');
	private char whiteType;
	private char blackType;
	
	private PieceType(char whiteType, char blackType){
		this.whiteType = whiteType;
		this.blackType = blackType;
	}
	public char getWhiteType() {
		return whiteType;
	}

	public char getBlackType() {
		return blackType;
	}

}
