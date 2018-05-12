package hill;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;

import evolHAEA.HEmP;
import evolHAEA.HyperCubeFromPoint;
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
import unalcol.optimization.real.mutation.PowerLawMutation;
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
		int envInst = 0;
		int numberOfReplicas = 0;


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
				if (args.length >= 2) {
					envInst = Integer.parseInt(args[1]);
				} else {
					System.err.println("Provide an environment instance");
					System.exit(1);
				}
				if (args.length >= 3) {
					numberOfReplicas = Integer.parseInt(args[2]);
				} else {
					System.err.println("Provide a number of replicas");
					System.exit(1);
				}
			} catch (NumberFormatException e) {
				System.err.println("Arguments must be integer.");
				System.exit(1);
			}
		} else {
			System.err.println("Missing arguments");
			System.exit(1);
		}

		String vrepcommand = new String("./vrep" + process + ".sh");

			for (int repli = numberOfReplicas * process; repli < (numberOfReplicas * process) + numberOfReplicas; repli++) {

				// // Initialize a v-rep simulator based on the Nsim parameter
				try {
					// ProcessBuilder qq = new ProcessBuilder(vrepcommand, "-h",
					// "scenes/Maze/MRun.ttt"); //Snake
					//ProcessBuilder qq = new ProcessBuilder("xvfb-run","-a",vrepcommand, "-h",  "scenes/Maze/defaultmhs.ttt");
					ProcessBuilder qq = new ProcessBuilder(vrepcommand, "-h", "scenes/Maze/defaultmhs.ttt");
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
				double[] min = DoubleArray.create(DIM, -10);
				double[] max = DoubleArray.create(DIM, 10);

				// Reference Points: Manual controllers from which the initial
				// population will be generated
				double[][] referencePoints = new double[][] { {-8.7022977429907, -2.6940289379121, -1.4431944620278, -1.3156390547095, -5.9917088877257, 2.1502158872271, -1.8173197715124, 6.3601008200855, -3.5660225156747, 5.5365650414796, 4.5212175819355, -1.5748587388453, -1.8679579950777, -3.3668785635023, 2.7163317783425, -4.0101967559982, 0.44827410718419, -1.7826905127019, -0.65681582474724, 2.1614455769806, -1.5849860711937, 4.6201628870038, -8.4232000226166, 0.34810318480051, -1.9561337672583, 2.1297740282082, -6.599877824989, -1.7311370083714, -0.30351509274486, -3.6028046113231, 1.1641933966043, -0.15531190391452, 1.819400090726, 10.656706273988, -4.7216174713638, -1.7198508307612, -5.1067507578955, -0.75965039822201, -0.6579242546011, -7.3013308106715, -0.21916202633551, -0.74727257602751, -0.53323046645488, -2.1442090520183, 2.2553169249299, 1.0034260756077, 7.4488926612386, -2.1539554315773, 2.8153406617647, -0.29526789914479, -1.0491002625198, -0.63245210557883, -2.9849698646478, -1.5463809407321, 0.96925360777549, -5.4092555565163, 1.9643007245311, 1.0459478488144, -0.56989714410641, -5.8919576498618, 2.6547816271166, -4.0627558905375, 5.3785225012908, -1.6034413483164, -1.3444207394666, -1.938815350591, 4.8898545538278, -2.2853795958756, -4.5907575279811, -2.1747926125386, -3.1106557973604, -0.77782506746639, 1.7396797732138, 2.011391695184, 0.6153368332229, 0.98637466757503, 3.2085267284138, 3.55296444308, -3.2946241076121, -1.7416794295971, -0.27879789327323, 3.0994469773162, -0.044354691587204, 0.53502706021983, 0.84570892297645, -1.0197552885501, -0.39889241840827, 0.3620244886194, -0.12366261796141, 0.29646795489099, 2.3240841335079, -8.4793716791394, -1.5566841197772, -6.7924664007132, -1.9910793384311, -0.78604287753067, 0.94263425272582, 0.99455490139006, 0.1831063108759, 10.90404258638, 10.744887188833, -4.9222827348566, -6.4798724096407, -5.6304014282439, -6.3528058457285, -2.208271088642, -3.7001251209713, 2.5781239043878, -2.9706634753782, 0.9786632379228, 0.020026964292823, -5.4532939399025, 2.4832338183363, 5.5787478218772, -4.5145348942327, -2.1718640639422, -0.24742084042858, -6.5209491579998, 0.98340759484828, -3.3252754648605, -0.94841689635436, -4.2841796312746, -1.4250901527817, 1.5286600673457, 3.837692285624, -1.1024029727031, 5.8515496386829, -3.0508515848578, -2.8128737198484, -1.9431243554053, -2.5090990179585, -8.3949707093562, -0.67998410170728, 2.0720362235925, -0.79073010602328, 2.2604178960305, 0.61465491232089, 2.3565744551852, 1.6467717459555, -0.73113096245264, 0.18985241357151, 3.2528697425, 0.87636462277402, -11.021363851094, -5.7281075493773, 1.5184498545129, -4.840164666457, 1.3674077626239, -2.2987748018878, -1.4315083080383, -2.1543288290971, -0.34256357082856, 5.3855339468474, 13.93828812775, -0.97423073355602, -5.0715253305112, 0.19062069143681, 0.39194993983837, -0.054760624972746, 0.17333144077449, -0.59174249319452, 0.015125217539116, 2.1710706839209, -4.4253987585661, -0.16869808641266, 0.45463318262276, 0.3808873668679, -1.7601356411731, -0.13205893642859, -5.9534729576773e-12, -8.9072011445436e-10, -4.5784812691234e-11, -1.5891738774221e-10, 6.0718920077286e-10, -4.9581008049421e-11, 4.9755584480526e-12, 3.0461048315964e-10, 2.9626531267145e-10, -2.3323492308976e-09, -4.4026928010955e-10, -1.1423027652003e-10, 5.3121531421634e-11, 0.61903920840411, -2.5387545460449e-10, -1.8069363123008e-11, -4.4456626411208e-11, 1.7101606973857e-10, -1.5142168508576e-11, 2.2987751203757e-12, 1.1293474376568e-10, 1.1529109609123e-10, -8.793904786674e-10, -1.3115399712235e-10, -4.238271594337e-11, 1.4848974383276e-11, -0.61903920840895, -3.405586182891e-10, -2.3207905052837e-11, -5.9813425351051e-11, 2.2984556514917e-10, -2.0079473115747e-11, 2.8652693197513e-12, 1.4600689472198e-10, 1.4827024668214e-10, -1.1343333788331e-09, -1.7480851755986e-10, -5.4766160102315e-11, 1.9979975684791e-11, -0.28021913792796, 6.9958794431925, 1.9149080083781, 1.6063659070415, -3.6176661260524, 1.318813811095, -0.23260515416759, -0.84301615625568, -4.6100931948043, 1.2200884419545, 3.143388151748, 0.47630009637218, -1.1064774052773, -0.16971508183716, -3.1912106322592, 2.2794946034352, -0.28934785714308, 0.2129963491506, -0.26788281062592, -0.43967702357483, -2.0035689388687, -3.7853548813787, 5.3783756402157, 3.0580856959208, 1.3283480946602, 0.73279123887078}};
				double[] radius = DoubleArray.create(DIM, 0.2);

				// Space<double[]> space = new HyperCube(min, max);
				Space<double[]> space = new HyperCubeFromPoint(min, max, referencePoints, radius);

				String morpho = "[(0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0 , 3.0, 1.0, 3.0, 1.0, 3.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]";

				OptimizationFunction<double[]> function = new HEmP(0.88f, sim, morpho, "GeneralTest", envInst);

				OptimizationGoal<double[]> goal = new OptimizationGoal<double[]>(function);

				//IntensityMutation variation = new GaussianMutation(0.05, new PermutationPick(10));
				IntensityMutation variation = new PowerLawMutation(0.2, new PermutationPick(23));

				// Search Method
				int MAXITERS = 3000;
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
				 FileTracer tracer = new FileTracer("HillClimb" + envInst + "R" + repli + "P"+process+".txt",
				 ',');
				ConsoleTracer tracer1 = new ConsoleTracer();

				// Tracer.addTracer(search, tracer);
				Tracer.addTracer(HEmP.class, tracer);
				Tracer.addTracer(search, tracer1);

				Solution<double[]> solution = search.solve(space, goal);

				// Double fitness = (Double) solution.data("goal");
				double[] individual = solution.object();

				try (Writer writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream("HillEnv" + envInst + "R" + repli + "P"+process+".txt"), "utf-8"))) {
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
					//ProcessBuilder qq = new ProcessBuilder("killall", "Xvfb");
					File log = new File("Simout/log");
					qq.redirectErrorStream(true);
					qq.redirectOutput(Redirect.appendTo(log));
					Process p = qq.start();
					int exitVal = p.waitFor();
					System.out.println("Terminated vrep" + process + " with error code " + exitVal);
				} catch (Exception e) {
					System.out.println(e.toString());
					e.printStackTrace();
				}
			}
		

	}

}
