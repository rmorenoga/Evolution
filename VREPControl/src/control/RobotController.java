package control;

import java.util.List;

import coppelia.remoteApi;
import simvrep.RobotBuilder;

public abstract class RobotController {

	protected String controllerName;
	protected remoteApi vrep;
	protected int clientID;
	protected RobotBuilder robot;
	protected List<Integer> moduleHandlers;
	protected int numberofModules;
	protected int numberofParameters;

	protected RobotController(remoteApi vrep, int clientID, RobotBuilder robot) {
		this.vrep = vrep;
		this.clientID = clientID;
		this.robot = robot;
		moduleHandlers = robot.getModuleHandlers();
		this.numberofModules = moduleHandlers.size();
		
	}
	
	public abstract void sendParameters();
	
	public String getControllerName() {
		return controllerName;
	}

	public int getNumberofModules() {
		return numberofModules;
	}

	public int getNumberofParameters() {
		return numberofParameters;
	}

}
