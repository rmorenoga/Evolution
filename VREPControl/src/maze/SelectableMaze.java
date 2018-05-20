package maze;

public class SelectableMaze extends Maze{
	
	protected char[][] structures;
	protected int selection = 0;
	protected float heightSet;
	protected float widthSet;
	
	public SelectableMaze(char[][] structures,int selection, float width, float height,int nBSteps){
		super(structures[selection],width,height,nBSteps);
		this.structures = structures;
		this.selection = selection;
		this.heightSet = height;
		this.widthSet = width;
	}
	
	public SelectableMaze(char[][] structures, int selection, float width, float height) {
		super(structures[selection],width,height);
		this.structures = structures;
		this.selection = selection;
		this.heightSet = height;
		this.widthSet = width;
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
	
	public void nextNoisyDimensions(float randomPercent){
		this.height = randomWithRange(this.heightSet-(this.heightSet*randomPercent), this.heightSet+(this.heightSet*randomPercent));
		this.width = randomWithRange(this.widthSet-(this.widthSet*randomPercent),this.widthSet+(this.widthSet*randomPercent));
	}
	
	/**
	 * Returns a random floating point number between min and max
	 * 
	 * @param min
	 *            the min of the interval
	 * @param max
	 *            the max of the interval
	 * @return a random floating point number
	 */
	float randomWithRange(float min, float max) {
		double range = Math.abs(max - min);
		return (float) (Math.random() * range) + (min <= max ? min : max);
	}
	

}
