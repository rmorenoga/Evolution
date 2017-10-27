package control;

public class CPGHANN extends ParameterMask{
	
	
	public CPGHANN(){
		numberofParameters = 133;
	}
	
	
	public void growParam(int numberofModules) {
		
		float[] grownparam = new float[numberofModules*numberofParameters] ; 
		
		for(int i=1;i<grownparam.length;i++){
			grownparam[i] = (float)Math.random();
		}
		
		maskedparameters = grownparam;
		
	}
	
	
	
	
	

}
