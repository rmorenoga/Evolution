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
	float[] extraparam;
	int extrap = -1;

	public RobotController(remoteApi vrep, int clientID, RobotBuilder robot, float[] parameters, float[] extraparam) {
		this(vrep, clientID, robot, parameters);
		this.extraparam = extraparam;
		this.extrap = extraparam.length;
		adjustextraParam();
		// System.out.println("extrap "+extrap);
	}
	
	public RobotController(remoteApi vrep, int clientID, RobotBuilder robot, ParameterMask parammask){
		this.vrep = vrep;
		this.clientID = clientID;
		this.robot = robot;
		moduleHandlers = robot.getModuleHandlersint();
		this.numberofModules = moduleHandlers.length;
		this.numberofParameters = SimulationConfiguration.getControllerparamnumber();
		connectedhandles = robot.getTree().getHandlerListint();
		connectedori = robot.getTree().getOriListint();
		
		parammask.growParam(numberofModules);
		
		if (parammask.getMaskedparameters().length >= numberofParameters * numberofModules) {
			this.parameters = parammask.getMaskedparameters();
		} else {
			System.err.println("RobotController");
			System.err.println("Error in the number of parameters, parameters lenght=" + parammask.getMaskedparameters().length);
			System.exit(-1);
		}
		
		if (parammask.getExtrap()>0){
			this.extrap = parammask.getExtrap();
			this.extraparam = parammask.getMaskextraparam();
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
		if (parameters.length >= numberofParameters * numberofModules) {
			this.parameters = parameters;
		} else {
			System.err.println("CPGController");
			System.err.println("Error in the number of parameters, parameters lenght=" + parameters.length);
			System.exit(-1);
		}
		adjustParam(SimulationConfiguration.getController());
	}

	private void adjustextraParam() {
		float[] grownextra = new float[extraparam.length];
		for (int i = 0; i < extraparam.length; i++) {
			grownextra[i] = ((extraparam[i] + 1) / 2);
		}
		extraparam = grownextra;
	}

	private void adjustParam(String controltype) {
		float[] grownparam = new float[parameters.length];
		if (controltype.contentEquals("CPG")) {
			float maxPhase = (float) SimulationConfiguration.getMaxPhase();
			float minPhase = (float) SimulationConfiguration.getMinPhase();
			float maxAmplitude = (float) SimulationConfiguration.getMaxAmplitude();
			float minAmplitude = (float) SimulationConfiguration.getMinAmplitude();
			float maxOffset = (float) SimulationConfiguration.getMaxOffset();
			float minOffset = (float) SimulationConfiguration.getMinOffset();
			// Assuming min raw parameter is -1 and max is 1
						// NewValue = (((OldValue - OldMin) * (NewMax - NewMin)) / (OldMax -
						// OldMin)) + NewMin
						// NewValue = (((OldValue - (-1)) * (NewMax - NewMin)) / (1 - (-1)))
						// + NewMin
			for (int i = 0; i < parameters.length; i = i + numberofParameters) {	
					grownparam[i] = (((parameters[i] + 1) * (maxAmplitude - minAmplitude)) / 2) + minAmplitude;
					grownparam[i+1] = (((parameters[i+1] + 1) * (maxOffset - minOffset)) / 2) + minOffset;
				for (int j = 0; j < 4; j++) {
					grownparam[i + j + 2] = (((parameters[i + j + 2] + 1) * (maxPhase - minPhase)) / 2) + minPhase;
				}
			}
			
		} else if(controltype.contentEquals("CPGH")||controltype.contentEquals("CPGHF")||controltype.contentEquals("CPGHLog")||controltype.contentEquals("CPGHFLog")) {
			float maxPhase = (float) SimulationConfiguration.getMaxPhase();
			float minPhase = (float) SimulationConfiguration.getMinPhase();
			float maxAmplitude = (float) SimulationConfiguration.getMaxAmplitude();
			float minAmplitude = (float) SimulationConfiguration.getMinAmplitude();
			float maxOffset = (float) SimulationConfiguration.getMaxOffset();
			float minOffset = (float) SimulationConfiguration.getMinOffset();
			float maxFreq = (float) SimulationConfiguration.getMaxAngularFreq();
			float minFreq = (float) SimulationConfiguration.getMinAngularFreq();

			// Assuming min raw parameter is -1 and max is 1
			// NewValue = (((OldValue - OldMin) * (NewMax - NewMin)) / (OldMax -
			// OldMin)) + NewMin
			// NewValue = (((OldValue - (-1)) * (NewMax - NewMin)) / (1 - (-1)))
			// + NewMin

			for (int i = 0; i < parameters.length; i = i + numberofParameters) {
				for (int j = 0; j < 5; j++) {
					grownparam[i + j] = (((parameters[i + j] + 1) * (maxAmplitude - minAmplitude)) / 2) + minAmplitude;
					grownparam[i + j + 5] = (((parameters[i + j + 5] + 1) * (maxOffset - minOffset)) / 2) + minOffset;
					grownparam[i + j + 30] = (((parameters[i + j + 30] + 1) * (maxFreq - minFreq)) / 2) + minFreq;
				}
				for (int j = 0; j < 20; j++) {
					grownparam[i + j + 10] = (((parameters[i + j + 10] + 1) * (maxPhase - minPhase)) / 2) + minPhase;
				}
			}
		}
		parameters = grownparam;
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

		if (extrap > 0) {
			FloatWA ExtraParam = new FloatWA(extraparam.length);
			System.arraycopy(extraparam, 0, ExtraParam.getArray(), 0, extraparam.length);
			char[] ep = ExtraParam.getCharArrayFromArray();
			strEP = new CharWA(ep.length);
			System.arraycopy(ep, 0, strEP.getArray(), 0, ep.length);
		}

		// Pause communication
		vrep.simxPauseCommunication(clientID, true);
		// Set Simulator signal values
		int result1 = vrep.simxSetStringSignal(clientID, "ControlParam", strCP, vrep.simx_opmode_oneshot);
		int result2 = vrep.simxSetStringSignal(clientID, "ConnHandles", strConn, vrep.simx_opmode_oneshot);
		int result3 = vrep.simxSetStringSignal(clientID, "ModHandles", strMH, vrep.simx_opmode_oneshot);
		int result4 = vrep.simxSetStringSignal(clientID, "ConnOri", strConnori, vrep.simx_opmode_oneshot);
		if (extrap > 0) {
			int result5 = vrep.simxSetStringSignal(clientID, "ExtraParam", strEP, vrep.simx_opmode_oneshot);
		}
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
