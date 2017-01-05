package evolJEAF;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.facade.EAFFacade;
import es.udc.gii.common.eaf.stoptest.StopTest;
import es.udc.gii.common.eaf.util.EAFRandom;
import mpi.MPI;
import simvrep.SimulationConfiguration;

public class MainJEAF {
	/**
	 * Start number for simulation server The first simulator when working in
	 * parallel will be the one indicated by this number
	 */
	public static int startNumber;

	public static void main(String[] args) {
		// Parameters to feed to the evolutionary algorithm
		long seed = -Long.MAX_VALUE;
		startNumber = 0;
		String xmlfile = "";

		// Acquire parameters through command line arguments
		if (args.length > 0) {
			try {
				 //for(int j=0;j<args.length;j++){
				 //System.out.println("Argument "+j+" = "+args[j]);
				 //}
				if (args.length >= 4) {//4
					startNumber = Integer.parseInt(args[3]);//3
					if (args.length >= 5) {//5
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
				System.err.println("StartNumber " + args[0] + " must be an integer.");
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
		// Get max simulation time from SimulationConfiguration
		int simultime = (int) Math.ceil(SimulationConfiguration.getMaxSimulationTime());
		// Check whether parallel evaluation will be used (Remember to change
		// all xml files accordingly)
		int myRank = 0;
		if (SimulationConfiguration.isUseMPI()) {
			/* Initialize Parallel Environment */
			MPI.Init(args);
			MPI.initThread(MPI.THREAD_MULTIPLE, 0, args);
			myRank = MPI.COMM_WORLD.Rank();
			myRank = myRank + startNumber;

		}

		// Initialize Random Number Generator
		EAFRandom.init(seed != -Long.MAX_VALUE ? seed : System.currentTimeMillis());

		// Initialize evolutionary algorithm
		EAFFacade facade = new EAFFacade();
		EvolutionaryAlgorithm algorithm;
		StopTest stopTest;
		EAFRandom.init();
		algorithm = facade.createAlgorithm("" + xmlfile);
		stopTest = facade.createStopTest("./" + xmlfile);

		// Start the evolutionary process
		facade.resolve(stopTest, algorithm);

		// If I am the process 0 signal the end of the process
		if (SimulationConfiguration.isUseMPI()) {
			if (myRank == 0) {
				System.out.println("Finished");
			}
		} else {
			System.out.println("Finished");
		}

		// Terminate the corresponding simulator
		try {
			ProcessBuilder qq = new ProcessBuilder("killall", "vrep" + myRank);
			File log = new File("Simout/log");
			qq.redirectErrorStream(true);
			qq.redirectOutput(Redirect.appendTo(log));
			Process p = qq.start();
			int exitVal = p.waitFor();
			System.out.println("Terminated vrep" + myRank + " with error code " + exitVal);
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}

		if (SimulationConfiguration.isUseMPI()) {
			// Terminate Parallel Environment
			MPI.Finalize();
		}
	}

}
