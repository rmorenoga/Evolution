package maze;

public class SelectableMaze extends Maze{
	
	protected char[][] structures;
	protected int selection = 0;
	
	public SelectableMaze(char[][] structures,int selection, float width, float height,int nBSteps){
		super(structures[selection],width,height,nBSteps);
		this.structures = structures;
		this.selection = selection;
	}
	
	public SelectableMaze(char[][] structures, int selection, float width, float height) {
		super(structures[selection],width,height);
		this.structures = structures;
		this.selection = selection;
	}
	
	public void selectMaze(int selection){
		this.selection = selection;
		this.structure = this.structures[selection];
	}
	
	public void selectNextMaze(){
		selection++;
		if (selection >= structures.length)
			selection = 0;
		this.structure = structures[selection];
	}
	
}
