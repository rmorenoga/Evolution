package maze;

public class Maze {

	public int nBSteps = 1;
	public char[] structure;
	public float width;
	public float height;
	protected float heightSet;
	protected float widthSet;
	
	public Maze(char[] structure, float width, float height,int nBSteps) {
		this.structure = structure;
		this.width = width;
		this.height = height;
		this.heightSet = height;
		this.widthSet = width;
		this.nBSteps = nBSteps;
	}

	public Maze(char[] structure, float width, float height) {
		this.structure = structure;
		this.width = width;
		this.height = height;
		this.heightSet = height;
		this.widthSet = width;
	}
	
	public void nextNoisyDimensions(float randomPercent){
		this.height = randomWithRange(this.heightSet-(this.heightSet*randomPercent), this.heightSet+(this.heightSet*randomPercent));
		this.width = randomWithRange(this.widthSet-(this.widthSet*randomPercent),this.widthSet+(this.widthSet*randomPercent));
	}
	
	private float randomWithRange(float min, float max) {
		double range = Math.abs(max - min);
		return (float) (Math.random() * range) + (min <= max ? min : max);
	}
	
	
	
	
}
