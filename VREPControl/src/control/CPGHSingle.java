package control;

import simvrep.SimulationConfiguration;

public class CPGHSingle extends ParameterMask {

	private boolean samePhaseDiff;
	private boolean blockfrequency;
	private float freq = 0.4f;
	private boolean snake = false;

	/**
	 * ParameterMask CPGHSingle constructor
	 * 
	 * @param extrap
	 *            indicates the number of extra parameters in the parameters
	 *            coming from the external algorithm
	 * @param samePhaseDiff
	 *            if true only one phase difference will be used for all faces
	 * @param blockfrequency
	 *            if true a preset value of frequency specified by the attribute
	 *            freq will be used the default value is 0.4f
	 */
	public CPGHSingle(int extrap, boolean samePhaseDiff, boolean blockfrequency) {
		super(extrap);
		this.samePhaseDiff = samePhaseDiff;
		this.blockfrequency = blockfrequency;
		if (controltype.contentEquals("CPGH") || controltype.contentEquals("CPGHF")
				|| controltype.contentEquals("CPGHLog") || controltype.contentEquals("CPGHFLog")) {

		} else {
			System.err.println("CPGHSingle");
			System.err.println("Controller must be of type CPGH or CPGHF");
			System.exit(-1);
		}
	}

	/**
	 * ParameterMask CPGHSingle constructor
	 * 
	 * @param extrap
	 *            indicates the number of extra parameters in the parameters
	 *            coming from the external algorithm
	 * @param samePhaseDiff
	 *            if true only one phase difference will be used for all faces
	 * @param blockfrequency
	 *            if true a preset value of frequency specified by the attribute
	 *            freq will be used the default value is 0.4f
	 * @param snake
	 *            if true the first phasediff in samePhaseDiff cases will use an
	 *            opposite sign from the rest
	 */
	public CPGHSingle(int extrap, boolean samePhaseDiff, boolean blockfrequency, boolean snake) {
		this(extrap, samePhaseDiff, blockfrequency);
		this.snake = snake;
	}

	/**
	 * ParameterMask CPGHSingle constructor
	 * 
	 * @param extrap
	 *            indicates the number of extra parameters in the parameters
	 *            coming from the external algorithm
	 * @param freq
	 *            the prespecified frequency in case blockfrequency is true the
	 *            default value is 0.4f
	 * @param samePhaseDiff
	 *            if true only one phase difference will be used for all faces
	 * @param blockfrequency
	 *            if true a preset value of frequency specified by the attribute
	 *            freq will be used
	 */
	public CPGHSingle(int extrap, float freq, boolean samePhaseDiff, boolean blockfrequency) {
		this(extrap, samePhaseDiff, blockfrequency);
		this.freq = freq;
	}

	/**
	 * ParameterMask CPGHSingle constructor
	 * 
	 * @param extrap
	 *            indicates the number of extra parameters in the parameters
	 *            coming from the external algorithm
	 * @param freq
	 *            the prespecified frequency in case blockfrequency is true the
	 *            default value is 0.4f
	 * @param samePhaseDiff
	 *            if true only one phase difference will be used for all faces
	 * @param blockfrequency
	 *            if true a preset value of frequency specified by the attribute
	 *            freq will be used
	 * @param snake
	 *            if true the first phasediff in samePhaseDiff cases will use an
	 *            opposite sign from the rest
	 */
	public CPGHSingle(int extrap, float freq, boolean samePhaseDiff, boolean blockfrequency, boolean snake) {
		this(extrap, freq, samePhaseDiff, blockfrequency);
		this.snake = snake;
	}
	
	public void growParam(int numberofModules){
		float[] grownparam;
		if (getParameters() == null) {
			System.err.println("CPGSingle");
			System.err.println("Parameters have not been set yet");
			System.exit(-1);
		}
		grownparam = organize(numberofModules,getParameters()); 
		maskedparameters = adjustParam(grownparam);
		
	}

	public float[] organize(int numberofModules, float[] parameters) {

		float[] grownparam;

		if (blockfrequency) {
			// parameters.length <= 30

			grownparam = new float[numberofParameters * numberofModules];

			for (int i = 0; i < grownparam.length; i = i + numberofParameters) {
				for (int j = 0; j < 7; j++) {
					grownparam[i + j] = parameters[j];
					grownparam[i + j + 7] = parameters[j + 7];
					grownparam[i + j + 42] = freq;
				}

				if (samePhaseDiff) {
					// parameters.length = 21
					if (parameters.length != 21) {
						System.err.println("CPGHSingle");
						System.err.println("Parameters must be of size 21 since samePhaseDiff is " + samePhaseDiff
								+ " and blockfrequency is " + blockfrequency);
						System.exit(-1);
					}
					if (!snake) {
						for (int j = 0; j < 4; j++) {
							grownparam[i + j + 14] = parameters[14];
							grownparam[i + j + 18] = parameters[15];
							grownparam[i + j + 22] = parameters[16];
							grownparam[i + j + 26] = parameters[17];
							grownparam[i + j + 30] = parameters[18];
							grownparam[i + j + 34] = parameters[19];
							grownparam[i + j + 38] = parameters[20];
						}
					} else {
						grownparam[i + 14] = parameters[14];
						grownparam[i + 18] = parameters[15];
						grownparam[i + 22] = parameters[16];
						grownparam[i + 26] = parameters[17];
						grownparam[i + 30] = parameters[18];
						grownparam[i + 34] = parameters[19];
						grownparam[i + 38] = parameters[20];
						for (int j = 1; j < 4; j++) {
							grownparam[i + j + 14] = -parameters[14];
							grownparam[i + j + 18] = -parameters[15];
							grownparam[i + j + 22] = -parameters[16];
							grownparam[i + j + 26] = -parameters[17];
							grownparam[i + j + 30] = -parameters[18];
							grownparam[i + j + 34] = -parameters[19];
							grownparam[i + j + 38] = -parameters[20];
						}
					}
				} else {
					// parameters.length = 30
					if (parameters.length != 42) {
						System.err.println("CPGHSingle");
						System.err.println("Parameters must be of size 42 since samePhaseDiff is " + samePhaseDiff
								+ " and blockfrequency is " + blockfrequency);
						System.exit(-1);
					}
					for (int j = 0; j < 28; j++) {
						grownparam[i + j + 14] = parameters[j + 14];
					}
				}

			}

		} else {
			// parameters.length <= 49

			grownparam = new float[numberofParameters * numberofModules];
			for (int i = 0; i < grownparam.length; i = i + numberofParameters) {
				for (int j = 0; j < 7; j++) {
					grownparam[i + j] = parameters[j];
					grownparam[i + j + 7] = parameters[j + 7];
					if (samePhaseDiff) {
						// parameters.length = 28
						if (parameters.length != 28) {
							System.err.println("CPGHSingle");
							System.err.println("Parameters must be of size 28 since samePhaseDiff is " + samePhaseDiff
									+ " and blockfrequency is " + blockfrequency);
							System.exit(-1);
						}
						grownparam[i + j + 42] = parameters[j + 21];
					} else {
						// parameters.length = 49
						if (parameters.length != 49) {
							System.err.println("CPGHSingle");
							System.err.println("Parameters must be of size 49 since samePhaseDiff is " + samePhaseDiff
									+ " and blockfrequency is " + blockfrequency);
							System.exit(-1);
						}
						grownparam[i + j + 42] = parameters[j + 42];
					}
				}
				if (samePhaseDiff) {
					// parameters.length = 28
					if (!snake) {
						for (int j = 0; j < 4; j++) {
							grownparam[i + j + 14] = parameters[14];
							grownparam[i + j + 18] = parameters[15];
							grownparam[i + j + 22] = parameters[16];
							grownparam[i + j + 26] = parameters[17];
							grownparam[i + j + 30] = parameters[18];
							grownparam[i + j + 34] = parameters[19];
							grownparam[i + j + 38] = parameters[20];
						}
					} else {
						grownparam[i + 14] = parameters[14];
						grownparam[i + 18] = parameters[15];
						grownparam[i + 22] = parameters[16];
						grownparam[i + 26] = parameters[17];
						grownparam[i + 30] = parameters[18];
						grownparam[i + 34] = parameters[19];
						grownparam[i + 38] = parameters[20];
						for (int j = 1; j < 4; j++) {
							grownparam[i + j + 14] = -parameters[14];
							grownparam[i + j + 18] = -parameters[15];
							grownparam[i + j + 22] = -parameters[16];
							grownparam[i + j + 26] = -parameters[17];
							grownparam[i + j + 30] = -parameters[18];
							grownparam[i + j + 34] = -parameters[19];
							grownparam[i + j + 38] = -parameters[20];
						}
					}
				} else {
					// parameters.length = 49
					for (int j = 0; j < 28; j++) {
						grownparam[i + j + 14] = parameters[j + 14];
					}
				}

			}
		}
		
		return grownparam;
	}

	public boolean isSamePhaseDiff() {
		return samePhaseDiff;
	}

	public void setSamePhaseDiff(boolean samePhaseDiff) {
		this.samePhaseDiff = samePhaseDiff;
	}

	public boolean isBlockfrequency() {
		return blockfrequency;
	}

	public void setBlockfrequency(boolean blockfrequency) {
		this.blockfrequency = blockfrequency;
	}

}
