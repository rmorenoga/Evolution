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
public class DekersAndAartsObjectiveFunction extends BenchmarkObjectiveFunction {

    @Override
    public double[][] getOptimum(int dim) {
        double[][] optimums = new double[2][];
        double[] optimum = new double[2];

        optimum[0] = 0.0;
        optimum[1] = 15.0/20.0;
        optimums[0] = optimum;
        
        optimum = new double[2];
        optimum[0] = 0.0;
        optimum[1] = -15.0/20.0;
        optimums[1] = optimum;
        
        return optimums;

    }

    @Override
    public double evaluate(double[] values) {

        double fitness = Double.MAX_VALUE;
        double x1, x2;

        //x ~ [-20.0, 20.0]
        x1 = values[0]*20.0;
        x2 = values[1]*20.0;

        fitness = 1.0E+5*x1*x1 + x2*x2 - (x1*x1 + x2*x2)*(x1*x1 + x2*x2) + 
                1.0e-5*Math.pow((x1*x1 + x2*x2),4.0);
        
        return Math.abs(fitness + 24771.093749999996);

    }

    @Override
    public void reset() {
    }

    @Override
    public int getDimension() {
        return 2;
    }
    
    
}
