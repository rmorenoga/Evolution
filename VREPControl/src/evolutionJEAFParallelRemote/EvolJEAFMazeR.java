package evolutionJEAFParallelRemote;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;

import mpi.MPI;
import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.facade.EAFFacade;
import es.udc.gii.common.eaf.stoptest.StopTest;
import es.udc.gii.common.eaf.util.EAFRandom;

public class EvolJEAFMazeR {

	public static int startNumber;

	// static EvolutionaryAlgorithm algorithm;

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
		// System.out.println("StartNumber = "+startNumber);
		// System.out.println("XMLFile = "+xmlfile);
		// System.exit(0);

		/* Initialize Parallel Environment */
		MPI.Init(args);
		MPI.initThread(MPI.THREAD_MULTIPLE, 0, args);
		int myRank = MPI.COMM_WORLD.Rank();
		myRank = myRank + startNumber;

		String vrepcommand = new String("./vrep" + myRank + ".sh");

		try {
			// ProcessBuilder qq=new ProcessBuilder(vrepcommand,"-h");
			// ProcessBuilder qq = new
			// ProcessBuilder("xvfb-run","--auto-servernum","--server-num=1",vrepcommand,
			// "-h");
			// ProcessBuilder qq=new ProcessBuilder(vrepcommand);
			ProcessBuilder qq = new ProcessBuilder(vrepcommand, "-h",
					"/home/rodr/EvolWork/Modular/Maze/MazeBuilderR01.ttt");
			// ProcessBuilder qq = new
			// ProcessBuilder(vrepcommand,"/home/rodr/EvolWork/Modular/Maze/MazeBuilderR01.ttt");
			// Map<String, String> env = qq.environment();
			qq.directory(new File("/home/rodr/V-REP/Vrep" + myRank + "/"));
			// qq.inheritIO();
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
		// algorithm = facade.createAlgorithm("" + "EvolconfigMazeRS1234.xml");
		// stopTest = facade.createStopTest("./" + "EvolconfigMazeRS1234.xml");
		algorithm = facade.createAlgorithm("" + xmlfile);
		stopTest = facade.createStopTest("./" + xmlfile);
		facade.resolve(stopTest, algorithm);
		
		if (myRank == 0) {
			long stopTime = System.currentTimeMillis();
			long elapsedTime = stopTime - startTime;
			System.out.println(elapsedTime);
			System.out.println("Finished");
		}
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
