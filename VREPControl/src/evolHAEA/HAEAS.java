package evolHAEA;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;


import simvrep.Simulation;
import unalcol.descriptors.Descriptors;
import unalcol.descriptors.WriteDescriptors;
import unalcol.evolution.EAFactory;
import unalcol.evolution.haea.HaeaOperators;
import unalcol.evolution.haea.HaeaStep;
import unalcol.evolution.haea.HaeaStepDescriptors;
import unalcol.evolution.haea.SimpleHaeaOperators;
import unalcol.evolution.haea.SimpleHaeaOperatorsDescriptor;
import unalcol.evolution.haea.WriteHaeaStep;
import unalcol.io.Write;
import unalcol.optimization.OptimizationFunction;
import unalcol.optimization.OptimizationGoal;
import unalcol.optimization.real.HyperCube;
import unalcol.optimization.real.mutation.GaussianMutation;
import unalcol.optimization.real.mutation.IntensityMutation;
import unalcol.optimization.real.mutation.Mutation;
import unalcol.optimization.real.mutation.OneFifthRule;
import unalcol.optimization.real.mutation.PickComponents;
import unalcol.optimization.real.xover.LinearXOver;
import unalcol.random.real.StandardGaussianGenerator;
import unalcol.search.Goal;
import unalcol.search.population.Population;
import unalcol.search.population.PopulationDescriptors;
import unalcol.search.population.PopulationSearch;
import unalcol.search.selection.Selection;
import unalcol.search.selection.Tournament;
import unalcol.search.solution.Solution;
import unalcol.search.space.Space;
import unalcol.search.variation.Variation;
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
		
		int nmodules = 4;
		int[] ori = new int[]{1,0,1,0};

		// Optimization function
		OptimizationFunction<double[]> function = new HDebugP(0.7f, sim, false,nmodules,ori,7,6,0);
		OptimizationGoal<double[]> goal = new OptimizationGoal<double[]>(function);
		
	
		
		// Variation Definition
		//AdaptMutationIntensity adapt = new OneFifthRule(20, 0.9);
		//IntensityMutation variation = new GaussianMutation(0.1, null, adapt);
		FavorFirst favor = new FavorFirst(7,6,true,0);
		Mutation variation = new FFirstIntMutation(0.1,new StandardGaussianGenerator(),favor,7,6,0);
		LinearXOver xover = new LinearXOver();

		int POPSIZE = 2;
		int MAXITERS = 2;
		Variation[] opers = new Variation[2];
		opers[0] = variation;
		opers[1] = xover;

		SimpleHaeaOperators operators = new SimpleHaeaOperators(opers);
		Selection selection = new Tournament(4);
		
		//HAEA
		EAFactory factory = new EAFactory();
		PopulationSearch search = factory.HAEA(POPSIZE, operators,selection,MAXITERS);
		//HAEA<double[]> search = new HAEA<double[]>(POPSIZE, operators, new Tournament<double[]>(4), MAXITERS);
		
		// Track Individuals and Goal Evaluations
		WriteDescriptors write_desc = new WriteDescriptors();
		Write.set(double[].class, new DoubleArrayPlainWrite(false));
		Write.set(HaeaStep.class, new WriteHaeaStep());
		//Descriptors.set(Population.class, new PopulationDescriptors());
		Descriptors.set(Population.class, new BestPopulationDescriptor());
		Descriptors.set(HaeaStep.class, new HaeaStepDescriptors());
		Descriptors.set(HaeaOperators.class, new SimpleHaeaOperatorsDescriptor());
		Write.set(Population.class, write_desc);
		Write.set(HaeaStep.class, write_desc);
		Write.set(HaeaOperators.class, write_desc);
//		SolutionDescriptors<double[]> desc = new SolutionDescriptors<double[]>();
//		Descriptors.set(Space.class, desc);
//		DoubleArrayPlainWrite write = new DoubleArrayPlainWrite(',');
//		Write.set(double[].class, write);
//		WriteDescriptors w_desc = new WriteDescriptors();
//		Write.set(Space.class, w_desc);

		// Add tracer based on descriptors set
		//FileTracer tracer = new FileTracer("Evolresult.txt", ',');
		ConsoleTracer tracer1 = new ConsoleTracer();
		Tracer.addTracer(search, tracer1);
		//Tracer.addTracer(search, tracer);
		
		System.out.println("WorstFitness BestFitness Median Average Variance SD BestIndividual FirstOperatorRate SecondOperatorRate");
		Solution<double[]> solution = search.solve(space, goal);

		System.out.println(solution.object());

		//tracer.close();
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
