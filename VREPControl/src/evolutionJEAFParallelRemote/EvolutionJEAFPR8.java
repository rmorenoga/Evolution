package evolutionJEAFParallelRemote;

import mpi.MPI;
import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.parallel.ParallelEvolutionaryAlgorithm;
import es.udc.gii.common.eaf.facade.EAFFacade;
import es.udc.gii.common.eaf.stoptest.StopTest;
import es.udc.gii.common.eaf.util.EAFRandom;


public class EvolutionJEAFPR8 {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		long seed = -Long.MAX_VALUE;
		
		/* Initialize Parallel Environment */
		MPI.Init(args);
		MPI.initThread(MPI.THREAD_MULTIPLE, 0, args);
		int myRank = MPI.COMM_WORLD.Rank();
		
		// Initialize Random Number Generator
		EAFRandom.init(seed != -Long.MAX_VALUE ? seed : System.currentTimeMillis());
		
		EAFFacade facade = new EAFFacade();
		EvolutionaryAlgorithm algorithm;
		StopTest stopTest;
		EAFRandom.init();
		algorithm = facade.createAlgorithm("" + "EvolutionconfigPR8.xml");
        stopTest = facade.createStopTest("./" + "EvolutionconfigPR8.xml");
        facade.resolve(stopTest, algorithm);
       
        if (myRank==0){
        	System.out.println("Finished");
        }
        
        
        /* Terminate Parallel Environment */
		MPI.Finalize();

		
        long stopTime = System.currentTimeMillis();
	      long elapsedTime = stopTime - startTime;
	      System.out.println(elapsedTime);
		

	}
	
}
