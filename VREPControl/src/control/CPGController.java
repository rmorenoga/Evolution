package control;

import java.util.ArrayList;
import java.util.List;

import coppelia.CharWA;
import coppelia.FloatWA;
import coppelia.IntWA;
import coppelia.remoteApi;
import simvrep.RobotBuilder;

public class CPGController extends RobotController {
	
	/**
	 * Signals to set in the simulator
	 */
	public CharWA strCP;
	public CharWA strMH;
	public CharWA strConn;
	
	int[] connectedhandles;
	float[] parameters;
	public int numberofParameters = 6;//FIXME: Get from SimulationConfiguration class
	
	public CPGController(remoteApi vrep, int clientID, RobotBuilder robot, float[] parameters) {
		super(vrep, clientID, robot);
		
		controllerName = "CPG";
		connectedhandles = robot.getTree().getHandlerListint();//TODO: Get connected modules from robot description
		this.parameters = parameters;
				
	}
	
	public CPGController(remoteApi vrep, int clientID, RobotBuilder robot){
		super(vrep, clientID, robot);
		controllerName = "CPG";
		connectedhandles = robot.getTree().getHandlerListint();
		parameters = new float[numberofModules*numberofParameters];
	}
	


	public void sendParameters() {
		
		for (int i = 0; i < moduleHandlers.length ;i++){
			moduleHandlers[i] = moduleHandlers[i] + 1;
		}
		
		FloatWA ControlParam = new FloatWA(parameters.length);
		System.arraycopy(parameters,0,ControlParam.getArray(),0,parameters.length);
		char[] p = ControlParam.getCharArrayFromArray();
		strCP = new CharWA(p.length);
		System.arraycopy(p,0,strCP.getArray(),0,p.length);
		
		IntWA Connhandles = new IntWA(connectedhandles.length);
		System.arraycopy(connectedhandles,0,Connhandles.getArray(),0,connectedhandles.length);
		char[] q = Connhandles.getCharArrayFromArray();
		strConn = new CharWA(q.length);
		System.arraycopy(q,0,strConn.getArray(),0,q.length);
		
		IntWA Modhandles = new IntWA(moduleHandlers.length);
		System.arraycopy(moduleHandlers,0,Modhandles.getArray(),0,moduleHandlers.length);
		char[] r = Modhandles.getCharArrayFromArray();
		strMH = new CharWA(r.length);
		System.arraycopy(r,0,strMH.getArray(),0,r.length);
		
		// Set Simulator signal values
		int result1 = vrep.simxSetStringSignal(clientID, "ControlParam", strCP, vrep.simx_opmode_blocking);
		int result2 = vrep.simxSetStringSignal(clientID, "ConnHandles", strConn, vrep.simx_opmode_blocking);
		int result3 = vrep.simxSetStringSignal(clientID, "ModHandles", strMH, vrep.simx_opmode_blocking);
		
		
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
