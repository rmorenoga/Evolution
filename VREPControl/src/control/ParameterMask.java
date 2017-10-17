package control;

import simvrep.SimulationConfiguration;

public abstract class ParameterMask {

	private float[] parameters;
	protected float[] maskedparameters;
	protected String controltype;
	protected int numberofParameters;

	public ParameterMask() {
		controltype = SimulationConfiguration.getController();
		this.numberofParameters = SimulationConfiguration.getControllerparamnumber();
	}

	/*
	 * Adjusts the parameters depending on model, will be called by RobotController previous to sending anything to the simulator
	 */
	public void growParam(int numberofModules) {
		if (parameters == null) {
			System.err.println("ParameterMask");
			System.err.println("Parameters have not been set yet");
			System.exit(-1);
		}
		//float[] grownparam = new float[numberofParameters * numberofModules];
		float[] grownparam = parameters; // Adjust the parameters according to the controller model
		maskedparameters = grownparam;

	}
	
	public void setParameters(float[] fullparam){
		this.parameters = fullparam;
	}

	public float[] getMaskedparameters() {
		if (maskedparameters == null) {
			System.err.println("ParameterMask");
			System.err.println("Parameters have not been grown yet when attemping to return maskedparameters");
			System.exit(-1);
		}
		return maskedparameters;
	}


	public float[] getParameters() {
		return parameters;
	}

}
