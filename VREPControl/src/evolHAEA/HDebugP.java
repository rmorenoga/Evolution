package evolHAEA;

import represent.Robot;
import simvrep.Simulation;
import unalcol.optimization.OptimizationFunction;
import unalcol.types.collection.bitarray.BitArray;

public class HDebugP extends OptimizationFunction<double[]>{
	
	boolean DEBUG = true;
	
	protected BitArray servers;
	public float alpha = 0.7f;
	
	public HDebugP(int numberOfServers) {
		servers = new BitArray(numberOfServers, false);
		if (DEBUG){
			System.out.println("Building HDebugP");
		}
	}
	
	public synchronized int getSimNumber() {
		if (DEBUG){
			System.out.println("Using getSimNumber()");
		}
		for(int i = 0;i < servers.size(); ++i)
			if(!servers.get(i)) {
				servers.set(i, true);
				return i;
			}
		return -1;
	}
	
	HDebugP(int numberOfServers, float alpha) {
		if (DEBUG){
			System.out.println("Building HDebugP");
		}
		servers = new BitArray(numberOfServers, false);
		this.alpha = alpha;
	}
	
	public synchronized int waitforsim(){
		if (DEBUG){
			System.out.println("Using waitforsim()");
		}
		int sim = -1;
		while(sim < 0) sim = getSimNumber();
		return sim;
		
	}
	
	public Double apply(double[] x) {
		if (DEBUG){
			System.out.println("Using apply()");
		}
		int simulator = -1;
		simulator = waitforsim();
		
		System.out.println("Got sim: "+simulator);
		
		// Morphology Parameters
				int Numberofmodules = 8;
				int[] orientation = new int[] { 1, 0, 1, 0, 1, 0, 1, 0 };
				
				// Simulation Parameters
				int MaxTime = 30;

				// Control Parameters
				float[] CP = new float[42];
				
				//CPG set parameters for each hormone
				for (int i = 0; i < 21; i++) {
					CP[i] = (float) x[i];
				}
				//CPG step parameters for each hormone
				for (int i = 21; i < 42;i++){
					CP[i] = (float) x[i]*0.01f;
				}
				
				// Array that receives fitness from the simulator or signals a crash
				float[] rfitness = new float[3];

				char[][] subenvperm = new char[][] {
					{ 's', 'l', 's', 'b', 's', 'r', 's' },
					{ 's', 'l', 's', 's', 'r', 's', 'b' },
					{ 'b', 's', 'l', 's', 's', 'r', 's' },
					{ 'b', 's', 'r', 's', 's', 'l', 's' },
					{ 's', 'r', 's', 's', 'l', 's', 'b' },
					{ 's', 'r', 's', 'b', 's', 'l', 's' },
					{'s','s'}};

				Robot robot = new Robot(Numberofmodules, orientation, CP);

				Simulation sim = new Simulation(simulator, MaxTime, robot);
				
				// Number of retries in case of simulator crash
				int maxTries = 5;
				
				// Retry if there is a simulator crash
				for (int j = 0; j < maxTries; j++) {

					if (sim.Connect()) {

						sim.SendSignals();
						sim.SendMaze(subenvperm[6], 0.8f);

						try {
							rfitness = sim.RunSimulation(alpha);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						sim.Disconnect();

						if (rfitness[0] == -1) {
							sim.RestartSim(j, "MRun.ttt");
							continue;
						}

					} else {
						// No connection could be established
						System.out.println("Failed connecting to remote API server");
						System.out.println("Trying again for the " + j + " time in " + simulator);
						continue;
					}
					break;
				}
				
				servers.set(simulator, false);
				return (double) rfitness[1];
	}

}
