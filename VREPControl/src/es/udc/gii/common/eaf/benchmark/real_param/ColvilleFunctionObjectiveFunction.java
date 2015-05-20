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
public class ColvilleFunctionObjectiveFunction extends BenchmarkObjectiveFunction {

    @Override
    public double evaluate(double[] values) {

        double fitness = Double.MAX_VALUE;
        double x1, x2, x3, x4;

        //x ~ [-10.0, 10.0]
        x1 = values[0] * 10.0;
        x2 = values[1] * 10.0;
        x3 = values[2] * 10.0;
        x4 = values[3] * 10.0;

        fitness = 100.0 * (x1 * x1 - x2) * (x1 * x1 - x2) + (x1 - 1.0) * 
                (x1 - 1.0) + (x3 - 1.0) * (x3 - 1.0) + 90.0 * (x3 * x3 - x4) * 
                (x3 * x3 - x4) + 10.1 * ((x2 - 1.0) * (x2 - 1.0) + (x4 - 1.0) * 
                (x4 - 1.0)) + 19.8 * (x2 - 1.0) * (x4 - 1.0);


        return fitness;

    }

    @Override
    public void reset() {
    }

    @Override
    public double[][] getOptimum(int dim) {
        double[][] optimums = new double[1][];
        double[] optimum = new double[4];

        for (int i = 0; i < 4; i++) {
            optimum[i] = 1.0/10.0;
        }

        optimums[0] = optimum;
        return optimums;
    }

    @Override
    public int getDimension() {
        return 4;
    }
    
    
}
