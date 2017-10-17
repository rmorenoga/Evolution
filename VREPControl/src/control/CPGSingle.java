package control;

import simvrep.SimulationConfiguration;

/**
 * ParameterMask subclass that organizes the parameters sent by an external
 * evolutionary algorithm CPGSingle accepts a set of parameters suitable for a
 * single module and copies it to all modules in the current configuration
 * parameters must be of size 6 if samePhaseDiff is false and 3 if samePhaseDiff
 * is true
 * 
 * @author rodr
 *
 */

public class CPGSingle extends ParameterMask {

	private boolean samePhaseDiff;
	private boolean snake = false;

	/**
	 * ParameterMask CPGSingle constructor
	 * 
	 * @param samePhaseDiff
	 *            if true only one phase difference will be used for all faces
	 */
	public CPGSingle(boolean samePhaseDiff) {
		this.samePhaseDiff = samePhaseDiff;
		if (controltype.contentEquals("CPG")) {

		} else {
			System.err.println("CPGSingle");
			System.err.println("Controller must be of type CPG");
			System.exit(-1);
		}
	}

	/**
	 * ParameterMask CPGSingle constructor
	 * 
	 * @param samePhaseDiff
	 *            if true only one phase difference will be used for all faces
	 */
	public CPGSingle(boolean samePhaseDiff, boolean snake) {
		this(samePhaseDiff);
		this.snake = snake;
	}

	public void growParam(int numberofModules) {
		float[] grownparam;
		if (getParameters() == null) {
			System.err.println("CPGSingle");
			System.err.println("Parameters have not been set yet");
			System.exit(-1);
		}
		if (samePhaseDiff) {
			if (getParameters().length != 3) {
				System.err.println("CPGSingle");
				System.err.println("Parameters must be of size 3 since samePhaseDiff is " + samePhaseDiff);
				System.exit(-1);
			}
			grownparam = new float[numberofParameters * numberofModules];

			for (int i = 0; i < grownparam.length; i = i + numberofParameters) {
				grownparam[i] = getParameters()[0];
				grownparam[i + 1] = getParameters()[1];
				if (!snake) {
					for (int j = 0; j < 4; j++) {
						grownparam[i + j + 2] = getParameters()[2];
					}
				} else {
					grownparam[i + 0 + 2] = getParameters()[2];
					for (int j = 1; j < 4; j++) {
						grownparam[i + j + 2] = -getParameters()[2];
					}
				}

			}

		} else {
			if (getParameters().length != 6) {
				System.err.println("CPGSingle");
				System.err.println("Parameters must be of size 6 since samePhaseDiff is " + samePhaseDiff);
				System.exit(-1);
			}
			grownparam = new float[numberofParameters * numberofModules];
			for (int i = 0; i < grownparam.length; i = i + numberofParameters) {
				grownparam[i] = getParameters()[0];
				grownparam[i + 1] = getParameters()[1];
				for (int j = 0; j < 4; j++) {
					grownparam[i + j + 2] = getParameters()[j + 2];
				}
			}

		}

		maskedparameters = adjustParam(grownparam);

	}

	protected float[] adjustParam(float[] parameters) {
		float[] grownparam = new float[parameters.length];

		if (controltype.contentEquals("CPG")) {
			float maxPhase = (float) SimulationConfiguration.getMaxPhase();
			float minPhase = (float) SimulationConfiguration.getMinPhase();
			float maxAmplitude = (float) SimulationConfiguration.getMaxAmplitude();
			float minAmplitude = (float) SimulationConfiguration.getMinAmplitude();
			float maxOffset = (float) SimulationConfiguration.getMaxOffset();
			float minOffset = (float) SimulationConfiguration.getMinOffset();
			// Assuming min raw parameter is -1 and max is 1
						// NewValue = (((OldValue - OldMin) * (NewMax - NewMin)) / (OldMax -
						// OldMin)) + NewMin
						// NewValue = (((OldValue - (-1)) * (NewMax - NewMin)) / (1 - (-1)))
						// + NewMin
			for (int i = 0; i < parameters.length; i = i + numberofParameters) {	
					grownparam[i] = (((parameters[i] + 1) * (maxAmplitude - minAmplitude)) / 2) + minAmplitude;
					grownparam[i+1] = (((parameters[i+1] + 1) * (maxOffset - minOffset)) / 2) + minOffset;
				for (int j = 0; j < 4; j++) {
					grownparam[i + j + 2] = (((parameters[i + j + 2] + 1) * (maxPhase - minPhase)) / 2) + minPhase;
				}
			}
		}
		return grownparam;
	}

}
