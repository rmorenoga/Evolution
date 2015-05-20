package evolutionJEAF;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.parallel.ParallelEvolutionaryAlgorithm;
import es.udc.gii.common.eaf.log.parallel.ParallelLogTool;
import java.util.Observable;

/**
 * Logs the population at the replace state in a parallel evolutionary algorithm.<p/>
 *
 * The output reads as follows:
 *
 * <pre>
 * State: ...
 * Generations: ...
 * Population:
 * ...
 * </pre>
 *
 * The "{@code ...}" are replaced by the current state of the algorithm, the
 * number of generations so far and the whole population repectively.<p/>
 *
 * In an island model this log tool logs each population of each island in an
 * independent file. For a distributed evaluation model, only a file is needed.
 * 
 * 
 */

public class ReplacePopLogTool extends ParallelLogTool{
	
	/** Creates a new instance of ReplacePopLogTool */
    public ReplacePopLogTool() {
    }
    
   
    public String getLogID() {
        return "ReplacePopLogTool";
    }
    
    public void update(Observable o, Object arg) {        
        ParallelEvolutionaryAlgorithm pea = (ParallelEvolutionaryAlgorithm)o;        
        
        if(pea.getCurrentObservable() instanceof EvolutionaryAlgorithm) {
            super.update(o, arg);
            EvolutionaryAlgorithm ea = (EvolutionaryAlgorithm)pea.getCurrentObservable();
            
            String state = "";
            
            if (ea.getState()==EvolutionaryAlgorithm.REPLACE_STATE){
            	state = "Replace state";
            	getLog().println("State: " + state + "\nGeneration: " +
                        ea.getGenerations() + "\nPopulation:\n" + ea.getPopulation() + "\n");
            }
            
           /* switch(ea.getState()) {
                case EvolutionaryAlgorithm.EVALUATE_STATE:
                    state = "Evaluate state";
                    break;
                case EvolutionaryAlgorithm.FINAL_STATE:
                    state = "Final state";
                    break;
                case EvolutionaryAlgorithm.INIT_EVALUATE_STATE:
                    state = "Init evaluate state";
                    break;
                case EvolutionaryAlgorithm.INIT_STATE:
                    state = "Init state";
                    break;
                case EvolutionaryAlgorithm.REPLACE_STATE:
                    state = "Replace state";
                    break;
                case EvolutionaryAlgorithm.REPRODUCTION_STATE:
                    state = "Reproduction state";
                    break;
                case EvolutionaryAlgorithm.SELECT_STATE:
                    state = "Select state";
                    break;
            }*/
            
        }
    }
}
