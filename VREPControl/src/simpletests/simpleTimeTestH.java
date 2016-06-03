package simpletests;

import coppelia.CharWA;
import coppelia.FloatWA;
import coppelia.IntW;
import coppelia.IntWA;
import coppelia.remoteApi;

public class simpleTimeTestH {

public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		
		//Simulation Parameters
		float fitness1 = 0;
		int Numberofmodules = 6;
		int MaxTime = 20;
		int[] orientation = new int[] {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1};
		IntW out = new IntW(0);
		CharWA datastring = new CharWA(1);
		FloatWA out2 = new FloatWA(3);
		
		//Pack Integers into one String data signal
				IntWA NumberandOri= new IntWA(Numberofmodules+2);
				int[] NO = new int[Numberofmodules+2];
				NO[0] = Numberofmodules;
				NO[1] = MaxTime;
				for (int i=2;i<Numberofmodules+2;i++)
				{
					NO[i] = orientation[i-2];
				}
				System.arraycopy(NO,0,NumberandOri.getArray(),0,NO.length);
				char[] p2 = NumberandOri.getCharArrayFromArray();
				CharWA strNO = new CharWA(p2.length);
				System.arraycopy(p2,0,strNO.getArray(),0,p2.length);
		
				System.out.println("Program started");
				remoteApi vrep = new remoteApi();
				vrep.simxFinish(-1); // just in case, close all opened connections
				
				int clientID = vrep.simxStart("127.0.0.1",19997,true,true,5000,5);
				
				if (clientID!=-1)
				{
					System.out.println("Connected to remote API server");
					
					//Setting simulation parameters		
				int ret = vrep.simxSetStringSignal(clientID, "NumberandOri", strNO, vrep.simx_opmode_oneshot_wait);	
				//Load first maze and starting the simulation
				ret = vrep.simxLoadScene(clientID, "/home/rodrigo/V-REP/Modular/ModularCPGAH21.ttt", 0, vrep.simx_opmode_oneshot_wait);	
				ret = vrep.simxStartSimulation(clientID, vrep.simx_opmode_oneshot_wait);
			
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
			       
			    //Reading simulation results    
			       vrep.simxGetStringSignal(clientID, "Position", datastring , vrep.simx_opmode_oneshot_wait);
			       out2.initArrayFromCharArray(datastring.getArray());
			       System.out.println("Return = "+Float.toString(out2.getArray()[0])+" "+Float.toString(out2.getArray()[1]));
			     
			    //Scaling fitness   
			    if (out2.getArray()[0]==0)
			    {
			    	fitness1 = out2.getArray()[1];
			    }else if(out2.getArray()[1]==0)
			    {
			     fitness1 = (out2.getArray()[0]*(100/7))+60;	
			    }
				System.out.println("Fitness = "+Float.toString(fitness1));
				
				//Closing first maze
			    while(vrep.simxCloseScene(clientID, vrep.simx_opmode_oneshot_wait)!=vrep.simx_return_ok)		   
			    {	   	
			    }
				
			    if (ret==vrep.simx_return_ok)
					System.out.println("Last command OK");
				else
					System.out.format("Remote API function call returned with error code: %d\n",ret);
				vrep.simxFinish(clientID);				
				}
				else
					System.out.println("Failed connecting to remote API server");
				System.out.println("Program ended");
				
				//(fitness1+fitness2+fitness3)/3;
				
				long stopTime = System.currentTimeMillis();
			      long elapsedTime = stopTime - startTime;
			      System.out.println(elapsedTime);
	}
}
