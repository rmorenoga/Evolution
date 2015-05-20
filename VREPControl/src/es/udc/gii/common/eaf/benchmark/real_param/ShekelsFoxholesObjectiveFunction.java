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

/**
 * El optimo de esta funcion objetivo es f(-32,-32) =aprox. 1.0
 * La dimensionalidad de esta funcion es 2.
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class ShekelsFoxholesObjectiveFunction extends BenchmarkObjectiveFunction {

    /** Creates a new instance of SchwefelsProblemObjectiveFunction */
    public ShekelsFoxholesObjectiveFunction() {
    }

    @Override
    public double evaluate(double[] values) {

        double fitness = 0.0;

        fitness = this.shekelFoxholes(values);

        return fitness - 0.9980038388186492;

    }

    private double shekelFoxholes(double[] values) {

        double fitness = 0.0;
        double sum2, sum25;
        double a;

        sum25 = 0.0;
        for (int j = 0; j < (int)Math.pow(5, values.length); j++) {

            sum2 = 0;

            for (int i = 0; i < values.length; i++) {
                a = this.generateA(values.length, i, j);
                //x ~ [-65.536, 65.536]
                sum2 += Math.pow((values[i]*65.536 - a), 6.0);
            }

            sum25 += 1.0 / ((j + 1) + sum2);

        }

        fitness = 1.0 / ((1.0 / 500.0) + sum25);

        return fitness;


    }

    private double generateA(int D, int i, int j) {

        double[] init_a = {-32.0, -16.0, 0.0, 16.0, 32.0};
        double a;

        int rows = D;
        int cols = (int) (Math.pow(init_a.length, D));
        int num;


        if (D == 1) {

//            a.add(new ArrayList<Double>());
//            
//            for (int i = 0; i<init_a.length; i++) {
//                a.get(0).add(new Double(init_a[i]));
//            }
            a = init_a[j];

        } else {

//            //Tengo que rellenar todas las filas de la matriz a:
//            for (int i = 0; i < D; i++) {
//
//                //columnas:
//                for (int j = 0; j < cols; j++) {
//
//                    a.add(new ArrayList<Double>());
            //Hay que calcular el indice de a_init que le toca:
            //num = a[i].length/(int)Math.pow(init_a.length, D-i);
            //num = (int)Math.pow(init_a.length,(i+1));
            num = cols / (int) Math.pow(init_a.length, (D - i - 1));
            if (i == 0) //System.out.println(i + " " + j + " " + num + " "+  (j%num));
            {
                //a.get(i).add(new Double(init_a[j % num]));
                //a[i][j] = init_a[j % num];
                a = init_a[j % num];
            } else {
                num /= init_a.length;
                //System.out.println(i + " " + j + " " + num + " "+  ((j/num)%init_a.length));
                //a[i][j] = init_a[(j / num) % init_a.length];
                //a.get(i).add(new Double(init_a[(j / num) % init_a.length]));
                a = init_a[(j / num) % init_a.length];
            }
        }

        //}

        //}

        return a;


    }

    @Override
    public void reset() {
    }

    @Override
    public double[][] getOptimum(int dim) {

        double[][] optimums = new double[1][];
        double[] optimum = new double[2];

        Arrays.fill(optimum, -32.0/65.536);

        optimums[0] = optimum;
        return optimums;

    }

    @Override
    public int getDimension() {
        return 2;
    }
    
    
}
