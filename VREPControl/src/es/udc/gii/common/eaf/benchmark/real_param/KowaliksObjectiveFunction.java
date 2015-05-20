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
 * El optimo de esta funcion objetivo es f(0.1928,0.1908,0.1231,0.1358) =aprox. 
 * 0.0003075
 * La dimensionalidad de esta funcion es 4.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class KowaliksObjectiveFunction extends BenchmarkObjectiveFunction {

    private double[] a = {0.1957, 0.1947, 0.1735, 0.1600, 0.0844, 0.0627,
        0.0456, 0.0342, 0.0323, 0.0235, 0.0246
    };
    private double[] b = {0.25, 0.5, 1.0, 2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 14.0, 16.0};

    /** Creates a new instance of SchwefelsProblemObjectiveFunction */
    public KowaliksObjectiveFunction() {
    }

    @Override
    public double evaluate(double[] values) {

        double fitness = 0.0;
        double b1;
        double x1, x2, x3, x4;
        double val;

        x1 = values[0] * 5.0;
        x2 = values[1] * 5.0;
        x3 = values[2] * 5.0;
        x4 = values[3] * 5.0;

        for (int i = 0; i < 11; i++) {

            b1 = 1.0 / b[i];
            //b1 = b[i];
            //x ~ [-5.0, 5.0]
            val = a[i] - ((x1 * b1 * ( b1 + x2)) / (b1 * b1 + b1 * x3 + x4));
            fitness += val*val;
            //fitness += Math.pow(a[i] - ((x1 * b1 * ( b1 + x2)) / (b1 * b1 + b1 * x3 + x4)), 2.0);
        }

        return fitness - 3.10018265474201E-4;

    }

    @Override
    public void reset() {
    }

    @Override
    public double[][] getOptimum(int dim) {

        double[][] optimums = new double[1][];
        double[] optimum = new double[4];

        optimum[0] = 0.192/5.0;
        optimum[1] = 0.190/5.0;
        optimum[2] = 0.123/5.0;
        optimum[3] = 0.135/5.0;

        optimums[0] = optimum;
        return optimums;

    }

    @Override
    public int getDimension() {
        return 4;
    }
}
