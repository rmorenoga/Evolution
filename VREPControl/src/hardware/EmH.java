package hardware;

import java.util.Arrays;
import java.util.Scanner;

import org.json.JSONObject;

import unalcol.optimization.OptimizationFunction;
import unalcol.types.collection.bitarray.BitArray;

public class EmH extends OptimizationFunction<double[]>{
	
	protected int iteration = 0;
	protected int maxDistance = 10;
	protected int maxTime = 10;
	protected BitArray servers;
	protected JSONObject resultLogger = null;

	/**
	 * Calculates the fitness given a distance and a time, both measured in a hardware experiment
	 * @param maxDistance
	 * @param maxTime
	 */

	public EmH(int maxDistance, int maxTime, int numberOfServers) {
		this.maxDistance = maxDistance;
		this.maxTime = maxTime;
		this.servers = new BitArray(numberOfServers, false);
	}
	
	public EmH(int maxDistance, int maxTime,JSONObject resultLogger) {
		this.maxDistance = maxDistance;
		this.maxTime = maxTime;
		this.servers = new BitArray(1, false);
		this.resultLogger = resultLogger;
	}

	public boolean isNonStationary() {
		return true;
	}
	
	@Override
	public void update(int k) {
		super.update(k);
		this.iteration = k;
	}

	
	public Double apply(double[] individual){
		
		JSONObject eval = new JSONObject();
		
		eval.put("individual", individual);
		
		int threadInstance = waitforthread();
		
		double distance;
		double D;
		double time;
		double t;
		double fitness;
		
		System.out.println("Iteration = "+ iteration);
		System.out.println("Individual to be evaluated in " + threadInstance);
		System.out.println(Arrays.toString(individual));

	    Scanner scan = new Scanner( System.in );
	 
	    System.out.print("Enter the distance obtained in " + threadInstance +":");
	    distance = scan.nextDouble();
	    eval.put("distanceRaw", distance);
	    
	    System.out.print("Enter the time obtained in " + threadInstance +":");
	    time = scan.nextDouble();
	    eval.put("timeRaw", time);
	    
	    D = distance/maxDistance;
	    t = time/maxTime;
	    eval.put("D", D);
	    eval.put("t", t);
	    
	    fitness = D -(t/D);
	    
	    eval.put("fitness", fitness);
	    
	    servers.set(threadInstance, false);
	    
	    System.out.println("Fitness in "+ threadInstance + " = " + (-fitness));
	    
	    resultLogger.accumulate("Gen" + iteration, eval);
		
		return -fitness;
		
	}
	
	public synchronized int waitforthread() {
		int sim = -1;
		while (sim < 0)
			sim = getThreadNumber();
		return sim;

	}
	
	public synchronized int getThreadNumber() {
		for (int i = 0; i < servers.size(); ++i)
			if (!servers.get(i)) {
				servers.set(i, true);
				return i;
			}
		return -1;
	}
}
