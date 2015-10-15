package Tests;

import coppelia.CharWA;
import coppelia.FloatWA;
import coppelia.IntW;
import coppelia.IntWA;
import coppelia.remoteApi;
import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;

/* Test to measure simulation time
 * The TimeTest.ttt scene from the /Simulation/Test/ folder should be open and
 * the default continuous remoteAPI server configuration should 
 * be used in the V-REP simulator 
 */

public class TimeTest {

	public static void main(String[] args) throws InterruptedException{

		long startTime = System.currentTimeMillis();
		long stopTime = 0;
		long elapsedTime = 0;
		long simtime = 0;

		// Use simulator instance 0 or the default remoteAPI server
		// configuration
		int myRank = 0;

		// Morphology Parameters
		int Numberofmodules = 8;
		int[] orientation = new int[] { 1, 0, 1, 0, 1, 0, 1, 0 };

		// Simulation Parameters
		int MaxTime = 10;

		// Pack Integers into one String data signal
		IntWA NumberandOri = new IntWA(Numberofmodules + 2);
		int[] NO = new int[Numberofmodules + 2];
		NO[0] = Numberofmodules;
		NO[1] = MaxTime;
		for (int i = 2; i < Numberofmodules + 2; i++) {
			NO[i] = orientation[i - 2];
		}
		NumberandOri.setArray(NO);
		CharWA strNO = new CharWA(1);
		strNO.setArray(NumberandOri.getCharArrayFromArray());

		// Simulator interaction start
		remoteApi vrep = new remoteApi(); // Create simulator control object
		vrep.simxFinish(-1); // just in case, close all opened connections
		// Connect with the corresponding simulator remote server
		int clientID = vrep.simxStart("127.0.0.1", 19997, true, true, 5000, 5);

		if (clientID != -1) {
			// Set Simulator signal values
			vrep.simxSetStringSignal(clientID, "NumberandOri", strNO,
					vrep.simx_opmode_oneshot_wait);

			// *******************************************************************************************************************************

			// Run Scene in the simulator
			simtime = RunSimulation(vrep, clientID, MaxTime, myRank);

		} else {
			// No connection could be established
			System.out.println("Failed connecting to remote API server");
		}

		// Calculate simulation time
		stopTime = System.currentTimeMillis();
		elapsedTime = stopTime - startTime;

		System.out.println("Simulation Time = " + simtime);
		System.out.println("Total time for the java program = " + elapsedTime);

	}

	static long RunSimulation(remoteApi vrep, int clientID, int MaxTime,
			int threadnum) throws InterruptedException{

		long startTime = System.currentTimeMillis();
		long stopTime = 0;
		long elapsedTime = 0;

		// Create all output variables
		IntW out = new IntW(0);

		// Start Simulation
		int ret = vrep.simxStartSimulation(clientID,
				vrep.simx_opmode_oneshot_wait);
		// System.out.println("Start: " + ret + " sim: " + threadnum);

		// Setting up and waiting for finished flag
		vrep.simxGetIntegerSignal(clientID, "finished", out,
				vrep.simx_opmode_streaming);
		out.setValue(0);

		while (out.getValue() == 0) {
			
			// Pause Java program execution 10 ms while waiting
			Thread.sleep(10);
			
			
			if (vrep.simxGetIntegerSignal(clientID, "finished", out,
					vrep.simx_opmode_buffer) == vrep.simx_return_ok) {
				// We received a finished signal and everything is ok
			} else {
				// We have not received the finished message and out may contain
				// garbage so we set it up to 0 again
				out.setValue(0);

				stopTime = System.currentTimeMillis();
				elapsedTime = stopTime - startTime;

				if (elapsedTime > 300000) {
					System.out
							.println("Too much time has passed the simulator is not responding");
					elapsedTime = 300000;
					return elapsedTime;
				}
			}
		}

		// Stop listening to the finished signal
		vrep.simxGetIntegerSignal(clientID, "finished", out,
				vrep.simx_opmode_discontinue);

		// Stop simulation
		vrep.simxStopSimulation(clientID, vrep.simx_opmode_oneshot_wait);

		// Calculate simulation time
		stopTime = System.currentTimeMillis();
		elapsedTime = stopTime - startTime;

		// Return the simulation real time
		return elapsedTime;

	}

}
