package simvrep;

import control.ParameterMask;
import control.RobotController;

public class EvaluatorMT {

	private char[] maze;
	private float mazewidth;
	private float mazeheight;
	private int mazeNBSteps;
	private String scene = null;
	private double chromosomeDouble[];
	private int nModules;
	private RobotBuilder robot;
	private RobotController controller;
	private Simulation sim;
	private float alpha;
	private ParameterMask parammask;
	// Number of retries in case of simulator crash
	int maxTries = 5;

	// public EvaluatorMT(double[] cromo, String scene, float[] parameters,
	// Simulation sim, float alpha, int extraparam, char[] maze, float
	// mazewidth) {
	public EvaluatorMT(double[] cromo, String scene, ParameterMask parammask, Simulation sim, float alpha, char[] maze,
			float mazewidth, float mazeheight, int mazeNBSteps) {
		this.scene = scene;
		this.sim = sim;
		this.alpha = alpha;
		this.maze = maze;
		this.mazewidth = mazewidth;
		this.mazeheight = mazeheight;
		this.mazeNBSteps = mazeNBSteps;
		this.parammask = parammask;
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

	public double evaluate() {
		// Array that receives fitness from the simulator or signals a crash
		float[] rfitness = new float[3];
		// double[] rfitnessd = new double[3];

		// Retry if there is a simulator crash
		for (int j = 0; j < maxTries; j++) {

			try {
				rfitness = sim.RunSimulation(alpha);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			if (rfitness[0] == -1) {
				restartsim(j);
				setup();
				continue;
			}
			break;
		}

		return (double) rfitness[1];

	}

	private void setup() {
		int ret;
		// Retry if there is a simulator crash
		for (int j = 0; j < maxTries; j++) {

			robot = new RobotBuilder(sim.getVrepApi(), sim.getClientID(), chromosomeDouble, this.scene);
			ret = robot.createRobot();

			controller = new RobotController(sim.getVrepApi(), sim.getClientID(), robot, parammask);
			controller.sendParameters();

			sim.SendMaze(maze, mazewidth, mazeheight,mazeNBSteps);

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

}
