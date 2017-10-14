package simvrep;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.lang.SystemUtils;





/**
 * SimulationConfiguration stores all the parameters of the simulation. 
 * <p>
 * The parameters of the simulation are loaded, when the program starts, from
 * simulationControl.xml. This file has to be placed in the same 
 * working directory as the main program. It throws an exception if a 
 * parameter is not well defined in the file.
 * <p> 
 * 
 * SimulationConfiguration.java
 * Created on 
 * @author Andres Faiña <anfv  at itu.dk>
 * Modified by 
 * @author Rodrigo Moreno <rmorenoga at unal.edu.co>
*/
public class SimulationConfiguration {
	
	//Parameters of the simulation
    private static int serverId;
    private static int waitTimeLoadSimulator;
    private static String simulator;
    private static double maxSimulationTime;
    private static double timeStartSim;
    private static int nAttempts;
    private static boolean useMPI;
    private static double poseUpdateRate;
    private static String vrepPath;
    private static int vrepStartingPort = 19997;
    private static Simulator vrep = null;
    private static Simulation vrepsim = null;
    private static SceneBuilder scene = null;
    private static RobotBuilder robot = null;
    
  public static RobotBuilder getRobot() {
		return robot;
	}

	public static void setRobot(RobotBuilder robot) {
		SimulationConfiguration.robot = robot;
	}

public static SceneBuilder getScene() {
	//if (scene != null){
		//System.out.println("Getting scene with rank: "+scene.getRank());
	//}
		return scene;
	}

	public static void setScene(SceneBuilder scene) {
		SimulationConfiguration.scene = scene;
	}

	//Parametes of the modules 
    private static String moduleSet;
    
  //Parameters of the tree
    private static int nMaxModulesIni;
    private static boolean fistModulesBase;
    private static boolean manipulatorBase;
    private static int firstNumConnections = 1000;
    private static int minTypeModules;
    private static int maxTypeModules;
    private static int nMaxConnections;
    
  //Control Parameters
  //TODO Adjust parameters to meet CPG parameters (our needs)
    
    private static String controller;
    private static int controllerparamnumber;
    private static double maxPhase;
    private static double minPhase;
    private static double maxAmplitude;
    private static double minAmplitude;
    private static double maxAngularFreq;
    private static double minAngularFreq;
    private static double maxOffset;
    private static double minOffset;

    
    private static double maxWeighing;
    private static double minWeighing;
    
    
  //various
    private static boolean debug;
    private static int jobId = 0;
    private static int maxModules = 1;
    
  //World/Scene Parameters
    private static int numberOfWorldsBase = 1;
    private static List<String> worldsBase = new ArrayList<String>();
    private static String functionToEvaluateWorlds = "min";
    
    static {
        try {

            XMLConfiguration config = new XMLConfiguration("./simulationConfig.xml");
            
            //Module Set
            SimulationConfiguration.moduleSet = config.getString("ModuleSet");
            
          //Simulation Parameters
            SimulationConfiguration.simulator = config.getString("Simulation.Simulator");
            SimulationConfiguration.serverId = config.getInt("Simulation.ServerId");
            SimulationConfiguration.waitTimeLoadSimulator = config.getInt("Simulation.WaitTimeLoadSimulator");
            SimulationConfiguration.maxSimulationTime = config.getDouble("Simulation.MaxSimulationTime");
            SimulationConfiguration.timeStartSim = config.getDouble("Simulation.TimeStartSimulation");
            SimulationConfiguration.nAttempts = config.getInt("Simulation.Attempts");
            SimulationConfiguration.useMPI = config.getBoolean("Simulation.UseMPI");
            SimulationConfiguration.poseUpdateRate = config.getDouble("Simulation.PoseUpdateRate");
            if(SimulationConfiguration.simulator.toLowerCase().contentEquals("vrep")){
            	if(SystemUtils.IS_OS_WINDOWS){
            		SimulationConfiguration.vrepPath = config.getString("Simulation.Vrep.VrepPathW");
            	} else {
            		SimulationConfiguration.vrepPath = config.getString("Simulation.Vrep.VrepPathL");
            	}
            }
            	
                
            
            
            //World Parameters
            SimulationConfiguration.numberOfWorldsBase = config.getInt("Worlds.NumberOfWorldsBase");
            for(int i=0; i<numberOfWorldsBase; i++){
                worldsBase.add(config.getString("Worlds.WorldBase" + i));
                if(worldsBase.get(i).isEmpty() || worldsBase.get(i)==null || worldsBase.get(i).equals(""))
                    throw new Exception("Error loading the worlds/scenes: \n");
            }
            SimulationConfiguration.functionToEvaluateWorlds = config.getString("Worlds.FunctionToEvaluateWorlds");
    
          //Control Parameters

            
            SimulationConfiguration.controller = config.getString("Control.Controller");
            if (SimulationConfiguration.controller.contentEquals("CPG")){
            	SimulationConfiguration.controllerparamnumber = 6;
            }else if (SimulationConfiguration.controller.contentEquals("CPGH") || SimulationConfiguration.controller.contentEquals("CPGHF") || SimulationConfiguration.controller.contentEquals("CPGHLog")|| SimulationConfiguration.controller.contentEquals("CPGHFLog")){
            	SimulationConfiguration.controllerparamnumber = 49;
            }
            
            
            SimulationConfiguration.maxPhase = Math.PI*config.getDouble("Control.Phase.MaxValue")/180;
            SimulationConfiguration.minPhase = Math.PI*config.getDouble("Control.Phase.MinValue")/180;
            SimulationConfiguration.maxAmplitude = config.getDouble("Control.Amplitude.MaxValue");
            SimulationConfiguration.minAmplitude = config.getDouble("Control.Amplitude.MinValue");
            SimulationConfiguration.maxAngularFreq = config.getDouble("Control.AngularFreq.MaxValue");
            SimulationConfiguration.minAngularFreq = config.getDouble("Control.AngularFreq.MinValue");
            SimulationConfiguration.maxOffset = config.getDouble("Control.Offset.MaxValue");
            SimulationConfiguration.minOffset = config.getDouble("Control.Offset.MinValue");
            
            SimulationConfiguration.maxWeighing = config.getDouble("Control.Weighing.MaxValue");
            SimulationConfiguration.minWeighing = config.getDouble("Control.Weighing.MinValue");

            
          //Tree parameters
            SimulationConfiguration.nMaxModulesIni = config.getInt("Tree.NMaxModulesIni");
            SimulationConfiguration.fistModulesBase = config.getBoolean("Tree.FirstModuleBase");
            SimulationConfiguration.manipulatorBase = config.getBoolean("Tree.ManipulatorBase");
            SimulationConfiguration.firstNumConnections = config.getInt("Tree.FirstNumConnections");
            SimulationConfiguration.minTypeModules = config.getInt("Tree.TypeModules.MinValue");
            SimulationConfiguration.maxTypeModules = config.getInt("Tree.TypeModules.MaxValue");
            SimulationConfiguration.nMaxConnections = config.getInt("Tree.NMaxConnections");
            
          //Various
            SimulationConfiguration.debug = config.getBoolean("Debug");

            SimulationConfiguration.maxModules = (config.getInt("Tree.MaxModules") + 3) / 9;
            
        } catch (Exception e) {
            //Error loading the parameters of the simulation
            System.err.println("Error loading the parameters of the simulation.");
            System.out.println(e);
            System.exit(-1);
        }
    }
    
    public static int getControllerparamnumber() {
		return controllerparamnumber;
	}

	public static void setControllerparamnumber(int controllerparamnumber) {
		SimulationConfiguration.controllerparamnumber = controllerparamnumber;
	}

	public static String getController() {
		return controller;
	}

	public static void setController(String controller) {
		SimulationConfiguration.controller = controller;
	}

	public SimulationConfiguration() {
    }

	public static int getServerId() {
		return serverId;
	}

	public static void setServerId(int serverId) {
		SimulationConfiguration.serverId = serverId;
	}

	public static int getWaitTimeLoadSimulator() {
		return waitTimeLoadSimulator;
	}

	public static void setWaitTimeLoadSimulator(int waitTimeLoadSimulator) {
		SimulationConfiguration.waitTimeLoadSimulator = waitTimeLoadSimulator;
	}

	public static String getSimulator() {
		return simulator;
	}

	public static void setSimulator(String simulator) {
		SimulationConfiguration.simulator = simulator;
	}

	public static double getMaxSimulationTime() {
		return maxSimulationTime;
	}

	public static void setMaxSimulationTime(double maxSimulationTime) {
		SimulationConfiguration.maxSimulationTime = maxSimulationTime;
	}

	public static double getTimeStartSim() {
		return timeStartSim;
	}

	public static void setTimeStartSim(double timeStartSim) {
		SimulationConfiguration.timeStartSim = timeStartSim;
	}

	public static int getnAttempts() {
		return nAttempts;
	}

	public static void setnAttempts(int nAttempts) {
		SimulationConfiguration.nAttempts = nAttempts;
	}

	public static boolean isUseMPI() {
		return useMPI;
	}

	public static void setUseMPI(boolean useMPI) {
		SimulationConfiguration.useMPI = useMPI;
	}

	public static double getPoseUpdateRate() {
		return poseUpdateRate;
	}

	public static void setPoseUpdateRate(double poseUpdateRate) {
		SimulationConfiguration.poseUpdateRate = poseUpdateRate;
	}

	public static String getVrepPath() {
		return vrepPath;
	}

	public static void setVrepPath(String vrepPath) {
		SimulationConfiguration.vrepPath = vrepPath;
	}

	public static int getVrepStartingPort() {
		return vrepStartingPort;
	}

	public static void setVrepStartingPort(int vrepStartingPort) {
		SimulationConfiguration.vrepStartingPort = vrepStartingPort;
	}

	public static Simulator getVrep() {
		return vrep;
	}
	
	public static Simulation getVrepsim(){
		return vrepsim;
	}

	public static void setVrep(Simulator vrep) {
		SimulationConfiguration.vrep = vrep;
	}
	
	public static void setVrepsim(Simulation vrepsim){
		SimulationConfiguration.vrepsim = vrepsim;
	}

	public static String getModuleSet() {
		return moduleSet;
	}

	public static void setModuleSet(String moduleSet) {
		SimulationConfiguration.moduleSet = moduleSet;
	}

	public static int getnMaxModulesIni() {
		return nMaxModulesIni;
	}

	public static void setnMaxModulesIni(int nMaxModulesIni) {
		SimulationConfiguration.nMaxModulesIni = nMaxModulesIni;
	}

	public static boolean isFistModulesBase() {
		return fistModulesBase;
	}

	public static void setFistModulesBase(boolean fistModulesBase) {
		SimulationConfiguration.fistModulesBase = fistModulesBase;
	}

	public static boolean isManipulatorBase() {
		return manipulatorBase;
	}

	public static void setManipulatorBase(boolean manipulatorBase) {
		SimulationConfiguration.manipulatorBase = manipulatorBase;
	}

	public static int getFirstNumConnections() {
		return firstNumConnections;
	}

	public static void setFirstNumConnections(int firstNumConnections) {
		SimulationConfiguration.firstNumConnections = firstNumConnections;
	}

	public static int getMinTypeModules() {
		return minTypeModules;
	}

	public static void setMinTypeModules(int minTypeModules) {
		SimulationConfiguration.minTypeModules = minTypeModules;
	}

	public static int getMaxTypeModules() {
		return maxTypeModules;
	}

	public static void setMaxTypeModules(int maxTypeModules) {
		SimulationConfiguration.maxTypeModules = maxTypeModules;
	}

	public static int getnMaxConnections() {
		return nMaxConnections;
	}

	public static void setnMaxConnections(int nMaxConnections) {
		SimulationConfiguration.nMaxConnections = nMaxConnections;
	}

	

	





	public static double getMaxWeighing() {
		return maxWeighing;
	}

	public static void setMaxWeighing(double maxWeighing) {
		SimulationConfiguration.maxWeighing = maxWeighing;
	}

	public static double getMinWeighing() {
		return minWeighing;
	}

	public static void setMinWeighing(double minWeighing) {
		SimulationConfiguration.minWeighing = minWeighing;
	}


	public static double getMaxPhase() {
		return maxPhase;
	}

	public static void setMaxPhase(double maxPhase) {
		SimulationConfiguration.maxPhase = maxPhase;
	}

	public static double getMinPhase() {
		return minPhase;
	}

	public static void setMinPhase(double minPhase) {
		SimulationConfiguration.minPhase = minPhase;
	}

	public static double getMaxAmplitude() {
		return maxAmplitude;
	}

	public static void setMaxAmplitude(double maxAmplitude) {
		SimulationConfiguration.maxAmplitude = maxAmplitude;
	}

	public static double getMinAmplitude() {
		return minAmplitude;
	}

	public static void setMinAmplitude(double minAmplitude) {
		SimulationConfiguration.minAmplitude = minAmplitude;
	}

	public static double getMaxAngularFreq() {
		return maxAngularFreq;
	}

	public static void setMaxAngularFreq(double maxAngularFreq) {
		SimulationConfiguration.maxAngularFreq = maxAngularFreq;
	}

	public static double getMinAngularFreq() {
		return minAngularFreq;
	}

	public static void setMinAngularFreq(double minAngularFreq) {
		SimulationConfiguration.minAngularFreq = minAngularFreq;
	}

	public static double getMaxOffset() {
		return maxOffset;
	}

	public static void setMaxOffset(double maxOffset) {
		SimulationConfiguration.maxOffset = maxOffset;
	}

	public static double getMinOffset() {
		return minOffset;
	}

	public static void setMinOffset(double minOffset) {
		SimulationConfiguration.minOffset = minOffset;
	}

	public static boolean isDebug() {
		return debug;
	}

	public static void setDebug(boolean debug) {
		SimulationConfiguration.debug = debug;
	}

	public static int getJobId() {
		return jobId;
	}

	public static void setJobId(int jobId) {
		SimulationConfiguration.jobId = jobId;
	}

	public static int getMaxModules() {
		return maxModules;
	}

	public static void setMaxModules(int maxModules) {
		SimulationConfiguration.maxModules = maxModules;
	}

	public static int getNumberOfWorldsBase() {
		return numberOfWorldsBase;
	}

	public static void setNumberOfWorldsBase(int numberOfWorldsBase) {
		SimulationConfiguration.numberOfWorldsBase = numberOfWorldsBase;
	}

	public static List<String> getWorldsBase() {
		return worldsBase;
	}

	public static void setWorldsBase(List<String> worldsBase) {
		SimulationConfiguration.worldsBase = worldsBase;
	}

	public static String getFunctionToEvaluateWorlds() {
		return functionToEvaluateWorlds;
	}

	public static void setFunctionToEvaluateWorlds(String functionToEvaluateWorlds) {
		SimulationConfiguration.functionToEvaluateWorlds = functionToEvaluateWorlds;
	}


    
}
       
