package control;

import coppelia.remoteApi;
import simvrep.RobotBuilder;

public class CPGController extends RobotController {

	public CPGController(remoteApi vrep, int clientID, RobotBuilder robot) {
		super(vrep, clientID, robot);
		ControllerName = "CPG";
		
		
	}


	public void sendParameters() {

		
	}
	


}
