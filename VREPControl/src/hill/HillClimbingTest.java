package hill;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
		
		int process = 0;
		
		if (args.length > 0) {
			try {
				// for(int j=0;j<args.length;j++){
				// System.out.println("Argument "+j+" = "+args[j]);
				// }
				if (args.length >= 1) {
					process = Integer.parseInt(args[0]);
				} else {
					System.err.println("Provide a process number");
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

		String vrepcommand = new String("./vrep" + process + ".sh");

		for (int iter = 10*process; iter < (10*process)+10; iter++) {

			// // Initialize a v-rep simulator based on the Nsim parameter
			 try {
			 // ProcessBuilder qq = new ProcessBuilder(vrepcommand, "-h",
			 // "scenes/Maze/MRun.ttt"); //Snake
			 ProcessBuilder qq = new ProcessBuilder(vrepcommand, "-h",
			 "scenes/Maze/defaultmhs.ttt");
			 qq.directory(new File("/home/rodr/V-REP/Vrep" + process + "/"));
			 File log = new File("Simout/log");
			 qq.redirectErrorStream(true);
			 qq.redirectOutput(Redirect.appendTo(log));
			 qq.start();
			 Thread.sleep(10000);
			 } catch (Exception e) {
			 System.out.println(e.toString());
			 e.printStackTrace();
			 }

			Simulation sim = new Simulation(process, 180);
			// Retry if there is a simulator crash
			for (int i = 0; i < 5; i++) {
				if (sim.Connect()) {
					break;
				} else {
					// No connection could be established
					System.out.println("Failed connecting to remote API server");
					System.out.println("Trying again for the " + i + " time in " + process);
				}
			}

			// Search Space Definition
			int DIM = 234;
			double[] min = DoubleArray.create(DIM, -1);
			double[] max = DoubleArray.create(DIM, 1);

			Space<double[]> space = new HyperCube(min, max);

			String morpho = "[(0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0 , 3.0, 1.0, 3.0, 1.0, 3.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]";

			OptimizationFunction<double[]> function = new HEmP(0.7f, sim, morpho, "fixed",6);

			OptimizationGoal<double[]> goal = new OptimizationGoal<double[]>(function);

			IntensityMutation variation = new GaussianMutation(0.05, new PermutationPick(10));

			// Search Method
			int MAXITERS = 200;
			boolean neutral = true;
			OptimizationFactory<double[]> factory = new OptimizationFactory<double[]>();
			LocalSearch<double[], Double> search = factory.hill_climbing(variation, neutral, MAXITERS);

			// Track Individuals and Goal Evaluations
			SolutionDescriptors<double[]> desc = new SolutionDescriptors<double[]>();
			Descriptors.set(Space.class, desc);
			DoubleArrayPlainWrite write = new DoubleArrayPlainWrite(',', false);
			Write.set(double[].class, write);
			WriteDescriptors write_desc = new WriteDescriptors();
			Write.set(Space.class, write_desc);

			// Add tracer based on descriptors set
			FileTracer tracer = new FileTracer("HillClimbTest"+iter+".txt", ',');
			ConsoleTracer tracer1 = new ConsoleTracer();

			Tracer.addTracer(search, tracer);
			Tracer.addTracer(HEmP.class, tracer);
			Tracer.addTracer(search, tracer1);

			Solution<double[]> solution = search.solve(space, goal);

			Double fitness = (Double) solution.data("goal");
			double[] individual = solution.object();

			try (Writer writer = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("HillClimbingResult"+iter+".txt"), "utf-8"))) {
				// writer.write(fitness.toString());
				for (int i = 0; i < individual.length; i++) {
					writer.write(Double.toString(individual[i]) + ", ");
				}
			} catch (Exception e) {

			}

			System.out.println(solution.object());

			tracer.close();
			tracer1.close();

			sim.Disconnect();

			// // Stop Simulators
			// // kill all the v-rep processes
			 try {
			 ProcessBuilder qq = new ProcessBuilder("killall", "vrep" + process);
			 File log = new File("Simout/log");
			 qq.redirectErrorStream(true);
			 qq.redirectOutput(Redirect.appendTo(log));
			 Process p = qq.start();
			 int exitVal = p.waitFor();
			 System.out.println("Terminated vrep" + process + " with error code " +
			 exitVal);
			 } catch (Exception e) {
			 System.out.println(e.toString());
			 e.printStackTrace();
			 }

		}

	}

}
