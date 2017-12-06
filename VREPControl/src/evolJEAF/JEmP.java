package evolJEAF;

import java.util.logging.Logger;

import control.CPGHSBase;
import control.CPGSingle;
import control.CPGHANN;
import simvrep.EvaluatorMT;
import simvrep.Simulation;
import simvrep.SimulationConfiguration;
import unalcol.random.integer.IntUniform;
import util.ChromoConversion;
import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import mpi.MPI;

public class JEmP extends ObjectiveFunction {

	boolean DEBUG = false;
	boolean seq = false;
	static Logger LOGGER = Logger.getLogger("failogger");
	private int maxSimulationTime = 3;
	private boolean useMPI = false;
	private int nAttempts = 10; //Attempts to connect to simulator

	public double evaluate(double[] values) {

		Simulation sim;
		String morpho = "[(0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0 , 3.0, 1.0, 3.0, 1.0, 3.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]";
		float alpha = 0.7f;
		IntUniform r = new IntUniform(5);
		double fitness = 1000;
		int rank = 0;

		this.getSimulationConfigurationParameters();

		if (useMPI) {
			rank = MPI.COMM_WORLD.Rank();
		}

		sim = SimulationConfiguration.getVrepsim();
		if (sim == null) {
			System.out.println("vrep simulator is null, connecting to vrep...");
			sim = new Simulation(rank, maxSimulationTime);
			for (int i = 0; i < nAttempts; i++) {
				if (sim.Connect()) {
					System.out.println("Connected to sim " + rank);
				} else {
					// No connection could be established
					System.out.println("Failed connecting to remote API server");
					System.out.println("Trying again for the " + i + " time in " + rank);
					continue;
				}
				break;
			}
			SimulationConfiguration.setVrepsim(sim);
		}

		float[] fullparam = new float[values.length];

		for (int i = 0; i < values.length; i++) {
			fullparam[i] = (float) values[i];
			//System.out.print(fullparam[i]+",");
		}
		// Parameter Mask: Allows control over which parameters are actually
		// sent to the robot depending on its controller, ParameterMask class
		// just sends everything adjusted for max and min values
		// ParameterMask parammask = new ParameterMask();
		//CPGSingle parammask = new CPGSingle(true,true);
		//CPGHSBase parammask = new CPGHSBase(true,true,true);
		CPGHANN parammask = new CPGHANN(fullparam.length,true);
		parammask.setParameters(fullparam);

		if (seq) {
			char[][] subenv = new char[][] { { 's', 'l', 's' }, { 's', 'r', 's' }, { 'b' } };
			int[] sequence = getSequence();

			double[] subfitness = new double[sequence.length];

			for (int i = 0; i < sequence.length; i++) {
				float width = randomWithRange(0.6f, 0.8f);
				float height = 0.088f;

				if (morpho != null && !morpho.equals("")) {
					double[] morphoDouble = ChromoConversion.str2double(morpho);
					EvaluatorMT evaluator = new EvaluatorMT(morphoDouble, "defaultmhs.ttt", parammask, sim, alpha,
							subenv[sequence[i]], width, height);
					subfitness[i] = evaluator.evaluate();
				}

				fitness = average(subfitness);
			}

		} else {

			char[][] subenvperm = new char[][] { { 's', 'l', 's', 'b', 's', 'r', 's' },
					{ 's', 'l', 's', 's', 'r', 's', 'b' }, { 'b', 's', 'l', 's', 's', 'r', 's' },
					{ 'b', 's', 'r', 's', 's', 'l', 's' }, { 's', 'r', 's', 's', 'l', 's', 'b' },
					{ 's', 'r', 's', 'b', 's', 'l', 's' }, { 's', 's' } };
			char[] subshort = new char[] { 's', 'b','l','r' };

			float width = randomWithRange(0.59f, 0.61f);
			 width = 0.5f;
			 float height = 0.088f;
			// System.out.println("Width = "+width);
			if (morpho != null && !morpho.equals("")) {
				double[] morphoDouble = ChromoConversion.str2double(morpho);
				// EvaluatorMT evaluator = new EvaluatorMT(morphoDouble,
				// "defaultmhs.ttt", parammask, sim, alpha,
				// subenvperm[r.generate()], width);
				// EvaluatorMT evaluator = new EvaluatorMT(morphoDouble,
				// "defaultmhs.ttt", parammask, sim, alpha, subenvperm[0],
				// width);
				EvaluatorMT evaluator = new EvaluatorMT(morphoDouble, "defaultmhs.ttt", parammask, sim, alpha, subshort,
						width, height);
				fitness = evaluator.evaluate();
			}
		}

		return fitness;

	}

	private void getSimulationConfigurationParameters() {

		try {

			// this.SERVER_ID = SimulationConfiguration.getServerId();
			this.maxSimulationTime = (int) SimulationConfiguration.getMaxSimulationTime();
			this.useMPI = SimulationConfiguration.isUseMPI();
			// this.timeStartSim = SimulationConfiguration.getTimeStartSim();
			this.nAttempts = SimulationConfiguration.getnAttempts();

		} catch (Exception e) {
			LOGGER.severe("Error loading the control parameters of the simulation.");
			System.out.println(e);
			System.exit(-1);
		}

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

	public void reset() {

	}

}
