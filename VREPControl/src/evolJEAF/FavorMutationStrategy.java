package evolJEAF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration.Configuration;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.operator.reproduction.mutation.de.mutationStrategy.RandomDEMutationStrategy;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.population.jade.JADEIndividual;
import es.udc.gii.common.eaf.util.EAFRandom;

public class FavorMutationStrategy extends RandomDEMutationStrategy{
	
	
	public Individual getMutatedIndividual(EvolutionaryAlgorithm algorithm, Individual target) {

        int basePos;
        double[] base;
        List<Individual> individuals;
        List<Individual> listInd;
        List<Integer> index_list;
        int randomPos;
        double auxGeneValue, x1, x2;
        double F;

        F = this.getFPlugin().get(algorithm);
        individuals = algorithm.getPopulation().getIndividuals();

        basePos = (int) EAFRandom.nextInt(individuals.size());
        base = ((Individual) individuals.get(basePos)).getChromosomeAt(0);
        index_list = new ArrayList<>();
        index_list.add(basePos);

        //se eligen los vectores diferenciales:
        listInd = new ArrayList<>();

        for (int i = 0; i < this.getDiffVector() * 2; i++) {

            do {

                randomPos = (int) EAFRandom.nextInt(individuals.size());

            } while (index_list.contains(randomPos));

            index_list.add(randomPos);
            listInd.add(individuals.get(randomPos));

        }

        double[][] diffVectorMatrix = new double[this.getDiffVector()*2][base.length];
        
        for (int j = 0; j < diffVectorMatrix.length; j++) {
            diffVectorMatrix[j] = Arrays.copyOf(listInd.get(j).getChromosomeAt(0), base.length);
        }
        
        if (base != null) {
            //Recorremos el numero de genes:
        	int division = base.length/5;
            for (int i = 0; i < base.length; i++) {

                auxGeneValue = base[i];

                for (int j = 0; j < this.getDiffVector(); j += 2) {

                    x1 = diffVectorMatrix[j][i];
                    x2 = diffVectorMatrix[j+1][i];
                    
                    if (i<division){
                    	auxGeneValue += F * (x1 - x2);
                    }else if(i>=division && i< division*2){
                    	auxGeneValue += F * (x1 - x2)*4/5;
                    }else if(i>=division*2 && i<division*3){
                    	auxGeneValue += F * (x1 - x2)*3/5;
                    }else if(i>=division*3 && i<division*4){
                    	auxGeneValue += F * (x1 - x2)*2/5;
                    }else if(i>=division*4){
                    	auxGeneValue += F * (x1 - x2)*1/5;
                    }                

                }

                base[i] = auxGeneValue;

            }

        }

        Individual mutatedIndividual;
        if (target instanceof JADEIndividual) {
            mutatedIndividual = new JADEIndividual();
            ((JADEIndividual)mutatedIndividual).setF(F);
        } else {
            mutatedIndividual = new Individual();
        }
        mutatedIndividual.setChromosomeAt(0, base);
        return mutatedIndividual;

}
	
	
}
