package evolutionJEAFParallel;

import java.io.*;

import mpi.MPI;
import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.parallel.ParallelEvolutionaryAlgorithm;
import es.udc.gii.common.eaf.facade.EAFFacade;
import es.udc.gii.common.eaf.stoptest.StopTest;
import es.udc.gii.common.eaf.util.EAFRandom;


public class EvolutionJEAFSerial8P {
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		long seed = -Long.MAX_VALUE;
		
		/* Initialize Parallel Environment */
		MPI.Init(args);
		MPI.initThread(MPI.THREAD_MULTIPLE, 0, args);
		int myRank = MPI.COMM_WORLD.Rank();
		
		/*String vrepcommand = new String("/home/rodrigo/V-REP/Vrep"+myRank+"/vrep"+myRank+".sh -h");
		
		try {
            Runtime rt = Runtime.getRuntime();
            rt.exec(vrepcommand);
            Thread.sleep(3000);
		} catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }*/
		
		/*try {
			Process qq=new ProcessBuilder(vrepcommand,"-h").start();
			BufferedReader br = new BufferedReader(new InputStreamReader(qq.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ( (line = br.readLine()) != null) {
			   builder.append(line);
			   builder.append(System.getProperty("line.separator"));
			}
			String result = builder.toString();
			System.out.println(result);
            Thread.sleep(3000);
		} catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }*/
		
		
		// Initialize Random Number Generator
		EAFRandom.init(seed != -Long.MAX_VALUE ? seed : System.currentTimeMillis());
		
		EAFFacade facade = new EAFFacade();
		EvolutionaryAlgorithm algorithm;
		StopTest stopTest;
		EAFRandom.init();
		algorithm = facade.createAlgorithm("" + "Evolutionconfigserial8P.xml");
        stopTest = facade.createStopTest("./" + "Evolutionconfigserial8P.xml");
        facade.resolve(stopTest, algorithm);
       
        if (myRank==0){
        	System.out.println("Finished");
        }
        
        
         //Terminate Parallel Environment 
		MPI.Finalize();

		
        long stopTime = System.currentTimeMillis();
	      long elapsedTime = stopTime - startTime;
	      System.out.println(elapsedTime);
		

	}
}
