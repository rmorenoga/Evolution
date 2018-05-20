package simvrep;

import control.ParameterMask;

public class SimulationSettings {

	public int maxTries;
	public boolean measureDToGoal;
	public boolean measureDToGoalByPart;
	public String scene;
	public int maxTime = 10;
	public float alpha = 0.88f;
	public boolean individualParameters;
	
	public SimulationSettings(int maxTries, boolean measureDToGoal,
			boolean measureDToGoalByPart, String scene, int maxTime, float alpha, boolean individualParameters) {
		this.maxTries = maxTries;
		this.measureDToGoal = measureDToGoal;
		this.measureDToGoalByPart = measureDToGoalByPart;
		this.scene = scene;
		this.maxTime = maxTime;
		this.alpha = alpha;
		this.individualParameters = individualParameters;
	}

	public SimulationSettings(int maxTries, String scene, int maxTime,boolean individualParameters) {
		this(maxTries,false,false,scene,maxTime,0.88f,individualParameters);
	}
}
