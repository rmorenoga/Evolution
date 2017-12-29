package hill;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;

import evolHAEA.HEmP;
import simvrep.Simulation;
import unalcol.descriptors.Descriptors;
import unalcol.descriptors.WriteDescriptors;
import unalcol.io.Write;
import unalcol.optimization.OptimizationFunction;
import unalcol.optimization.OptimizationGoal;
import unalcol.optimization.method.OptimizationFactory;
import unalcol.optimization.real.HyperCube;
import unalcol.optimization.real.mutation.GaussianMutation;
import unalcol.optimization.real.mutation.IntensityMutation;
import unalcol.optimization.real.mutation.PermutationPick;
import unalcol.search.local.LocalSearch;
import unalcol.search.solution.Solution;
import unalcol.search.solution.SolutionDescriptors;
import unalcol.search.space.Space;
import unalcol.tracer.ConsoleTracer;
import unalcol.tracer.FileTracer;
import unalcol.tracer.Tracer;
import unalcol.types.real.array.DoubleArray;
import unalcol.types.real.array.DoubleArrayPlainWrite;

public class HillClimbingTest {

	public static void main(String[] args) {

		String vrepcommand = new String("./vrep" + 0 + ".sh");

//		// Initialize a v-rep simulator based on the Nsim parameter
//		try {
//			// ProcessBuilder qq = new ProcessBuilder(vrepcommand, "-h",
//			// "scenes/Maze/MRun.ttt"); //Snake
//			ProcessBuilder qq = new ProcessBuilder(vrepcommand, "-h", "scenes/Maze/defaultmhs.ttt");
//			qq.directory(new File("/home/rodr/V-REP/Vrep" + 0 + "/"));
//			File log = new File("Simout/log");
//			qq.redirectErrorStream(true);
//			qq.redirectOutput(Redirect.appendTo(log));
//			qq.start();
//			Thread.sleep(10000);
//		} catch (Exception e) {
//			System.out.println(e.toString());
//			e.printStackTrace();
//		}

		Simulation sim = new Simulation(0, 100);
		// Retry if there is a simulator crash
		for (int i = 0; i < 5; i++) {
			if (sim.Connect()) {
				break;
			} else {
				// No connection could be established
				System.out.println("Failed connecting to remote API server");
				System.out.println("Trying again for the " + i + " time in " + 0);
			}
		}

		// Search Space Definition
		int DIM = 208;
		double[] min = DoubleArray.create(DIM, -1);
		double[] max = DoubleArray.create(DIM, 1);

		Space<double[]> space = new HyperCube(min, max);

		String morpho = "[(0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0 , 3.0, 1.0, 3.0, 1.0, 3.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]";

		OptimizationFunction<double[]> function = new HEmP(0.7f, sim, morpho, "incrementalbump");

		OptimizationGoal<double[]> goal = new OptimizationGoal<double[]>(function);

		IntensityMutation variation = new GaussianMutation(0.05, new PermutationPick(10));

		// Search Method
		int MAXITERS = 2;
		boolean neutral = true;
		OptimizationFactory<double[]> factory = new OptimizationFactory<double[]>();
		LocalSearch<double[],Double> search = factory.hill_climbing(variation, neutral, MAXITERS);

		// Track Individuals and Goal Evaluations
		SolutionDescriptors<double[]> desc = new SolutionDescriptors<double[]>();
		Descriptors.set(Space.class, desc);
		DoubleArrayPlainWrite write = new DoubleArrayPlainWrite(',',false);
		Write.set(double[].class, write);
		WriteDescriptors write_desc = new WriteDescriptors();
		Write.set(Space.class, write_desc);

		// Add tracer based on descriptors set
		FileTracer tracer = new FileTracer("HillClimbTest.txt", ',');
		ConsoleTracer tracer1 = new ConsoleTracer();

		Tracer.addTracer(search, tracer);
		Tracer.addTracer(HEmP.class,tracer);
		Tracer.addTracer(search, tracer1);

		Solution<double[]> solution = search.solve(space, goal);

		System.out.println(solution.object());

		tracer.close();
		tracer1.close();

		sim.Disconnect();

//		// Stop Simulators
//		// kill all the v-rep processes
//		try {
//			ProcessBuilder qq = new ProcessBuilder("killall", "vrep" + 0);
//			File log = new File("Simout/log");
//			qq.redirectErrorStream(true);
//			qq.redirectOutput(Redirect.appendTo(log));
//			Process p = qq.start();
//			int exitVal = p.waitFor();
//			System.out.println("Terminated vrep" + 0 + " with error code " + exitVal);
//		} catch (Exception e) {
//			System.out.println(e.toString());
//			e.printStackTrace();
//		}

	}

}
