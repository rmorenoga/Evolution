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
import java.util.ArrayList;
import java.util.List;

/**
 * El optimo de esta funcion objetivo es f(0,-1) = 3.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class GoldsteinPriceObjectiveFunction extends BenchmarkObjectiveFunction {

    /** Creates a new instance of SchwefelsProblemObjectiveFunction */
    public GoldsteinPriceObjectiveFunction() {
    }

    @Override
    public double evaluate(double[] values) {

        double fitness = 0.0;
        double x1, x2;

        //x1, x2 ~ [-2.0, 2.0]
        x1 = values[0] * 2.0;
        x2 = values[1] * 2.0;

        fitness = (1.0 + Math.pow(x1 + x2 + 1.0, 2.0) * (19.0 - 14.0 * x1 +
                3.0 * x1 * x1 - 14.0 * x2 + 6.0 * x1 * x2 + 3.0 * x2 * x2)) * (30.0 + Math.pow(2.0 * x1 - 3.0 * x2, 2.0) * (18.0 - 32.0 * x1 + 12.0 * x1 * x1 + 48.0 * x2 - 36.0 * x1 * x2 + 27.0 * x2 * x2));


        return fitness - 3.0;

    }

    @Override
    public void reset() {
    }

    @Override
    public double[][] getOptimum(int dim) {

        double[][] optimums = new double[1][];
        double[] optimum = new double[2];

        optimum[0] = 0.0;
        optimum[1] = -0.5;

        optimums[0] = optimum;
        return optimums;

    }

    @Override
    public int getDimension() {
        return 2;
    }

    
    
}
