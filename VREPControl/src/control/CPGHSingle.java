package control;

import simvrep.SimulationConfiguration;

public class CPGHSingle extends ParameterMask {
	
	private boolean samePhaseDiff;
	private boolean blockfrequency;
	private float freq = 0.7f;
	
	/**
	 * ParameterMask CPGHSingle constructor
	 * 
	 * @param extrap
	 *            indicates the number of extra parameters in the parameters
	 *            coming from the external algorithm
	 * @param samePhaseDiff
	 *            if true only one phase difference will be used for all faces
	 * @param blockfrequency
	 * 			  if true 
	 */
	public CPGHSingle(int extrap, boolean samePhaseDiff, boolean blockfrequency) {
		super(extrap);
		this.samePhaseDiff = samePhaseDiff;
		this.blockfrequency = blockfrequency;	
		if (controltype.contentEquals("CPGH") || controltype.contentEquals("CPGHF") || controltype.contentEquals("CPGHLog")|| controltype.contentEquals("CPGHFLog")) {
			
		}else{
			System.err.println("CPGHSingle");
			System.err.println("Controller must be of type CPGH or CPGH");
			System.exit(-1);
		}
	}
	
	public void growParam(int numberofModules) {
		
		float[] grownparam;
		if (parameters == null) {
			System.err.println("CPGSingle");
			System.err.println("Parameters have not been set yet");
			System.exit(-1);
		}
		
		if (blockfrequency){
			// parameters.length <= 30 
						
			grownparam = new float[numberofParameters * numberofModules];
			
			for (int i = 0; i < parameters.length; i = i + numberofParameters) {
				for (int j = 0; j < 5; j++) {
					grownparam[i + j] = parameters[j] ;
					grownparam[i + j + 5] = parameters[j + 5];
					grownparam[i + j + 30] = freq;
				}
				for (int j = 0; j < 20; j++) {
					if (samePhaseDiff){
						// parameters.length = 11
						if (parameters.length != 11) {
							System.err.println("CPGHSingle");
							System.err.println("Parameters must be of size 11 since samePhaseDiff is " + samePhaseDiff + " and blockfrequency is " + blockfrequency);
							System.exit(-1);
						}
						grownparam[i + j + 10] = parameters[10];
					}else{
						// parameters.length = 30
						if (parameters.length != 30) {
							System.err.println("CPGHSingle");
							System.err.println("Parameters must be of size 30 since samePhaseDiff is " + samePhaseDiff + " and blockfrequency is " + blockfrequency);
							System.exit(-1);
						}
						grownparam[i + j + 10] = parameters[j + 10] ;
					}
					
				}
			}
			
		}else{
			// parameters.length <= 35
			
			grownparam = new float[numberofParameters * numberofModules];
			for (int i = 0; i < parameters.length; i = i + numberofParameters) {
				for (int j = 0; j < 5; j++) {
					grownparam[i + j] = parameters[j] ;
					grownparam[i + j + 5] = parameters[j + 5];
					if (samePhaseDiff){
						// parameters.length = 16
						if (parameters.length != 16) {
							System.err.println("CPGHSingle");
							System.err.println("Parameters must be of size 16 since samePhaseDiff is " + samePhaseDiff + " and blockfrequency is " + blockfrequency);
							System.exit(-1);
						}
						grownparam[i + j + 30] = parameters[j + 11];
					}else{
						// parameters.length = 35
						if (parameters.length != 35) {
							System.err.println("CPGHSingle");
							System.err.println("Parameters must be of size 35 since samePhaseDiff is " + samePhaseDiff + " and blockfrequency is " + blockfrequency);
							System.exit(-1);
						}
						grownparam[i + j + 30] = parameters[j + 30];
					}
				}
				for (int j = 0; j < 20; j++) {
					if (samePhaseDiff){
						// parameters.length = 16
						grownparam[i + j + 10] = parameters[10];
					}else{
						// parameters.length = 35
						grownparam[i + j + 10] = parameters[j + 10] ;
					}
				}
			}
		}
		
		maskedparameters = adjustParam(grownparam);
		
	}
	
	
	
	
	
	
	
	

	
}
