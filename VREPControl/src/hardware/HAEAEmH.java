package hardware;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import emst.evolution.haea.ModifiedHaeaStep;
import emst.evolution.json.haea.JSONHaeaStepObjectManager;
import emst.evolution.json.setting.EvolutionryAlgorithmSetting;
import emst.evolution.search.multithread.MultithreadOptimizationGoal;
import emst.evolution.search.population.PopulationDescriptors;
import emst.evolution.search.selection.ModifiedTournament;
import evolHAEA.EmP;
import evolHAEA.PeriodicHAEAStep;
import evolHAEA.PeriodicOptimizationGoal;
import maze.Maze;
import simvrep.Simulation;
import simvrep.SimulationSettings;
import unalcol.algorithm.iterative.ForLoopCondition;
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
import unalcol.search.population.IterativePopulationSearch;
import unalcol.search.population.Population;
import unalcol.search.population.PopulationSearch;
import unalcol.search.selection.Selection;
import unalcol.search.solution.Solution;
import unalcol.search.variation.Variation;
import unalcol.tracer.ConsoleTracer;
import unalcol.tracer.Tracer;
import unalcol.types.real.array.DoubleArray;
import unalcol.types.real.array.DoubleArrayPlainWrite;
import util.ChromoConversion;

public class HAEAEmH {
	
	public static int Nsim;
	public static List<Simulation> simulators;
	
	public static void main(String[] args) throws IOException {
		
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
		
		for (int repli = 0; repli < 1;repli++){
			
			simulators = new ArrayList<Simulation>();
			System.out.println(Nsim);
			// Start Simulators
			for (int j = 0; j < Nsim; j++) {
	
				String vrepcommand = new String("./vrep" + j + ".sh");
	
				// Initialize a v-rep simulator based on the Nsim parameter
				try {
					// ProcessBuilder qq = new ProcessBuilder(vrepcommand,
					// "-h",
					// "scenes/Maze/MRun.ttt"); //Snake
					ProcessBuilder qq = new ProcessBuilder("xvfb-run","-a",vrepcommand, "-h",  "scenes/Maze/defaultmhsH.ttt");
					//ProcessBuilder qq = new ProcessBuilder(vrepcommand, "-h", "scenes/Maze/defaultmhsH.ttt");
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
	
				Simulation sim = new Simulation(j);
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
			
			
			SimulationSettings settings = new SimulationSettings(5,"defaultmhsH.ttt",180,false,false);
			Maze maze = new Maze(new char[]{'s','r','l'},0.4f,0.088f,1);
			//Maze maze = new Maze(new char[]{'l'},0.4f,0.088f,1);
			
			int realDIM = 234;
			double[] min = DoubleArray.create(realDIM, -10);
			double[] max = DoubleArray.create(realDIM, 10);
			
//			double[][] referencePoints = new double[][] { 
//				{-8.7022977429907, -2.6940289379121, -1.4431944620278, -1.3156390547095, -5.9917088877257, 2.1502158872271, -1.8173197715124, 6.3601008200855, -3.5660225156747, 5.5365650414796, 4.5212175819355, -1.5748587388453, -1.8679579950777, -3.3668785635023, 2.7163317783425, -4.0101967559982, 0.44827410718419, -1.7826905127019, -0.65681582474724, 2.1614455769806, -1.5849860711937, 4.6201628870038, -8.4232000226166, 0.34810318480051, -1.9561337672583, 2.1297740282082, -6.599877824989, -1.7311370083714, -0.30351509274486, -3.6028046113231, 1.1641933966043, -0.15531190391452, 1.819400090726, 10.656706273988, -4.7216174713638, -1.7198508307612, -5.1067507578955, -0.75965039822201, -0.6579242546011, -7.3013308106715, -0.21916202633551, -0.74727257602751, -0.53323046645488, -2.1442090520183, 2.2553169249299, 1.0034260756077, 7.4488926612386, -2.1539554315773, 2.8153406617647, -0.29526789914479, -1.0491002625198, -0.63245210557883, -2.9849698646478, -1.5463809407321, 0.96925360777549, -5.4092555565163, 1.9643007245311, 1.0459478488144, -0.56989714410641, -5.8919576498618, 2.6547816271166, -4.0627558905375, 5.3785225012908, -1.6034413483164, -1.3444207394666, -1.938815350591, 4.8898545538278, -2.2853795958756, -4.5907575279811, -2.1747926125386, -3.1106557973604, -0.77782506746639, 1.7396797732138, 2.011391695184, 0.6153368332229, 0.98637466757503, 3.2085267284138, 3.55296444308, -3.2946241076121, -1.7416794295971, -0.27879789327323, 3.0994469773162, -0.044354691587204, 0.53502706021983, 0.84570892297645, -1.0197552885501, -0.39889241840827, 0.3620244886194, -0.12366261796141, 0.29646795489099, 2.3240841335079, -8.4793716791394, -1.5566841197772, -6.7924664007132, -1.9910793384311, -0.78604287753067, 0.94263425272582, 0.99455490139006, 0.1831063108759, 10.90404258638, 10.744887188833, -4.9222827348566, -6.4798724096407, -5.6304014282439, -6.3528058457285, -2.208271088642, -3.7001251209713, 2.5781239043878, -2.9706634753782, 0.9786632379228, 0.020026964292823, -5.4532939399025, 2.4832338183363, 5.5787478218772, -4.5145348942327, -2.1718640639422, -0.24742084042858, -6.5209491579998, 0.98340759484828, -3.3252754648605, -0.94841689635436, -4.2841796312746, -1.4250901527817, 1.5286600673457, 3.837692285624, -1.1024029727031, 5.8515496386829, -3.0508515848578, -2.8128737198484, -1.9431243554053, -2.5090990179585, -8.3949707093562, -0.67998410170728, 2.0720362235925, -0.79073010602328, 2.2604178960305, 0.61465491232089, 2.3565744551852, 1.6467717459555, -0.73113096245264, 0.18985241357151, 3.2528697425, 0.87636462277402, -11.021363851094, -5.7281075493773, 1.5184498545129, -4.840164666457, 1.3674077626239, -2.2987748018878, -1.4315083080383, -2.1543288290971, -0.34256357082856, 5.3855339468474, 13.93828812775, -0.97423073355602, -5.0715253305112, 0.19062069143681, 0.39194993983837, -0.054760624972746, 0.17333144077449, -0.59174249319452, 0.015125217539116, 2.1710706839209, -4.4253987585661, -0.16869808641266, 0.45463318262276, 0.3808873668679, -1.7601356411731, -0.13205893642859, -5.9534729576773e-12, -8.9072011445436e-10, -4.5784812691234e-11, -1.5891738774221e-10, 6.0718920077286e-10, -4.9581008049421e-11, 4.9755584480526e-12, 3.0461048315964e-10, 2.9626531267145e-10, -2.3323492308976e-09, -4.4026928010955e-10, -1.1423027652003e-10, 5.3121531421634e-11, 0.61903920840411, -2.5387545460449e-10, -1.8069363123008e-11, -4.4456626411208e-11, 1.7101606973857e-10, -1.5142168508576e-11, 2.2987751203757e-12, 1.1293474376568e-10, 1.1529109609123e-10, -8.793904786674e-10, -1.3115399712235e-10, -4.238271594337e-11, 1.4848974383276e-11, -0.61903920840895, -3.405586182891e-10, -2.3207905052837e-11, -5.9813425351051e-11, 2.2984556514917e-10, -2.0079473115747e-11, 2.8652693197513e-12, 1.4600689472198e-10, 1.4827024668214e-10, -1.1343333788331e-09, -1.7480851755986e-10, -5.4766160102315e-11, 1.9979975684791e-11, -0.28021913792796, 6.9958794431925, 1.9149080083781, 1.6063659070415, -3.6176661260524, 1.318813811095, -0.23260515416759, -0.84301615625568, -4.6100931948043, 1.2200884419545, 3.143388151748, 0.47630009637218, -1.1064774052773, -0.16971508183716, -3.1912106322592, 2.2794946034352, -0.28934785714308, 0.2129963491506, -0.26788281062592, -0.43967702357483, -2.0035689388687, -3.7853548813787, 5.3783756402157, 3.0580856959208, 1.3283480946602, 0.73279123887078}
//			};
//			double[] radius = DoubleArray.create(realDIM, 0.2);
			
			HyperCube realSpace = new HyperCube(min, max);
			//HyperCube realSpace = new HyperCubeFromPoint(min, max, referencePoints, radius);
			
			//Snake morphology
			String morpho = "[(0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0 ,3.0 , 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]";
			//T with shoulder module morphology
			//String morpho = "[(0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,3.0, 1.0, 2.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0, 1.0, 2.0, 1.0, 1.0, 2.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0 , 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0]";
			double[] morphology = ChromoConversion.str2double(morpho);
			
			OptimizationFunction<double[]> function = new EmP(simulators,Nsim,morphology,maze,settings);
			//ShortChallengeEmP function = new ShortChallengeEmP(simulators,Nsim,morphology,maze,settings);
			//GenerationalEnvEmP function = new GenerationalEnvEmP(simulators,Nsim,morphology,maze,settings,10);
			MultithreadOptimizationGoal<double[]> goal = new PeriodicOptimizationGoal<double[]>(function);
			goal.setMax_threads(Nsim);
			
			IntensityMutation realVariation = new PowerLawMutation(0.2, new PermutationPick(23));
			LinearXOver realXOver = new LinearXOver(); // Use Tournament(4)
			SimpleXOver simpleXOver = new SimpleXOver();
			
			int POPSIZE = 30;
			int MAXITERS = 100;
			Variation[] opers = new Variation[3];
			opers[0] = realVariation;
			opers[1] = realXOver;
			opers[2] = simpleXOver;
			
			SimpleHaeaOperators operators = new SimpleHaeaOperators(opers);
			Selection selection = new ModifiedTournament(4);
			
			//ModifiedHaeaStep step = new ModifiedHaeaStep(POPSIZE, selection, operators);
			ModifiedHaeaStep step = new PeriodicHAEAStep(POPSIZE, selection, operators);
			step.setJsonManager(new JSONHaeaStepObjectManager());
			PopulationSearch search = new IterativePopulationSearch(step,
					new ForLoopCondition<Population>(MAXITERS));
			
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
			
			EvolutionryAlgorithmSetting easetting = new EvolutionryAlgorithmSetting(
					"HAEAHrlR"+repli, POPSIZE, MAXITERS);
			
			Solution<double[]> solution = search.solve(realSpace, goal);
			
			System.out.println(solution.object());
			
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




}
