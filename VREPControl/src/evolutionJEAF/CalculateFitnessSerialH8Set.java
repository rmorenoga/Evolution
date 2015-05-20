package evolutionJEAF;

import coppelia.CharWA;
import coppelia.FloatWA;
import coppelia.IntW;
import coppelia.IntWA;
import coppelia.remoteApi;
import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;

public class CalculateFitnessSerialH8Set extends ObjectiveFunction{

	public double evaluate(double[] values) {

		long startTime = System.currentTimeMillis();
		
		//Simulation Parameters
        float[] fitness = new float[4];
		//float fitness2 = 0;
		//float fitness3 = 0;
		int Numberofmodules = 8;//Default 2
		int MaxTime = 20;
		IntW out = new IntW(0);
		CharWA datastring = new CharWA(1);
		FloatWA out2 = new FloatWA(3);
		
		double temp = 0;
		int[] orientation = new int[8];
        for(int i=0;i<8;i++){
			  temp = values[i];
			  if (temp>0)
				  orientation[i] = 1;
			  else
				  orientation[i] = 0;
		  }
		
      //Pack Integers into one String data signal
      		IntWA NumberandOri= new IntWA(Numberofmodules+2);
      		int[] NO = new int[Numberofmodules+2];
      		NO[0] = Numberofmodules;
      		NO[1] = MaxTime;
      		for (int i=2;i<Numberofmodules+2;i++)
      		{
      			NO[i] = orientation[i-2];
      		}
      		NumberandOri.setArray(NO);
      		CharWA strNO=new CharWA(1);
      		strNO.setArray(NumberandOri.getCharArrayFromArray());
        
		
      	//Pack Floats into one String data signal
    	FloatWA ControlParam =new FloatWA(18);
    	float[] CP = new float[18];
    		
		for (int i=0;i<18;i++)
		{
			CP[i] = (float) values[i+8];
		}
		ControlParam.setArray(CP);
		CharWA strCP=new CharWA(1);
		strCP.setArray(ControlParam.getCharArrayFromArray());
		
		System.out.println("Program started");
		remoteApi vrep = new remoteApi();
		vrep.simxFinish(-1); // just in case, close all opened connections
		
		int clientID = vrep.simxStart("127.0.0.1",19997,true,true,5000,5);
		
		if (clientID!=-1)
		{
			System.out.println("Connected to remote API server");
			
			//Setting simulation parameters		
		int ret = vrep.simxSetStringSignal(clientID, "NumberandOri", strNO, vrep.simx_opmode_oneshot_wait);	
		ret = vrep.simxSetStringSignal(clientID, "ControlParam", strCP, vrep.simx_opmode_oneshot_wait) ;
//*******************************************************************************************************************************
		
		//Load first maze and starting the simulation
		ret = vrep.simxLoadScene(clientID, "/home/rodrigo/V-REP/Modular/ModularCPGAH21_5.ttt", 0, vrep.simx_opmode_oneshot_wait);	
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
	    	fitness[0] = out2.getArray()[1];
	    }else if(out2.getArray()[1]==0)
	    {
	     fitness[0] = (out2.getArray()[0]*(100/7))+60;	
	    }
		System.out.println("Fitness = "+Float.toString(fitness[0]));
		
		//Closing first maze
	    while(vrep.simxCloseScene(clientID, vrep.simx_opmode_oneshot_wait)!=vrep.simx_return_ok)		   
	    {	   	
	    }
		
	  //*******************************************************************************************************************	    
		
		  //Load third maze and starting the simulation
		  		ret = vrep.simxLoadScene(clientID, "/home/rodrigo/V-REP/Modular/ModularCPGAH23_5.ttt", 0, vrep.simx_opmode_oneshot_wait);
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
		  	      // System.out.println("Return = "+Float.toString(out2.getArray()[0])+" "+Float.toString(out2.getArray()[1]));
		  	     
		  	    //Scaling fitness   
		  	    if (out2.getArray()[0]==0)
		  	    {
		  	    	fitness[2] = out2.getArray()[1];
		  	    }else if(out2.getArray()[1]==0)
		  	    {
		  	     fitness[2] = (out2.getArray()[0]*(100/7))+60;	
		  	    }
		  	//	System.out.println("Fitness = "+Float.toString(fitness[2]));
		  		
		  		//Closing third maze
		  	    while(vrep.simxCloseScene(clientID, vrep.simx_opmode_oneshot_wait)!=vrep.simx_return_ok)		   
		  	    {	   	
		  	    }

	//*******************************************************************************************************************************
		    	//Setting new goals    
		  	 //GoalX = 2.7f;
			 //GoalY = -3.875f;
			 //CP[3] = GoalX;
			 //CP[4] = GoalY;
			 //ControlParam.setArray(CP);
			 //strCP=new CharWA(1);
			 //strCP.setArray(ControlParam.getCharArrayFromArray());   
			 //ret = vrep.simxSetStringSignal(clientID, "ControlParam", strCP, vrep.simx_opmode_oneshot_wait) ;
		  	    
		  	    
		  	//Load second maze and starting the simulation
		  		ret = vrep.simxLoadScene(clientID, "/home/rodrigo/V-REP/Modular/ModularCPGAH22_5.ttt", 0, vrep.simx_opmode_oneshot_wait);
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
		  	   //    System.out.println("Return = "+Float.toString(out2.getArray()[0])+" "+Float.toString(out2.getArray()[1]));
		  	     
		  	    //Scaling fitness   
		  	    if (out2.getArray()[0]==0)
		  	    {
		  	    	fitness[1] = out2.getArray()[1];
		  	    }else if(out2.getArray()[1]==0)
		  	    {
		  	     fitness[1] = (out2.getArray()[0]*(100/6.3f))+60;	
		  	    }
		  		//System.out.println("Fitness = "+Float.toString(fitness[1]));
		  		
		  		//Closing second maze
		  	    while(vrep.simxCloseScene(clientID, vrep.simx_opmode_oneshot_wait)!=vrep.simx_return_ok)		   
		  	    {	   	
		  	    }
		  	    
	//*******************************************************************************************************************************

		    	//Setting new goals    
		  	/* GoalX = 2.7f;
			 GoalY = -3.875f;
			 CP[3] = GoalX;
			 CP[4] = GoalY;
			 ControlParam.setArray(CP);
			 strCP=new CharWA(1);
			 strCP.setArray(ControlParam.getCharArrayFromArray());   
			 ret = vrep.simxSetStringSignal(clientID, "ControlParam", strCP, vrep.simx_opmode_oneshot_wait) ;
		  	    */
		  	    
		  	//Load second maze and starting the simulation
		  		ret = vrep.simxLoadScene(clientID, "/home/rodrigo/V-REP/Modular/ModularCPGAH24_5.ttt", 0, vrep.simx_opmode_oneshot_wait);
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
		  	      // System.out.println("Return = "+Float.toString(out2.getArray()[0])+" "+Float.toString(out2.getArray()[1]));
		  	     
		  	    //Scaling fitness   
		  	    if (out2.getArray()[0]==0)
		  	    {
		  	    	fitness[3] = out2.getArray()[1];
		  	    }else if(out2.getArray()[1]==0)
		  	    {
		  	     fitness[3] = (out2.getArray()[0]*(100/6.3f))+60;	
		  	    }
		  		//System.out.println("Fitness = "+Float.toString(fitness[3]));
		  		
		  		//Closing second maze
		  	    while(vrep.simxCloseScene(clientID, vrep.simx_opmode_oneshot_wait)!=vrep.simx_return_ok)		   
		  	    {	   	
		  	    }
		  	    
	//*******************************************************************************************************************************
		  	    
	    
	    
	    
	    if (ret==vrep.simx_return_ok)
			System.out.println("Last command OK");
		else
			System.out.format("Remote API function call returned with error code: %d\n",ret);
		vrep.simxFinish(clientID);				
		}
		else
			System.out.println("Failed connecting to remote API server");
		System.out.println("Program ended");
		
		double fitnessd = (fitness[0]+fitness[1]+fitness[2]+fitness[3])/4;
		
		long stopTime = System.currentTimeMillis();
	      long elapsedTime = stopTime - startTime;
	      System.out.println(elapsedTime);
		
		return fitnessd;   
		
	}
	
	public void reset() {
    }
	
	
}
