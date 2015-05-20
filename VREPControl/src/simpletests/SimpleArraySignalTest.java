package simpletests;
import coppelia.StringW;
import coppelia.IntWA;
import coppelia.remoteApi;
import coppelia.CharWA;
import coppelia.FloatWA;


public class SimpleArraySignalTest {

	public static void main(String[] args) {
		
		
		StringW str1=new StringW("");
		//IntWA out = new IntWA(4);
		FloatWA out = new FloatWA(4);
		CharWA str=new CharWA(1);
		
		System.out.println("Program started");
		remoteApi vrep = new remoteApi();
		vrep.simxFinish(-1); // just in case, close all opened connections
		int clientID = vrep.simxStart("127.0.0.1",19997,true,true,5000,5);
		if (clientID!=-1)
		{
			System.out.println("Connected to remote API server");
			int ret = vrep.simxGetStringSignal(clientID, "toClient",str , vrep.simx_opmode_oneshot_wait);
			out.initArrayFromCharArray(str.getArray());
			System.out.println("Int: "+ out.getArray()[0]+" "+out.getArray()[1]+" "+out.getArray()[2]+" "+out.getArray()[3]);
			System.out.println("String: "+ str.getArray());
			out.initArray(3);
			out.setArray(new float[]{1.2f, 5.6f, 3.4f});
			str.initArray(1);
			str.setArray(out.getCharArrayFromArray());
			ret = vrep.simxSetStringSignal(clientID, "in",str, vrep.simx_opmode_oneshot_wait);
		}	
		else
			System.out.println("Failed connecting to remote API server");
		System.out.println("Program ended");
	}

}
