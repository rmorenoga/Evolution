package evolutionJEAF;



import coppelia.remoteApi;
import coppelia.IntW;
import coppelia.IntWA;
import coppelia.FloatWA;


public class CalculateFitnessTest {

	public static void main(String[] args) {
		
		int Numberofmodules = 8;
		int[] orientation = new int[] {1,1,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1};
		float CPGAmpli = 0.5f;
		float CPGOff = 0.1f;
		float CPGPhase = (float) (-Math.PI/6);
		float GoalX = 3f;
		float GoalY = 3f;
		IntW out = new IntW(3);
		//long startTime = System.currentTimeMillis();
		System.out.println("Program started");
		remoteApi vrep = new remoteApi();
		vrep.simxFinish(-1); // just in case, close all opened connections
		int clientID = vrep.simxStart("127.0.0.1",19994,true,true,5000,5);
		if (clientID!=-1)
		{
			System.out.println("Connected to remote API server");
		int ret = vrep.simxSetIntegerSignal(clientID, "desiredRobotCount", Numberofmodules-1, vrep.simx_opmode_oneshot_wait);	
		for (int i=0;i<Numberofmodules;i++)
		{
			ret = vrep.simxSetIntegerSignal(clientID, "ori"+i, orientation[i], vrep.simx_opmode_oneshot_wait);
			//System.out.println("ori"+i);
		}
		
		ret = vrep.simxSetFloatSignal(clientID, "CPGAmpli", CPGAmpli , vrep.simx_opmode_oneshot_wait);
		ret = vrep.simxSetFloatSignal(clientID, "CPGOff", CPGOff , vrep.simx_opmode_oneshot_wait);
		ret = vrep.simxSetFloatSignal(clientID, "CPGPhase", CPGPhase , vrep.simx_opmode_oneshot_wait);
		
		ret = vrep.simxSetFloatSignal(clientID, "GoalX", GoalX , vrep.simx_opmode_oneshot_wait);
		ret = vrep.simxSetFloatSignal(clientID, "GoalY", GoalY , vrep.simx_opmode_oneshot_wait);
		
		ret = vrep.simxLoadScene(clientID, "/home/rodrigo/V-REP/Modular/ModularCPG.ttt", 0, vrep.simx_opmode_oneshot_wait);
		ret = vrep.simxStartSimulation(clientID, vrep.simx_opmode_oneshot_wait);
		//System.out.println("Retrieved Signal: "+Integer.toString(out.getValue()));
		ret = vrep.simxGetIntegerSignal(clientID, "finished", out,vrep.simx_opmode_streaming);
		//ret = vrep.simxGetIntegerSignal(clientID, "finished", out, vrep.simx_opmode_remove);
		//ret = vrep.simxGetIntegerSignal(clientID, "finished", out, vrep.simx_opmode_buffer);		
		//System.out.println("Retrieved Signal: "+Integer.toString(out.getValue()));
		out.setValue(0);
		long startTime = System.currentTimeMillis();
		while(out.getValue()==0)
		{
			if (vrep.simxGetIntegerSignal(clientID, "finished", out, vrep.simx_opmode_buffer)==vrep.simx_return_ok)
			{
				//System.out.println("Retrieved Signal: "+Integer.toString(out.getValue()));
			}else
			{
				out.setValue(0);
				//System.out.println("Retrieved Signal: "+Integer.toString(out.getValue()));
			}
		}
		
		ret=vrep.simxStopSimulation(clientID, vrep.simx_opmode_oneshot_wait);
		long stopTime = System.currentTimeMillis();
	      long elapsedTime = stopTime - startTime;
	      System.out.println(elapsedTime);
		
		
		
		if (ret==vrep.simx_return_ok)
			System.out.println("Signal set");
		else
			System.out.format("Remote API function call returned with error code: %d\n",ret);
		vrep.simxFinish(clientID);				
		}
		else
			System.out.println("Failed connecting to remote API server");
		System.out.println("Program ended");

	}

}


