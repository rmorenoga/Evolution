package evolHAEA;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

import emst.evolution.haea.HaeaFactory;
import emst.evolution.search.multithread.MultithreadOptimizationGoal;
import emst.evolution.search.selection.ModifiedElitism;
import simvrep.Simulation;

import hill.HillMAS;
import unalcol.descriptors.Descriptors;
import unalcol.descriptors.WriteDescriptors;

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
import unalcol.optimization.real.mutation.OneFifthRule;
import unalcol.optimization.real.xover.LinearXOver;
import unalcol.search.Goal;


import unalcol.search.population.Population;
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
import unalcol.types.real.array.DoubleArrayPlainWrite;

public class HAEAParallel {

	public static int Nsim;
	public static List<Simulation> simulators;

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

		simulators = new ArrayList<Simulation>();
		System.out.println(Nsim);
		// Start Simulators
		for (int j = 0; j < Nsim; j++) {

			/*String vrepcommand = new String("./vrep" + j + ".sh");

			// Initialize a v-rep simulator based on the Nsim parameter
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
			}*/

			Simulation sim = new Simulation(j, 12);
			// Retry if there is a simulator crash
			for (int i = 0; i < 5; i++) {
				if (sim.Connect()) {
					simulators.add(sim);
				} else {
					// No connection could be established
					System.out.println("Failed connecting to remote API server");
					System.out.println("Trying again for the " + i + " time in " + j);
					continue;
				}
				break;
			}

		}

		// Search Space Definition
		int DIM = 42;
		double[] min = DoubleArray.create(DIM, -1);
		double[] max = DoubleArray.create(DIM, 1);

		Space<double[]> space = new HyperCube(min, max);

		// Optimization function
		OptimizationFunction<double[]> function = new HDebugP(Nsim,simulators);
		MultithreadOptimizationGoal<double[]> goal = new MultithreadOptimizationGoal<double[]>(function);
		goal.setMax_threads(Nsim);

		// Variation Definition
		//AdaptMutationIntensity adapt = new OneFifthRule(20, 0.9);
		IntensityMutation variation = new GaussianMutation(0.1, null);
		LinearXOver xover = new LinearXOver();

		int POPSIZE = 2;
		int MAXITERS = 2;
		Variation[] opers = new Variation[2];
		opers[0] = variation;
		opers[1] = xover;

		SimpleHaeaOperators operators = new SimpleHaeaOperators(opers);
		Selection selection = new Tournament(4,new ModifiedElitism(1.0,0.0));
		
		HaeaFactory factory = new HaeaFactory();
		PopulationSearch search = factory.HAEA(POPSIZE, operators,selection,MAXITERS);

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

		// Add tracer based on descriptors set
		FileTracer tracer = new FileTracer("Evolresult.txt", ',');
		ConsoleTracer tracer1 = new ConsoleTracer();
		Tracer.addTracer(search, tracer1);
		Tracer.addTracer(search, tracer);

		Solution<double[]> solution = search.solve(space, goal);

		System.out.println(solution.object());

		tracer.close();
		tracer1.close();
		
		for (Simulation sim : simulators){
			sim.Disconnect();
		}

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
