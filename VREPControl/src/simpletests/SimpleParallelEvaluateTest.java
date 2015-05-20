package simpletests;

import mpi.MPI;
import coppelia.CharWA;
import coppelia.FloatWA;
import coppelia.IntW;
import coppelia.remoteApi;

public class SimpleParallelEvaluateTest {
	
	
	public void evaluate(){
		int myRank = MPI.COMM_WORLD.Rank();
		CharWA datastring = new CharWA(1);
		FloatWA out2 = new FloatWA(3);
		float fitness = 0;
		
		
		System.out.println("Program started");
		remoteApi vrep = new remoteApi(); //If used will trigger "Native Library already loaded in another class loader" error
		//remoteApi vrep = SimpleParallelControl.vrep;
		int clientID = -1;
		//if(myRank==0){
			clientID = vrep.simxStart("127.0.0.1",19997+myRank,true,true,5000,5); //Start the connection with the simulator for thread 0
		//}else {	
			//clientID = vrep.simxStart("127.0.0.1",19998,true,true,5000,5); //Start the connection with the simulator for thread 1
		//}
		if (clientID!=-1)
		{
			IntW out = new IntW(0);
			System.out.println("Connected to remote API server");
			int ret = vrep.simxLoadScene(clientID, "/home/rodrigo/V-REP/Modular/ModularCPGA1.ttt", 0, vrep.simx_opmode_oneshot_wait);
			ret = vrep.simxStartSimulation(clientID, vrep.simx_opmode_oneshot_wait);//Run a simple command in the simulator
			
			
			//Setting up and waiting for finished flag
			ret = vrep.simxGetIntegerSignal(clientID, "finished", out,vrep.simx_opmode_streaming);	
			out.setValue(0);
			//long startTime = System.currentTimeMillis();
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
			
			//Stopping simulation
	  	       ret = vrep.simxStopSimulation(clientID, vrep.simx_opmode_oneshot_wait);
				//ret  = vrep.simxPauseSimulation(clientID, vrep.simx_opmode_oneshot_wait);
	  	       
	  	   //Reading simulation results    
		       vrep.simxGetStringSignal(clientID, "Position", datastring , vrep.simx_opmode_oneshot_wait);
		       out2.initArrayFromCharArray(datastring.getArray());
		       System.out.println("Return = "+Float.toString(out2.getArray()[0])+" "+Float.toString(out2.getArray()[1]));
		     
		    //Scaling fitness   
		    if (out2.getArray()[0]==0)
		    {
		    	fitness = out2.getArray()[1];
		    }else if(out2.getArray()[1]==0)
		    {
		     fitness = (out2.getArray()[0]*(100/7))+60;	
		    }
			System.out.println("Fitness = "+Float.toString(fitness));
			
	  	    while(vrep.simxCloseScene(clientID, vrep.simx_opmode_oneshot_wait)!=vrep.simx_return_ok)		   
		  	    {	   	
		  	    }
	  	       
			if (ret==vrep.simx_return_ok)
				System.out.println("Command OK");
			else
				System.out.format("Remote API function call returned with error code: %d\n",ret);
			vrep.simxFinish(clientID);				
		}
			else
				System.out.println("Failed connecting to remote API server");
			System.out.println("Program ended");
	}
	

}
