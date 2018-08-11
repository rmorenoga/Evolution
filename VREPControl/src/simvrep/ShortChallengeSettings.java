package simvrep;

public class ShortChallengeSettings extends SimulationSettings{
	
	protected float[] times;
	protected float[] fractions;
	protected int selection = 0;
	
	public ShortChallengeSettings(float[] times, float[] fractions, int selection,int maxTries,String scene, float alpha, boolean individualParameters, boolean noisy){
		super(maxTries,scene,times[selection],alpha,individualParameters,fractions[selection],noisy);
		this.times = times;
		this.fractions = fractions;
		this.selection = selection;
	}
	
	public ShortChallengeSettings(float[] times, float[] fractions,int selection,int maxTries, String scene,boolean individualParameters, boolean noisy){
		super(maxTries,scene,times[selection],individualParameters,fractions[selection],noisy);
		this.times = times;
		this.fractions = fractions;
		this.selection = selection;
	}
	
	public void selectChallenge(int selection){
		this.selection = selection;
		this.maxTime = times[selection];
		this.environmentFraction = fractions[selection];
	}
	
	public void selectNextChallenge(){
		selection++;
		if (selection >= times.length)
			selection = times.length-1;
		this.maxTime = times[selection];
		this.environmentFraction = fractions[selection];
	
	}

	public int getSelection() {
		return selection;
	}
		

}
