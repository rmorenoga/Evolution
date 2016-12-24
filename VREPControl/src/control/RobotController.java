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
	float[] parameters;

	protected RobotController(remoteApi vrep, int clientID, RobotBuilder robot, float[] parameters) {
		this.vrep = vrep;
		this.clientID = clientID;
		this.robot = robot;
		moduleHandlers = robot.getModuleHandlersint();
		this.numberofModules = moduleHandlers.length;
		if (parameters.length >= numberofParameters*numberofModules){
			this.parameters = parameters;
		} else {
			System.err.println("CPGController");
			System.err.println("Error in the number of parameters, parameters lenght=" + parameters.length);
			System.exit(-1);
		}
		
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
