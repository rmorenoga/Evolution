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
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class WeierstrassObjectiveFunction extends BenchmarkObjectiveFunction {

    private double a = 0.5;
    
    private double b = 3.0;
    
    private int kMax = 20;
    
    /** Creates a new instance of SchwefelsProblemObjectiveFunction */
    public WeierstrassObjectiveFunction() {
    }

    @Override
    public double evaluate(double[] values) {

        double fitness = 0.0;
        
        for (int i = 0; i < values.length; i++) {
            values[i] = desnormalize(values[i]);
        }
       
        double first_term, snd_term, ak, bk;
        snd_term = 0;
        for (int i = 0; i < values.length; i++) {
            first_term = 0;
            snd_term = 0;
            for (int k = 0; k < kMax; k++) {
                ak = Math.pow(this.a, k);
                bk = Math.pow(this.b, k);
                first_term += ak*Math.cos(2.0*Math.PI*bk*(values[i] + 0.5));
                snd_term += ak*Math.cos(2.0*Math.PI*bk*0.5);
            }
            
            fitness += first_term;
            
        }

        fitness -= values.length*snd_term;
        
        return fitness;

    }

    private double desnormalize(double value) {
        
        double new_value;
        
        new_value = value*0.5;
        
        return new_value;
        
    }
    
    
    @Override
    public void reset() {
    }


    @Override
    public double[][] getOptimum(int dim) {

        double[][] optimums = new double[1][];
        double[] optimum = new double[dim];

        for (int i = 0; i < dim; i++) {
            optimum[i] = 0.0;
        }

        optimums[0] = optimum;
        return optimums;

    }
    
    public static void main(String[] args) {
        
        double value;
        double[] x = {0.0/0.5, 0.0/0.5, 0.0/0.5, 0.0/0.5, 0.0/0.5, 0.0/0.5, 0.0/0.5, 0.0/0.5, 0.0/0.5, 0.0/0.5};
        WeierstrassObjectiveFunction obj = new WeierstrassObjectiveFunction();
        
        value = obj.evaluate(x);
        
        System.out.println(value);
    }
}
