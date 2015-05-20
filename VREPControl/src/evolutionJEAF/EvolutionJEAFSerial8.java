package evolutionJEAF;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.parallel.ParallelEvolutionaryAlgorithm;
import es.udc.gii.common.eaf.facade.EAFFacade;
import es.udc.gii.common.eaf.stoptest.StopTest;
import es.udc.gii.common.eaf.util.EAFRandom;


public class EvolutionJEAFSerial8 {
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		EAFFacade facade = new EAFFacade();
		EvolutionaryAlgorithm algorithm;
		StopTest stopTest;
		EAFRandom.init();
		algorithm = facade.createAlgorithm("" + "Evolutionconfigserial8.xml");
        stopTest = facade.createStopTest("./" + "Evolutionconfigserial8.xml");
        facade.resolve(stopTest, algorithm);
        
        System.out.println("Finished");
		
        long stopTime = System.currentTimeMillis();
	      long elapsedTime = stopTime - startTime;
	      System.out.println(elapsedTime);
		

	}
	
	
}
