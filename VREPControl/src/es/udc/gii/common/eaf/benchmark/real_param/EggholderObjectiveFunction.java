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
 * @author Grupo Integrado de Ingeniería (<a
 * href="http://www.gii.udc.es">www.gii.udc.es</a>)
 * @since 1.0
 */
public class EggholderObjectiveFunction extends BenchmarkObjectiveFunction {

    /**
     * Creates a new instance of SchwefelsProblemObjectiveFunction
     */
    public EggholderObjectiveFunction() {
    }

    @Override
    public double evaluate(double[] values) {

        double fitness = 0.0;

        for (int i = 0; i < values.length; i++) {
            values[i] = desnormalize(values[i]);
        }

        for (int i = 0; i < values.length - 1; i++) {
            fitness += (-(values[i + 1] + 47.0) * Math.sin(Math.sqrt(Math.abs(values[i + 1] + values[i] * 0.5 + 47.0)))
                    - values[i] * Math.sin(Math.sqrt(Math.abs(values[i] - (values[i + 1] + 47.0)))));
        }

        return fitness - (values.length == 2 ? -959.6406627106155 : -8247.22733990241795254);

    }

    private double desnormalize(double value) {

        double new_value;

        new_value = value * 512.0;

        return new_value;

    }

    @Override
    public void reset() {
    }

    @Override
    public double[][] getOptimum(int dim) {

        double[][] optimums = new double[1][];
//        double[] optimum = new double[dim];
//
//        for (int i = 0; i < dim; i++) {
//            optimum[i] = 0.0;
//        }
//
//        optimums[0] = optimum;
        return optimums;

    }

    public static void main(String[] args) {

        double value;
        //double[] x = {512.0/512.0, 404.2319/512.0};
        double[] x = {440 / 512.0, 455.0 / 512.0, 470.0 / 512.0, 426 / 512.0,
            441 / 512.0, 455 / 512.0, 471 / 512.0, 426 / 512.0, 442 / 512.0, 456 / 512.0};
        EggholderObjectiveFunction obj = new EggholderObjectiveFunction();

        value = obj.evaluate(x);

        System.out.println(value);
    }
}
