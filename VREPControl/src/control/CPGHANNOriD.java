package control;

public class CPGHANNOriD extends CPGHANN{
	
	private int[] connectedOrientationList; 
	private int numberofModules;
	
	
	public CPGHANNOriD(int numberofParameters){
		super(numberofParameters);
	}
	
	@Override
	public int[] getConnectedOriList(int numberofmodules) {
		this.numberofModules = numberofmodules;
		
		generateConnectedOrifromRepresentation();

		
		return connectedOrientationList;
	}

	@Override
	public void generateConnectedOrifromRepresentation() {
		connectedOrientationList = new int[numberofModules*4];
		
		connectedOrientationList[0] = -1;
		connectedOrientationList[1] = 1;
		connectedOrientationList[2] = -1;
		connectedOrientationList[3] = -1;
		
				
		for (int i=4;i<numberofModules*4;i++){
			connectedOrientationList[i] = getOrientationSetForIndividualModuleFromRepresentation()[i-4];
		}
		 
		
	}

	public int[] getOrientationSetForIndividualModuleFromRepresentation() {
		return new int[]{11, 3, -1, -1, 13, 1, -1, -1, 11, 3, -1, -1, 13, 1, -1, -1, 11, 3, -1, -1, 13, 1, -1, -1, 11, -1, -1, -1};
	}

	
	
	

}
