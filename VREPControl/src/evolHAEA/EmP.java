package evolHAEA;

import java.util.ArrayList;
import java.util.List;

import maze.Maze;
import simvrep.Simulation;
import simvrep.SimulationSettings;
import simvrep.VRepEvaluator;
import unalcol.optimization.OptimizationFunction;
import unalcol.tracer.Tracer;
import unalcol.types.collection.bitarray.BitArray;

public class EmP extends OptimizationFunction<double[]>{
	
	protected List<Simulation> simulators;
	protected BitArray servers;
	protected double[] morphology;
	protected Maze maze;
	protected SimulationSettings settings;
	protected int iteration = 0;
	
	public boolean isNonStationary() {
		return true;
	}
	
	@Override
	public void update(int k) {
		super.update(k);
		this.iteration = k;
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
		
		int simInstance = waitforsim();
		VRepEvaluator evaluator = new VRepEvaluator(simulators.get(simInstance), settings);
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
	
		
		servers.set(simInstance, false);
		System.out.println("Iteration: " + this.iteration);
		
		/***** Only for hill climbing***/
		System.out.println("Fitness = " + (-fitness));

		String tr = new String("Indv = " + individual[0]);

		for (int i = 1; i < individual.length; i++) {
			tr = tr + ", " + individual[i];
		}

		tr = tr + "; " + (-fitness);

		Tracer.trace(this, tr);
		/******/
		
		if (!settings.measureDToGoal){
			return -fitness;
		}else{
			return fitness;
		}
		
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
