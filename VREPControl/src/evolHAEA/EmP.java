package evolHAEA;

import java.util.ArrayList;
import java.util.List;

import maze.Maze;
import simvrep.Simulation;
import simvrep.SimulationSettings;
import simvrep.VRepEvaluator;
import unalcol.optimization.OptimizationFunction;
import unalcol.types.collection.bitarray.BitArray;

public class EmP extends OptimizationFunction<double[]>{
	
	protected List<Simulation> simulators;
	protected BitArray servers;
	protected double[] morphology;
	protected Maze maze;
	protected SimulationSettings settings;
	
	public boolean isNonStationary() {
		return true;
	}
	
	public EmP(List<Simulation> simulators, int numberOfServers, double[] morphology, Maze maze, SimulationSettings settings) {
		this.simulators = simulators;
		this.servers = new BitArray(numberOfServers, false);
		this.morphology = morphology;
		this.maze = maze;
		this.settings = settings;
	}
	
	public EmP(Simulation sim, double[] morphology,Maze maze,SimulationSettings settings) {
		simulators = new ArrayList<Simulation>();
		simulators.add(sim);
		servers = new BitArray(1, false);
		this.morphology = morphology;
		this.maze = maze;
		this.settings = settings;
	}
	
	
	public Double apply(double[] individual){
		
		VRepEvaluator evaluator = getVRrepEvaluator();
		double fitness = 0;
		
		if (settings.noisy) 
			applyNoiseToMaze();	
		
		evaluator.configure(individual, morphology, maze);
		for(int i = 0; i < settings.maxTries; i++) {
			if(evaluator.run() == -1)
				evaluator.configure(individual, morphology, maze);
			else
				i = settings.maxTries;
		}
		fitness += evaluator.evaluate();
	
		
		servers.set(evaluator.getSimulation().getSimnumber(), false);
		
		return -fitness;
	}
	

	private VRepEvaluator getVRrepEvaluator() {
		int instance = -1;
		instance = waitforsim();
		return new VRepEvaluator(simulators.get(instance), settings);
	}

	public synchronized int waitforsim() {
		int sim = -1;
		while (sim < 0)
			sim = getSimNumber();
		return sim;

	}
	
	public synchronized int getSimNumber() {
		for (int i = 0; i < servers.size(); ++i)
			if (!servers.get(i)) {
				servers.set(i, true);
				return i;
			}
		return -1;
	}
	
	private void applyNoiseToMaze() {
		this.maze.nextNoisyDimensions(this.settings.noiseRadiusMaze);
	}


}
