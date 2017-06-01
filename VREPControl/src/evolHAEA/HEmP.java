package evolHAEA;

import java.util.ArrayList;
import java.util.List;

import simvrep.EvaluatorMT;
import simvrep.Simulation;
import unalcol.optimization.OptimizationFunction;
import unalcol.random.integer.IntUniform;
import unalcol.types.collection.bitarray.BitArray;
import util.ChromoConversion;

public class HEmP extends OptimizationFunction<double[]> {
	
	boolean DEBUG = false;
	protected List<Simulation> simulators;
	protected String morpho;
	protected int extraparam;
	protected BitArray servers;
	public float alpha = 0.7f;
	protected IntUniform r = new IntUniform(5);
	
	
	public HEmP(int numberOfServers, List<Simulation> simulators,String morpho,int extraparam){
		servers = new BitArray(numberOfServers, false);
		this.simulators = simulators;
		this.morpho = morpho;
		this.extraparam = extraparam;
		if (DEBUG) {
			System.out.println("Building HEmP");
		}
	}
	
	public HEmP(float alpha, Simulation sim,String morpho,int extraparam){
		this.alpha = alpha;
		simulators = new ArrayList<Simulation>();
		simulators.add(sim);
		servers = new BitArray(1, false);
		this.morpho = morpho;
		this.extraparam = extraparam;
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
		
		double fitness = 1000;
		
		Simulation sim;
		
		int	simulator = -1;
		simulator = waitforsim();
		System.out.println("Got sim: " + simulator);
		
		sim = simulators.get(simulator);
		
		float[] fullparam = new float[x.length];
		
		for (int i = 0; i< x.length; i++){
			fullparam[i] = (float) x[i];
		}
		
		char[][] subenvperm = new char[][] { { 's', 'l', 's', 'b', 's', 'r', 's' },
			{ 's', 'l', 's', 's', 'r', 's', 'b' }, { 'b', 's', 'l', 's', 's', 'r', 's' },
			{ 'b', 's', 'r', 's', 's', 'l', 's' }, { 's', 'r', 's', 's', 'l', 's', 'b' },
			{ 's', 'r', 's', 'b', 's', 'l', 's' }, { 's', 's' } };
			
		float width = randomWithRange(0.6f, 0.8f);
		
		if (morpho != null && !morpho.equals("")) {
			double[] morphoDouble = ChromoConversion.str2double(morpho);
			EvaluatorMT evaluator = new EvaluatorMT(morphoDouble, "defaultmh.ttt", fullparam, sim,alpha,extraparam,subenvperm[r.generate()],width);
			fitness = evaluator.evaluate();
		}
		
		//System.out.println("Fitness in "+ simulator+ " = "+fitness);
		
		servers.set(simulator, false);
		
		return fitness;
		
		
	}
	
	float randomWithRange(float min, float max)
	{
	   double range = Math.abs(max - min);     
	   return (float)(Math.random() * range) + (min <= max ? min : max);
	}
	
	
	
	
}
