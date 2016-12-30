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
						int MaxTime = 30;
						
						/*float[] ampset = new float []{0.5f,0.4f,0.3f,0.5f,0.6f,0.3f,0.5f};
						float[] offsetset = new float[] {0.1f,-0.1f,0.2f,0.3f,-0.4f,0.3f,0.4f} ;
						float[] phaseset = new float[] {0.3f,0.2f,0.35f,0.4f,0.3f,0.2f,0.3f};
						float[] ampstep = new float []{0.01f,0.01f,0.01f,0.01f,0.01f,0.01f,0.01f};
						float[] offstep = new float []{0.01f,0.01f,0.01f,0.01f,0.01f,0.01f,0.01f};
						float[] phasestep = new float []{0.01f,0.01f,0.01f,0.01f,0.01f,0.01f,0.01f};
						
						float[] CP = new float[42];
						for (int i = 0; i < 7; i++) {
							CP[i] = ampset[i];
							CP[i+7] = offsetset[i];
							CP[i+14] = phaseset[i];
							CP[i+21] = ampstep[i];
							CP[i+28] = offstep[i];
							CP[i+35] = phasestep[i];
						}*/
						
		double[] values = new double[] {-0.5298734718668228, 1.0, -1.0, -1.0, 1.0, -0.39413958000362403, 0.3650443550379747, -1.0, 1.0, 0.7125335980963801, 1.0, -0.008876361337063043, -0.5770563431447173, 0.5724250667395164, -1.0, -1.0, -0.987802348345221, -0.8220043327253795, -1.0, -0.8, -0.09516716462731512, 0.4790909282571704, -1.0, -0.06664932800000001, 1.0, -1.0, -1.0, -0.22379606147396336, 1.0, -0.5355456811277005, -0.42433729302519657, 0.6436565809384294, -0.02640908399800146, 0.9999137617018253, -1.0, 1.0, 1.0, -0.3469889860060943, -0.6175689996196543, 1.0, -1.0, -0.7780120651200579};
						// Control Parameters
						float[] CP = new float[42];
						for (int i = 0; i < values.length; i++) {
							CP[i] = (float) values[i];
						}
						
						// Array that receives fitness from the simulator or signals a crash
						float[] rfitness = new float[3];
						
						char[][] subenvperm = new char[][] {
							{ 's', 'l', 's', 'b', 's', 'r', 's' },
							{ 's', 'l', 's', 's', 'r', 's', 'b' },
							{ 'b', 's', 'l', 's', 's', 'r', 's' },
							{ 'b', 's', 'r', 's', 's', 'l', 's' },
							{ 's', 'r', 's', 's', 'l', 's', 'b' },
							{ 's', 'r', 's', 'b', 's', 'l', 's' },
							{'s','s'}};
						
						Robot robot = new Robot(Numberofmodules,orientation,CP);
						
						Simulation sim = new Simulation(simulator,MaxTime);
						
						if(sim.Connect()){
							sim.prepareSignals(robot);
							sim.SendSignals();
							sim.SendMaze(subenvperm[6],0.8f);
							
							rfitness = sim.RunSimulation(0.7f);
							
							sim.Disconnect();
							
							
							
							
						}else{
							// No connection could be established
							System.out.println("Failed connecting to remote API server");
						}
						
						System.out.println("Fitness = " + rfitness[1]);
						
						
						
	}

}
