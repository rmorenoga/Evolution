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
	
	public SimulationSettings(int maxTries, boolean measureDToGoal,
			boolean measureDToGoalByPart, String scene, float maxTime, float alpha, boolean individualParameters) {
		this.maxTries = maxTries;
		this.measureDToGoal = measureDToGoal;
		this.measureDToGoalByPart = measureDToGoalByPart;
		this.scene = scene;
		this.maxTime = maxTime;
		this.alpha = alpha;
		this.individualParameters = individualParameters;
		this.shortChallenge = false;
	}

	public SimulationSettings(int maxTries, String scene, float maxTime,boolean individualParameters) {
		this(maxTries,false,false,scene,maxTime,0.88f,individualParameters);
	}
	
	public SimulationSettings(int maxTries,String scene, float maxTime, float alpha, boolean individualParameters, float environmentFraction){
		this.maxTries = maxTries;
		this.measureDToGoal = true;
		this.measureDToGoalByPart = false;
		this.scene = scene;
		this.maxTime = maxTime;
		this.alpha = alpha;
		this.individualParameters = individualParameters;
		this.shortChallenge = true;
		this.environmentFraction = environmentFraction;
	}
	
	public SimulationSettings(int maxTries, String scene, float maxTime,boolean individualParameters, float environmentFraction) {
		this(maxTries,scene,maxTime,0.88f,individualParameters, environmentFraction);	
	}
}
