/*
* Copyright (C) 2010 Grupo Integrado de Ingeniería
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/ 


/*
 * BestMeanLogTool.java
 *
 * Created on 4 de diciembre de 2006, 18:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.log;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.algorithm.population.Individual;
import es.udc.gii.common.eaf.algorithm.productTrader.IndividualsProductTrader;
import es.udc.gii.common.eaf.algorithm.productTrader.specification.BestIndividualSpecification;
import es.udc.gii.common.eaf.benchmark.BenchmarkObjectiveFunction;
import es.udc.gii.common.eaf.log.LogTool;
import java.util.Map;
import java.util.Observable;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class FesGNELogTool extends LogTool {

    /** Creates a new instance of BestMeanLogTool */
    public FesGNELogTool() {
    }

    public void setParameters(Map<String, String> parameters) {
    }

    @Override
    public void configure(Configuration conf) {
        try {
            super.configure(conf);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {

        EvolutionaryAlgorithm algorithm = (EvolutionaryAlgorithm) o;
        BestIndividualSpecification bestSpec =
                new BestIndividualSpecification();
        Individual best;
        double gne;

        super.update(o, arg);

        if (algorithm.getState() == EvolutionaryAlgorithm.REPLACE_STATE && arg == null) {
            best = (Individual) IndividualsProductTrader.get(bestSpec,
                    algorithm.getPopulation().getIndividuals(), 1, algorithm.getComparator()).get(0);

            gne = this.calculateGNE(best,(BenchmarkObjectiveFunction
                    ) algorithm.getProblem().getObjectiveFunctions().get(0));
            super.getLog().println(
                    algorithm.getFEs() + " - " +
                    gne);
        }
    }

    @Override
    public String getLogID() {
        return "fesgne";
    }

    private double calculateGNE(Individual best, BenchmarkObjectiveFunction objective) {

        double aux_gne, gne;
        double[] chromosome;
        double[][] optimums;

        chromosome = best.getChromosomeAt(0);
        optimums = objective.getOptimum(chromosome.length);

        //Se calcula el gne para el primer optimo:
        gne = 0;
        for (int i = 0; i < chromosome.length; i++) {

            gne += Math.pow(chromosome[i] - optimums[0][i], 2.0);

        }
        gne /= chromosome.length;
        gne = Math.sqrt(gne);

        //Si hay más óptimos nos quedamos con el menos error de todos:
        aux_gne = Double.MAX_VALUE;
        if (optimums.length > 1) {
            for (int j = 1; j < optimums.length; j++) {

                aux_gne = 0;
                for (int k = 0; k < chromosome.length; k++) {

                    aux_gne += Math.pow(chromosome[k] - optimums[j][k], 2.0);

                }
                aux_gne /= chromosome.length;
                aux_gne = Math.sqrt(gne);

            }
            
            gne = Math.min(gne, aux_gne);
        }

        return gne;

    }
}
