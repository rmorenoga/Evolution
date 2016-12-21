package control;

import java.util.List;

import coppelia.remoteApi;
import simvrep.RobotBuilder;

public abstract class RobotController {

	protected String ControllerName;
	protected remoteApi vrep;
	protected int clientID;
	protected RobotBuilder robot;
	protected List<Integer> moduleHandlers;

	protected RobotController(remoteApi vrep, int clientID, RobotBuilder robot) {
		this.vrep = vrep;
		this.clientID = clientID;
		this.robot = robot;
		moduleHandlers = robot.getModuleHandlers();
	}
	
	public abstract void sendParameters();
	
	public String getControllerName() {
		return ControllerName;
	}

}
