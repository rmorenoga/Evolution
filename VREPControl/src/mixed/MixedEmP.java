package mixed;

import java.util.ArrayList;
import java.util.List;

import maze.Maze;
import simvrep.Simulation;
import simvrep.SimulationSettings;
import simvrep.VRepEvaluator;
import unalcol.optimization.OptimizationFunction;
import unalcol.random.integer.IntUniform;
import unalcol.types.collection.bitarray.BitArray;
import unalcol.types.collection.vector.Vector;

public class MixedEmP extends OptimizationFunction<MixedGenome>{
	
	protected List<Simulation> simulators;
	protected BitArray servers;
	protected double[] morphology;
	protected Vector<Maze> mazes;
	protected SimulationSettings settings;
	
	public boolean isNonStationary() {
		return true;
	}

	public MixedEmP(List<Simulation> simulators, int numberOfServers, double[] morphology, Vector<Maze> mazes, SimulationSettings settings) {
		this.simulators = simulators;
		this.servers = new BitArray(numberOfServers, false);
		this.morphology = morphology;
		this.mazes = mazes;
		this.settings = settings;
	}
	
	public MixedEmP(Simulation sim, double[] morphology,Vector<Maze> mazes,SimulationSettings settings) {
		simulators = new ArrayList<Simulation>();
		simulators.add(sim);
		servers = new BitArray(1, false);
		this.morphology = morphology;
		this.mazes = mazes;
		this.settings = settings;
	}

	
	@Override
	public Double apply(MixedGenome individual) {
		
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println(individual.sensors.toString());
		System.out.println(individual.annWeights.toString());
		
		VRepEvaluator evaluator = getVRrepEvaluator();
		double fitness = 0;
		for(Maze maze: mazes) {
			evaluator.configure(individual, morphology, maze);
			for(int i = 0; i < settings.maxTries; i++) {
				if(evaluator.run() == -1)
					evaluator.configure(individual, morphology, maze);
				else
					i = settings.maxTries;
			}
			fitness += evaluator.evaluate();
		}
		
		servers.set(evaluator.getSimulation().getSimnumber(), false);
		System.out.println("-------------------------------------------");
		return fitness / mazes.size();
	}
	
	

	private VRepEvaluator getVRrepEvaluator() {
		int instance = -1;
		instance = waitforsim();
		return new VRepEvaluator(simulators.get(instance), settings);
	}

	public synchronized int getSimNumber() {
		for (int i = 0; i < servers.size(); ++i)
			if (!servers.get(i)) {
				servers.set(i, true);
				return i;
			}
		return -1;
	}
	
	public synchronized int waitforsim() {
		int sim = -1;
		while (sim < 0)
			sim = getSimNumber();
		return sim;

	}

	
	

}
