package maze;

public class Maze {

	public int nBSteps = 1;
	public char[] structure;
	public float width;
	public float height;
	
	public Maze(char[] structure, float width, float height,int nBSteps) {
		this.structure = structure;
		this.width = width;
		this.height = height;
		this.nBSteps = nBSteps;
	}

	public Maze(char[] structure, float width, float height) {
		this.structure = structure;
		this.width = width;
		this.height = height;
	}
	
	
	
}
