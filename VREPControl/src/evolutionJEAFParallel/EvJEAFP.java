package evolutionJEAFParallel;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.facade.EAFFacade;
import es.udc.gii.common.eaf.stoptest.StopTest;
import es.udc.gii.common.eaf.util.EAFRandom;
import mpi.MPI;

/**
 * JEAF Main evolutionary class for parallel evaluation
 * It asks for two parameters in the command line
 * the start number for the simulators and the path
 * to the xml configuration file.
 * It starts the simulators necessary and
 * calls the .xml configuration file to start the evolutionary process 
 * @author rodr
 *
 */
public class EvJEAFP {
	
	public static int startNumber;

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		long seed = -Long.MAX_VALUE;
		startNumber = 0;
		String xmlfile = "";
		
		if (args.length > 0) {
			try {
				// for(int j=0;j<args.length;j++){
				// System.out.println("Argument "+j+" = "+args[j]);
				// }
				if (args.length >= 4) {
					startNumber = Integer.parseInt(args[3]);
					if (args.length >= 5) {
						xmlfile = args[4];
					} else {
						System.err.println("Provide a xml file");
						System.exit(1);
					}

				} else {
					System.err.println("Provide a start number");
					System.exit(1);
				}
			} catch (NumberFormatException e) {
				System.err.println("StartNumber " + args[0]
						+ " must be an integer.");
				System.exit(1);
			}
		} else {
			System.err.println("Missing arguments");
			System.exit(1);
		}
		if (xmlfile == "") {
			System.err.println("Provide a xml file");
			System.exit(1);
		}
		
		/* Initialize Parallel Environment */
		MPI.Init(args);
		MPI.initThread(MPI.THREAD_MULTIPLE, 0, args);
		int myRank = MPI.COMM_WORLD.Rank();
		myRank = myRank + startNumber;
		
		String vrepcommand = new String("./vrep" + myRank + ".sh"); 
		
		/*Initialize a v-rep simulator based on the starNumber parameter */
		try {
			ProcessBuilder qq = new ProcessBuilder(vrepcommand,
					"scenes/Maze/MDebug.ttt");
			qq.directory(new File("/home/julian/V-REP/Vrep" + myRank + "/"));
			File log = new File("Simout/log");
			qq.redirectErrorStream(true);
			qq.redirectOutput(Redirect.appendTo(log));
			qq.start();
			Thread.sleep(10000);
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		// Initialize Random Number Generator
				EAFRandom.init(seed != -Long.MAX_VALUE ? seed : System
						.currentTimeMillis());

				EAFFacade facade = new EAFFacade();
				EvolutionaryAlgorithm algorithm;
				StopTest stopTest;
				EAFRandom.init();
				
				/* Specify the configuration file for the evolution to run */
				algorithm = facade.createAlgorithm("" + xmlfile);
				stopTest = facade.createStopTest("./" + xmlfile);
				
				// Start the evolutionary process
				facade.resolve(stopTest, algorithm);
				
				// If I am the process 0 measure time
				if (myRank == startNumber) {
					long stopTime = System.currentTimeMillis();
					long elapsedTime = stopTime - startTime;
					System.out.println(elapsedTime);
					System.out.println("Finished");
				}
				
				// kill all the v-rep processes
				try {
					// ProcessBuilder qq=new ProcessBuilder("killall","-r","vrep");
					// ProcessBuilder qq=new
					// ProcessBuilder("killall","-r","vrep"+myRank+".sh");
					ProcessBuilder qq = new ProcessBuilder("killall", "vrep" + myRank);
					File log = new File("Simout/log");
					qq.redirectErrorStream(true);
					qq.redirectOutput(Redirect.appendTo(log));
					Process p = qq.start();
					int exitVal = p.waitFor();
					System.out.println("Terminated vrep" + myRank + " with error code "
							+ exitVal);
				} catch (Exception e) {
					System.out.println(e.toString());
					e.printStackTrace();
				}
				
				// Terminate Parallel Environment
				MPI.Finalize();

			}
}
