package evolutionJEAFParallel;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;

import mpi.MPI;
import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.facade.EAFFacade;
import es.udc.gii.common.eaf.stoptest.StopTest;
import es.udc.gii.common.eaf.util.EAFRandom;

public class EvolJEAFMaze {
	public static int startNumber;
	//static EvolutionaryAlgorithm algorithm;
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		long seed = -Long.MAX_VALUE;
		startNumber = 0;
//		if (args.length > 0) {
//		    try {
////		    	for(int j=0;j<args.length;j++){
////		    		System.out.println("Argument "+j+" = "+args[j]);
////		    	}
//		        startNumber = Integer.parseInt(args[0]);
//		    } catch (NumberFormatException e) {
//		        System.err.println("StartNumber " + args[0] + " must be an integer.");
//		        System.exit(1);
//		    }
//		}else{
//			 System.err.println("Provide a start number");
//			 System.exit(1);
//		}
		//System.out.println("StartNumber = "+startNumber);
		
		
		
		/* Initialize Parallel Environment */
		MPI.Init(args);
		MPI.initThread(MPI.THREAD_MULTIPLE, 0, args);
		int myRank = MPI.COMM_WORLD.Rank();
		myRank = myRank +startNumber;
		
		
		String vrepcommand = new String("./vrep"+myRank+".sh");
		
		/*Initialize a v-rep simulator based on the starNumber parameter */
		try {
			ProcessBuilder qq=new ProcessBuilder(vrepcommand,"-h");
			//ProcessBuilder qq=new ProcessBuilder(vrepcommand);
			qq.directory(new File("/home/rodrigo/V-REP/Vrep"+myRank+"/"));
			File log = new File("Simout/log");
			qq.redirectErrorStream(true);
			qq.redirectOutput(Redirect.appendTo(log));
			qq.start();
            Thread.sleep(5000);
		} catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
		
		
		// Initialize Random Number Generator
		EAFRandom.init(seed != -Long.MAX_VALUE ? seed : System.currentTimeMillis());
		
		EAFFacade facade = new EAFFacade();
		EvolutionaryAlgorithm algorithm;
		StopTest stopTest;
		EAFRandom.init();
		/* Specify the configuration file for the evolutionary run */
		algorithm = facade.createAlgorithm("" + "EvolconfigMaze.xml");
        stopTest = facade.createStopTest("./" + "EvolconfigMaze.xml");
        // Start the evolutionary process
        facade.resolve(stopTest, algorithm);
        
        // If I am the process 0 terminate all simulators after finishing the evolutionary process
        if (myRank==0){        	
          long stopTime = System.currentTimeMillis();
  	      long elapsedTime = stopTime - startTime;
  	      System.out.println(elapsedTime);
        	System.out.println("Finished");
        	 try {
        		 // kill all the v-rep processes
     			ProcessBuilder qq=new ProcessBuilder("killall","-r","vrep");
     			File log = new File("Simout/log");
     			qq.redirectErrorStream(true);
     			qq.redirectOutput(Redirect.appendTo(log));
     			Process p = qq.start();
                 int exitVal = p.waitFor();
                 System.out.println("Terminated all vrep with error code "+exitVal);
     		} catch(Exception e) {
                 System.out.println(e.toString());
                 e.printStackTrace();
             }       	
        }
        
        
         //Terminate Parallel Environment 
		MPI.Finalize();

		
        
		

	}
	
	
	
}
