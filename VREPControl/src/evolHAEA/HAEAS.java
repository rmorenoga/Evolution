package evolHAEA;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;


import simvrep.Simulation;
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
import unalcol.optimization.real.mutation.Mutation;
import unalcol.optimization.real.mutation.OneFifthRule;
import unalcol.optimization.real.mutation.PickComponents;
import unalcol.optimization.real.xover.LinearXOver;
import unalcol.random.real.StandardGaussianGenerator;
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
import unalcol.types.real.array.DoubleArrayPlainWrite;;

public class HAEAS {

	public static Simulation sim;

	public static void main(String[] args) {

		sim = new Simulation(0, 30);

		if (!sim.Connect()) {
			System.exit(-1);
		}

		// Search Space Definition
		int DIM = 42;
		double[] min = DoubleArray.create(DIM, -1);
		double[] max = DoubleArray.create(DIM, 1);

		Space<double[]> space = new HyperCube(min, max);

		// Optimization function
		OptimizationFunction<double[]> function = new HDebugP(0.7f, sim);
		Goal<double[]> goal = new OptimizationGoal<double[]>(function);
		
		
		
		// Variation Definition
		//AdaptMutationIntensity adapt = new OneFifthRule(20, 0.9);
		//IntensityMutation variation = new GaussianMutation(0.1, null, adapt);
		PickComponents favor = new FavorFirst(42,7,6,true);
		Mutation variation = new FFirstIntMutation(0.1,new StandardGaussianGenerator(),favor,7,6);
		ArityTwo<double[]> xover = new LinearXOver();

		int POPSIZE = 2;
		int MAXITERS = 2;
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

		// Add tracer based on descriptors set
		FileTracer tracer = new FileTracer("Evolresult.txt", ',');
		ConsoleTracer tracer1 = new ConsoleTracer();
		Tracer.addTracer(goal, tracer1);
		Tracer.addTracer(goal, tracer);

		Solution<double[]> solution = search.apply(space, goal);

		System.out.println(solution.quality());

		tracer.close();
		tracer1.close();

		sim.Disconnect();

		// kill all the v-rep processes
		try {
			ProcessBuilder qq = new ProcessBuilder("killall", "vrep" + 0);
			File log = new File("Simout/log");
			qq.redirectErrorStream(true);
			qq.redirectOutput(Redirect.appendTo(log));
			Process p = qq.start();
			int exitVal = p.waitFor();
			System.out.println("Terminated vrep" + 0 + " with error code " + exitVal);
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}

	}
}
