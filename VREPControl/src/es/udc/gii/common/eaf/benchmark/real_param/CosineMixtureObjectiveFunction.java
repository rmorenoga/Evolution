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
package es.udc.gii.common.eaf.benchmark.real_param;

import es.udc.gii.common.eaf.benchmark.BenchmarkObjectiveFunction;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class CosineMixtureObjectiveFunction extends BenchmarkObjectiveFunction {

    @Override
    public double[][] getOptimum(int dim) {
        double[][] optimums = new double[1][];
        double[] optimum = new double[dim];

        for (int i = 0; i < dim; i++) {
            optimum[i] = 0.0;
        }

        optimums[0] = optimum;
        return optimums;

    }

    @Override
    public double evaluate(double[] values) {

        double fitness = Double.MAX_VALUE;
        double s1, s2;

        s1 = 0.0;
        s2 = 0.0;

        //x ~ [-1.0, 1.0]
        for (int i = 0; i < values.length; i++) {

            s1 += Math.cos(5 * Math.PI * values[i]);
            s2 += values[i] * values[i];

        }

        fitness = 0.1 * s1 - s2;

        return -(fitness - values.length/10.0);

    }

    @Override
    public void reset() {
    }
}
