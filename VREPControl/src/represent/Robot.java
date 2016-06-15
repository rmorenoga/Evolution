package represent;

public class Robot {

	// Morphology Parameters
	/**
	 * Number of modules in the current configuration
	 */
	int Numberofmodules = 8;
	
	/**
	 * Orientation of the modules in the configuration
	 */
	int[] orientation = new int[] { 1, 0, 1, 0, 1, 0, 1, 0 };
	
	/**
	 * The control parameters of the robot
	 */
	float[] ControlParam;
	
	
	/**
	 * Creates a robot configuration object
	 * @param Numberofmodules the number of modules in the current configuration 
	 * @param orientation the orientation of the modules in the configuration
	 * @param ControlParam the control parameters of the robot
	 */
	public Robot(int Numberofmodules, int[] orientation, float[] ControlParam){
		this.Numberofmodules = Numberofmodules;
		this.orientation = orientation;
		this.ControlParam = ControlParam;
	}


	public int getNumberofmodules() {
		return Numberofmodules;
	}


	public void setNumberofmodules(int numberofmodules) {
		Numberofmodules = numberofmodules;
	}


	public int[] getOrientation() {
		return orientation;
	}


	public void setOrientation(int[] orientation) {
		this.orientation = orientation;
	}


	public float[] getControlParam() {
		return ControlParam;
	}


	public void setControlParam(float[] controlParam) {
		ControlParam = controlParam;
	}
	
	
	
	
	
}
