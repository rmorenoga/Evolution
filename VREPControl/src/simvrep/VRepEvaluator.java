package simvrep;

import control.RobotController;
import maze.Maze;
import mixed.MixedGenome;
import unalcol.types.collection.bitarray.BitArray;
import unalcol.types.collection.vector.Vector;

public class VRepEvaluator {
	
	private Simulation simulation;
	private RobotBuilder robot;
	private RobotController controller;
	private SimulationSettings settings;
	private float[] result = new float[4];

	public VRepEvaluator(Simulation simulation, SimulationSettings settings) {
		this.simulation = simulation;
		this.settings = settings;
	}

	public void configure(double[] individual, double[] morphology, Maze maze) {
		
		float[] floatAnnWeights = new float[individual.length];

		for (int i = 0; i < individual.length; i++) {
			floatAnnWeights[i] = (float) individual[i];
		}
		
		int ret;
		// Retry if there is a simulator crash
		for (int j = 0; j < settings.maxTries; j++) {

			robot = new RobotBuilder(simulation.getVrepApi(), simulation.getClientID(), morphology);
			ret = robot.createRobot();

			controller = new RobotController(simulation.getVrepApi(), simulation.getClientID(), robot, floatAnnWeights,settings.individualParameters,settings.logPath);
			controller.sendParameters();
			
			if (!settings.shortChallenge)
				simulation.SendMaze(maze.structure, maze.width, settings.measureDToGoal,settings.measureDToGoalByPart, maze.height,maze.nBSteps);
			else
				simulation.SendMaze(maze.structure, maze.width, settings.measureDToGoal,settings.measureDToGoalByPart, maze.height,maze.nBSteps, settings.environmentFraction);
			
			
			simulation.SendMaxTime(settings.maxTime);

			if (ret == -1)
				restartsim();
			else
				j = settings.maxTries;
		}
		
	}
	
	private void restartsim(){
		simulation.Disconnect();
		try {
			Thread.sleep(3000);
		} catch (Exception e) {

		}
		simulation.RestartSim(settings.scene);
		boolean connected = false;
		for (int k = 0; k < settings.maxTries && !connected; k++) {
			connected = simulation.Connect();
		}
		if (!connected) {
			System.err.println("Fatal error: could not connect to simulator: " + simulation.simnumber);
			System.exit(-1);
		}
	}

	public int run() {
		try {
			result = simulation.RunSimulation();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (result[0] == -1) {
			restartsim();
			return -1;
		}
		return 0;
	}

	public Double evaluate() {
		
		double fitness = 1000;
		float alpha = settings.alpha;
		float beta = 1 - alpha;
		
		if (settings.measureDToGoal){
			if (result[1] == 1) {
				// The robot could get out of the maze so the simulator returns the time spent normalized
				fitness = beta * result[3];
			} else if (result[1] == 0) {
				// The robot could not get out of the maze so the fitness is the
				// distance to goal + the maximum time allowed
				fitness = alpha * result[2] + beta * 1.0f;
			}
		}else{
			if (result[1] == 1) {
				// The robot could get out of the maze so the simulator returns the time spent normalized
				fitness = 1-result[3]/(2);
			} else if (result[1] == 0) {
				// The robot could not get out of the maze so the fitness is the
				// distance to goal + the maximum time allowed
				fitness = result[2]-result[3]/(1+result[2]);
			}
		}	

		return fitness;

	}

	public Simulation getSimulation() {
		return simulation;
	}
	
	

}
