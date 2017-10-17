package evolHAEA;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import emst.evolution.haea.HaeaFactory;
import emst.evolution.haea.ModifiedHaeaStep;
import emst.evolution.json.haea.JSONHaeaStepObject;
import emst.evolution.json.haea.JSONHaeaStepObjectManager;
import emst.evolution.json.setting.EvolutionryAlgorithmSetting;
import emst.evolution.search.multithread.MultithreadOptimizationGoal;
import emst.evolution.search.selection.ModifiedElitism;
import simvrep.Simulation;

import hill.HillMAS;
import unalcol.algorithm.iterative.ForLoopCondition;
import unalcol.descriptors.Descriptors;
import unalcol.descriptors.WriteDescriptors;

import unalcol.evolution.haea.HaeaOperators;
import unalcol.evolution.haea.HaeaStep;
import unalcol.evolution.haea.HaeaStepDescriptors;
import unalcol.evolution.haea.SimpleHaeaOperators;
import unalcol.evolution.haea.SimpleHaeaOperatorsDescriptor;
import emst.evolution.search.population.PopulationDescriptors;
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
import unalcol.random.real.StandardUniformGenerator;
import unalcol.search.Goal;
import unalcol.search.population.IterativePopulationSearch;
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

			String vrepcommand = new String("./vrep" + j + ".sh");

			// Initialize a v-rep simulator based on the Nsim parameter
			try {
				//ProcessBuilder qq = new ProcessBuilder(vrepcommand, "-h", "scenes/Maze/MRun.ttt"); //Snake
				ProcessBuilder qq = new ProcessBuilder(vrepcommand, "-h",  "scenes/Maze/defaultmhs.ttt"); 
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

			Simulation sim = new Simulation(j, 30);
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
		//int DIM = 169; //Snake
		//int DIM = 281; //CPGH
		int DIM = 3;
		double[] min = DoubleArray.create(DIM, -1);
		double[] max = DoubleArray.create(DIM, 1);

		Space<double[]> space = new HyperCube(min, max);

//		int nmodules = 4;
//		int[] ori = new int[]{1,0,1,0}; //Snake
		
		//String morpho = "[(0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 3.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0, 3.0, 2.0, 1.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0 , 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]";
		String morpho = "[(0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0 , 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]";
		
		// Optimization function
		//OptimizationFunction<double[]> function = new HDebugP(Nsim,simulators,true,nmodules,ori,7,6,1);
		OptimizationFunction<double[]> function = new HEmP(Nsim,simulators,morpho,false);
		MultithreadOptimizationGoal<double[]> goal = new MultithreadOptimizationGoal<double[]>(function);
		goal.setMax_threads(Nsim);

		// Variation Definition
		//AdaptMutationIntensity adapt = new OneFifthRule(20, 0.9);
		
		//Normal mutation and DEXover
		//IntensityMutation variation = new GaussianMutation(0.1, null);
		DEXOver xover = new DEXOver(0.9,0.9,new StandardUniformGenerator(),DIM); //Use NUniqueIndividuals() selection with the DE operator 
		
		
		//Favor mutation and DEXover for old modules in snake configuration
		//Favor first contains the array defining the decrease in mutation probability
		//FavorFirst favor = new FavorFirst(7,6,true,1);
		//Mutation variation = new FFirstIntMutation(0.1,new StandardGaussianGenerator(),favor,7,6,1); //Snake
		
		//Favor Mutation and DEXover for EMeRGE modules in any configuration
		//Favor first contains the array defining the decrease in mutation probability
		//FavorFirst favor = new FavorFirst(5,7,false,1);
		//int[] indices = favor.get(DIM);
		//Mutation variation = new FFirstIntMutation(0.1,new StandardGaussianGenerator(),favor,5,7,1);
		//DEXOver xover = new DEXOver(0.9,favor.getFavorVector(DIM,indices),new StandardUniformGenerator(),DIM);
		
		

		int POPSIZE = 4;
		int MAXITERS = 2;
		Variation[] opers = new Variation[1];
		//opers[0] = variation;
		opers[0] = xover;

		SimpleHaeaOperators operators = new SimpleHaeaOperators(opers);
		Selection selection = new NUniqueIndividuals();
		
		ModifiedHaeaStep step = new ModifiedHaeaStep(POPSIZE, selection, operators);
		step.setJsonManager(new JSONHaeaStepObjectManager());
		PopulationSearch search = new IterativePopulationSearch(step, new ForLoopCondition<Population>(MAXITERS));

		// Track Individuals and Goal Evaluations
				WriteDescriptors write_desc = new WriteDescriptors();
				Write.set(double[].class, new DoubleArrayPlainWrite(false));
				Write.set(HaeaStep.class, new WriteHaeaStep());
				//Descriptors.set(Population.class, new PopulationDescriptors());
				Descriptors.set(Population.class, new PopulationDescriptors());
				Descriptors.set(HaeaStep.class, new HaeaStepDescriptors());
				Descriptors.set(HaeaOperators.class, new SimpleHaeaOperatorsDescriptor());
				Write.set(Population.class, write_desc);
				Write.set(HaeaStep.class, write_desc);
				Write.set(HaeaOperators.class, write_desc);

		// Add tracer based on descriptors set
		//FileTracer tracer = new FileTracer("Evolresult.txt", ',');
		ConsoleTracer tracer1 = new ConsoleTracer();
		Tracer.addTracer(search, tracer1);
		//Tracer.addTracer(search, tracer);
		
		
		EvolutionryAlgorithmSetting easetting = new EvolutionryAlgorithmSetting("testjson", POPSIZE, MAXITERS);

		Solution<double[]> solution = search.solve(space, goal);

		System.out.println(solution.object());

		//tracer.close();
		tracer1.close();
		
		JSONObject result = new JSONObject();
		result.put("settings", easetting.encode());
		result.put("evolution", step.getJsonManager().encode());
		JSONObject jsonsolution = new JSONObject();
		jsonsolution.put("best_individual", solution.object());
		jsonsolution.put("best_fitness", solution.info(Goal.GOAL_TEST));
		result.put("solution", jsonsolution);
		String path="./";
		try( Writer writer = new BufferedWriter(new OutputStreamWriter(
		              new FileOutputStream(path + easetting.title + ".json"), "utf-8"))) {
			writer.write(result.toString());
		}catch(Exception e){
			
		}
		
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
