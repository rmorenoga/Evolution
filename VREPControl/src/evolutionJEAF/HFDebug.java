package evolutionJEAF;

import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import represent.Robot;
import simvrep.Simulation;

/**
 * JEAF Objective function for use with MDebug.ttt
 * Configures a robot and launches a simulation with
 * a particular 3 part composed environment.
 * 
 * @author rodr
 *
 */

public class HFDebug extends ObjectiveFunction{
	public void reset(){
		
	}

	public double evaluate(double[] values) {
		
		// Use simulator instance 0 or the default remoteAPI server
		// configuration
		int simulator= 0;
		
		// Morphology Parameters
				int Numberofmodules = 8;
				int[] orientation = new int[] { 1, 0, 1, 0, 1, 0, 1, 0};
				
				// Simulation Parameters
				int MaxTime = 30;
				
		// Control Parameters
		float[] CP = new float[42];
		for (int i = 0; i <values.length; i++){
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
			{ 's', 'r', 's', 'b', 's', 'l', 's' } };
			
		Robot robot = new Robot(Numberofmodules,orientation,CP);
		
		Simulation sim = new Simulation(simulator,MaxTime);
		
		if(sim.Connect()){
			sim.prepareSignals(robot);
			sim.SendSignals();
			sim.SendMaze(subenvperm[2],0.8f, true , true);

			try {
				rfitness = sim.RunSimulation();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			sim.Disconnect();	
			
			
		}else{
			// No connection could be established
			System.out.println("Failed connecting to remote API server");
		}
		
		return rfitness[2];		
		
		
	}
}
