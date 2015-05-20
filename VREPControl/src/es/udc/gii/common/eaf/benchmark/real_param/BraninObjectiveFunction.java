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
 * El optimo de esta funcion objetivo es f(-3.142,12.275) = f(3.142,2.275) =
 * f(9.425,2,425) = 0.398.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class BraninObjectiveFunction extends BenchmarkObjectiveFunction {
    
    /** Creates a new instance of SchwefelsProblemObjectiveFunction */
    public BraninObjectiveFunction() {
    }
    
    @Override
    public double evaluate(double[] values) {
        
        double fitness = 0.0;
        double x0, x1;
        
        //x0 ~ [-5, 10]
        x0 = values[0]*7.5 + 2.5;
        //x1 ~ [0, 15]
        x1 = values[1]*7.5 + 7.5;
        
        fitness = Math.pow(x1 - (5.1/(4.0*Math.PI*Math.PI))*x0*x0+
                (5.0/Math.PI)*x0-6.0,2.0) +
                10.0*(1.0 - (1.0/(8.0*Math.PI)))*Math.cos(x0) + 10.0;
        
        return fitness  - 0.39788735772973816;
        
    }
    
    @Override
    public void reset() {
    }
    
    @Override
    public double[][] getOptimum(int dim) {
        
        double[][] optimums = new double[3][];
        double[] optimum = new double[2];
        
        optimum[0] = (3.142 - 2.5)/7.5;
        optimum[1] = (2.275 - 7.5)/7.5;
        optimums[0] = optimum;
        
        optimum = new double[2];
        optimum[0] = (-3.142 - 2.5)/7.5;
        optimum[1] = (12.275 - 7.5)/7.5;
        optimums[1] = optimum;
        
        optimum = new double[2];
        optimum[0] = (9.425 - 2.5)/7.5;
        optimum[1] = (2.425 - 7.5)/7.5;
        optimums[2] = optimum;
        
        
        return optimums;
        
    }

    @Override
    public int getDimension() {
        return 2;
    }
    
}
