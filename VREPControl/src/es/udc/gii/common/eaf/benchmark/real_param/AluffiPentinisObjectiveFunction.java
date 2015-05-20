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
public class AluffiPentinisObjectiveFunction extends BenchmarkObjectiveFunction {

    @Override
    public double evaluate(double[] values) {

        double fitness = Double.MAX_VALUE;
        double x1, x2;

        //x ~[-10.0, 10.0]
        x1 = values[0] * 10.0;
        x2 = values[1] * 10.0;

        fitness = 0.25 * x1 * x1 * x1 * x1 - 0.5 * x1 * x1 + 0.1 * x1 + 0.5 * x2 * x2;

        return fitness + 0.3523860365437344;

    }

    @Override
    public void reset() {
    }

    @Override
    public double[][] getOptimum(int dim) {
        double[][] optimums = new double[1][];
        double[] optimum = new double[2];

        optimum[0] = -0.10465;
        optimum[1] = 0.0;

        optimums[0] = optimum;
        return optimums;
    }

    @Override
    public int getDimension() {
        return 2;
    }
    
    
}
