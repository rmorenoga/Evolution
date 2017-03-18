package hill;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;

import unalcol.descriptors.Descriptors;
import unalcol.descriptors.WriteDescriptors;
import unalcol.io.Write;
import unalcol.optimization.OptimizationFunction;
import unalcol.optimization.OptimizationGoal;
import unalcol.optimization.method.OptimizationFactory;
import unalcol.optimization.real.HyperCube;
import unalcol.optimization.real.mutation.GaussianMutation;
import unalcol.optimization.real.mutation.IntensityMutation;
import unalcol.optimization.real.mutation.OneFifthRule;
import unalcol.search.Goal;
import unalcol.search.local.LocalSearch;
import unalcol.search.solution.Solution;
import unalcol.search.solution.SolutionDescriptors;
import unalcol.search.space.Space;
import unalcol.tracer.ConsoleTracer;
import unalcol.tracer.FileTracer;
import unalcol.tracer.Tracer;
import unalcol.types.real.array.DoubleArray;
import unalcol.types.real.array.DoubleArrayPlainWrite;



public class HillTest {
	
	public static int simNumber;
	public static float alpha;
		
	public static void hillm (int simNumber, float alpha){
		
		

		/*Initialize a v-rep simulator based on the simNumber parameter */
		
		String vrepcommand = new String("./vrep" + simNumber + ".sh");
		
		try {
			
			ProcessBuilder qq = new ProcessBuilder(vrepcommand, "-h",
					"/home/rodr/EvolWork/Modular/Maze/MazeBuilderR01.ttt");
			 //ProcessBuilder qq = new
			 //ProcessBuilder(vrepcommand,"/home/rodrigo/V-REP/Modular/Maze/MazeBuilder01.ttt");
			 //ProcessBuilder qq = new
					 //ProcessBuilder(vrepcommand,"-h","/home/rodrigo/V-REP/Modular/Maze/MazeBuilder01.ttt");
			
			qq.directory(new File("/home/rodr/V-REP/Vrep" + simNumber + "/"));
			//qq.directory(new File("/home/rodrigo/V-REP/Vrep" + simNumber + "/"));
			File log = new File("Simout/log");
			qq.redirectErrorStream(true);
			qq.redirectOutput(Redirect.appendTo(log));
			qq.start();
			Thread.sleep(10000);
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		
		/* Use unalcol to implement HillClimbing */
		
		//Search Space Definition 
		int DIM = 3;
		double[] min = DoubleArray.create(DIM,-1);
		double[] max = DoubleArray.create(DIM,1);
		
	Space<double[]> space = new HyperCube(min,max);
	
	//Optimization function
	//OptimizationFunction<double[]> function = new HillMazeRand3(simNumber, alpha);
	OptimizationFunction<double[]> function = new HillMAS(simNumber, alpha);
	OptimizationGoal<double[]> goal = new OptimizationGoal<double[]>(function);
	
	
	//Variation Definition
	OneFifthRule adapt = new OneFifthRule(20,0.9);
	IntensityMutation variation = new GaussianMutation(0.1,null);
	
	//Search Method
	int MAXITERS = 1000;
	boolean neutral = true;
	OptimizationFactory factory = new OptimizationFactory();
	LocalSearch search = factory.hill_climbing(variation, neutral, MAXITERS);
			
	//Track Individuals and Goal Evaluations
	SolutionDescriptors<double[]> desc = new SolutionDescriptors<double[]>();
	Descriptors.set(Space.class,desc);
	DoubleArrayPlainWrite write = new DoubleArrayPlainWrite(',');
	Write.set(double[].class, write);
	WriteDescriptors w_desc = new WriteDescriptors();
	Write.set(Space.class, w_desc);
	//Write.set(HillMazeRand3.class,);
	
	//Add tracer based on descriptors set
	FileTracer tracer = new FileTracer("Hillsim"+simNumber+".txt",',');
	ConsoleTracer tracer1 = new ConsoleTracer();
	//Tracer.addTracer(goal, tracer);
//	Tracer.addTracer(search, tracer);
//	Tracer.addTracer(HillMazeRand3.class,tracer);
//	Tracer.addTracer(search,tracer1);
//	Tracer.addTracer(HillMazeRand3.class,tracer1);.
	Tracer.addTracer(search, tracer);
	Tracer.addTracer(HillMAS.class,tracer);
	Tracer.addTracer(search,tracer1);
	//Tracer.addTracer(HillMaze.class,tracer1);
	
	
	
	Solution<double[]> solution = search.solve(space, goal);
	
	System.out.println(solution.object());
	
	tracer.close();
	tracer1.close();
	
	/* Kill all the v-rep processes */
	try {
		
		ProcessBuilder qq = new ProcessBuilder("killall", "vrep" + simNumber);
		File log = new File("Simout/log");
		qq.redirectErrorStream(true);
		qq.redirectOutput(Redirect.appendTo(log));
		Process p = qq.start();
		int exitVal = p.waitFor();
		System.out.println("Terminated vrep" + simNumber + " with error code "
				+ exitVal);
	} catch (Exception e) {
		System.out.println(e.toString());
		e.printStackTrace();
	}
	
	}
	
	
	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		
		simNumber = 0;
		alpha = 0.7f;
		/* If in local machine */
//
//		if (args.length > 0) {
//			try {
//				
//				if (args.length >= 2) {
//					simNumber = Integer.parseInt(args[0]);
//					alpha = Float.parseFloat(args[1]);
//				} else {
//					System.err.println("Provide two arguments");
//					System.exit(1);
//				}
//			} catch (NumberFormatException e) {
//				System.err.println("The arguments must be numerical values (simnumber, alpha)");
//				System.exit(1);
//			}
//		} else {
//			System.err.println("Missing arguments");
//			System.exit(1);
//		}
		
		/* If in remote machine */

		if (args.length > 0) {
			try {
				
				if (args.length >= 2) {
					simNumber = Integer.parseInt(args[0]);
					alpha = Float.parseFloat(args[1]);
				} else {
					System.err.println("Provide two arguments");
					System.exit(1);
				}
			} catch (NumberFormatException e) {
				System.err.println("The arguments must be numerical values (simnumber, alpha)");
				System.exit(1);
			}
		} else {
			System.err.println("Missing arguments");
			System.exit(1);
		}
		
		
		
		System.out.println("SimNumber = "+simNumber);
		System.out.println("Alpha = "+alpha);
		
		
		hillm(simNumber, alpha);
		
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println(elapsedTime);
		System.out.println("Finished");
		
		

		
		
	}

}
