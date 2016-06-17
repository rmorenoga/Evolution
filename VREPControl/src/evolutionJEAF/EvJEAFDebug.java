package evolutionJEAF;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.facade.EAFFacade;
import es.udc.gii.common.eaf.stoptest.StopTest;
import es.udc.gii.common.eaf.util.EAFRandom;

public class EvJEAFDebug {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		EAFFacade facade = new EAFFacade();
		EvolutionaryAlgorithm algorithm;
		StopTest stopTest;
		EAFRandom.init();
		algorithm = facade.createAlgorithm("" + "EvJEAFDebug.xml");
        stopTest = facade.createStopTest("./" + "EvJEAFDebug.xml");
        facade.resolve(stopTest, algorithm);
        
        long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println(elapsedTime);
		
        System.out.println("Finished");
		

	}
	
}
