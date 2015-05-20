/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.real_param.multimodal;

import es.udc.gii.common.eaf.benchmark.BenchmarkObjectiveFunction;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public class SixHumpCamelBackObjectiveFunction extends BenchmarkObjectiveFunction {


    @Override
    public double evaluate(double[] values) {

        double fitness = 0.0;
        double x0, x1;

        x0 = values[0];
        x1 = values[1];

        x0 = 1.9*x0;
        x1 = 1.1*x1;
        
        fitness = 4.0 * x0 * x0 - 2.1 * x0 * x0 * x0 * x0 + (1.0 / 3.0) * x0 * x0 * x0 * x0 * x0 * x0 +
                x0 * x1 - 4.0 * x1 * x1 + 4.0 * x1 * x1 * x1 * x1;
               
        return fitness + 1.0316284534898776;


    }

    @Override
    public void reset() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double[][] getOptimum(int dim) {

        double[][] optimum = new double[3][dim];
        double x_1, x_2;

        //Transformar a [-1.0, 1.0]
        x_1 = (-Math.PI - 2.5)/7.5;
        x_2 = 12.275/7.5 - 1.0;

        optimum[0] = new double[dim];
        optimum[0][0] = x_1;
        optimum[0][1] = x_2;

        //Transformar a [-1.0, 1.0]
        x_1 = (Math.PI - 2.5)/7.5;
        x_2 = 2.275/7.5 - 1.0;

        optimum[1] = new double[dim];
        optimum[1][0] = x_1;
        optimum[1][1] = x_2;

        //Transformar a [-1.0, 1.0]
        x_1 = (3.0*Math.PI - 2.5)/7.5;
        x_2 = 2.475/7.5 - 1.0;

        optimum[2] = new double[dim];
        optimum[2][0] = x_1;
        optimum[2][1] = x_2;

        return optimum;

    }

    @Override
    public int getDimension() {
        return 2;
    }

    



}
