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
public class BraninObjectiveFunction extends BenchmarkObjectiveFunction {


    private double a = 1.0;

    private double b = 5.1/(4.0*Math.PI*Math.PI);

    private double c = 5.0/Math.PI;

    private double d = 6.0;

    private double g = 10.0;

    private double h = 1.0/(8.0*Math.PI);

    @Override
    public double evaluate(double[] values) {

        double fitness = 0.0;
        double x_1, x_2;

        x_1 = values[0];
        x_2 = values[1];

        x_1 = 7.5*x_1 + 2.5;
        x_2 = (x_2 + 1.0)*7.5;


        fitness = a*Math.pow((x_2 - b*x_1*x_1 + c*x_1 - d), 2.0) + g*(1.0 - h)*Math.cos(x_1) + g;
        

        return Math.abs(fitness - 5.0 / (4.0 * Math.PI));


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
