package control;

import java.util.List;

import coppelia.remoteApi;
import simvrep.RobotBuilder;

public abstract class RobotController {

	protected String controllerName;
	protected remoteApi vrep;
	protected int clientID;
	protected RobotBuilder robot;
	protected int[] moduleHandlers;
	protected int numberofModules;
	protected int numberofParameters;

	protected RobotController(remoteApi vrep, int clientID, RobotBuilder robot) {
		this.vrep = vrep;
		this.clientID = clientID;
		this.robot = robot;
		moduleHandlers = robot.getModuleHandlersint();
		this.numberofModules = moduleHandlers.length;
		
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
