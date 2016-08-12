package Tests;

import simvrep.SimulationOld;
import coppelia.CharWA;
import coppelia.FloatWA;
import coppelia.IntWA;
import coppelia.remoteApi;

public class IndvModTest {

	
	public static void main(String[] args) throws InterruptedException{
		
		long startTime = System.currentTimeMillis();
		long stopTime = 0;
		long elapsedTime = 0;
		long simtime = 0;

		// Use simulator instance 0 or the default remoteAPI server
		// configuration
		int simulator= 0;
		
		// Morphology Parameters
				int Numberofmodules = 8;
				int[] orientation = new int[] { 1, 0, 1, 0, 1, 0, 1, 0 };
				
				// Simulation Parameters
				int MaxTime = 20;
				
				float[] ampli = new float []{1.0f,0.711348f,0.70585454f,0.71121466f,0.7105515f,0.7125742f,0.72154087f,0.7007457f};
				float[] offset = new float[] {-0.31413087f,0.6831712f,0.68761957f,0.45336556f,0.2831308f,0.6872697f,0.453305f,0.44914752f} ;
				float[] phase = new float[] {-0.71726465f,0.43345675f,0.63771856f,0.4337447f,0.24287833f,0.429496f,0.43420318f,0.6380856f};
				for (int i=0; i< phase.length; i++) {
					  phase[i] = phase[i] * (float) Math.PI;
				}

				// Pack Floats into one String data signal
				float[] CP = new float[24];
				for (int i = 0; i < ampli.length; i++) {
					CP[(i*3)] = ampli[i];
					CP[(i*3)+1] = offset[i];
					CP[(i*3)+2] = phase[i];
				}
				FloatWA ControlParam = new FloatWA(CP.length);
				System.arraycopy(CP,0,ControlParam.getArray(),0,CP.length);
				char[] p = ControlParam.getCharArrayFromArray();
				CharWA strCP = new CharWA(p.length);
				System.arraycopy(p,0,strCP.getArray(),0,p.length);
				
				
				// Pack Integers into one String data signal
				IntWA NumberandOri = new IntWA(Numberofmodules + 2);
				int[] NO = new int[Numberofmodules + 2];
				NO[0] = Numberofmodules;
				NO[1] = MaxTime;
				for (int i = 2; i < Numberofmodules + 2; i++) {
					NO[i] = orientation[i - 2];
				}
				System.arraycopy(NO,0,NumberandOri.getArray(),0,NO.length);
				char[] p2 = NumberandOri.getCharArrayFromArray();
				CharWA strNO = new CharWA(p2.length);
				System.arraycopy(p2,0,strNO.getArray(),0,p2.length);
				
				// Array that receives fitness from the simulator or signals a crash
				float[] rfitness = new float[3];
				
				// Simulator interaction start
				remoteApi vrep = new remoteApi(); // Create simulator control object
				SimulationOld sim = new SimulationOld(simulator);
				vrep.simxFinish(-1); // just in case, close all opened connections
				// Connect with the corresponding simulator remote server
				int clientID = vrep.simxStart("127.0.0.1", 19997-simulator, true, true, 5000, 5);

				if (clientID != -1) {
					// Set Simulator signal values
					vrep.simxSetStringSignal(clientID, "NumberandOri", strNO,
							vrep.simx_opmode_oneshot_wait);
					vrep.simxSetStringSignal(clientID, "ControlParam", strCP,
							vrep.simx_opmode_oneshot_wait);

					// *******************************************************************************************************************************

					// Run Scene in the simulator
					rfitness = sim.RunSimulation(vrep, clientID, MaxTime, simulator);
					
					
					
					// *******************************************************************************************************************************
				
					// Close connection with the simulator
					vrep.simxFinish(clientID);
					
				} else {
					// No connection could be established
					System.out.println("Failed connecting to remote API server");
				}

				// Calculate simulation time
				stopTime = System.currentTimeMillis();
				elapsedTime = stopTime - startTime;

				System.out.println("Simulation Time = " + simtime);
				System.out.println("Total time for the java program = " + elapsedTime);
				System.out.println("Fitness = " + rfitness[1]);

			}
	}

