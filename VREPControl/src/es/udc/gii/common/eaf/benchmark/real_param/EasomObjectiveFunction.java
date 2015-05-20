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
public class EasomObjectiveFunction extends BenchmarkObjectiveFunction {

    @Override
    public double evaluate(double[] values) {

        double fitness = Double.MAX_VALUE;
        double x1, x2;

        //x ~ [-100.0, 100.0]
        x1 = values[0] * 100.0;
        x2 = values[1] * 100.0;

        fitness = -Math.cos(x1) * Math.cos(x2) *
                Math.exp(-((x1 - Math.PI) * (x1 - Math.PI)) - (x2 - Math.PI) * (x2 - Math.PI));


        return fitness + 1.0;

    }

    @Override
    public void reset() {
    }

    @Override
    public double[][] getOptimum(int dim) {
        double[][] optimums = new double[1][];
        double[] optimum = new double[2];

        for (int i = 0; i < 2; i++) {
            optimum[i] = Math.PI/100.0;
        }

        optimums[0] = optimum;
        return optimums;
    }

    @Override
    public int getDimension() {
        return 2;
    }
    
    
}
