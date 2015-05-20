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
public class PermObjectiveFunction extends BenchmarkObjectiveFunction {

    @Override
    public double[][] getOptimum(int dim) {
        
        double[][] optimums = new double[1][];
        double[] optimum = new double[dim];
        
        for (int i = 0; i<dim; i++) {
            optimum[i] = (double)((1.0/(i+1))/(double)dim);
        }
        
        optimums[0] = optimum;
        return optimums;
        
    }

    @Override
    public double evaluate(double[] values) {
        
        double fitness = Double.MAX_VALUE;
        double b, s_out, s_in;
        int dim = values.length;
        double x;
        double pow_1, pow_2;
        
        b = 0.5;
        s_out = 0;
        
        //x ~ [-dim, dim]
        
        for (int i = 0; i<values.length; i++) {
            
            s_in = 0;
            
            for (int j = 0; j<values.length; j++) {
                x = values[j]*dim;
                
                //Es más rápido con el for que con el power:
                pow_1 = 1.0;
                pow_2 = 1.0;
                
                for (int k = 0; k<i+1; k++) {
                    pow_1 *= x;
                    pow_2 *= 1.0/(double)(j+1);
                }
                s_in += ((j+1)+b)*(pow_1-pow_2);
//                s_in += (Math.pow((double)j+1,(double)i+1)+b)*(Math.pow(x/(double)(j+1),(double)i+1)-1.0);
                //s_in += ((j+1)+b)*(Math.pow(x,(double)i+1)-Math.pow(1.0/(double)(j+1),(double)i+1));
            }
            s_out = s_out + s_in*s_in;
        }
        
        fitness = s_out;
        
        return fitness;
        
    }

    @Override
    public void reset() {}

}
