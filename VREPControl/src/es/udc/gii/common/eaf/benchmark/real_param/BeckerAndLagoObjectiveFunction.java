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
public class BeckerAndLagoObjectiveFunction extends BenchmarkObjectiveFunction {

    @Override
    public double[][] getOptimum(int dim) {
        double[][] optimums = new double[4][];
        double[] optimum = new double[2];

        optimum[0] = 0.5;
        optimum[1] = 0.5;
        optimums[0] = optimum;
        
        optimum = new double[2];
        optimum[0] = -0.5;
        optimum[1] = -0.5;
        optimums[1] = optimum;
        
        optimum = new double[2];
        optimum[0] = 0.5;
        optimum[1] = -0.5;
        optimums[2] = optimum;
        
        optimum = new double[2];
        optimum[0] = -0.5;
        optimum[1] = 0.5;
        optimums[3] = optimum;
        
        return optimums;

    }

    @Override
    public double evaluate(double[] values) {

        double fitness = Double.MAX_VALUE;
        double x1, x2;

        //x ~ [-10.0, 10.0]
        x1 = Math.abs(values[0]*10.0);
        x2 = Math.abs(values[1]*10.0);

        fitness = (x1 - 5.0)*(x1 - 5.0) + (x2 - 5.0)*(x2 - 5.0);
        
        return fitness;

    }

    @Override
    public void reset() {
    }

    @Override
    public int getDimension() {
        return 2;
    }
    
    
}
