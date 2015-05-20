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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.multiobjective.fon;

import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import org.apache.commons.configuration.Configuration;

/**
 * Fonseca and Flemming, 1995a
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class Fon_Objective_2 extends ObjectiveFunction {

    public Fon_Objective_2() {
    }

    @Override
    public String toString() {
        return "Fon_Objective_2";
    }

    @Override
    public void reset() {
    }

    @Override
    public double evaluate(double[] values) {
        double[] x = new double[values.length];
        double sum = 0;
        double k = 1 / Math.sqrt(values.length);

        for (int i = 0; i < values.length; i++) {
            x[i] = 4 * values[i];
            sum += (x[i] + k) * (x[i] + k);
        }

        return 1 - Math.exp(-sum);
    }

    @Override
    public void configure(Configuration conf) {
    }    
}
