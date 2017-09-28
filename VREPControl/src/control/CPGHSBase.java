package control;

public class CPGHSBase extends CPGHSingle{
	
	private float[] fixed = new float[] { -1.0f, -0.05675713991376258f, -0.2704007348336987f, 0.3f, 0.3f, 0.3f, 0.4f };

	public CPGHSBase(int extrap, boolean samePhaseDiff, boolean blockfrequency) {
		super(extrap, samePhaseDiff, blockfrequency);
	}

	public CPGHSBase(int extrap, boolean samePhaseDiff, boolean blockfrequency,boolean snake) {
		super(extrap, samePhaseDiff, blockfrequency,snake);
	}
	
	public CPGHSBase(int extrap, float freq, boolean samePhaseDiff, boolean blockfrequency) {
		super(extrap, freq, samePhaseDiff, blockfrequency);
	}
	
	public CPGHSBase(int extrap, float freq, boolean samePhaseDiff, boolean blockfrequency,boolean snake) {
		super(extrap, freq, samePhaseDiff, blockfrequency,snake);
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
				// rawParameters.length = 12
				// MaskedParameters.length = 15
				if (parameters.length != 12) {
					System.err.println("CPGHSBase");
					System.err.println("Parameters must be of size 12 since samePhaseDiff is " + isSamePhaseDiff() + " and blockfrequency is " + isBlockfrequency());
					System.exit(-1);
				}
				
				MaskedParameters = new float[15];

				MaskedParameters[0] = fixed[0];
				MaskedParameters[5] = fixed[1];
				MaskedParameters[10] = fixed[2];

				for (int j = 1; j < 5; j++) {
					MaskedParameters[j] = parameters[j - 1]; // Amplitude
					MaskedParameters[j + 5] = parameters[j - 1 + 4]; // Offset
					MaskedParameters[j + 10] = parameters[j - 1 + 8]; // Phase difference												
				}
			}else{
				// rawParameters.length = 24
				// MaskedParameters.length = 30
				if (parameters.length != 24) {
					System.err.println("CPGHSBase");
					System.err.println("Parameters must be of size 24 since samePhaseDiff is " + isSamePhaseDiff() + " and blockfrequency is " + isBlockfrequency());
					System.exit(-1);
				}
				
				MaskedParameters = new float[30];

				MaskedParameters[0] = fixed[0];
				MaskedParameters[5] = fixed[1];
				MaskedParameters[10] = fixed[2];
				MaskedParameters[11] = fixed[3];
				MaskedParameters[12] = fixed[4];
				MaskedParameters[13] = fixed[5];


				for (int j = 1; j < 5; j++) {
					MaskedParameters[j] = parameters[j - 1]; // Amplitude
					MaskedParameters[j + 5] = parameters[j - 1 + 4]; // Offset
				}
				for (int j = 4; j < 20; j++) {
					MaskedParameters[j + 10] = parameters[j - 4 + 8]; // Phase
																			// difference
				}
			}		
		}else{
			if (isSamePhaseDiff()){
				// rawParameters.length = 16
				// MaskedParameters.length = 20
				if (parameters.length != 16) {
					System.err.println("CPGHSBase");
					System.err.println("Parameters must be of size 16 since samePhaseDiff is " + isSamePhaseDiff() + " and blockfrequency is " + isBlockfrequency());
					System.exit(-1);
				}
				
				MaskedParameters = new float[20];

				MaskedParameters[0] = fixed[0];
				MaskedParameters[5] = fixed[1];
				MaskedParameters[10] = fixed[2];
				MaskedParameters[15] = fixed[6];

				for (int j = 1; j < 5; j++) {
					MaskedParameters[j] = parameters[j - 1]; // Amplitude
					MaskedParameters[j + 5] = parameters[j - 1 + 4]; // Offset
					MaskedParameters[j + 10] = parameters[j - 1 + 8]; // Phase difference
					MaskedParameters[j + 15] = parameters[j - 1 + 12]; // Frequency (v)														
				}
						
			}else{
				// rawParameters.length = 28
				// MaskedParameters.length = 35
				if (parameters.length != 28) {
					System.err.println("CPGHSBase");
					System.err.println("Parameters must be of size 28 since samePhaseDiff is " + isSamePhaseDiff() + " and blockfrequency is " + isBlockfrequency());
					System.exit(-1);
				}
				
				MaskedParameters = new float[35];

				MaskedParameters[0] = fixed[0];
				MaskedParameters[5] = fixed[1];
				MaskedParameters[10] = fixed[2];
				MaskedParameters[11] = fixed[3];
				MaskedParameters[12] = fixed[4];
				MaskedParameters[13] = fixed[5];
				MaskedParameters[30] = fixed[6];

				for (int j = 1; j < 5; j++) {
					MaskedParameters[j] = parameters[j - 1]; // Amplitude
					MaskedParameters[j + 5] = parameters[j - 1 + 4]; // Offset
					MaskedParameters[j + 30] = parameters[j - 1 + 24]; // Frequency
																			// (v)
				}
				for (int j = 4; j < 20; j++) {
					MaskedParameters[j + 10] = parameters[j - 4 + 8]; // Phase
																			// difference
				}
			}		
			

		}
		
		return super.organize(numberofModules,MaskedParameters);
	}
	
}
