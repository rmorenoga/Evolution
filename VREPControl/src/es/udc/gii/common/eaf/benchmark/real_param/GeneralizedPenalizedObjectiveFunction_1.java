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

import java.util.Arrays;


/**
 * El optimo de esta funcion objetivo es f(1,...,1) = 0.   
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class GeneralizedPenalizedObjectiveFunction_1 
        extends GeneralizedPenalizedObjectiveFunction {
    
    /** Creates a new instance of SchwefelsProblemObjectiveFunction */
    public GeneralizedPenalizedObjectiveFunction_1() {
    }
    
    @Override
    public double evaluate(double[] values) {
        
        double fitness = 0.0;
        double sumU, sumY, aux1, aux2, x, x_1;
        
        sumU = 0.0;
        sumY = 0.0;
        for (int i = 0; i<values.length; i++) {
            
            //x ~[-50, 50]
            x = values[i]*50.0;
            sumU += this.u(x,10.0,100.0,4.0);
        }
        
        for (int i = 1; i<values.length; i++) {
            
            //x~[-50.0, 50.0]
            x_1 = values[i-1]*50.0;
            x = values[i]*50.0;
            
            aux1 = Math.pow(this.y(x_1)-1.0,2.0);
            aux2 = (1.0 + 10.0*Math.pow(
                    Math.sin(Math.PI*this.y(x)),2.0));
            
            sumY += aux1*aux2;
        }
        
        x = values[0]*50.0;
        x_1 = values[values.length-1]*50.0;
        
        fitness = (Math.PI/values.length)*(10.0*Math.pow(Math.sin(
                Math.PI*this.y(x)),2.0) + sumY + 
                Math.pow(this.y(x_1) - 1.0,2.0)) + sumU;
        
        return fitness;
        
    }
    
    @Override
    public void reset() {
    }

    
    @Override
    public double[][] getOptimum(int dim) {

        double[][] optimums = new double[1][];
        double[] optimum = new double[dim];

        Arrays.fill(optimum, -1.0/50.0);

        optimums[0] = optimum;
        return optimums;

    }
    
}
