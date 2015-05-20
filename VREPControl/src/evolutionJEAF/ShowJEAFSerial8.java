package evolutionJEAF;


import java.io.File;
import java.lang.ProcessBuilder.Redirect;

import coppelia.CharWA;
import coppelia.remoteApi;
import coppelia.IntW;
import coppelia.IntWA;
import coppelia.FloatWA;


public class ShowJEAFSerial8 {

	
	public static void main(String[] args) {
		
		//System.getProperties().list(System.out);
		
	    String Scenedir = "/home/rodrigo/V-REP/Modular/ModularComplex1-2.ttt";
		int myRank = 3;		
		int maxTries = 3;
		String vrepcommand = new String("./vrep"+myRank+".sh");
		//double[] values = new double[] {0.8536504620108267, -0.07293052576805517, 0.8350016257455486, 0.9102255286278951, -0.5678017531476228, -0.008966133924681596, -0.7569201116829705, 0.8934171151373216, 0.430566570152515, -0.05505375335654006, 0.308258662227745}; 
		//double[] values = new double[] {0.763741345234382, -0.8252553998868861, -0.9589260122024488, -0.4800232082125224, -0.5971527015151525, -0.7883073704091224, -0.13421602113754583, 0.10931753272805644, 0.9834412128574326, -0.5793182551334726, 0.5378274982598259};
		//double[] values = new double[] {0.4465934752123313, 0.30717810224877007, -0.3945431065798324, 0.09779192183760244, -0.9932809215808824, -0.7657277907090718, 0.21562070991388585, -0.16428425810118602, -0.414718054506506, -0.3014283247662487, -0.7131631572729754};
		double[] values = new double[] {1.0, 0.21896281280871818, -0.7123231193153826};
		int MaxTime = 300; 
		long startTime = System.currentTimeMillis();
		
         if (args.length > 0) {
 		    try {
// 		    	for(int j=0;j<args.length;j++){
// 		    		System.out.println("Argument "+j+" = "+args[j]);
// 		    	}
 		        //startNumber = Integer.parseInt(args[0]);
 		        values[0] = Double.parseDouble(args[0]);
 		        values[1] = Double.parseDouble(args[1]);
 		        values[2] = Double.parseDouble(args[2]);
 		    	Scenedir = args[3];
 		    	MaxTime = Integer.parseInt(args[4]);
 		    } catch (NumberFormatException e) {
 		        System.err.println("Argument " + args[0] + " must be a double");
 		        System.exit(1);
 		    }
 		}
         
         

 		float ampli = (float) values[0];		
 		float offset = (float) values[1];
 		float phase = (float) (values[2])*(float)Math.PI;
		//int tam = (int) (values[3]*15);
		//if(tam<2)
			//tam = 2;
		//else if(tam>15)
			//tam = 15;
		
		/*double temp = 0;
		int[] z = new int[8];
        for(int i=0;i<8;i++){
			  temp = values[i+3];
			  if (temp>0)
				  z[i] = 1;
			  else
				  z[i] = 0;
		  }*/
		
	
//		try {
//			ProcessBuilder qq=new ProcessBuilder(vrepcommand);
//			//Map<String, String> env = qq.environment();
//			qq.directory(new File("/home/rodrigo/V-REP/Vrep"+myRank+"/"));
//			//qq.inheritIO();
//			File log = new File("Simout/log");
//			qq.redirectErrorStream(true);
//			qq.redirectOutput(Redirect.appendTo(log));
//			qq.start();
//            Thread.sleep(5000);
//		} catch(Exception e) {
//            System.out.println(e.toString());
//            e.printStackTrace();
//        }
			
		//Simulation Parameters
        //float fitness1 = 0;
		//float fitness2 = 0;
		//float fitness3 = 0;
		//float fitness4 = 0;
        float[] rfitness = new float[2];
        float[] fitness = new float[] {1000,1000,1000,1000};
        //float[] fitness = new float[] {1000,1000,1000,1000};
		int Numberofmodules = 8;//Default 2
		//int MaxTime = 60;
		int[] orientation = new int[] {0,1,0,1,0,1,0,1}; //Default
		//int[] orientation = z;
		float CPGAmpli = ampli;//Default 0.5f
		float CPGOff = offset;//Default 0.1f
		float CPGPhase = phase;//Default (float) (-Math.PI/6)
	//	float GoalX = 0f;
		//float GoalY = -7f;
//		IntW out = new IntW(0);
//		CharWA datastring = new CharWA(1);
//		FloatWA out2 = new FloatWA(3);
		
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
		FloatWA ControlParam =new FloatWA(3);
		float[] CP = new float[3];
		CP[0] = CPGAmpli;
		CP[1] = CPGOff;
		CP[2] = CPGPhase;
		//CP[3] = GoalX;
		//CP[4] = GoalY;
		ControlParam.setArray(CP);
		CharWA strCP=new CharWA(1);
		strCP.setArray(ControlParam.getCharArrayFromArray());
		
		
		
		/*Repeat after restarting simulator in case of crash*/
		for(int j=0;j<maxTries;j++){
		
		
		
		
		
		
		//System.out.println("Program started");
		remoteApi vrep = new remoteApi();
		vrep.simxFinish(-1); // just in case, close all opened connections
		
		int clientID = vrep.simxStart("127.0.0.1",19994,true,true,5000,5);
		
		if (clientID!=-1)
		{
			//System.out.println("Connected to remote API server");
			
			//Setting simulation parameters		
		int ret = vrep.simxSetStringSignal(clientID, "NumberandOri", strNO, vrep.simx_opmode_oneshot_wait);	
		ret = vrep.simxSetStringSignal(clientID, "ControlParam", strCP, vrep.simx_opmode_oneshot_wait) ;
//*******************************************************************************************************************************
	
		
		rfitness = RunScene(vrep, clientID, Scenedir,  MaxTime);
		//rfitness = RunScene(vrep, clientID, "/home/rodr/EvolWork/Modular/ModularCPGA1.ttt",  MaxTime);
		if(rfitness[0]==-1){
			//RestartSim( myRank, j);
			continue;
		}
		fitness[0] = rfitness[1]; 
		System.out.println(fitness[0]);
		
//*******************************************************************************************************************************		
		
//		rfitness = RunScene(vrep, clientID, "/home/rodrigo/Desktop/Modular/ModularCPGA2.ttt",  MaxTime);
//		//rfitness = RunScene(vrep, clientID, "/home/rodr/EvolWork/Modular/ModularCPGA2.ttt",  MaxTime);
//		if(rfitness[0]==-1){
//			//RestartSim( myRank, j);
//			continue;
//		}
//		fitness[1] = rfitness[1]; 
//		System.out.println(fitness[1]);
//*******************************************************************************************************************************		
		
//		rfitness = RunScene(vrep, clientID, "/home/rodrigo/Desktop/Modular/ModularCPGA3.ttt",  MaxTime);
//		//rfitness = RunScene(vrep, clientID, "/home/rodr/EvolWork/Modular/ModularCPGA3.ttt",  MaxTime);
//		if(rfitness[0]==-1){
//			//RestartSim( myRank, j);
//			continue;
//		}
//		fitness[2] = rfitness[1]; 
//		System.out.println(fitness[2]);
//*******************************************************************************************************************************		
		
//		rfitness = RunScene(vrep, clientID, "/home/rodrigo/Desktop/Modular/ModularCPGA4.ttt",  MaxTime);
//		//rfitness = RunScene(vrep, clientID, "/home/rodr/EvolWork/Modular/ModularCPGA4.ttt",  MaxTime);
//		if(rfitness[0]==-1){
//			//RestartSim( myRank, j);
//			continue;
//		}
//		fitness[3] = rfitness[1]; 
//		System.out.println(fitness[3]);
	  //if (ret==vrep.simx_return_ok)
		//System.out.println("Last command OK");
	    //else
		//System.out.format("Remote API function call returned with error code: %d\n",ret);
	    vrep.simxFinish(clientID);				
		}
		else{
			System.out.println("Failed connecting to remote API server");
			System.out.println("Trying again for the "+j+" time");
			continue;
		}
			
		//System.out.println("Program ended");
	
		//System.out.println("Minimum fitness = "+Float.toString(minimum(fitness)));
	
		//double fitnessd = (fitness[0]+fitness[1]+fitness[2]+fitness[3])/4;
	    
	    
		break;
		}
		
		double fitnessd = fitness[0];
		
		long stopTime = System.currentTimeMillis();
	      long elapsedTime = stopTime - startTime;
	      System.out.println(elapsedTime);
		
		    System.out.println("Fitness = "+fitnessd);
		    
//		    try {
//     			ProcessBuilder qq=new ProcessBuilder("killall","-r","vrep");
//     			File log = new File("Simout/log");
//     			qq.redirectErrorStream(true);
//     			qq.redirectOutput(Redirect.appendTo(log));
//     			Process p = qq.start();
//                 int exitVal = p.waitFor();
//                 System.out.println("Terminated all vrep with error code "+exitVal);
//     		} catch(Exception e) {
//                 System.out.println(e.toString());
//                 e.printStackTrace();
//             }  
		    
	}
	
	
	
	 static float[] RunScene(remoteApi vrep, int clientID,String scene, int MaxTime){
			
			IntW out = new IntW(0);
			CharWA datastring = new CharWA(1);
			FloatWA out2 = new FloatWA(3);
			float[] fitnessout = new float[2];
			
	//Load scene maze and start the simulation
			int ret = vrep.simxLoadScene(clientID, scene, 0, vrep.simx_opmode_oneshot_wait);	
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
			
			vrep.simxGetIntegerSignal(clientID, "finished", out,vrep.simx_opmode_discontinue);
			
			//Stopping simulation
		       ret = vrep.simxStopSimulation(clientID, vrep.simx_opmode_oneshot_wait);
		       //System.out.println(ret);
		       
		    //Reading simulation results    
		       ret = vrep.simxGetStringSignal(clientID, "Position", datastring , vrep.simx_opmode_oneshot_wait);
		       if(ret != vrep.simx_return_ok){
		    	   System.out.println("Position Signal not received");
		       }
		       out2.initArrayFromCharArray(datastring.getArray());
		      // System.out.println("Return = "+Float.toString(out2.getArray()[0])+" "+Float.toString(out2.getArray()[1]));
		       
		      if(out2.getArray().length==0) {
		    	  System.out.println("out2 is empty");
		    	  fitnessout[0] = -1;
	              fitnessout[1] = 1000;
	              return fitnessout;
		      }
		      
		      fitnessout[0] = 0;
		    //Scaling fitness   
		    if (out2.getArray()[0]==0)
		    {
		    	fitnessout[1] = out2.getArray()[1];
		    }else if(out2.getArray()[1]==0)
		    {
		     fitnessout[1] = (out2.getArray()[0]*(100/7f))+(float)MaxTime;	
		    }
			//System.out.println("Fitness = "+Float.toString(fitness[0]));
			
			//Closing first maze
		    while(vrep.simxCloseScene(clientID, vrep.simx_opmode_oneshot_wait)!=vrep.simx_return_ok)		   
		    {	   	
		    }
		    
		    return fitnessout;
}
	
	
	
	
	
	public static float maximum(float[] inputset)
    {
    
    	float maxValue = 0;
    	for (int i=0;i<inputset.length;i++){
    		if(inputset[i]>=maxValue){
    			maxValue = inputset[i];
    		}
    	}
    	
    	return maxValue;
    }
	
}
