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
 * El optimo de esta funcion objetivo es f(0.114,0.556,0.852) = -3.86.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class HartmansFamily3ObjectiveFunction extends BenchmarkObjectiveFunction {

    /** Creates a new instance of SchwefelsProblemObjectiveFunction */
    public HartmansFamily3ObjectiveFunction() {
    }

    @Override
    public double evaluate(double[] values) {

        double fitness = 0.0;
        double sum, x;

        double[][] a = {{3.0, 10.0, 30.0}, {0.1, 10.0, 35.0}, {3.0, 10.0, 30.0},
            {0.1, 10.0, 35}
        };
        double[] c = {1.0, 1.2, 3.0, 3.2};
        double[][] p = {{0.3689, 0.1170, 0.2673}, {0.4699, 0.4387, 0.7470}, {
                0.1091, 0.8732, 0.5547
            }, {0.038150, 0.5743, 0.8828}};

        for (int i = 0; i < 4; i++) {

            sum = 0.0;
            for (int j = 0; j < 3; j++) {

                //x ~ [0,1]
                x = (values[j] + 1.0) / 2.0;

                sum += a[i][j] * Math.pow(x - p[i][j], 2.0);

            }

            fitness += c[i] * Math.exp(-sum);

        }

        fitness = -fitness;

        return fitness + 3.8627475058548155;

    }

    @Override
    public void reset() {
    }

    @Override
    public double[][] getOptimum(int dim) {

        double[][] optimums = new double[1][];
        double[] optimum = new double[3];

        optimum[0] = (0.114 * 2.0 - 1.0);
        optimum[1] = (0.556 * 2.0 - 1.0);
        optimum[2] = (0.852 * 2.0 - 1.0);

        optimums[0] = optimum;
        return optimums;

    }

    @Override
    public int getDimension() {
        return 3;
    }
    
    
}
