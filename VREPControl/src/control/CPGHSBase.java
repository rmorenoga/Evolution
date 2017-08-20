package control;

/**
 * Helper class to be used with CPGHSingle to fix the parameter set
 * corresponding to the base hormone of CPGH (hormone0)
 * 
 * @author golde_000
 *
 */
public class CPGHSBase extends SubCPGHS {

	private float[] rawParameters;
	private float[] fixed = new float[] { 0.5f, 0.1f, 0.3f, 0.3f, 0.3f, 0.3f, 0.5f };

	public CPGHSBase(){
		super(false,false);
	}
	
	public CPGHSBase(boolean samePhaseDiff, boolean blockfrequency) {
		super(samePhaseDiff,blockfrequency);
	}

	public void setRawParameters(float[] parameters) {
		this.rawParameters = parameters;
	}

	@Override
	public float[] getMaskedParameters() {
		if (rawParameters == null) {
			System.err.println("CPGHSBase");
			System.err.println("rawParameters has not been set yet");
			System.exit(-1);
		}
		float[] MaskedParameters;
		if (isBlockfrequency()) {
			if (isSamePhaseDiff()){
				// rawParameters.length = 12
				// MaskedParameters.length = 15
				if (rawParameters.length != 12) {
					System.err.println("CPGHSBase");
					System.err.println("Parameters must be of size 12 since samePhaseDiff is " + isSamePhaseDiff() + " and blockfrequency is " + isBlockfrequency());
					System.exit(-1);
				}
				
				MaskedParameters = new float[15];

				MaskedParameters[0] = fixed[0];
				MaskedParameters[5] = fixed[1];
				MaskedParameters[10] = fixed[2];

				for (int j = 1; j < 5; j++) {
					MaskedParameters[j] = rawParameters[j - 1]; // Amplitude
					MaskedParameters[j + 5] = rawParameters[j - 1 + 4]; // Offset
					MaskedParameters[j + 10] = rawParameters[j - 1 + 8]; // Phase difference												
				}
			}else{
				// rawParameters.length = 24
				// MaskedParameters.length = 30
				if (rawParameters.length != 24) {
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
					MaskedParameters[j] = rawParameters[j - 1]; // Amplitude
					MaskedParameters[j + 5] = rawParameters[j - 1 + 4]; // Offset
				}
				for (int j = 4; j < 20; j++) {
					MaskedParameters[j + 10] = rawParameters[j - 4 + 8]; // Phase
																			// difference
				}
			}		
		}else{
			if (isSamePhaseDiff()){
				// rawParameters.length = 16
				// MaskedParameters.length = 20
				if (rawParameters.length != 16) {
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
					MaskedParameters[j] = rawParameters[j - 1]; // Amplitude
					MaskedParameters[j + 5] = rawParameters[j - 1 + 4]; // Offset
					MaskedParameters[j + 10] = rawParameters[j - 1 + 8]; // Phase difference
					MaskedParameters[j + 15] = rawParameters[j - 1 + 12]; // Frequency (v)														
				}
						
			}else{
				// rawParameters.length = 28
				// MaskedParameters.length = 35
				if (rawParameters.length != 28) {
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
					MaskedParameters[j] = rawParameters[j - 1]; // Amplitude
					MaskedParameters[j + 5] = rawParameters[j - 1 + 4]; // Offset
					MaskedParameters[j + 30] = rawParameters[j - 1 + 24]; // Frequency
																			// (v)
				}
				for (int j = 4; j < 20; j++) {
					MaskedParameters[j + 10] = rawParameters[j - 4 + 8]; // Phase
																			// difference
				}
			}		
			

		}
		
		return MaskedParameters;

	}

}
