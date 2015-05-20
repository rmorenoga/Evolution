package evolutionJEAF;
import mpi.MPI;
import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;

public class CalculateFitnessJEAFTest extends ObjectiveFunction{


    public double evaluate(double[] values) {


        double fitness = 0.0;
        double sum = values[0]+values[1]+values[2]+values[3]+values[4];
        double square = sum*sum;
        
        fitness = square*10;
        System.out.println("Best Individual "+EvolutionJEAFTest.algorithm.getBestIndividual().getFitness());
        
        return fitness;
        
    }
	
    public void reset() {
    }
	
	
	
}
