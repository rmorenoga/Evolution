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

		for (int envinst = 0; envinst < 1; envinst++) {

			for (int repli = 10 * process; repli < (10 * process) + 10; repli++) {

				// // Initialize a v-rep simulator based on the Nsim parameter
				try {
					// ProcessBuilder qq = new ProcessBuilder(vrepcommand, "-h",
					// "scenes/Maze/MRun.ttt"); //Snake
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
				double[][] referencePoints = new double[][] { { -9.960496041571, 0.48770541628706, -1.8726482635565,
						-3.3147750732174, -4.4203062112601, -2.7755524942299, 1.0791326986978, 2.4575271521755,
						-2.3255724274345, 1.0641914525615, 9.5253971719276, -0.62022146086914, 1.0805203731381,
						-2.9004181390142, -0.71549291507441, -2.8453331124343, 0.47196336155314, -0.89182847971913,
						2.3899376454812, 1.3842326772448, 2.3973345010105, -3.8300930619903, -4.2477424133259,
						-3.1637124434277, 1.7524697303111, 0.22889572822404, -4.4991823395621, -1.4438827694834,
						-4.2382454216631, 7.8895423823548, -4.7715064243778, 0.68251215081754, -0.89243442404923,
						-2.9725192571305, 3.4483616808649, 3.0843115986506, 2.2810218451344, 0.65690490170098,
						-0.86046074205366, -4.6753024580067, 2.0941799100807, -4.4475780502926, -3.4460950738499,
						-3.5779586625023, 0.25632167495679, 1.0073053443793, 3.0712609204477, 1.263253907612,
						1.5513504852852, 2.9675597500165, -1.1069423284687, -0.9378758462451, -0.25692913429154,
						2.3875566440772, -3.3754843660976, 4.6687372308082, -4.9999642663762, -1.3177731592481,
						1.6442784530769, -2.6885629120182, 0.91105079574055, 0.68816002757531, -0.49101445264175,
						1.5798075614383, -0.11108820737545, -3.9874514308218, 2.6719901405741, 1.2938644872143,
						-3.2918889692879, -0.61507630720942, -1.1919312021175, 1.9803597367888, 4.1054996890616,
						-6.1028594065134, -4.9916460999343, -5.596870422244, -0.90220923475085, 1.7746165000358,
						0.24730356383706, 2.0639527041832, 1.7820313837956, -3.7281454166747, -3.5680956959767,
						2.126037494905, -1.3597342421565, -4.8757393015966, -13.599280392968, -1.0236212585887,
						-1.9986937085117, -0.98449738725683, 2.5381702749837, -1.6835471578218, -4.3700635067024,
						3.9816438229699, -2.2651719602316, -2.4499355525659, -1.526743017978, -1.345473060294,
						-7.2882222857705, 0.7206426266054, 2.0703291671297, -5.2726035618763, 3.2504122865573,
						-1.370377559848, -4.8605241160934, -5.4623680985104, 1.3668704448347, 2.0953819899284,
						-1.0165107112464, 0.11710583362635, 2.3171761579802, 4.22594840294, -4.193289609025,
						-3.8917624910303, -3.6296317327091, -0.84629245024443, 3.0292729184377, -0.75693289634797,
						-1.8649934571064, -1.0376584365055, -4.9367805574695, 7.336950274952, -2.7615614754866,
						1.1702010140037, 0.85223609423324, -1.7119983846677, -3.1986505559022, 3.5543584022737,
						0.19156212786222, 1.0762174609863, -2.5955502048723, -1.9701496907169, 0.41419140145703,
						0.52314291210286, 0.28727735784645, -1.5962185852475, -0.29601521997371, -4.1794053343873,
						-5.4248326641569, 0.77036074344697, -0.12216594872868, -3.1384846421613, -2.0483962467594,
						-2.6731795481088, 3.7750413148562, -2.4964892340431, -0.63702914483335, -0.040250485814532,
						-1.5346427139702, 0.070767563873369, -8.2223849625811, -0.0593240073269, -3.5837280732118,
						-2.5825103300542, 0.33118145235272, 2.743080158461, -0.026202524504173, 0.40328766074883,
						0.1014494531832, -5.0046470340879, 1.5230592244679, 2.714283734033, 1.4675028447197,
						-0.097203313776742, -0.17910239929233, -1.8129779308315, 0.0033123179608771, -0.16761809261654,
						-0.50629779526629, 0.029063818798847, 0.17558124969962, -2.0572390193485, 0.16982752862005,
						-0.11004259649187, -0.079700570730394, 4.0626254515556, 0.0074705586387355, 0.014833173326512,
						-4.7424642596489, 0.0014586219474112, -1.5393080466963, -0.020200994625055, 0.61937370601997,
						-0.00038562766489294, 0.0035796542267202, 0.0012903844816828, -0.00013508503620867,
						-0.00069558710458478, -0.00062458377162858, 0.0022266378345129, 0.0008851969864916,
						-0.0035643924078613, 0.000319329932868, -0.062686051923112, -0.00020777005094149,
						-0.61943078654181, 0.00044947453389559, -0.0042946950734949, -0.0015065027096362,
						0.00015519158427074, 0.00081622807738659, 0.00073839593027347, -0.0025981918811329,
						-0.0010279003444346, 0.0042061224664752, -0.0003697315729208, 0.073058234912299,
						0.00023080364030603, -0.66000442155113, -2.2810908064684, -2.2399241179821, 0.67118645642374,
						-0.20454105776002, 0.69324797404124, -0.13245426951191, -2.8248962091085, 1.8215481283257,
						2.4724389268808, 1.3205365149443, -2.1096182987895, 2.0850356628176, 0.38936060422223,
						-5.3074412058732, 4.1750091508276, 1.5124596507441, 1.5581076642247, -1.3426595611686,
						-2.574852327335, -1.5145332312447, -0.78386328365214, -0.48912332543451, 0.17186665779134,
						2.4164124784381, 4.2377342934697 }, };
				double[] radius = DoubleArray.create(DIM, 0.1);

				// Space<double[]> space = new HyperCube(min, max);
				Space<double[]> space = new HyperCubeFromPoint(min, max, referencePoints, radius);

				String morpho = "[(0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0 , 3.0, 1.0, 3.0, 1.0, 3.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]";

				OptimizationFunction<double[]> function = new HEmP(0.88f, sim, morpho, "GeneralTest", envinst);

				OptimizationGoal<double[]> goal = new OptimizationGoal<double[]>(function);

				IntensityMutation variation = new GaussianMutation(0.05, new PermutationPick(10));

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
				// FileTracer tracer = new FileTracer("HillClimb"+repli+".txt",
				// ',');
				ConsoleTracer tracer1 = new ConsoleTracer();

				// Tracer.addTracer(search, tracer);
				// Tracer.addTracer(HEmP.class, tracer);
				Tracer.addTracer(search, tracer1);

				Solution<double[]> solution = search.solve(space, goal);

				// Double fitness = (Double) solution.data("goal");
				double[] individual = solution.object();

				try (Writer writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream("HillEnv" + envinst + "R" + repli + ".txt"), "utf-8"))) {
					// writer.write(fitness.toString());
					for (int i = 0; i < individual.length; i++) {
						writer.write(Double.toString(individual[i]) + ", ");
					}
				} catch (Exception e) {

				}

				System.out.println(solution.object());

				// tracer.close();
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
					System.out.println("Terminated vrep" + process + " with error code " + exitVal);
				} catch (Exception e) {
					System.out.println(e.toString());
					e.printStackTrace();
				}
			}
		}

	}

}
