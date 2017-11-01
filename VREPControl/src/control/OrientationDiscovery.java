package control;

public interface OrientationDiscovery {
	
	public int[] getConnectedOriList(int numberofmodules);
	
	public void generateConnectedOrifromRepresentation();//Must receive an orientation representation from a higher layer adaptation algorithm
	
	public int[] getOrientationSetForIndividualModuleFromRepresentation();
	
}
