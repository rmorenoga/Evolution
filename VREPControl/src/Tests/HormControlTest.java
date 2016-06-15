package Tests;

import represent.Robot;
import simvrep.Simulation;

public class HormControlTest {
	
	public static void main(String[] args) throws InterruptedException{
		// Use simulator instance 0 or the default remoteAPI server
				// configuration
				int simulator= 0;
				
				// Morphology Parameters
						int Numberofmodules = 8;
						int[] orientation = new int[] { 1, 0, 1, 0, 1, 0, 1, 0 };
						
						// Simulation Parameters
						int MaxTime = 10;
						
						float[] ampset = new float []{0.5f,0.4f,0.3f,0.5f,0.6f,0.3f,0.5f};
						float[] offsetset = new float[] {0.1f,-0.1f,0.2f,0.3f,-0.4f,0.3f,0.4f} ;
						float[] phaseset = new float[] {0.3f,0.2f,0.35f,0.4f,0.3f,0.2f,0.3f};
						float[] ampstep = new float []{0.01f,0.01f,0.01f,0.01f,0.01f,0.01f,0.01f};
						float[] offstep = new float []{0.01f,0.01f,0.01f,0.01f,0.01f,0.01f,0.01f};
						float[] phasestep = new float []{0.01f,0.01f,0.01f,0.01f,0.01f,0.01f,0.01f};
						
						float[] CP = new float[]{42};
						for (int i = 0; i < 7; i++) {
							CP[i] = ampset[i];
							CP[i+7] = offsetset[i];
							CP[i+14] = phaseset[i];
							CP[i+21] = ampstep[i];
							CP[i+28] = offstep[i];
							CP[i+35] = phasestep[i];
						}
						
						// Array that receives fitness from the simulator or signals a crash
						float[] rfitness = new float[3];
						
						Robot robot = new Robot(Numberofmodules,orientation,CP);
						
						Simulation sim = new Simulation(simulator,MaxTime,robot);
						
						if(sim.Connect()){
							
							sim.SendSignals();
							
							rfitness = sim.RunSimulation(0.7f);
							
							sim.Disconnect();
							
							
							
							
						}else{
							// No connection could be established
							System.out.println("Failed connecting to remote API server");
						}
						
						System.out.println("Fitness = " + rfitness[1]);
						
						
						
	}

}
