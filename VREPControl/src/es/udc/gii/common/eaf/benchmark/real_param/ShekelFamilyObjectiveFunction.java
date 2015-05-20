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
import java.util.Arrays;
import org.apache.commons.configuration.Configuration;

/**
 * El optimo de esta funcion objetivo es f(0.201,0.150,0.477,0.275,0.311,0.657)
 * = -3.32.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class ShekelFamilyObjectiveFunction extends BenchmarkObjectiveFunction {
    
    private int m;
    
    private double[][] a = {{4.0, 4.0, 4.0, 4.0}, {1.0, 1.0, 1.0, 1.0}, {
            8.0, 8.0, 8.0, 8.0}, {6.0, 6.0, 6.0, 6.0}, {3.0, 7.0, 3.0, 7.0},{
                2.0, 9.0, 2.0, 9.0}, {5.0, 5.0, 3.0, 3.0}, {8.0, 1.0, 8.0, 1.0}, {
                    6.0, 2.0, 6.0, 2.0}, {7.0, 3.6, 7.0, 3.6}};
    private double[] c = {0.1, 0.2, 0.2, 0.4, 0.4, 0.6, 0.3, 0.7, 0.5, 0.5};
    
    /** Creates a new instance of SchwefelsProblemObjectiveFunction */
    public ShekelFamilyObjectiveFunction() {
        this.m = 10;
    }
    
    public ShekelFamilyObjectiveFunction(int m) {
        this.m = m;
    }    

    @Override
    public void configure(Configuration conf) {
        super.configure(conf);
        this.m = conf.getInt("M");
    }
            
    @Override
    public double evaluate(double[] values) {
        
        double fitness;
        
        fitness = this.shekel(values);
        
        return fitness - this.getOptimumValue();
        
    }
    
    private double shekel(double[] values) {
        
        double fitness = 0.0;
        double sum, x;
        
               
        
        
        for (int i = 0; i<this.m; i++) {
            
            sum = 0.0;
            for (int j = 0; j<values.length; j++) {
                
                //x ~ [0,10.0]
                x = values[j]*5.0 + 5.0;
                
                sum += (x - a[i][j])*(x - a[i][j]);
                
            }
            
            fitness += 1.0/(sum + c[i]);
            
        }
        
        fitness = -fitness;
        
        return fitness;
    }
    
    @Override
    public void reset() {
    }
    
    private double getOptimumValue() {
        
        double opt = Double.NaN;
        
        double[] optimum = new double[4];
        
        for (int i = 0; i<4; i++) {
            optimum[i] = 4.0/5.0 - 1.0;
        }
        
        opt = this.shekel(optimum);
        
        return opt;
    }
    
    @Override
    public double[][] getOptimum(int dim) {

        double[][] optimums = new double[1][];
        double[] optimum = new double[4];

        Arrays.fill(optimum, 4.0/5.0-1.0);

        optimums[0] = optimum;
        return optimums;

    }
    
    public void setM(int m) {
        this.m = m;
    }

    @Override
    public int getDimension() {
        return 4;
    }

    
    
}
