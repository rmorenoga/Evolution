package simvrep;

import control.RobotController;

public class EvaluatorMT {
	
	private int nAttempts = 10;
	private double maxSimulationTime = 60;
	private String scene = null;
	private double chromosomeDouble[];
	private int nModules;
	private RobotBuilder robot;
	private RobotController controller;
	private Simulation sim;
	private float alpha;

	public void setMaxSimulationTime(double number) {
		this.maxSimulationTime = number;
	}
	
	public EvaluatorMT(double[] cromo, String scene, float[] parameters, Simulation sim, float alpha) {
		this.scene = scene;
		this.sim = sim;
		this.alpha = alpha;
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
		
		this.getSimulationConfigurationParameters();
		
		robot = new RobotBuilder(sim.getVrepApi(), sim.getClientID(), chromosomeDouble, this.scene);
		robot.createRobot();
		
		controller = new RobotController(sim.getVrepApi(), sim.getClientID(),robot,parameters);
		controller.sendParameters();
		
		char[][] subenvperm = new char[][] { { 's', 'l', 's', 'b', 's', 'r', 's' },
			{ 's', 'l', 's', 's', 'r', 's', 'b' }, { 'b', 's', 'l', 's', 's', 'r', 's' },
			{ 'b', 's', 'r', 's', 's', 'l', 's' }, { 's', 'r', 's', 's', 'l', 's', 'b' },
			{ 's', 'r', 's', 'b', 's', 'l', 's' }, { 's', 's' } };
			
			float width = randomWithRange(0.6f, 0.8f);
			
			sim.SendMaze(subenvperm[6], width);
		
	}
	
	
	public double evaluate() {
		// Array that receives fitness from the simulator or signals a crash
				float[] rfitness = new float[3];
		// Number of retries in case of simulator crash
				int maxTries = 5;

				// Retry if there is a simulator crash
				for (int j = 0; j < maxTries; j++) {

					try {
						rfitness = sim.RunSimulation(alpha);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// sim.Disconnect();

					if (rfitness[0] == -1) {
						 sim.Disconnect();
						 try {
							 Thread.sleep(3000);
						 }catch(Exception e){
							 
						 }
						 sim.RestartSim(j, "defaultmh.ttt");
						 boolean connected = false;
					 for (int k =0;k<maxTries && !connected;k++){
							 connected = sim.Connect();
						 }	
						 if (!connected){
							 System.err.println("Fatal error: could not connect to simulator: "+sim.simnumber);
							 System.exit(-1);
						 }
						 continue;


					}

					break;
				}


				return (double) rfitness[1];
			
			
	}
	
	
	
	private void getSimulationConfigurationParameters() {

		try {

			this.maxSimulationTime = SimulationConfiguration.getMaxSimulationTime();
			this.nAttempts = SimulationConfiguration.getnAttempts();

		} catch (Exception e) {
			System.err.println("Error loading the control parameters of the simulation.");
			System.out.println(e);
			System.exit(-1);
		}

	}
	
	float randomWithRange(float min, float max)
	{
	   double range = Math.abs(max - min);     
	   return (float)(Math.random() * range) + (min <= max ? min : max);
	}
	
}
