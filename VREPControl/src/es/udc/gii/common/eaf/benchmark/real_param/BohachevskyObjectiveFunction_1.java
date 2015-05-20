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
public class BohachevskyObjectiveFunction_1 extends BenchmarkObjectiveFunction {

    @Override
    public double[][] getOptimum(int dim) {
        double[][] optimums = new double[1][];
        double[] optimum = new double[2];

        optimum[0] = 0.0;
        optimum[1] = 0.0;
        optimums[0] = optimum;
        
        return optimums;

    }

    @Override
    public double evaluate(double[] values) {

        double fitness = Double.MAX_VALUE;
        double x1, x2;

        //x ~ [-50.0, 50.0]
        x1 = values[0]*50.0;
        x2 = values[1]*50.0;

        fitness = x1*x1 + 2*x2*x2 - 0.3*Math.cos(3.0*Math.PI*x1
                ) - 0.4*Math.cos(4.0*Math.PI*x2) + 0.7; 
        
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
