package simpletests;
// Copyright 2006-2014 Coppelia Robotics GmbH. All rights reserved. 
// marc@coppeliarobotics.com
// www.coppeliarobotics.com
// 
// -------------------------------------------------------------------
// THIS FILE IS DISTRIBUTED "AS IS", WITHOUT ANY EXPRESS OR IMPLIED
// WARRANTY. THE USER WILL USE IT AT HIS/HER OWN RISK. THE ORIGINAL
// AUTHORS AND COPPELIA ROBOTICS GMBH WILL NOT BE LIABLE FOR DATA LOSS,
// DAMAGES, LOSS OF PROFITS OR ANY OTHER KIND OF LOSS WHILE USING OR
// MISUSING THIS SOFTWARE.
// 
// You are free to use/modify/distribute this file for whatever purpose!
// -------------------------------------------------------------------
//
// This file was automatically created for V-REP release V3.1.2 on June 16th 2014

import coppelia.IntW;
import coppelia.IntWA;
import coppelia.FloatWA;
import coppelia.remoteApi;

// Make sure to have the server side running in V-REP: 
// in a child script of a V-REP scene, add following command
// to be executed just once, at simulation start:
//
// simExtRemoteApiStart(19999)
//
// then start simulation, and run this program.
//
// IMPORTANT: for each successful call to simxStart, there
// should be a corresponding call to simxFinish at the end!

public class simpleTest
{
	
	public static void main(String[] args)
	{
		IntW handle = new IntW(-1);
		int robothandle = -1;
		FloatWA robotposition = new FloatWA(3);
		float[] newposition = new float[3];
		
		
		System.out.println("Program started");
		remoteApi vrep = new remoteApi();
		vrep.simxFinish(-1); // just in case, close all opened connections
		int clientID = vrep.simxStart("127.0.0.1",19999,true,true,5000,5);
		if (clientID!=-1)
		{
			System.out.println("Connected to remote API server");	
			IntWA objectHandles = new IntWA(1);
			int ret=vrep.simxGetObjects(clientID,vrep.sim_handle_all,objectHandles,vrep.simx_opmode_oneshot_wait);
			ret = vrep.simxGetObjectHandle(clientID, "InitControl#", handle, vrep.simx_opmode_oneshot_wait);
			robothandle = handle.getValue();
			ret = vrep.simxGetObjectPosition(clientID, robothandle, -1, robotposition, vrep.simx_opmode_oneshot_wait);
			newposition = robotposition.getArray();
			System.out.println(newposition[0]+","+newposition[1]+","+newposition[2]);
			newposition = new float[] {0.0f, 0.0f, 0.3f};
			//robotposition.setArray(newposition); 
			ret = vrep.simxSetObjectPosition(clientID, robothandle, -1, robotposition, vrep.simx_opmode_oneshot_wait);
			if (ret==vrep.simx_return_ok)
				System.out.format("Number of objects in the scene: %d\n",objectHandles.getArray().length);
			else
				System.out.format("Remote API function call returned with error code: %d\n",ret);
			vrep.simxFinish(clientID);
		}
		else
			System.out.println("Failed connecting to remote API server");
		System.out.println("Program ended");
	}
}
			
