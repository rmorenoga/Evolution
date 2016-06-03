package evolutionJEAFParallel;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;

import mpi.MPI;
import coppelia.CharWA;
import coppelia.FloatWA;
import coppelia.IntW;
import coppelia.IntWA;
import coppelia.remoteApi;
import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;

public class CalcFitnessMaze extends ObjectiveFunction {

	public double evaluate(double[] values) {

		// Retrieve process number from mpj
		int myRank = MPI.COMM_WORLD.Rank();
		myRank = myRank + EvolJEAFMaze.startNumber;
		// Start Measuring evaluation time
		long startTime = System.currentTimeMillis();

		// Individual CPG Parameters
		float ampli = (float) values[0];
		float offset = (float) values[1];
		float phase = (float) (values[2]) * (float) Math.PI;

		// Pack Floats into one String data signal
		FloatWA ControlParam = new FloatWA(3);
		float[] CP = new float[3];
		CP[0] = ampli;
		CP[1] = offset;
		CP[2] = phase;
		System.arraycopy(CP,0,ControlParam.getArray(),0,CP.length);
		char[] p = ControlParam.getCharArrayFromArray();
		CharWA strCP = new CharWA(p.length);
		System.arraycopy(p,0,strCP.getArray(),0,p.length);

		// Morphology Parameters
		int Numberofmodules = 8;
		int[] orientation = new int[] { 0, 1, 0, 1, 0, 1, 0, 1 };

		// Simulation Parameters
		int MaxTime = 40;

		// Pack Integers into one String data signal
		IntWA NumberandOri = new IntWA(Numberofmodules + 3);
		int[] NO = new int[Numberofmodules + 3];
		NO[0] = Numberofmodules;
		NO[1] = MaxTime;
		NO[2] = myRank;
		for (int i = 3; i < Numberofmodules + 3; i++) {
			NO[i] = orientation[i - 3];
		}
		System.arraycopy(NO,0,NumberandOri.getArray(),0,NO.length);
		char[] p2 = NumberandOri.getCharArrayFromArray();
		CharWA strNO = new CharWA(p2.length);
		System.arraycopy(p2,0,strNO.getArray(),0,p2.length);

		// Maze Parameters (Already a string)
		char[] mazeseq = new char[] { 's' }; // Default Maze Sequence
		// char[] mazeseq = new char[]{'s','l','s','s','l','s','l','s','l','l'};
		CharWA strSeq = new CharWA(mazeseq.length);
		System.arraycopy(mazeseq,0,strSeq.getArray(),0,mazeseq.length);

		// Array that receives fitness from the simulator or signals a crash
		float[] rfitness = new float[2];

		// Fitness to be returned to Evolutionary Algorithm
		float[] fitness = new float[] { 1000, 1000, 1000, 1000 };

		// Number of retries in case of simulator crash
		int maxTries = 5;

		// Retry if there is a simulator crash
		for (int j = 0; j < maxTries; j++) {

			// Simulator interaction start, see v-rep manual
			remoteApi vrep = new remoteApi();// Create simulator control object
			vrep.simxFinish(-1); // just in case, close all opened connections
									// be careful in multi-threaded environments

			// Connect with the corresponding simulator remote server
			int clientID = vrep.simxStart("127.0.0.1", 19997 - myRank, true,
					true, 5000, 5);

			if (clientID != -1) {

				// Set Simulator signal values
				vrep.simxSetStringSignal(clientID, "NumberandOri", strNO,
						vrep.simx_opmode_oneshot_wait);
				vrep.simxSetStringSignal(clientID, "ControlParam", strCP,
						vrep.simx_opmode_oneshot_wait);
				// *******************************************************************************************************************************

				// New Maze Parameters (Already a string)
				mazeseq = new char[] { 's', 'l' }; // Default Maze Sequence
				strSeq.initArray(mazeseq.length);
				System.arraycopy(mazeseq,0,strSeq.getArray(),0,mazeseq.length);
				vrep.simxSetStringSignal(clientID, "Maze", strSeq,
						vrep.simx_opmode_oneshot_wait);
				
				// Run Scene in the simulator
				rfitness = RunScene(vrep, clientID,
						"/home/rodrigo/V-REP/Modular/Maze/MazeBuilder01.ttt",
						MaxTime);
				if (rfitness[0] == -1) {
					RestartSim(myRank, j);
					continue;
				}
				fitness[0] = rfitness[1];
				// System.out.println("Fitness 1 = "+fitness[0]);

				// if (ret==vrep.simx_return_ok)
				// System.out.println("Last command OK");
				// else
				// System.out.format("Remote API function call returned with error code: %d\n",ret);
				vrep.simxFinish(clientID);
			} else {
				System.out.println("Failed connecting to remote API server");
				System.out.println("Trying again for the " + j + " time");
				continue;
			}

			// System.out.println("Program ended");

			// System.out.println("Minimum fitness = "+Float.toString(minimum(fitness)));

			// double fitnessd =
			// (fitness[0]+fitness[1]+fitness[2]+fitness[3])/4;

			break;
		}

		double fitnessd = fitness[0];

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println(elapsedTime);

		// System.out.println("Total Fitness = "+fitnessd+" myrank "+myRank);

		return fitnessd;

	}

	public void reset() {

	}

	void RestartSim(int myRank, int j) {
		// String vrepcommand = new
		// String("/home/rodrigo/V-REP/Vrep"+myRank+"/vrep"+myRank+".sh -h");
		String vrepcommand = new String("./vrep" + myRank + ".sh");
		// String vrepkill = new String("killall -r vrep"+myRank);
		System.out.println("Restarting simulator and trying again for the " + j
				+ " time in " + myRank);
		/*
		 * try { Runtime rt = Runtime.getRuntime(); rt.exec(vrepkill);
		 * Thread.sleep(2000); rt.exec(vrepcommand); Thread.sleep(3000);
		 * System.out.println("Trying again now"); } catch(Exception e) {
		 * System.out.println(e.toString()); e.printStackTrace(); }
		 */
		try {
			ProcessBuilder qq = new ProcessBuilder("killall", "-r", "vrep"
					+ myRank);
			File log = new File("Simout/log");
			qq.redirectErrorStream(true);
			qq.redirectOutput(Redirect.appendTo(log));
			Process p = qq.start();
			int exitVal = p.waitFor();
			System.out.println("Terminated vrep" + myRank + " with error code "
					+ exitVal);

			qq = new ProcessBuilder(vrepcommand, "-h");
			// Map<String, String> env = qq.environment();
			qq.directory(new File("/home/rodrigo/V-REP/Vrep" + myRank + "/"));
			// qq.inheritIO();
			// File log = new File("Simout/log");
			qq.redirectErrorStream(true);
			qq.redirectOutput(Redirect.appendTo(log));
			qq.start();
			Thread.sleep(5000);
			System.out.println("Trying again now");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}

	}

	float[] RunScene(remoteApi vrep, int clientID, String scene, int MaxTime) {

		IntW out = new IntW(0);
		CharWA datastring = new CharWA(1);
		FloatWA out2 = new FloatWA(3);
		float[] fitnessout = new float[2];

		// Load scene maze and start the simulation
		int ret = vrep.simxLoadScene(clientID, scene, 0,
				vrep.simx_opmode_oneshot_wait);
		ret = vrep.simxStartSimulation(clientID, vrep.simx_opmode_oneshot_wait);

		// Setting up and waiting for finished flag
		ret = vrep.simxGetIntegerSignal(clientID, "finished", out,
				vrep.simx_opmode_streaming);
		out.setValue(0);
		// long startTime = System.currentTimeMillis();
		while (out.getValue() == 0) {
			if (vrep.simxGetIntegerSignal(clientID, "finished", out,
					vrep.simx_opmode_buffer) == vrep.simx_return_ok) {
				// System.out.println("Retrieved Signal: "+Integer.toString(out.getValue()));
			} else {
				out.setValue(0);
				// System.out.println("Retrieved Signal: "+Integer.toString(out.getValue()));
			}
		}

		vrep.simxGetIntegerSignal(clientID, "finished", out,
				vrep.simx_opmode_discontinue);

		// Stopping simulation
		ret = vrep.simxStopSimulation(clientID, vrep.simx_opmode_oneshot_wait);
		// System.out.println(ret);

		// Reading simulation results
		ret = vrep.simxGetStringSignal(clientID, "Position", datastring,
				vrep.simx_opmode_oneshot_wait);
		if (ret != vrep.simx_return_ok) {
			System.out.println("Position Signal not received");
		}
		out2.initArrayFromCharArray(datastring.getArray());
		// System.out.println("Return = "+Float.toString(out2.getArray()[0])+" "+Float.toString(out2.getArray()[1]));

		if (out2.getArray().length == 0) {
			System.out.println("out2 is empty");
			fitnessout[0] = -1;
			fitnessout[1] = 1000;
			return fitnessout;
		}

		fitnessout[0] = 0;
		// Scaling fitness
		if (out2.getArray()[0] == 0) {
			// The robot could get out of the maze do the fitness is the time it
			// spent
			fitnessout[1] = out2.getArray()[1] * 0.01f;
		} else if (out2.getArray()[1] == 0) {
			// The robot could not get out of the maze so the fitness is the
			// distance to goal + the maximum time allowed
			fitnessout[1] = out2.getArray()[0] + (float) MaxTime * 0.01f;
		}
		// System.out.println("Fitness = "+Float.toString(fitness[0]));

		// Closing first maze
		while (vrep.simxCloseScene(clientID, vrep.simx_opmode_oneshot_wait) != vrep.simx_return_ok) {
		}

		return fitnessout;
	}

	public static float maximum(float[] inputset) {

		float maxValue = 0;
		for (int i = 0; i < inputset.length; i++) {
			if (inputset[i] >= maxValue) {
				maxValue = inputset[i];
			}
		}

		return maxValue;
	}

}
