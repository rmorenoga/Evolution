package evolHAEA;

import java.util.ArrayList;
import java.util.List;

import control.CPGHANN;
import control.CPGHANNOriD;
import control.CPGHSBase;
import control.CPGHSingle;
import control.CPGSingle;
import control.ParameterMask;
import simvrep.EvaluatorMT;
import simvrep.Simulation;
import unalcol.optimization.OptimizationFunction;
import unalcol.random.integer.IntUniform;
import unalcol.tracer.Tracer;
import unalcol.types.collection.bitarray.BitArray;
import util.ChromoConversion;

public class HEmP extends OptimizationFunction<double[]> {

	boolean DEBUG = false;
	protected List<Simulation> simulators;
	protected String morpho;
	protected BitArray servers;
	public float alpha = 0.88f;
	protected IntUniform r = new IntUniform(5);
	protected String mode;
	protected int fixednum = 0;

	@Override
	public boolean isNonStationary() {
		return true;
	}

	public HEmP(int numberOfServers, List<Simulation> simulators, String morpho, String mode) {
		servers = new BitArray(numberOfServers, false);
		this.simulators = simulators;
		this.morpho = morpho;
		this.mode = mode;
		if (DEBUG) {
			System.out.println("Building HEmP");
		}
	}

	public HEmP(int numberOfServers, List<Simulation> simulators, String morpho, String mode, int fixednum) {
		this(numberOfServers, simulators, morpho, mode);
		this.fixednum = fixednum;
	}

	public HEmP(float alpha, Simulation sim, String morpho, String mode) {
		this.alpha = alpha;
		simulators = new ArrayList<Simulation>();
		simulators.add(sim);
		servers = new BitArray(1, false);
		this.morpho = morpho;
		this.mode = mode;
	}

	public HEmP(float alpha, Simulation sim, String morpho, String mode, int fixednum) {
		this(alpha, sim, morpho, mode);
		this.fixednum = fixednum;
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
		// Parameter Mask: Allows control over which parameters are actually
		// sent to the robot depending on its controller, ParameterMask class
		// just sends everything adjusted for max and min values
		// Submask: Helper classes that fix certain parts of the controller
		// ParameterMask parammask = new ParameterMask();
		// CPGSingle parammask = new CPGSingle(true,true);
		// CPGHSingle parammask = new CPGHSingle(true,true);
		// CPGHSBase parammask = new CPGHSBase(true,true,true);
		CPGHANN parammask = new CPGHANN(fullparam.length, true);
		parammask.setParameters(fullparam);
		
		//Distance measure
		boolean measureDToGoal = false;
		boolean measureDToGoalByPart = false;

		switch (mode) {
		case "sequence":
			char[][] subenv = new char[][] { { 's', 'l', 's' }, { 's', 'r', 's' }, { 'b' } };
			int[] sequence = getSequence();

			double[] subfitness = new double[sequence.length];

			for (int i = 0; i < sequence.length; i++) {
				float width = randomWithRange(0.6f, 0.8f);
				float height = 0.088f;
				int nBSteps = 2;
				if (morpho != null && !morpho.equals("")) {
					double[] morphoDouble = ChromoConversion.str2double(morpho);
					EvaluatorMT evaluator = new EvaluatorMT(morphoDouble, "defaultmhs.ttt", parammask, sim, alpha,
							subenv[sequence[i]], width, height,nBSteps, measureDToGoal,measureDToGoalByPart);
					subfitness[i] = evaluator.evaluate();
				}

				fitness = average(subfitness);
			}
			break;
		case "fixed":

			char[][] subenvperm = new char[][] { { 's', 'l', 's', 'b', 's', 'r', 's' },
					{ 's', 'l', 's', 's', 'r', 's', 'b' }, { 'b', 's', 'l', 's', 's', 'r', 's' },
					{ 'b', 's', 'r', 's', 's', 'l', 's' }, { 's', 'r', 's', 's', 'l', 's', 'b' },
					{ 's', 'r', 's', 'b', 's', 'l', 's' }, { 's', 's' } ,{'s','l','b','r'}};
			//char[] subshort = new char[]{'s','b','l','r'};

			float width = randomWithRange(0.59f, 0.61f);
			width = 0.35f;
			float height = 0.08f;
			int nBSteps = 2;
			// System.out.println("Width = "+width);
			if (morpho != null && !morpho.equals("")) {
				double[] morphoDouble = ChromoConversion.str2double(morpho);
				// EvaluatorMT evaluator = new EvaluatorMT(morphoDouble,
				// "defaultmhs.ttt", parammask, sim, alpha,
				// subenvperm[r.generate()], width);
				// EvaluatorMT evaluator = new EvaluatorMT(morphoDouble,
				// "defaultmhs.ttt", parammask, sim, alpha, subenvperm[0],
				// width);
				EvaluatorMT evaluator = new EvaluatorMT(morphoDouble, "defaultmhs.ttt", parammask, sim, alpha,
						subenvperm[fixednum], width, height,nBSteps,measureDToGoal,measureDToGoalByPart);
				fitness = evaluator.evaluate();
			}
			break;

		case "incrementalbump":

			char[] subbump = new char[] { 's', 'b', 's' };
			width = 0.5f;
			float[] heights = new float[] { 0.02f, 0.06f, 0.08f };
			nBSteps = 2;
			double[] partialfitness = new double[3];

			double[] morphoDouble = ChromoConversion.str2double(morpho);
			EvaluatorMT evaluator = new EvaluatorMT(morphoDouble, "defaultmhs.ttt", parammask, sim, alpha,
					new char[] { 's', 's', 's' }, width, heights[0],nBSteps,measureDToGoal,measureDToGoalByPart);
			partialfitness[0] = evaluator.evaluate();

			if (partialfitness[0] < 0.3) {

				evaluator = new EvaluatorMT(morphoDouble, "defaultmhs.ttt", parammask, sim, alpha, subbump, width,
						heights[1],nBSteps,measureDToGoal,measureDToGoalByPart);
				partialfitness[1] = evaluator.evaluate();

				if (partialfitness[1] < 0.3) {

					evaluator = new EvaluatorMT(morphoDouble, "defaultmhs.ttt", parammask, sim, alpha, subbump, width,
							heights[2],nBSteps,measureDToGoal,measureDToGoalByPart);
					partialfitness[2] = evaluator.evaluate();

					fitness = partialfitness[2];

				} else {
					fitness = partialfitness[1] + 5;
				}
			} else {
				fitness = partialfitness[0] + 10;
			}
			break;

		case "turnleft":

			char[] subturn = new char[] { 's', 'l', 's' };
			width = 0.5f;
			height = 0.08f;
			nBSteps = 2;
			morphoDouble = ChromoConversion.str2double(morpho);
			evaluator = new EvaluatorMT(morphoDouble, "defaultmhs.ttt", parammask, sim, alpha, subturn, width, height,nBSteps,measureDToGoal,measureDToGoalByPart);
			fitness = evaluator.evaluate();

		case "turnright":

			subturn = new char[] { 's', 'r', 's' };
			width = 0.5f;
			height = 0.08f;
			nBSteps = 2;
			morphoDouble = ChromoConversion.str2double(morpho);
			evaluator = new EvaluatorMT(morphoDouble, "defaultmhs.ttt", parammask, sim, alpha, subturn, width, height,nBSteps,measureDToGoal,measureDToGoalByPart);
			fitness = evaluator.evaluate();

		case "simplebump":

			subturn = new char[] { 's', 'b', 's' };
			width = 0.5f;
			height = 0.08f;
			nBSteps = 2;
			morphoDouble = ChromoConversion.str2double(morpho);
			evaluator = new EvaluatorMT(morphoDouble, "defaultmhs.ttt", parammask, sim, alpha, subturn, width, height,nBSteps,measureDToGoal,measureDToGoalByPart);
			fitness = evaluator.evaluate();

		case "GeneralTest":

			subenvperm = new char[][] { { 's', 'l', 'b', 'r' }, { 's', 'l', 'r', 'b' }, { 's', 'r', 'b', 'l' },
					{ 's', 'r', 'l', 'b' }, { 's', 'b', 'l', 'r' }, { 's', 'b', 'r', 'l' } };
			width = 0.4f;
			height = 0.08f;
			nBSteps = 1;
			morphoDouble = ChromoConversion.str2double(morpho);
			evaluator = new EvaluatorMT(morphoDouble, "defaultmhs.ttt", parammask, sim, alpha, subenvperm[fixednum],
					width, height,nBSteps,measureDToGoal,measureDToGoalByPart);
			fitness = evaluator.evaluate();

		}

		// System.out.println("Fitness in "+ simulator+ " = "+fitness);

		servers.set(simulator, false);

		System.out.println("Fitness = " + fitness);

		String tr = new String("Indv = " + fullparam[0]);

		for (int i = 1; i < fullparam.length; i++) {
			tr = tr + ", " + fullparam[i];
		}

		tr = tr + ", " + fitness;

		Tracer.trace(this, tr);

		return -fitness;

	}

	/**
	 * Returns a random floating point number between min and max
	 * 
	 * @param min
	 *            the min of the interval
	 * @param max
	 *            the max of the interval
	 * @return a random floating point number
	 */
	float randomWithRange(float min, float max) {
		double range = Math.abs(max - min);
		return (float) (Math.random() * range) + (min <= max ? min : max);
	}

	/**
	 * Returns a random sequence of three different integer numbers from 0 to 3
	 * 
	 * @return an array containing a sequence of three different integer numbers
	 *         from 0 to 3
	 */
	int[] getSequence() {
		IntUniform r = new IntUniform(3);
		int[] seq = r.generate(3);
		while (seq[1] == seq[0]) {
			seq[1] = r.next();
		}
		seq[2] = 5 - ((seq[0] + 1) + (seq[1] + 1));
		return seq;
	}

	double average(double[] array) {
		double sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum = sum + array[i];
		}
		return sum / array.length;
	}

}
