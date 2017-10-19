package control;

import java.util.List;

import coppelia.CharWA;
import coppelia.FloatWA;
import coppelia.IntWA;
import coppelia.remoteApi;
import simvrep.RobotBuilder;
import simvrep.SimulationConfiguration;

public class RobotController {

	/**
	 * Signals to set in the simulator
	 */
	public CharWA strCP;
	public CharWA strEP;
	public CharWA strMH;
	public CharWA strConn;
	public CharWA strConnori;
	public CharWA strGenmodel;
	public CharWA strPropmodel;
	public CharWA strPropDirection;

	int[] connectedhandles;
	int[] connectedori;

	protected String controllerName;
	protected remoteApi vrep;
	protected int clientID;
	protected RobotBuilder robot;
	protected int[] moduleHandlers;
	protected int numberofModules;
	protected int numberofParameters;
	float[] parameters;
	private String Genmodel;
	private String Propmodel;
	private String PropDirection;

	
	public RobotController(remoteApi vrep, int clientID, RobotBuilder robot, ParameterMask parammask){
		this.vrep = vrep;
		this.clientID = clientID;
		this.robot = robot;
		moduleHandlers = robot.getModuleHandlersint();
		this.numberofModules = moduleHandlers.length;
		this.numberofParameters = parammask.getNumberofParameters();
		connectedhandles = robot.getTree().getHandlerListint();
		connectedori = robot.getTree().getOriListint();
		Genmodel = SimulationConfiguration.getGenmodel();
		Propmodel = SimulationConfiguration.getPropmodel();
		PropDirection = SimulationConfiguration.getPropdirection();
		
		parammask.growParam(numberofModules);
		
		if (parammask.getMaskedparameters().length >= numberofParameters * numberofModules) {
			this.parameters = parammask.getMaskedparameters();
		} else {
			System.err.println("RobotController");
			System.err.println("Error in the number of parameters, parameters lenght=" + parammask.getMaskedparameters().length);
			System.exit(-1);
		}
		
	}

	public RobotController(remoteApi vrep, int clientID, RobotBuilder robot, float[] parameters) {
		this.vrep = vrep;
		this.clientID = clientID;
		this.robot = robot;
		moduleHandlers = robot.getModuleHandlersint();
		this.numberofModules = moduleHandlers.length;
		this.numberofParameters = SimulationConfiguration.getControllerparamnumber();
		connectedhandles = robot.getTree().getHandlerListint();
		connectedori = robot.getTree().getOriListint();
		Genmodel = SimulationConfiguration.getGenmodel();
		Propmodel = SimulationConfiguration.getPropmodel();
		PropDirection = SimulationConfiguration.getPropdirection();		
		
		if (parameters.length >= numberofParameters * numberofModules) {
			this.parameters = parameters;
		} else {
			System.err.println("CPGController");
			System.err.println("Error in the number of parameters, parameters lenght=" + parameters.length);
			System.exit(-1);
		}
	}


	public void sendParameters() {

		for (int i = 0; i < moduleHandlers.length; i++) {
			moduleHandlers[i] = moduleHandlers[i] + 1;
		}
		
		for (int i = 0; i < connectedhandles.length; i++) {
			if(connectedhandles[i] != -1){
			connectedhandles[i] = connectedhandles[i] + 1;
			}
		}
		
		for (int i = 0; i < connectedori.length; i++) {
			if(connectedori[i] != -1){
			connectedori[i] = connectedori[i] + 1;
			}
		}

		FloatWA ControlParam = new FloatWA(parameters.length);
		System.arraycopy(parameters, 0, ControlParam.getArray(), 0, parameters.length);
		char[] p = ControlParam.getCharArrayFromArray();
		strCP = new CharWA(p.length);
		System.arraycopy(p, 0, strCP.getArray(), 0, p.length);

		IntWA Connhandles = new IntWA(connectedhandles.length);
		System.arraycopy(connectedhandles, 0, Connhandles.getArray(), 0, connectedhandles.length);
		char[] q = Connhandles.getCharArrayFromArray();
		strConn = new CharWA(q.length);
		System.arraycopy(q, 0, strConn.getArray(), 0, q.length);
		
		IntWA Connori = new IntWA(connectedori.length);
		System.arraycopy(connectedori, 0, Connori.getArray(), 0, connectedori.length);
		char[] s = Connori.getCharArrayFromArray();
		strConnori = new CharWA(s.length);
		System.arraycopy(s, 0, strConnori.getArray(), 0, s.length);

		IntWA Modhandles = new IntWA(moduleHandlers.length);
		System.arraycopy(moduleHandlers, 0, Modhandles.getArray(), 0, moduleHandlers.length);
		char[] r = Modhandles.getCharArrayFromArray();
		strMH = new CharWA(r.length);
		System.arraycopy(r, 0, strMH.getArray(), 0, r.length);
		
		strGenmodel = new CharWA(Genmodel);
		strPropmodel = new CharWA(Propmodel);
		strPropDirection = new CharWA(PropDirection);
		
		// Pause communication
		vrep.simxPauseCommunication(clientID, true);
		
		// Set Simulator signal values
		int result1 = vrep.simxSetStringSignal(clientID, "ControlParam", strCP, vrep.simx_opmode_oneshot);
		int result2 = vrep.simxSetStringSignal(clientID, "ConnHandles", strConn, vrep.simx_opmode_oneshot);
		int result3 = vrep.simxSetStringSignal(clientID, "ModHandles", strMH, vrep.simx_opmode_oneshot);
		int result4 = vrep.simxSetStringSignal(clientID, "ConnOri", strConnori, vrep.simx_opmode_oneshot);
		int result5 = vrep.simxSetStringSignal(clientID, "Genmodel", strGenmodel, vrep.simx_opmode_oneshot);
		int result6 = vrep.simxSetStringSignal(clientID, "Propmodel", strPropmodel, vrep.simx_opmode_oneshot);
		int result7 = vrep.simxSetStringSignal(clientID, "PropDirection", strPropDirection, vrep.simx_opmode_oneshot);
		int result8 = vrep.simxSetIntegerSignal(clientID, "Nparameters", numberofParameters, vrep.simx_opmode_oneshot);
		
		
		// Unpause communication
		vrep.simxPauseCommunication(clientID, false);

	}

	public String getControllerName() {
		return controllerName;
	}

	public int getNumberofModules() {
		return numberofModules;
	}

	public int getNumberofParameters() {
		return numberofParameters;
	}

	public int getParameterSize() {
		return parameters.length;
	}

	public float[] getParameters() {
		return parameters;
	}

	public void setParameters(float[] parameters) {
		this.parameters = parameters;
	}
}
