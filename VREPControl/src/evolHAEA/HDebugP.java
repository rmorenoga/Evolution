package evolHAEA;

import java.util.ArrayList;
import java.util.List;

import represent.Robot;
import simvrep.Simulation;
import unalcol.optimization.OptimizationFunction;
import unalcol.types.collection.bitarray.BitArray;

public class HDebugP extends OptimizationFunction<double[]> {

	boolean DEBUG = false;

	protected List<Simulation> simulators;
	protected boolean Indv;
	protected int nmodules;
	protected int[] ori;
	protected int numberofmessages;
	protected int pack;
	protected int extraparam;

	
	protected BitArray servers;
	public float alpha = 0.7f;
	//protected boolean paralleleval = false;

	public HDebugP(int numberOfServers, List<Simulation> simulators, boolean Indv, int nmodules, int[] ori, int numberofmessages, int pack , int extraparam) {
		servers = new BitArray(numberOfServers, false);
		this.simulators = simulators;
		this.Indv = Indv;
		this.nmodules = nmodules;
		this.ori = ori;
		this.numberofmessages = numberofmessages;
		this.pack = pack;
		this.extraparam = extraparam;
		if (DEBUG) {
			System.out.println("Building HDebugP");
		}
		//paralleleval = true;
	}

/*	public HDebugP(int numberOfServers, float alpha) {
		if (DEBUG) {
			System.out.println("Building HDebugP");
		}
		servers = new BitArray(numberOfServers, false);
		this.alpha = alpha;
		//paralleleval = true;
	}*/

	public HDebugP(float alpha, Simulation sim, boolean Indv, int nmodules, int[] ori, int numberofmessages, int pack , int extraparam) {
		this.alpha = alpha;
		this.Indv = Indv;
		this.nmodules = nmodules;
		this.ori = ori;
		this.numberofmessages = numberofmessages;
		this.pack = pack;
		simulators = new ArrayList<Simulation>();
		simulators.add(sim);
		servers = new BitArray(1, false);
		this.extraparam = extraparam;
		//paralleleval = false;
	}

	public synchronized int getSimNumber() {
		if (DEBUG) {
			System.out.println("Using getSimNumber()");
		}
		for (int i = 0; i < servers.size(); ++i)
			if (!servers.get(i)) {
				servers.set(i, true);
				return i;
			}
		return -1;
	}

	public synchronized int waitforsim() {
		if (DEBUG) {
			System.out.println("Using waitforsim()");
		}
		int sim = -1;
		while (sim < 0)
			sim = getSimNumber();
		return sim;

	}
	

	public Double apply(double[] x) {
		if (DEBUG) {
			System.out.println("Using apply()");
		}
		Simulation sim;


		//if (paralleleval) {
		int	simulator = -1;
			simulator = waitforsim();
			System.out.println("Got sim: " + simulator);
		//}

		// Morphology Parameters
		int Numberofmodules = nmodules;
		int[] orientation = ori;

		// Control Parameters
		float[] CP;
		if (Indv){
			CP = new float[numberofmessages*pack*nmodules+extraparam];
			for (int i = 0; i< x.length; i++){
				CP[i] = (float) x[i];
			}
		}else{
			CP = new float[numberofmessages*pack+extraparam];
			
			
			// CPG set parameters for each hormone
			for (int i = 0; i < 21+extraparam; i++) {
				CP[i] = (float) x[i];
			}
			// CPG step parameters for each hormone
			for (int i = 21; i < 42; i++) {
				CP[i] = (float) x[i+extraparam] * 0.001f;
			}
		}
		

		// Array that receives fitness from the simulator or signals a crash
		float[] rfitness = new float[3];

		char[][] subenvperm = new char[][] { { 's', 'l', 's', 'b', 's', 'r', 's' },
				{ 's', 'l', 's', 's', 'r', 's', 'b' }, { 'b', 's', 'l', 's', 's', 'r', 's' },
				{ 'b', 's', 'r', 's', 's', 'l', 's' }, { 's', 'r', 's', 's', 'l', 's', 'b' },
				{ 's', 'r', 's', 'b', 's', 'l', 's' }, { 's', 's' } };

		Robot robot = new Robot(Numberofmodules, orientation, CP);

		//if (paralleleval) {
			sim = simulators.get(simulator);
		//}

		// Number of retries in case of simulator crash
		int maxTries = 5;

		// Retry if there is a simulator crash
		for (int j = 0; j < maxTries; j++) {
			
			float width = randomWithRange(0.6f, 0.8f);

			// if (sim.Connect()) {
			sim.prepareSignals(robot);
			sim.SendSignals();
			sim.SendMaze(subenvperm[6], width);

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
				 sim.RestartSim(j, "MRun.ttt");
				 boolean connected = false;
			 for (int k =0;k<maxTries && !connected;k++){
					 connected = sim.Connect();
				 }	
				 if (!connected){
					 System.err.println("Fatal error: could not connect to simulator: "+sim.simnumber);
					 System.exit(-1);
				 }
				 continue;
//				long startTime = System.currentTimeMillis();
//				long stopTime = System.currentTimeMillis();
//				long elapsedTime = 0;
//
//				elapsedTime = stopTime - startTime;
//
//				while (elapsedTime < 60000) {
//					stopTime = System.currentTimeMillis();
//					elapsedTime = stopTime - startTime;
//				}
//
//				System.exit(0);

			}

			// } else {
			// No connection could be established
			// System.out.println("Failed connecting to remote API server");
			// System.out.println("Trying again for the " + j + " time in " +
			// simulator);
			// continue;
			// }
			break;
		}

		//if (paralleleval) {
			servers.set(simulator, false);
		//}

		return (double) rfitness[1];
	}

	@Override
	public boolean isNonStationary() {
		return true;
	}
	
	/**
	 * Taken from http://stackoverflow.com/questions/7961788/math-random-explained and modified to use floats
	 * @param min the minimum included value
	 * @param max the maximum included value
	 * @return a random float value within the range specified by min and max
	 */
	
	float randomWithRange(float min, float max)
	{
	   double range = Math.abs(max - min);     
	   return (float)(Math.random() * range) + (min <= max ? min : max);
	}

}
