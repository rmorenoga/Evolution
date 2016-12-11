package simvrep;

import java.util.logging.Logger;

import coppelia.IntW;
import coppelia.remoteApi;
//import modules.control.SinusoidalController;
import mpi.MPI;

public class Evaluator {
	private double chromosomeDouble[];
	private int nAttempts = 10;
    private int nModules;
    private boolean useMPI = false;
    private boolean guiOn = true;
    static Logger LOGGER = Logger.getLogger("failogger");
    private int SERVER_ID = 0; // Numer of the Vrep server
    private double maxSimulationTime = 3;
    private double timeStartSim = 3;
    
    private int status = -1;
    private double timeStep = 0.05;
    private String scene = null;
    private String vrepComand;
    private RobotBuilder robot;
    //private SinusoidalController controller;
    
    private remoteApi vrepApi;
    private int clientID;
    private double time;
    private static int eval = 0;
    
    public void setGuiOn(boolean guiOn) {
        this.guiOn = guiOn;
    }

    public void setMaxSimulationTime(double number) {
        this.maxSimulationTime = number;
    }

    public Evaluator(double[] cromo) {
        this(cromo, "");
    }
    
    
    public Evaluator(double[] cromo, String scene) {
    	
    	this.scene = scene;
        if (scene == null || scene.isEmpty() || scene.equals("")) {
            this.scene = SimulationConfiguration.getWorldsBase().get(0);
        }
        
        this.chromosomeDouble = cromo;
        
        if ((cromo.length + 3) % 9 == 0) {
            nModules = (cromo.length + 3) / 9;
        }else {
            System.err.println("Vrep Evaluator");
            System.err.println("Error in the number of modules nModules; cromo.length=" + cromo.length);
            System.exit(-1);
        }
        
        this.getSimulationConfigurationParameters();
        
        if (useMPI) {
            int base = (SimulationConfiguration.getJobId() % 319) * 100;
//            if ((base + 100) > 65535)
//                base = (base + 100) % 65535;
            SERVER_ID = base + MPI.COMM_WORLD.Rank();
        }
        
        if (SimulationConfiguration.isDebug()) {
            System.out.print("\nSERVER: " + this.SERVER_ID + " Individual (Vrep Evaluator): ");
            for (int i = 0; i < this.chromosomeDouble.length; i++) {
                System.out.print(this.chromosomeDouble[i] + ", ");
            }
        }
        
        int rank = 0;
        if (SimulationConfiguration.isUseMPI()) {
            rank = MPI.COMM_WORLD.Rank();
        }
        
        Simulator vrepSimulator = SimulationConfiguration.getVrep();
        if(vrepSimulator == null){
            System.out.println("vrep simulator is null, connecting to vrep...");
            vrepSimulator = new Simulator();
            vrepSimulator.connect2Vrep();
            vrepApi = vrepSimulator.getVrepApi();
            SimulationConfiguration.setVrep(vrepSimulator);
        }
        
        vrepApi = vrepSimulator.getVrepApi(); 
        clientID = vrepSimulator.getClientID();
        
        robot = new RobotBuilder(vrepApi, clientID, chromosomeDouble, this.scene);
        robot.createRobot();
        
        //controller = new SinusoidalController(vrepApi, clientID, robot);
        
    }
    
    public double evaluate() {
    	
    	//TODO Send control parameters before simulation start, can be done also when building the robot
    	
    	// start the simulation:
        vrepApi.simxStartSimulation(clientID, remoteApi.simx_opmode_oneshot_wait);
        
        //TODO Wait for simulation to finish
        
     // stop the simulation:
       // vrepApi.simxStopSimulation(clientID, remoteApi.simx_opmode_oneshot_wait);
        vrepApi.simxPauseSimulation(clientID, remoteApi.simx_opmode_oneshot_wait);
        //TODO Receive simulation results
        
     // Before closing the connection to V-REP, make sure that the last command sent out had time to arrive. You can guarantee this with (for example):
        IntW pingTime = new IntW(0);
        vrepApi.simxGetPingTime(clientID, pingTime);//TODO Do something with this
        
        
        
//      //Close the scene, We dont need to close the scene but just in case
//        int iter = 0;
//        int ret = vrepApi.simxCloseScene(clientID, remoteApi.simx_opmode_oneshot_wait);
//        while(ret != remoteApi.simx_return_ok && iter < 100){
//            ret = vrepApi.simxCloseScene(clientID, remoteApi.simx_opmode_oneshot_wait);
//            iter++;
//        }
//        if(ret != remoteApi.simx_return_ok)
//            System.err.println("The scene has not been closed after 100 trials.");
        
        double fitness = 0;
        
        
        return fitness;
    }
    
    private void getSimulationConfigurationParameters() {

        try {

            this.SERVER_ID = SimulationConfiguration.getServerId();
            this.maxSimulationTime = SimulationConfiguration.getMaxSimulationTime();
            this.useMPI = SimulationConfiguration.isUseMPI();
            this.timeStartSim = SimulationConfiguration.getTimeStartSim();
            this.nAttempts = SimulationConfiguration.getnAttempts();

        } catch (Exception e) {
            LOGGER.severe("Error loading the control parameters of the simulation.");
            System.out.println(e);
            System.exit(-1);
        }

    }
    
    public void getVrepPing(){
        IntW pingTime = new IntW(0);
        vrepApi.simxGetPingTime(clientID, pingTime);
        }

}


