
public class Position {
	int rank;
	int file;
	
	public Position(int rank, int file){
		this.rank = rank;
		this.file = file;
	}
	public int getRank() {
		return rank;
	}

	public int getFile() {
		return file;
	}
	public boolean isValid(){
		return (rank >= 0 && rank < 8 && file >= 0 && file < 8);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + file;
		result = prime * result + rank;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (file != other.file)
			return false;
		if (rank != other.rank)
			return false;
		return true;
	}

}
