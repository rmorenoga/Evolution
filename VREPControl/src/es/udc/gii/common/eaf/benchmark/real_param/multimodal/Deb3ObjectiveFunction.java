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
public class Deb3ObjectiveFunction extends BenchmarkObjectiveFunction {


    @Override
    public double evaluate(double[] values) {

        double fitness = 0.0;
        double[] norm_values = new double[values.length];
        
        for (int i = 0; i < values.length; i++) {
            norm_values[i] = ((values[i] + 1.0)/2.0);
        }
        
        double sum = 0.0;
        
        for (int i = 0; i < norm_values.length; i++) {
            sum += Math.pow(Math.sin(5.0*Math.PI*(Math.pow(norm_values[i], 3.0/4.0) - 0.05)), 6.0);
        }
        
        fitness = -(1.0/values.length)*sum + 0.9999999999990554;
               
        return fitness;


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
