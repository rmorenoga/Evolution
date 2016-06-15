package hill;

import coppelia.CharWA;
import coppelia.FloatWA;
import coppelia.IntWA;
import coppelia.remoteApi;
import simvrep.SimulationOld;
import unalcol.optimization.OptimizationFunction;
import unalcol.tracer.Tracer;

public class HillMAS extends OptimizationFunction<double[]>{
	
	public int simNumber = 0;
	public float alpha = 0.7f;
	
	HillMAS(int simNumber) {
		this.simNumber = simNumber;
	}
	
	public HillMAS(){
		
	}

	HillMAS(int simNumber, float alpha) {
		this.simNumber = simNumber;
		this.alpha = alpha;
	}
	
	public Double apply(double[] x) {
		// Start Measuring evaluation time
				long startTime = System.currentTimeMillis();

				float ampli = (float) x[0];
				float offset = (float) x[1];
				float phase = (float) (x[2]) * (float) Math.PI;

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
				int[] orientation = new int[] { 1, 0, 1, 0, 1, 0, 1, 0 };

				// Simulation Parameters
				int MaxTime = 120;

				// Pack Integers into one String data signal
				IntWA NumberandOri = new IntWA(Numberofmodules + 3);
				int[] NO = new int[Numberofmodules + 3];
				NO[0] = Numberofmodules;
				NO[1] = MaxTime;
				NO[2] = 0;
				for (int i = 3; i < Numberofmodules + 3; i++) {
					NO[i] = orientation[i - 3];
				}
				System.arraycopy(NO,0,NumberandOri.getArray(),0,NO.length);
				char[] p2 = NumberandOri.getCharArrayFromArray();
				CharWA strNO = new CharWA(p2.length);
				System.arraycopy(p2,0,strNO.getArray(),0,p2.length);
				
				// All combinations of sub-environments 
				char[][] subenvperm = new char[][] {
						{ 's', 'l', 's', 'b', 's', 'r', 's' },
						{ 's', 'l', 's', 's', 'r', 's', 'b' },
						{ 'b', 's', 'l', 's', 's', 'r', 's' },
						{ 'b', 's', 'r', 's', 's', 'l', 's' },
						{ 's', 'r', 's', 's', 'l', 's', 'b' },
						{ 's', 'r', 's', 'b', 's', 'l', 's' } };
				
				// Maze Parameters (Already a string)
				char[] mazeseq = new char[] { 's' }; // Default Maze Sequence
				CharWA strSeq = new CharWA(mazeseq.length);
				System.arraycopy(mazeseq,0,strSeq.getArray(),0,mazeseq.length);


				// Array that receives fitness from the simulator or signals a crash
				float[] rfitness = new float[3];

				// Fitness to be returned by each combination of subenvironments for a
				// given individual
				float[] fitness = new float[] { 1, 1, 1, 1, 1, 1 };

				// Number of retries in case of simulator crash
				int maxTries = 5;
				
				// Retry if there is a simulator crash
				for (int j = 0; j < maxTries; j++) {

					// Simulator interaction start
					remoteApi vrep = new remoteApi(); // Create simulator control object
					SimulationOld sim = new SimulationOld(this.simNumber);
					vrep.simxFinish(-1); // just in case, close all opened connections
					// Connect with the corresponding simulator remote server
					int clientID = vrep.simxStart("127.0.0.1", 19997 - simNumber, true,
							true, 5000, 5);

					if (clientID != -1) {
						// Set Simulator signal values
						vrep.simxSetStringSignal(clientID, "NumberandOri", strNO,
								vrep.simx_opmode_oneshot_wait);
						vrep.simxSetStringSignal(clientID, "ControlParam", strCP,
								vrep.simx_opmode_oneshot_wait);
						vrep.simxSetStringSignal(clientID, "Maze", strSeq,
								vrep.simx_opmode_oneshot_wait);
						
						try {
							// *******************************************************************************************************************************
							for (int i = 0; i < subenvperm.length; i++) {

								// New Maze Parameters (Already a string)
								mazeseq = subenvperm[i];
								strSeq.initArray(mazeseq.length);
								System.arraycopy(mazeseq,0,strSeq.getArray(),0,mazeseq.length);
								vrep.simxSetStringSignal(clientID, "Maze", strSeq,
										vrep.simx_opmode_oneshot_wait);

								// Run Scene in the simulator
								rfitness = sim.RunSimulation(vrep, clientID, MaxTime,
										this.alpha);

								if (rfitness[0] == -1) {
									sim.RestartSim(j);
									continue;
								}

								// Retrieve the fitness if there is no crash
								fitness[i] = rfitness[1];
							}

							// *******************************************************************************************************************************
						} catch (InterruptedException e) {
							System.err.println("InterruptedException: "
									+ e.getMessage());
						}
						// Close connection with the simulator
						vrep.simxFinish(clientID);
					} else {
						// No connection could be established
						System.out.println("Failed connecting to remote API server");
						System.out.println("Trying again for the " + j + " time");
						continue;
					}

					break;
				}
				float sum = 0;
				for (int i = 0; i < fitness.length; i++ ){
					sum = sum + fitness[i];
				}
				
				double fitnesst = sum;
				
				//String tr = new String(fitness[0]+","+fitness[1]+","+fitness[2]+","+fitness[3]+","+fitness[4]+","+fitness[5]);
				
		        //Tracer.trace(this,tr);

				return fitnesst;
	}

}
