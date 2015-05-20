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
 * The axis parallel hyper-ellipsoid is similar to De Jong's function 1. It is 
 * also known as the weighted sphere model. Again, it is continuos, 
 * convex and unimodal.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class AxisParallelHyperEllipsoidObjectiveFunction extends BenchmarkObjectiveFunction {

    @Override
    public double evaluate(double[] values) {

        double fitness = 0.0;

        //x ~ [-100.0, 100.0]
        for (int i = 0; i < values.length; i++) {
            fitness += (i + 1) * (values[i] * 100) * (values[i] * 100);
        }

        return fitness;

    }

    @Override
    public void reset() {
    }

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
}
