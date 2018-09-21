package simvrep;

import control.ParameterMask;

public class SimulationSettings {

	public int maxTries;
	public boolean measureDToGoal;
	public boolean measureDToGoalByPart;
	public String scene;
	public float maxTime = 10;
	public float alpha = 0.88f;
	public boolean individualParameters;
	public boolean shortChallenge;
	public float environmentFraction = 1; 
	public boolean noisy;
	public float noiseRadiusMaze = 0.2f;
	
	public SimulationSettings(int maxTries, boolean measureDToGoal,
			boolean measureDToGoalByPart, String scene, float maxTime, float alpha, boolean individualParameters, boolean noisy) {
		this.maxTries = maxTries;
		this.measureDToGoal = measureDToGoal;
		this.measureDToGoalByPart = measureDToGoalByPart;
		this.scene = scene;
		this.maxTime = maxTime;
		this.alpha = alpha;
		this.individualParameters = individualParameters;
		this.shortChallenge = false;
		this.noisy = noisy;
	}

	public SimulationSettings(int maxTries, String scene, float maxTime,boolean individualParameters, boolean noisy) {
		this(maxTries,false,false,scene,maxTime,0.88f,individualParameters,noisy);
	}
	
	public SimulationSettings(int maxTries,String scene, float maxTime, float alpha, boolean individualParameters, float environmentFraction, boolean noisy){
		this.maxTries = maxTries;
		this.measureDToGoal = false;
		this.measureDToGoalByPart = false;
		this.scene = scene;
		this.maxTime = maxTime;
		this.alpha = alpha;
		this.individualParameters = individualParameters;
		this.shortChallenge = true;
		this.environmentFraction = environmentFraction;
		this.noisy = noisy;
	}
	
	public SimulationSettings(int maxTries, String scene, float maxTime,boolean individualParameters, float environmentFraction, boolean noisy) {
		this(maxTries,scene,maxTime,1,individualParameters, environmentFraction, noisy);	
	}
}
