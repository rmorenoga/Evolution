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

	/**
	 * ParameterMask CPGSingle constructor
	 * 
	 * @param extrap
	 *            indicates the number of extra parameters in the parameters
	 *            coming from the external algorithm
	 * @param samePhaseDiff
	 *            if true only one phase difference will be used for all faces
	 */
	public CPGSingle(int extrap, boolean samePhaseDiff) {
		super(extrap);
		this.samePhaseDiff = samePhaseDiff;
		if (controltype.contentEquals("CPG")) {
			
		}else{
			System.err.println("CPGSingle");
			System.err.println("Controller must be of type CPG");
			System.exit(-1);
		}
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
				for (int j = 0; j < 4; j++) {
					grownparam[i + j + 2] = getParameters()[2];
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

}
