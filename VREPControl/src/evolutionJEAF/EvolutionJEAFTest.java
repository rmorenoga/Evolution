package evolutionJEAF;
import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.parallel.ParallelEvolutionaryAlgorithm;
import es.udc.gii.common.eaf.facade.EAFFacade;
import es.udc.gii.common.eaf.stoptest.StopTest;
import es.udc.gii.common.eaf.util.EAFRandom;
import es.udc.gii.common.eaf.facade.EAFFacade;


public class EvolutionJEAFTest {

	public static EvolutionaryAlgorithm algorithm;
	
	public static void main(String[] args) {
		
		EAFFacade facade = new EAFFacade();
		//EvolutionaryAlgorithm algorithm;
		StopTest stopTest;
		EAFRandom.init();
		algorithm = facade.createAlgorithm("" + "walk_config.xml");
        stopTest = facade.createStopTest("./" + "walk_config.xml");
        facade.resolve(stopTest, algorithm);
        
        System.out.println("Finished");
		
		
		
		
		
		

	}

}
