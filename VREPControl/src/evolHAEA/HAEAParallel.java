package evolHAEA;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;

import hill.HillMAS;
import unalcol.descriptors.Descriptors;
import unalcol.descriptors.WriteDescriptors;
import unalcol.evolution.haea.HAEA;
import unalcol.evolution.haea.HaeaOperators;
import unalcol.evolution.haea.SimpleHaeaOperators;
import unalcol.io.Write;
import unalcol.optimization.OptimizationFunction;
import unalcol.optimization.OptimizationGoal;
import unalcol.optimization.real.HyperCube;
import unalcol.optimization.real.mutation.AdaptMutationIntensity;
import unalcol.optimization.real.mutation.GaussianMutation;
import unalcol.optimization.real.mutation.IntensityMutation;
import unalcol.optimization.real.mutation.OneFifthRule;
import unalcol.optimization.real.xover.LinearXOver;
import unalcol.search.Goal;
import unalcol.search.Solution;
import unalcol.search.SolutionDescriptors;
import unalcol.search.population.variation.ArityTwo;
import unalcol.search.population.variation.Operator;
import unalcol.search.selection.Tournament;
import unalcol.search.space.Space;
import unalcol.tracer.ConsoleTracer;
import unalcol.tracer.FileTracer;
import unalcol.tracer.Tracer;
import unalcol.types.real.array.DoubleArray;
import unalcol.types.real.array.DoubleArrayPlainWrite;

public class HAEAParallel {

	public static int Nsim;

	public static void main(String[] args) {

		Nsim = 0;

		if (args.length > 0) {
			try {
				// for(int j=0;j<args.length;j++){
				// System.out.println("Argument "+j+" = "+args[j]);
				// }
				if (args.length >= 1) {
					Nsim = Integer.parseInt(args[0]);
				} else {
					System.err.println("Provide a number of simulators");
					System.exit(1);
				}
			} catch (NumberFormatException e) {
				System.err.println("Nsim " + args[0] + " must be an integer.");
				System.exit(1);
			}
		} else {
			System.err.println("Missing arguments");
			System.exit(1);
		}
		

		// Start Simulators
		for (int j = 0; j < Nsim; j++) {

			String vrepcommand = new String("./vrep" + j + ".sh");

			/* Initialize a v-rep simulator based on the Nsim parameter */
			try {
				ProcessBuilder qq = new ProcessBuilder(vrepcommand, "-h", "scenes/Maze/MDebug.ttt");
				qq.directory(new File("/home/rodr/V-REP/Vrep" + j + "/"));
				File log = new File("Simout/log");
				qq.redirectErrorStream(true);
				qq.redirectOutput(Redirect.appendTo(log));
				qq.start();
				Thread.sleep(10000);
			} catch (Exception e) {
				System.out.println(e.toString());
				e.printStackTrace();
			}
		}

		// Search Space Definition
		int DIM = 42;
		double[] min = DoubleArray.create(DIM, -1);
		double[] max = DoubleArray.create(DIM, 1);

		Space<double[]> space = new HyperCube(min, max);

		// Optimization function
		OptimizationFunction<double[]> function = new HDebugP(Nsim);
		Goal<double[]> goal = new OptimizationGoal<double[]>(function);
		goal.setMaxThreads(Nsim);

		// Variation Definition
		AdaptMutationIntensity adapt = new OneFifthRule(20, 0.9);
		IntensityMutation variation = new GaussianMutation(0.1, null, adapt);
		ArityTwo<double[]> xover = new LinearXOver();

		int POPSIZE = 5;
		int MAXITERS = 1;
		Operator<double[]>[] opers = (Operator<double[]>[]) new Operator[2];
		opers[0] = variation;
		opers[1] = xover;

		HaeaOperators<double[]> operators = new SimpleHaeaOperators<double[]>(opers);
		HAEA<double[]> search = new HAEA<double[]>(POPSIZE, operators, new Tournament<double[]>(4), MAXITERS);

		// Track Individuals and Goal Evaluations
		SolutionDescriptors<double[]> desc = new SolutionDescriptors<double[]>();
		Descriptors.set(Space.class, desc);
		DoubleArrayPlainWrite write = new DoubleArrayPlainWrite(',');
		Write.set(double[].class, write);
		WriteDescriptors w_desc = new WriteDescriptors();
		Write.set(Space.class, w_desc);

		//Add tracer based on descriptors set
		FileTracer tracer = new FileTracer("Evolresult.txt",',');
		ConsoleTracer tracer1 = new ConsoleTracer();
		Tracer.addTracer(search, tracer1);
		Tracer.addTracer(search,tracer);

		Solution<double[]> solution = search.apply(space, goal);

		System.out.println(solution.quality());
		
		tracer.close();
		tracer1.close();

		// Stop Simulators
		for (int j = 0; j < Nsim; j++) {
			// kill all the v-rep processes
			try {
				ProcessBuilder qq = new ProcessBuilder("killall", "vrep" + j);
				File log = new File("Simout/log");
				qq.redirectErrorStream(true);
				qq.redirectOutput(Redirect.appendTo(log));
				Process p = qq.start();
				int exitVal = p.waitFor();
				System.out.println("Terminated vrep" + j + " with error code " + exitVal);
			} catch (Exception e) {
				System.out.println(e.toString());
				e.printStackTrace();
			}
		}

	}

}
