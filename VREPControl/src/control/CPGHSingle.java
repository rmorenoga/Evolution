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

	public void growParam(int numberofModules) {

		float[] grownparam;
		if (getParameters() == null) {
			System.err.println("CPGSingle");
			System.err.println("Parameters have not been set yet");
			System.exit(-1);
		}

		if (blockfrequency) {
			// parameters.length <= 30

			grownparam = new float[numberofParameters * numberofModules];

			for (int i = 0; i < grownparam.length; i = i + numberofParameters) {
				for (int j = 0; j < 5; j++) {
					grownparam[i + j] = getParameters()[j];
					grownparam[i + j + 5] = getParameters()[j + 5];
					grownparam[i + j + 30] = freq;
				}

				if (samePhaseDiff) {
					// parameters.length = 15
					if (getParameters().length != 15) {
						System.err.println("CPGHSingle");
						System.err.println("Parameters must be of size 15 since samePhaseDiff is " + samePhaseDiff
								+ " and blockfrequency is " + blockfrequency);
						System.exit(-1);
					}
					if (!snake) {
						for (int j = 0; j < 4; j++) {
							grownparam[i + j + 10] = getParameters()[10];
							grownparam[i + j + 14] = getParameters()[11];
							grownparam[i + j + 18] = getParameters()[12];
							grownparam[i + j + 22] = getParameters()[13];
							grownparam[i + j + 26] = getParameters()[14];
						}
					} else {
						grownparam[i + 10] = getParameters()[10];
						grownparam[i + 14] = getParameters()[11];
						grownparam[i + 18] = getParameters()[12];
						grownparam[i + 22] = getParameters()[13];
						grownparam[i + 26] = getParameters()[14];
						for (int j = 1; j < 4; j++) {
							grownparam[i + j + 10] = -getParameters()[10];
							grownparam[i + j + 14] = -getParameters()[11];
							grownparam[i + j + 18] = -getParameters()[12];
							grownparam[i + j + 22] = -getParameters()[13];
							grownparam[i + j + 26] = -getParameters()[14];
						}
					}
				} else {
					// parameters.length = 30
					if (getParameters().length != 30) {
						System.err.println("CPGHSingle");
						System.err.println("Parameters must be of size 30 since samePhaseDiff is " + samePhaseDiff
								+ " and blockfrequency is " + blockfrequency);
						System.exit(-1);
					}
					for (int j = 0; j < 20; j++) {
						grownparam[i + j + 10] = getParameters()[j + 10];
					}
				}

			}

		} else {
			// parameters.length <= 35

			grownparam = new float[numberofParameters * numberofModules];
			for (int i = 0; i < grownparam.length; i = i + numberofParameters) {
				for (int j = 0; j < 5; j++) {
					grownparam[i + j] = getParameters()[j];
					grownparam[i + j + 5] = getParameters()[j + 5];
					if (samePhaseDiff) {
						// parameters.length = 20
						if (getParameters().length != 20) {
							System.err.println("CPGHSingle");
							System.err.println("Parameters must be of size 20 since samePhaseDiff is " + samePhaseDiff
									+ " and blockfrequency is " + blockfrequency);
							System.exit(-1);
						}
						grownparam[i + j + 30] = getParameters()[j + 15];
					} else {
						// parameters.length = 35
						if (getParameters().length != 35) {
							System.err.println("CPGHSingle");
							System.err.println("Parameters must be of size 35 since samePhaseDiff is " + samePhaseDiff
									+ " and blockfrequency is " + blockfrequency);
							System.exit(-1);
						}
						grownparam[i + j + 30] = getParameters()[j + 30];
					}
				}
				if (samePhaseDiff) {
					// parameters.length = 20
					if (!snake) {
						for (int j = 0; j < 4; j++) {
							grownparam[i + j + 10] = getParameters()[10];
							grownparam[i + j + 14] = getParameters()[11];
							grownparam[i + j + 18] = getParameters()[12];
							grownparam[i + j + 22] = getParameters()[13];
							grownparam[i + j + 26] = getParameters()[14];
						}
					} else {
						grownparam[i + 10] = getParameters()[10];
						grownparam[i + 14] = getParameters()[11];
						grownparam[i + 18] = getParameters()[12];
						grownparam[i + 22] = getParameters()[13];
						grownparam[i + 26] = getParameters()[14];
						for (int j = 1; j < 4; j++) {
							grownparam[i + j + 10] = -getParameters()[10];
							grownparam[i + j + 14] = -getParameters()[11];
							grownparam[i + j + 18] = -getParameters()[12];
							grownparam[i + j + 22] = -getParameters()[13];
							grownparam[i + j + 26] = -getParameters()[14];
						}
					}
				} else {
					// parameters.length = 35
					for (int j = 0; j < 20; j++) {
						grownparam[i + j + 10] = getParameters()[j + 10];
					}
				}

			}
		}
		maskedparameters = adjustParam(grownparam);

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
