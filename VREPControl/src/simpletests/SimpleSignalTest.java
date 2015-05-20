package simpletests;
import coppelia.IntW;
import coppelia.remoteApi;

public class SimpleSignalTest {

	public static void main(String[] args) {
		
		System.out.println("Program started");
		remoteApi vrep = new remoteApi();
		vrep.simxFinish(-1); // just in case, close all opened connections
		int clientID = vrep.simxStart("127.0.0.1",19997,true,true,5000,5);
		if (clientID!=-1)
		{
			System.out.println("Connected to remote API server");
		int ret = vrep.simxSetIntegerSignal(clientID, "desiredRobotCount", 8, vrep.simx_opmode_oneshot_wait);	
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
