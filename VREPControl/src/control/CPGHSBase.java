package control;

public class CPGHSBase extends CPGHSingle{
	
	//private float[] fixed = new float[] { -1.0f, -0.05675713991376258f, -0.2704007348336987f, 0.3f, 0.3f, 0.3f, 0.4f };
	private float[] fixed = new float[] { 0.5f, 0.0f, 0.3f, 0.3f, 0.3f, 0.3f, 0.4f };
	
	public CPGHSBase(boolean samePhaseDiff, boolean blockfrequency) {
		super(samePhaseDiff, blockfrequency);
	}

	public CPGHSBase(boolean samePhaseDiff, boolean blockfrequency,boolean snake) {
		super(samePhaseDiff, blockfrequency,snake);
	}
	
	public CPGHSBase(float freq, boolean samePhaseDiff, boolean blockfrequency) {
		super(freq, samePhaseDiff, blockfrequency);
	}
	
	public CPGHSBase(float freq, boolean samePhaseDiff, boolean blockfrequency,boolean snake) {
		super(freq, samePhaseDiff, blockfrequency,snake);
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
		
		float[] MaskedParameters;
		if (isBlockfrequency()) {
			if (isSamePhaseDiff()){
				// rawParameters.length = 18
				// MaskedParameters.length = 21
				if (parameters.length != 18) {
					System.err.println("CPGHSBase");
					System.err.println("Parameters must be of size 18 since samePhaseDiff is " + isSamePhaseDiff() + " and blockfrequency is " + isBlockfrequency());
					System.exit(-1);
				}
				
				MaskedParameters = new float[21];

				MaskedParameters[0] = fixed[0];
				MaskedParameters[7] = fixed[1];
				MaskedParameters[14] = fixed[2];

				for (int j = 1; j < 7; j++) {
					MaskedParameters[j] = parameters[j - 1]; // Amplitude
					MaskedParameters[j + 7] = parameters[j - 1 + 6]; // Offset
					MaskedParameters[j + 14] = parameters[j - 1 + 12]; // Phase difference												
				}
			}else{
				// rawParameters.length = 36
				// MaskedParameters.length = 42
				if (parameters.length != 36) {
					System.err.println("CPGHSBase");
					System.err.println("Parameters must be of size 36 since samePhaseDiff is " + isSamePhaseDiff() + " and blockfrequency is " + isBlockfrequency());
					System.exit(-1);
				}
				
				MaskedParameters = new float[42];

				MaskedParameters[0] = fixed[0];
				MaskedParameters[7] = fixed[1];
				MaskedParameters[14] = fixed[2];
				MaskedParameters[15] = fixed[3];
				MaskedParameters[16] = fixed[4];
				MaskedParameters[17] = fixed[5];


				for (int j = 1; j < 7; j++) {
					MaskedParameters[j] = parameters[j - 1]; // Amplitude
					MaskedParameters[j + 7] = parameters[j - 1 + 6]; // Offset
				}
				for (int j = 4; j < 28; j++) {
					MaskedParameters[j + 14] = parameters[j - 4 + 12]; // Phase
																			// difference
				}
			}		
		}else{
			if (isSamePhaseDiff()){
				// rawParameters.length = 24
				// MaskedParameters.length = 28
				if (parameters.length != 24) {
					System.err.println("CPGHSBase");
					System.err.println("Parameters must be of size 24 since samePhaseDiff is " + isSamePhaseDiff() + " and blockfrequency is " + isBlockfrequency());
					System.exit(-1);
				}
				
				MaskedParameters = new float[28];

				MaskedParameters[0] = fixed[0];
				MaskedParameters[7] = fixed[1];
				MaskedParameters[14] = fixed[2];
				MaskedParameters[21] = fixed[6];

				for (int j = 1; j < 7; j++) {
					MaskedParameters[j] = parameters[j - 1]; // Amplitude
					MaskedParameters[j + 7] = parameters[j - 1 + 6]; // Offset
					MaskedParameters[j + 14] = parameters[j - 1 + 12]; // Phase difference
					MaskedParameters[j + 21] = parameters[j - 1 + 18]; // Frequency (v)														
				}
						
			}else{
				// rawParameters.length = 42
				// MaskedParameters.length = 49
				if (parameters.length != 42) {
					System.err.println("CPGHSBase");
					System.err.println("Parameters must be of size 42 since samePhaseDiff is " + isSamePhaseDiff() + " and blockfrequency is " + isBlockfrequency());
					System.exit(-1);
				}
				
				MaskedParameters = new float[49];

				MaskedParameters[0] = fixed[0];
				MaskedParameters[7] = fixed[1];
				MaskedParameters[14] = fixed[2];
				MaskedParameters[15] = fixed[3];
				MaskedParameters[16] = fixed[4];
				MaskedParameters[17] = fixed[5];
				MaskedParameters[42] = fixed[6];

				for (int j = 1; j < 7; j++) {
					MaskedParameters[j] = parameters[j - 1]; // Amplitude
					MaskedParameters[j + 7] = parameters[j - 1 + 6]; // Offset
					MaskedParameters[j + 42] = parameters[j - 1 + 36]; // Frequency
																			// (v)
				}
				for (int j = 4; j < 28; j++) {
					MaskedParameters[j + 14] = parameters[j - 4 + 12]; // Phase
																			// difference
				}
			}		
			

		}
		
		return super.organize(numberofModules,MaskedParameters);
	}
	
}
