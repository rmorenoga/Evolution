package evolHAEA;

import java.util.ArrayList;
import java.util.List;

import control.CPGHSBase;
import control.CPGHSingle;
import control.CPGSingle;
import control.ParameterMask;
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
	protected boolean seq;

	public HEmP(int numberOfServers, List<Simulation> simulators, String morpho, int extraparam, boolean seq) {
		servers = new BitArray(numberOfServers, false);
		this.simulators = simulators;
		this.morpho = morpho;
		this.extraparam = extraparam;
		this.seq = seq;
		if (DEBUG) {
			System.out.println("Building HEmP");
		}
	}

	public HEmP(float alpha, Simulation sim, String morpho, int extraparam, boolean seq) {
		this.alpha = alpha;
		simulators = new ArrayList<Simulation>();
		simulators.add(sim);
		servers = new BitArray(1, false);
		this.morpho = morpho;
		this.seq = seq;
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

		int simulator = -1;
		simulator = waitforsim();
		System.out.println("Got sim: " + simulator);

		sim = simulators.get(simulator);

		float[] fullparam = new float[x.length];

		for (int i = 0; i < x.length; i++) {
			fullparam[i] = (float) x[i];
		}
		//Parameter Mask: Allows control over which parameters are actually sent to the robot depending on its controller, ParameterMask class just sends everything adjusted for max and min values
		// Submask: Helper classes that fix certain parts of the controller
		//ParameterMask parammask = new ParameterMask(extraparam);
		//CPGSingle parammask = new CPGSingle(extraparam,true,true);
		//CPGHSingle parammask = new CPGHSingle(extraparam,true,true);
		CPGHSBase parammask = new CPGHSBase(extraparam,true,true,true);
		parammask.setandsepParam(fullparam);

		if (seq) {
			char[][] subenv = new char[][] {{'s','l','s'},{'s','r','s'},{'b'}};
			int[] sequence = getSequence(); 
			
			double[] subfitness = new double[sequence.length];
			
			for (int i = 0; i<sequence.length ;i++){
				float width = randomWithRange(0.6f, 0.8f);

				if (morpho != null && !morpho.equals("")) {
					double[] morphoDouble = ChromoConversion.str2double(morpho);
					EvaluatorMT evaluator = new EvaluatorMT(morphoDouble, "defaultmhs.ttt", parammask, sim, alpha, subenv[sequence[i]], width);
					subfitness[i] = evaluator.evaluate();
				}
				
				fitness  = average(subfitness);
			}

		} else {

			char[][] subenvperm = new char[][] { { 's', 'l', 's', 'b', 's', 'r', 's' },
					{ 's', 'l', 's', 's', 'r', 's', 'b' }, { 'b', 's', 'l', 's', 's', 'r', 's' },
					{ 'b', 's', 'r', 's', 's', 'l', 's' }, { 's', 'r', 's', 's', 'l', 's', 'b' },
					{ 's', 'r', 's', 'b', 's', 'l', 's' }, { 's', 's' } };
			char[] subshort = new char[]{'s','l','b','r'};

			float width = randomWithRange(0.59f, 0.61f);
			width = 0.5f;
			//System.out.println("Width = "+width);
			if (morpho != null && !morpho.equals("")) {
				double[] morphoDouble = ChromoConversion.str2double(morpho);
				//EvaluatorMT evaluator = new EvaluatorMT(morphoDouble, "defaultmhs.ttt", parammask, sim, alpha, subenvperm[r.generate()], width);
				//EvaluatorMT evaluator = new EvaluatorMT(morphoDouble, "defaultmhs.ttt", parammask, sim, alpha, subenvperm[0], width);
				EvaluatorMT evaluator = new EvaluatorMT(morphoDouble, "defaultmhs.ttt", parammask, sim, alpha, subshort, width);
				fitness = evaluator.evaluate();
			}
		}

		// System.out.println("Fitness in "+ simulator+ " = "+fitness);

		servers.set(simulator, false);

		return fitness;

	}

	/**
	 * Returns a random floating point number between min and max
	 * @param min the min of the interval
	 * @param max the max of the interval
	 * @return a random floating point number
	 */
	float randomWithRange(float min, float max) {
		double range = Math.abs(max - min);
		return (float) (Math.random() * range) + (min <= max ? min : max);
	}
	
	/**
	 * Returns a random sequence of three different integer numbers from 0 to 3
	 * @return an array containing a sequence of three different integer numbers from 0 to 3  
	 */
	int[] getSequence(){
		IntUniform r = new IntUniform(3);
		int[] seq = r.generate(3);
		while (seq[1] == seq[0]) {
			seq[1] = r.next();
		}
		seq[2] = 5 - ((seq[0] + 1) + (seq[1] + 1));
		return seq;
	}
	
	double average(double[] array){
		double sum = 0;
		for(int i=0;i<array.length;i++){
			sum = sum + array[i];
		}
		return sum/array.length;
	}

}
