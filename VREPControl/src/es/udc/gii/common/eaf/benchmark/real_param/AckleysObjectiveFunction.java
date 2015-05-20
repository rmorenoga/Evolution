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
 * SchwefelsProblemObjectiveFunction.java
 *
 * Created on 4 de julio de 2007, 19:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_param;

import es.udc.gii.common.eaf.benchmark.BenchmarkObjectiveFunction;

/**
 * El optimo de esta funcion objetivo es f(0,...,0) = 0.   
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class AckleysObjectiveFunction extends BenchmarkObjectiveFunction {

    /** Creates a new instance of SchwefelsProblemObjectiveFunction */
    public AckleysObjectiveFunction() {
    }

    @Override
    public double evaluate(double[] values) {

        double fitness = 0.0;
        double sum2, sumCos, value;
       
        
        sum2 = 0.0;
        sumCos = 0.0;
        for (int i = 0; i < values.length; i++) {

            value = this.desnormalize(values[i]);
            
            sum2 += value * value;
            sumCos += Math.cos(2.0 * Math.PI * value);

        }

        fitness = -20.0 * Math.exp(-0.2 * Math.sqrt((1.0 / values.length) * sum2)) -
                Math.exp((1.0 / values.length) * sumCos) + 20.0 + Math.E;


        return fitness;

    }

    private double desnormalize(double value) {
        
        double new_value;
        
        new_value = value*32.0;
        
        return new_value;
        
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
