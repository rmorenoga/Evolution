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
 * El optimo de esta funcion objetivo es f(0.08993,-0.7126) = f(-0.08983,
 * 0.7126) = -1.0316285.
 * Es una función multimodal de dimensión 2.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class SixHumpCamelBackObjectiveFunction extends BenchmarkObjectiveFunction {

    /** Creates a new instance of SchwefelsProblemObjectiveFunction */
    public SixHumpCamelBackObjectiveFunction() {
    }

    @Override
    public double evaluate(double[] values) {

        double fitness = 0.0;
        double x0, x1;

        //x0, x1 ~ [-5.0, 5.0]
        x0 = values[0] * 5.0;
        x1 = values[1] * 5.0;

        fitness = 4.0 * x0 * x0 - 2.1 * x0 * x0 * x0 * x0 + (1.0 / 3.0) * x0 * x0 * x0 * x0 * x0 * x0 +
                x0 * x1 - 4.0 * x1 * x1 + 4.0 * x1 * x1 * x1 * x1;


        return fitness + 1.0316284275548802;

    }

    @Override
    public void reset() {
    }

    @Override
    public double[][] getOptimum(int dim) {

        double[][] optimums = new double[2][];
        double[] optimum = new double[2];

        optimum[0] = (0.08983 / 5.0);
        optimum[1] = (-0.7126 / 5.0);
        optimums[0] = (optimum);

        optimum = new double[2];
        optimum[0] =(-0.08983 / 5.0);
        optimum[1] = (0.7126 / 5.0);
        optimums[1] = (optimum);

        return optimums;

    }

    @Override
    public int getDimension() {
        return 2;
    }
    
    
}
