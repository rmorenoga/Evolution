package simvrep;

import control.ParameterMask;
import control.RobotController;
import coppelia.FloatWA;

public class EvaluatorMT {

	private char[] maze;
	private float mazewidth;
	private float mazeheight;
	private int mazeNBSteps;
	private boolean measureDToGoal = true;
	private boolean measureDToGoalByPart = true;
	private String scene = null;
	private double chromosomeDouble[];
	private int nModules;
	private RobotBuilder robot;
	private RobotController controller;
	private Simulation sim;
	private float alpha;
	private ParameterMask parammask;
	private float distancePercent;
	// Number of retries in case of simulator crash
	int maxTries = 5;

	// public EvaluatorMT(double[] cromo, String scene, float[] parameters,
	// Simulation sim, float alpha, int extraparam, char[] maze, float
	// mazewidth) {
	public EvaluatorMT(double[] cromo, String scene, ParameterMask parammask, Simulation sim, float alpha, char[] maze,
			float mazewidth, float mazeheight, int mazeNBSteps, boolean measureDToGoal, boolean measureDToGoalByPart) {
		this.scene = scene;
		this.sim = sim;
		this.alpha = alpha;
		this.maze = maze;
		this.mazewidth = mazewidth;
		this.mazeheight = mazeheight;
		this.mazeNBSteps = mazeNBSteps;
		this.measureDToGoal = measureDToGoal;
		this.measureDToGoalByPart = measureDToGoalByPart;
		this.parammask = parammask;
		this.distancePercent = 1;
		if (scene == null || scene.isEmpty() || scene.equals("")) {
			this.scene = SimulationConfiguration.getWorldsBase().get(0);
		}

		this.chromosomeDouble = cromo;

		if ((cromo.length + 3) % 8 == 0) {
			nModules = (cromo.length + 3) / 8;
		} else {
			System.err.println("Vrep Evaluator");
			System.err.println("Error in the number of modules nModules; cromo.length=" + cromo.length);
			System.exit(-1);
		}
		
		setup();

	}
	
	public EvaluatorMT(double[] cromo, String scene, ParameterMask parammask, Simulation sim, float alpha, char[] maze,
			float mazewidth, float mazeheight, int mazeNBSteps, float distancePercent){
		this(cromo, scene, parammask, sim, alpha, maze, mazewidth, mazeheight, mazeNBSteps, true, false);
		this.distancePercent = distancePercent;	
	}
	


	public double evaluate() {
		// Array that receives fitness from the simulator or signals a crash
		float[] result = new float[4];
		// double[] rfitnessd = new double[3];

		// Retry if there is a simulator crash
		for (int j = 0; j < maxTries; j++) {

			try {
				result = sim.RunSimulation();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			if (result[0] == -1) {
				restartsim(j);
				setup();
				continue;
			}
			break;
		}
		
		double fitness = (double)Calcfitness(result,alpha);

		return  fitness;

	}

	private void setup() {
		int ret;
		// Retry if there is a simulator crash
		for (int j = 0; j < maxTries; j++) {

			robot = new RobotBuilder(sim.getVrepApi(), sim.getClientID(), chromosomeDouble, this.scene);
			ret = robot.createRobot();

			controller = new RobotController(sim.getVrepApi(), sim.getClientID(), robot, parammask);
			controller.sendParameters();

			sim.SendMaze(maze, mazewidth, measureDToGoal,measureDToGoalByPart, mazeheight,mazeNBSteps,distancePercent);

			sim.SendMaxTime();

			if (ret == -1) {
				restartsim(j);
				continue;
			}
			break;
		}
	}
	
	private void restartsim(int j){
		sim.Disconnect();
		try {
			Thread.sleep(3000);
		} catch (Exception e) {

		}
		sim.RestartSim(j, scene);
		boolean connected = false;
		for (int k = 0; k < maxTries && !connected; k++) {
			connected = sim.Connect();
		}
		if (!connected) {
			System.err.println("Fatal error: could not connect to simulator: " + sim.simnumber);
			System.exit(-1);
		}
	}

	/**
	 * Calculate fitness based on the output coming from the simulator
	 * 
	 * @param results
	 *            the results coming from the simulator
	 * @param alpha
	 *            the weight of the distance in the fitness calculation
	 * @return the calculated fitness
	 */
	private float Calcfitness(float[] results, float alpha) {


		float fitness = 10000f;
		float beta = 1 - alpha;
		
		if (measureDToGoal){
			if (results[1] == 1) {
				// The robot could get out of the maze so the simulator returns the time spent normalized
				fitness = beta * results[3];
			} else if (results[1] == 0) {
				// The robot could not get out of the maze so the fitness is the
				// distance to goal + the maximum time allowed
				fitness = alpha * results[2] + beta * 1.0f;
			}
		}else{
			if (results[1] == 1) {
				// The robot could get out of the maze so the simulator returns the time spent normalized
				fitness = 1-results[3]/(2);
			} else if (results[1] == 0) {
				// The robot could not get out of the maze so the fitness is the
				// distance to goal + the maximum time allowed
				fitness = results[2]-results[3]/(1+results[2]);
			}
		}

		

		return fitness;

	}

	
}
