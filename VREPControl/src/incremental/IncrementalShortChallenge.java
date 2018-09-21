package incremental;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import emst.evolution.haea.ModifiedHaeaStep;
import emst.evolution.json.haea.JSONHaeaStepObjectManager;
import emst.evolution.json.setting.EvolutionryAlgorithmSetting;
import emst.evolution.search.multithread.MultithreadOptimizationGoal;
import emst.evolution.search.population.PopulationDescriptors;
import emst.evolution.search.selection.ModifiedTournament;
import evolHAEA.EmP;
import evolHAEA.HyperCubeFromPoint;
import evolHAEA.PeriodicHAEAStep;
import evolHAEA.PeriodicOptimizationGoal;
import maze.Maze;
import simvrep.ShortChallengeSettings;
import simvrep.Simulation;
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
import unalcol.optimization.real.HyperCube;
import unalcol.optimization.real.mutation.IntensityMutation;
import unalcol.optimization.real.mutation.PermutationPick;
import unalcol.optimization.real.mutation.PowerLawMutation;
import unalcol.optimization.real.xover.LinearXOver;
import unalcol.optimization.real.xover.SimpleXOver;
import unalcol.search.Goal;
import unalcol.search.RealQualityGoal;
import unalcol.search.population.IterativePopulationSearch;
import unalcol.search.population.Population;
import unalcol.search.population.PopulationSearch;
import unalcol.search.selection.Selection;
import unalcol.search.solution.Solution;
import unalcol.search.variation.Variation;
import unalcol.sort.Order;
import unalcol.tracer.ConsoleTracer;
import unalcol.tracer.Tracer;
import unalcol.types.real.array.DoubleArray;
import unalcol.types.real.array.DoubleArrayPlainWrite;
import util.ChromoConversion;

public class IncrementalShortChallenge {

	public static int Nsim;
	public static List<Simulation> simulators;

	public static void main(String[] args) {

		double fitness;
		double maxFitness = -0.5;
		int maxReplicas = 10;
		double[][] lastPop;
		double[][] seedPop = null;

		launchSimulators(args);
		
		String morpho = "[(0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0 , 3.0, 1.0, 3.0, 1.0, 3.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]";
		double[] morphology = ChromoConversion.str2double(morpho);
		
		float[] times;
		float[] envFractions;
		List<Maze> mazeChallenges  = new ArrayList<Maze>();
		List<ShortChallengeSettings> challengeSettings = new ArrayList<ShortChallengeSettings>();
		List<double[][]> seeds = new ArrayList<double[][]>();
		
		/* Read seed populations*/
		
		seedPop = readSeeds("seedPops.json","seedPop0");
		
		/*Experiments*/
		
		times = new float[]{        2.5f,3.2f,5.23f,7.5f ,9.6f,13.9f,16.6f,20.5f,24.9f,30};//b
		envFractions = new float[]{0.05f,0.1f,0.17f,0.24f,0.3f,0.45f,0.55f,0.67f,0.8f ,1};//b
		challengeSettings.add(new ShortChallengeSettings(times, envFractions, 0, 5, "defaultmhs.ttt", false,false));
		mazeChallenges.add(new Maze(new char[] { 's' }, 0.4f, 0.088f, 1));
		seeds.add(null);
	/*			
		times = new float[] {        2.5f,5.23f,10.2f,13.9f,16.6f,20.5f,21.5f,23.4f,24   ,24.9f,27.6f,30};
		envFractions = new float[] {0.05f,0.17f,0.33f,0.45f,0.55f,0.67f,0.7f ,0.75f,0.78f,0.82f,0.91f,1};
		challengeSettings.add(new ShortChallengeSettings(times, envFractions, 0, 5, "defaultmhs.ttt", false,false));
		mazeChallenges.add(new Maze(new char[] { 'l' }, 0.4f, 0.088f, 1));
		seeds.add(null);	
		
		times = new float[] {        2.5f,5.23f,10.2f,13.9f,16.6f,20.5f,21.5f,23.4f,24   ,24.9f,27.6f,30};
		envFractions = new float[] {0.05f,0.17f,0.33f,0.45f,0.55f,0.67f,0.7f ,0.75f,0.78f,0.82f,0.91f,1};
		challengeSettings.add(new ShortChallengeSettings(times, envFractions, 0, 5, "defaultmhs.ttt", false,false));
		mazeChallenges.add(new Maze(new char[] { 'r' }, 0.4f, 0.088f, 1));
		seeds.add(null);
		
		times = new float[]{        2.5f,3.2f,5.23f,7.5f ,9.6f,13.9f,16.6f,20.5f,24.9f,30};//b
		envFractions = new float[]{0.05f,0.1f,0.17f,0.24f,0.3f,0.45f,0.55f,0.67f,0.8f ,1};//b
		challengeSettings.add(new ShortChallengeSettings(times, envFractions, 0, 5, "defaultmhs.ttt", false,false));
		mazeChallenges.add(new Maze(new char[] { 'b' }, 0.4f, 0.088f, 1));
		seeds.add(null);
		
		times = new float[]{        2.5f,3.2f,5.23f,7.5f ,9.6f,13.9f,16.6f,20.5f,24.9f,30};//b
		envFractions = new float[]{0.05f,0.1f,0.17f,0.24f,0.3f,0.45f,0.55f,0.67f,0.8f ,1};//b
		challengeSettings.add(new ShortChallengeSettings(times, envFractions, 0, 5, "defaultmhs.ttt", false,false));
		mazeChallenges.add(new Maze(new char[] { 'b' }, 0.4f, 0.088f, 1));
		seeds.add(seedPop);
		
		times = new float[] {        2.5f ,5.23f ,10.2f,13.9f,16.6f,20.5f,21.5f,23.4f,24   ,24.9f,27.6f,30   ,32.5f,35.23f,40.2f,43.9f,46.6f,50.5f,51.5f,53.4f,54   ,54.9f,57.6f,60   ,69.5f,70.2f,72.23f,74.5f,76.6f,80.9f,83.6f,87.5f,91.9f,100};
		envFractions = new float[] {0.016f,0.056f,0.11f,0.15f,0.18f,0.22f,0.23f,0.25f,0.26f,0.27f,0.30f,0.33f,0.35f,0.39f ,0.44f,0.48f,0.51f,0.55f,0.56f,0.58f,0.59f,0.60f,0.63f,0.66f,0.68f,0.7f ,0.72f ,0.74f,0.76f,0.81f,0.85f,0.89f,0.93f,1};
		challengeSettings.add(new ShortChallengeSettings(times, envFractions, 0, 5, "defaultmhs.ttt", false,false));
		mazeChallenges.add(new Maze(new char[] { 'l','r','b' }, 0.4f, 0.088f, 1));
		seeds.add(seedPop);	
		
		times = new float[] {        2.5f ,5.23f ,10.2f,13.9f,16.6f,20.5f,21.5f,23.4f,24   ,24.9f,27.6f,30   ,32.5f,35.23f,40.2f,43.9f,46.6f,50.5f,51.5f,53.4f,54   ,54.9f,57.6f,60   ,69.5f,70.2f,72.23f,74.5f,76.6f,80.9f,83.6f,87.5f,91.9f,100};
		envFractions = new float[] {0.016f,0.056f,0.11f,0.15f,0.18f,0.22f,0.23f,0.25f,0.26f,0.27f,0.30f,0.33f,0.35f,0.39f ,0.44f,0.48f,0.51f,0.55f,0.56f,0.58f,0.59f,0.60f,0.63f,0.66f,0.68f,0.7f ,0.72f ,0.74f,0.76f,0.81f,0.85f,0.89f,0.93f,1};
		challengeSettings.add(new ShortChallengeSettings(times, envFractions, 0, 5, "defaultmhs.ttt", false,false));
		mazeChallenges.add(new Maze(new char[] { 'r','l','b' }, 0.4f, 0.088f, 1));
		seeds.add(seedPop);
*/
		ShortChallengeSettings settings;
		Maze maze;
		
		for (int challenge = 0;challenge < challengeSettings.size();challenge++){
			
			settings = challengeSettings.get(challenge);
			maze = mazeChallenges.get(challenge);

			for (int repli = 0; repli < maxReplicas; repli++) {
				
				settings.selectChallenge(0);
				lastPop = seeds.get(challenge);

				JSONObject challengeResult = new JSONObject();
				
				JSONObject test = new JSONObject();
				test.put("Name", "IncrF"+new String(maze.structure)+challenge+repli);
				test.put("times", settings.getTimes());
				test.put("envFractions", settings.getFractions());
				test.put("noisy", settings.noisy);
				test.put("individualParamters", settings.individualParameters);
				test.put("maze", maze.structure);
				test.put("width", maze.width);
				test.put("height", maze.height);
				test.put("steps", maze.nBSteps);
				test.put("maxFitness", maxFitness);
				
				challengeResult.put("test", test);
				

				for (int i = 0; i < settings.getFractions().length; i++) {

					JSONObject challengeStep = new JSONObject();

					if (lastPop != null) {
						simulators = new ArrayList<Simulation>();
						connectToSimulator(0);
						EmP function = new EmP(simulators, 1, morphology, maze, settings);
						fitness = function.apply(lastPop[0]);
						simulators.get(0).Disconnect();
						challengeStep.put("lastBest", lastPop[0]);
						challengeStep.put("fitness", fitness);
					} else {
						fitness = 1.0;
						challengeStep.put("lastBest", -1);
						challengeStep.put("fitness", fitness);
					}

					System.out.println("Fitness = " + fitness + ", Challenge: " + settings.getSelection());

					if (fitness > maxFitness) {
						try {
							Solution<double[]>[] bestPop = evolve(morphology, maze, settings, 50, 30,
									maxFitness,(String) test.get("Name"), lastPop);
							
							lastPop = new double[bestPop.length][];
							double[] lastPopFitness = new double[bestPop.length];
							for(int j = 0; j < lastPop.length; j++){
								lastPop[j] = bestPop[j].object();
								lastPopFitness[j] = (double) bestPop[j].info(Goal.GOAL_TEST);
								System.out.println("Fitness "+ j + " = " + bestPop[j].info(Goal.GOAL_TEST));
							}
							fitness = (double) bestPop[0].info(Goal.GOAL_TEST);

							challengeStep.put("lastBestEvol", lastPop[0]);
							challengeStep.put("lastPopEvol", lastPop);
							challengeStep.put("lastPopFitness", lastPopFitness);
							challengeStep.put("fitnessEvol", fitness);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						challengeStep.put("lastBestEvol", -1);
						challengeStep.put("lastPopEvol", -1);
						challengeStep.put("lastPopFitness", -1);
						challengeStep.put("fitnessEvol", -1);
					}

					settings.selectNextChallenge();
					challengeResult.put("challenge" + i, challengeStep);
				}

				try (Writer writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(test.get("Name")+".json"), "utf-8"))) {
					writer.write(challengeResult.toString());
				} catch (Exception e) {

				}

			}
		}
		
		stopSimulators();
	}

	
	static Solution<double[]>[] evolve(double[] morphology, Maze maze, ShortChallengeSettings settings,int POPSIZE,int MAXITERS, double maxFitness,String name, double[][] lastPop)
		throws IOException { // Must test for fitness and max iterations finishing conditions

		simulators = new ArrayList<Simulation>();
		for (int j = 0; j < Nsim; j++) {
			connectToSimulator(j);
		}
		
		int realDIM = 234;
		double[] min = DoubleArray.create(realDIM, -10);
		double[] max = DoubleArray.create(realDIM, 10);
		
		HyperCube realSpace;
		if (lastPop!= null){
		double[][] referencePoints = lastPop; 
		double[] radius = DoubleArray.create(realDIM, 0.2);//Can this be done without problems?
		realSpace = new HyperCubeFromPoint(min, max, referencePoints, radius);
		}else{
			realSpace = new HyperCube(min, max);
		}
		
		
		
		//HyperCube realSpace = new HyperCube(min, max);
		

		OptimizationFunction<double[]> function = new EmP(simulators, Nsim, morphology, maze, settings);
		MultithreadOptimizationGoal<double[]> goal = new PeriodicOptimizationGoal<double[]>(function);
		goal.setMax_threads(Nsim);

		IntensityMutation realVariation = new PowerLawMutation(0.2, new PermutationPick(23));
		LinearXOver realXOver = new LinearXOver(); // Use Tournament(4)
		SimpleXOver simpleXOver = new SimpleXOver();

		//int POPSIZE = 30;
		//int MAXITERS = 100;
		Variation[] opers = new Variation[3];
		opers[0] = realVariation;
		opers[1] = realXOver;
		opers[2] = simpleXOver;

		SimpleHaeaOperators operators = new SimpleHaeaOperators(opers);
		Selection selection = new ModifiedTournament(4);

		// ModifiedHaeaStep step = new ModifiedHaeaStep(POPSIZE, selection, operators);
		ModifiedHaeaStep step = new PeriodicHAEAStep(POPSIZE, selection, operators);
		step.setJsonManager(new JSONHaeaStepObjectManager());
		PopulationSearch search = new IterativePopulationSearch(step, new ForLoopFitnessCondition(MAXITERS, maxFitness));

		// Track Individuals and Goal Evaluations
		WriteDescriptors write_desc = new WriteDescriptors();
		Descriptors.set(Population.class, new PopulationDescriptors());
		Descriptors.set(HaeaStep.class, new HaeaStepDescriptors());
		Descriptors.set(HaeaOperators.class, new SimpleHaeaOperatorsDescriptor());
		Write.set(double[].class, new DoubleArrayPlainWrite(false));
		Write.set(HaeaStep.class, new WriteHaeaStep());
		// Descriptors.set(Population.class, new
		// PopulationDescriptors());
		Write.set(Population.class, write_desc);
		Write.set(HaeaStep.class, write_desc);
		Write.set(HaeaOperators.class, write_desc);

		// Add tracer based on descriptors set
		// FileTracer tracer = new FileTracer("Evolresult.txt", ',');
		ConsoleTracer tracer1 = new ConsoleTracer();
		Tracer.addTracer(search, tracer1);

		EvolutionryAlgorithmSetting easetting = new EvolutionryAlgorithmSetting(name +"evol"+ settings.getSelection(), POPSIZE,
				MAXITERS);

		Solution<double[]> solution = search.solve(realSpace, goal);
		Population<Solution<double[]>> pop = ((PeriodicHAEAStep)step).lastPop;
		 
		//Solution<double[]>[] spop = new Solution[pop.size()];
		int[] indexes = new int[pop.size()];
		for(int i = 0; i < pop.size(); i++) {
			//System.out.println(pop.get(i).object());
			//spop[i] = pop.get(i).object();
			indexes[i] = i;
		}
    	Double[] popFitness = goal.apply((Solution[])pop.object());
    	sort(indexes, popFitness, goal.order());
    	
    	Solution<double[]>[] bestpop = new Solution[(int)(0.1 * pop.size())];
    	for(int i = 0; i < bestpop.length; i++)
    		bestpop[i] = (Solution)pop.get(indexes[i]);

		//System.out.println(solution.object());

		tracer1.close();
		
		JSONObject result = new JSONObject();
		result.put("settings", easetting.encode());
		result.put("evolution", step.getJsonManager().encode());
		JSONObject jsonsolution = new JSONObject();
		jsonsolution.put("best_individual", solution.object());
		jsonsolution.put("best_fitness", solution.info(Goal.GOAL_TEST));
		result.put("solution", jsonsolution);
		String path = "./";
		try (Writer writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(path + easetting.title + ".json"), "utf-8"))) {
			writer.write(result.toString());
		} catch (Exception e) {

		}

		for (Simulation sim : simulators) {
			sim.Disconnect();
		}
		
		return bestpop;

	}
	
private static double[][] readSeeds(String fileName,String key) {
		
		double[][] seed = null;
		
		try {
			JSONParser parser = new JSONParser();
			Object readParser = parser.parse(new FileReader(
                    fileName));
			JSONObject seedObject = (JSONObject) readParser;
			if (seedObject.containsKey(key)) {
				JSONArray seedJSONArray = (JSONArray) seedObject.get(key);

				seed = new double[seedJSONArray.size()][];

				for (int i = 0; i < seedJSONArray.size(); i++) {
					JSONArray innerArray = (JSONArray) seedJSONArray.get(i);
					double[] innerResult = new double[innerArray.size()];
					for (int j = 0; j < innerArray.size(); j++) {
						innerResult[j] = (double) innerArray.get(j);
						 //System.out.print(innerResult[j]+", ");
					}
					//System.out.println();
					seed[i] = innerResult;
				}
			}
		}catch (Exception e) {
            e.printStackTrace();
        }
		return seed;
	}

	private static void sort(int[] indexes, Double[] fitness, Order<Double> order) {
		quicksort(indexes, fitness, order, 0, indexes.length - 1);
	}

	private static void quicksort(int[] indexes, Double[] fitness, Order<Double> order, int lo, int hi) {
		if (lo < hi) {
			int p = partition(indexes, fitness, order, lo, hi);
			quicksort(indexes,fitness,order,lo,p-1);
			quicksort(indexes,fitness, order, p+1,hi);
		}
		
	}

	private static int partition(int[] indexes, Double[] fitness, Order<Double> order, int lo, int hi) {
		double pivot = fitness[indexes[hi]];
		int i = lo;
		int temp;
		for (int j = lo; j<hi;j++) {
			if (order.compare(fitness[indexes[j]], pivot) > 0) {
				temp = indexes[i];
				indexes[i] = indexes[j];
				indexes[j] = temp;
				i = i + 1;
			}
		}
		temp = indexes[i];
		indexes[i] = indexes[hi];
		indexes[hi] = temp;
		return i;
	}

	
	static void connectToSimulator(int simulatorNumber) {
		Simulation sim = new Simulation(simulatorNumber);
		// Retry if there is a simulator crash
		for (int i = 0; i < 5; i++) {
			if (sim.Connect()) {
				simulators.add(sim);
			} else {
				// No connection could be established
				System.out.println("Failed connecting to remote API server");
				System.out.println("Trying again for the " + i + " time in " + simulatorNumber);
				continue;
			}
			break;
		}
	}

	static void stopSimulators() {
		
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
	
	static void launchSimulators(String[] args) {
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

		
		System.out.println(Nsim);
		// Start Simulators
		for (int j = 0; j < Nsim; j++) {

			String vrepcommand = new String("./vrep" + j + ".sh");

			// Initialize a v-rep simulator based on the Nsim parameter
			try {
				// ProcessBuilder qq = new ProcessBuilder(vrepcommand,
				// "-h",
				// "scenes/Maze/MRun.ttt"); //Snake
				// ProcessBuilder qq = new ProcessBuilder("xvfb-run","-a",vrepcommand, "-h",
				// "scenes/Maze/defaultmhs.ttt");
				ProcessBuilder qq = new ProcessBuilder(vrepcommand,"-h", "scenes/Maze/defaultmhs.ttt");
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

	}
}
