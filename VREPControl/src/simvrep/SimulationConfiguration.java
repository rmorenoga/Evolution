package simvrep;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.XMLConfiguration;





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
    private static double maxPhaseControl;
    private static double minPhaseControl;
    private static double maxAmplitudeControl;
    private static double minAmplitudeControl;
    private static double maxAngularFreqControl;
    private static double minAngularFreqControl;

    private static double maxFrequencyModulator;
    private static double minFrequencyModulator;
    private static double maxAmplitudeModulator;
    private static double minAmplitudeModulator;
    
    private static double maxWeighing;
    private static double minWeighing;
    
    private static boolean usePhaseControl;
    private static boolean useAmplitudeControl;
    private static boolean useAngularFControl;

    private static boolean useAmplitudeModulator;
    private static boolean useFrequencyModulator;
    private static boolean useBranchFrequencyControl;

    
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
            if(SimulationConfiguration.simulator.toLowerCase().contentEquals("vrep"))
                SimulationConfiguration.vrepPath = config.getString("Simulation.Vrep.VrepPath");
            
            //World Parameters
            SimulationConfiguration.numberOfWorldsBase = config.getInt("Worlds.NumberOfWorldsBase");
            for(int i=0; i<numberOfWorldsBase; i++){
                worldsBase.add(config.getString("Worlds.WorldBase" + i));
                if(worldsBase.get(i).isEmpty() || worldsBase.get(i)==null || worldsBase.get(i).equals(""))
                    throw new Exception("Error loading the worlds/scenes: \n");
            }
            SimulationConfiguration.functionToEvaluateWorlds = config.getString("Worlds.FunctionToEvaluateWorlds");
    
          //Control Parameters
            //TODO Adjust to meet CPG parameters
            SimulationConfiguration.maxPhaseControl = config.getDouble("Control.PhaseControl.MaxValue");
            SimulationConfiguration.minPhaseControl = config.getDouble("Control.PhaseControl.MinValue");
            SimulationConfiguration.maxAmplitudeControl = config.getDouble("Control.AmplitudeControl.MaxValue");
            SimulationConfiguration.minAmplitudeControl = config.getDouble("Control.AmplitudeControl.MinValue");
            SimulationConfiguration.maxAngularFreqControl = config.getDouble("Control.AngularFreqControl.MaxValue");
            SimulationConfiguration.minAngularFreqControl = config.getDouble("Control.AngularFreqControl.MinValue");

            SimulationConfiguration.maxAmplitudeModulator = config.getDouble("Control.AmplitudeModulator.MaxValue");
            SimulationConfiguration.minAmplitudeModulator = config.getDouble("Control.AmplitudeModulator.MinValue");
            SimulationConfiguration.maxFrequencyModulator = config.getDouble("Control.FrequencyModulator.MaxValue");
            SimulationConfiguration.minFrequencyModulator = config.getDouble("Control.FrequencyModulator.MinValue");

            SimulationConfiguration.maxWeighing = config.getDouble("Control.Weighing.MaxValue");
            SimulationConfiguration.minWeighing = config.getDouble("Control.Weighing.MinValue");


            SimulationConfiguration.usePhaseControl = config.getBoolean("Control.UsePhaseControl");
            SimulationConfiguration.useAmplitudeControl = config.getBoolean("Control.UseAmplitudeControl");
            SimulationConfiguration.useAngularFControl = config.getBoolean("Control.UseAngularFreqControl");

            SimulationConfiguration.useAmplitudeModulator = config.getBoolean("Control.UseAmplitudeModulator");
            SimulationConfiguration.useFrequencyModulator = config.getBoolean("Control.UseFrequencyModulator");
            SimulationConfiguration.useBranchFrequencyControl = config.getBoolean("Control.BranchFrequencyModulator");
            if(useBranchFrequencyControl && !useFrequencyModulator){
                throw new Exception("Error in the parameters of frequency control: \n" +
                        "useFrequencyControl: " + useFrequencyModulator +
                        ";  useBranchFrequencyControl: " + useBranchFrequencyControl);
            }
            
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

	public static void setVrep(Simulator vrep) {
		SimulationConfiguration.vrep = vrep;
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

	public static double getMaxPhaseControl() {
		return maxPhaseControl;
	}

	public static void setMaxPhaseControl(double maxPhaseControl) {
		SimulationConfiguration.maxPhaseControl = maxPhaseControl;
	}

	public static double getMinPhaseControl() {
		return minPhaseControl;
	}

	public static void setMinPhaseControl(double minPhaseControl) {
		SimulationConfiguration.minPhaseControl = minPhaseControl;
	}

	public static double getMaxAmplitudeControl() {
		return maxAmplitudeControl;
	}

	public static void setMaxAmplitudeControl(double maxAmplitudeControl) {
		SimulationConfiguration.maxAmplitudeControl = maxAmplitudeControl;
	}

	public static double getMinAmplitudeControl() {
		return minAmplitudeControl;
	}

	public static void setMinAmplitudeControl(double minAmplitudeControl) {
		SimulationConfiguration.minAmplitudeControl = minAmplitudeControl;
	}

	public static double getMaxAngularFreqControl() {
		return maxAngularFreqControl;
	}

	public static void setMaxAngularFreqControl(double maxAngularFreqControl) {
		SimulationConfiguration.maxAngularFreqControl = maxAngularFreqControl;
	}

	public static double getMinAngularFreqControl() {
		return minAngularFreqControl;
	}

	public static void setMinAngularFreqControl(double minAngularFreqControl) {
		SimulationConfiguration.minAngularFreqControl = minAngularFreqControl;
	}

	public static double getMaxFrequencyModulator() {
		return maxFrequencyModulator;
	}

	public static void setMaxFrequencyModulator(double maxFrequencyModulator) {
		SimulationConfiguration.maxFrequencyModulator = maxFrequencyModulator;
	}

	public static double getMinFrequencyModulator() {
		return minFrequencyModulator;
	}

	public static void setMinFrequencyModulator(double minFrequencyModulator) {
		SimulationConfiguration.minFrequencyModulator = minFrequencyModulator;
	}

	public static double getMaxAmplitudeModulator() {
		return maxAmplitudeModulator;
	}

	public static void setMaxAmplitudeModulator(double maxAmplitudeModulator) {
		SimulationConfiguration.maxAmplitudeModulator = maxAmplitudeModulator;
	}

	public static double getMinAmplitudeModulator() {
		return minAmplitudeModulator;
	}

	public static void setMinAmplitudeModulator(double minAmplitudeModulator) {
		SimulationConfiguration.minAmplitudeModulator = minAmplitudeModulator;
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

	public static boolean isUsePhaseControl() {
		return usePhaseControl;
	}

	public static void setUsePhaseControl(boolean usePhaseControl) {
		SimulationConfiguration.usePhaseControl = usePhaseControl;
	}

	public static boolean isUseAmplitudeControl() {
		return useAmplitudeControl;
	}

	public static void setUseAmplitudeControl(boolean useAmplitudeControl) {
		SimulationConfiguration.useAmplitudeControl = useAmplitudeControl;
	}

	public static boolean isUseAngularFControl() {
		return useAngularFControl;
	}

	public static void setUseAngularFControl(boolean useAngularFControl) {
		SimulationConfiguration.useAngularFControl = useAngularFControl;
	}

	public static boolean isUseAmplitudeModulator() {
		return useAmplitudeModulator;
	}

	public static void setUseAmplitudeModulator(boolean useAmplitudeModulator) {
		SimulationConfiguration.useAmplitudeModulator = useAmplitudeModulator;
	}

	public static boolean isUseFrequencyModulator() {
		return useFrequencyModulator;
	}

	public static void setUseFrequencyModulator(boolean useFrequencyModulator) {
		SimulationConfiguration.useFrequencyModulator = useFrequencyModulator;
	}

	public static boolean isUseBranchFrequencyControl() {
		return useBranchFrequencyControl;
	}

	public static void setUseBranchFrequencyControl(boolean useBranchFrequencyControl) {
		SimulationConfiguration.useBranchFrequencyControl = useBranchFrequencyControl;
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
       
