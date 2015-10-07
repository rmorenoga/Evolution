package examples;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;

import mpi.MPI;
import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.facade.EAFFacade;
import es.udc.gii.common.eaf.stoptest.StopTest;
import es.udc.gii.common.eaf.util.EAFRandom;

public class EvoJEAFMaze {

	public static void main(String[] args) {

		long seed = -Long.MAX_VALUE;

		String vrepcommand = new String("./vrep" + 0 + ".sh");

		/* Initialize a v-rep simulator */
		try {

			ProcessBuilder qq = new ProcessBuilder(vrepcommand, "-h",
					"/home/rodrigo/V-REP/Modular/Maze/MazeBuilder01.ttt");

			qq.directory(new File("/home/rodrigo/V-REP/Vrep" + 0 + "/"));

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
		// Provide the configuration file location
		algorithm = facade.createAlgorithm("./"
				+ "bin/examples/EvoconfigMaze.xml");
		stopTest = facade.createStopTest("./"
				+ "bin/examples/EvoconfigMaze.xml");

		// Start the evolutionary process
		facade.resolve(stopTest, algorithm);

		// Kill all the v-rep processes
		try {

			ProcessBuilder qq = new ProcessBuilder("killall", "vrep" + 0);
			File log = new File("Simout/log");
			qq.redirectErrorStream(true);
			qq.redirectOutput(Redirect.appendTo(log));
			Process p = qq.start();
			int exitVal = p.waitFor();
			System.out.println("Terminated vrep" + 0 + " with error code "
					+ exitVal);
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}

	}

}
