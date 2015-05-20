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
 * El optimo de esta funcion objetivo es f(0.201,0.150,0.477,0.275,0.311,0.657)
 * = -3.32.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class HartmansFamily6ObjectiveFunction extends BenchmarkObjectiveFunction {

    /** Creates a new instance of SchwefelsProblemObjectiveFunction */
    public HartmansFamily6ObjectiveFunction() {
    }

    @Override
    public double evaluate(double[] values) {

        double fitness = 0.0;
        double sum, x;

        double[][] a = {{10.0, 3.0, 17.0, 3.5, 1.7, 8.0}, {
                0.05, 10.0, 17.0, 0.1, 8.0, 14.9
            }, {3.0, 3.5, 1.7, 10.0, 17.0, 8.0},
            {17.0, 8.0, 0.05, 10.0, 0.1, 14.0}
        };
        double[] c = {1.0, 1.2, 3.0, 3.2};
        double[][] p = {{0.1312, 0.1696, 0.5569, 0.0124, 0.8283, 0.5886},
            {0.2329, 0.1435, 0.8307, 0.3736, 0.1004, 0.9991}, {
                0.2348, 0.1415, 0.3522, 0.2883, 0.3047, 0.6650
            }, {
                0.4047, 0.8828, 0.8732, 0.5743, 0.1091, 0.0381
            }
        };



        for (int i = 0; i < 4; i++) {


            sum = 0.0;
            for (int j = 0; j < values.length; j++) {

                //x ~ [0,1]
                x = (values[j] + 1.0) / 2.0;

                sum += a[i][j] * Math.pow(x - p[i][j], 2.0);

            }

            fitness += c[i] * Math.exp(-sum);

        }

        fitness = -fitness;

        return fitness + 3.329666901993764;

    }

    @Override
    public void reset() {
    }

    @Override
    public double[][] getOptimum(int dim) {

        double[][] optimums = new double[1][];
        double[] optimum = new double[6];

        optimum[0] = (0.20169 * 2.0 - 1.0);
        optimum[1] = (0.150011 * 2.0 - 1.0);
        optimum[2] = (0.476874 * 2.0 - 1.0);
        optimum[3] = (0.275332 * 2.0 - 1.0);
        optimum[4] = (0.311652 * 2.0 - 1.0);
        optimum[5] = (0.6573 * 2.0 - 1.0);

        optimums[0] = optimum;
        return optimums;

    }

    @Override
    public int getDimension() {
        return 6;
    }
    
    
}
