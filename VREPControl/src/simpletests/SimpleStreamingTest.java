package simpletests;

import coppelia.StringW;
import coppelia.IntWA;
import coppelia.remoteApi;


public class SimpleStreamingTest {

	public static void main(String[] args) {
		
		System.out.println("Program started");
		remoteApi vrep = new remoteApi();
		vrep.simxFinish(-1); // just in case, close all opened connections
		int clientID = vrep.simxStart("127.0.0.1",19997,true,true,5000,5);
		
		
		if (clientID!=-1)
		{
			// Initialization phase:
			StringW str=new StringW("");
			IntWA out = new IntWA(1);
			String strout = new String("");
			System.out.println("Connected to remote API server");
			//int ret = vrep.simxReadStringStream(clientID,"toClient",str,vrep.simx_opmode_streaming);
			int ret = 0;
            System.out.println("Return: "+ret+" "+vrep.simx_return_ok);
			// while we are connected:
			while (vrep.simxGetConnectionId(clientID)!=-1)
			{ 
				//if (vrep.simxReadStringStream(clientID,"toClient",str,vrep.simx_opmode_buffer)==vrep.simx_return_ok)
				//{ 
					//vrep.simxReadStringStream(clientID,"toClient",str,vrep.simx_opmode_buffer);
					// Data produced by the child script was retrieved! Send it back to the child script:
					//vrep.simxWriteStringStream(clientID,"fromClient",str.getValue(),vrep.simx_opmode_oneshot);
				
				    //vrep.simxReadStringStream(clientID,"toClient",str,vrep.simx_opmode_buffer);
					//strout = str.getValue();
					//out.initArrayFromString(strout);
					//System.out.println("String: "+strout);
					System.out.println("Int: "+out.getArray()[0]);
				//}
			}
		}
		else
			System.out.println("Failed connecting to remote API server");
		System.out.println("Program ended");

	}

}
