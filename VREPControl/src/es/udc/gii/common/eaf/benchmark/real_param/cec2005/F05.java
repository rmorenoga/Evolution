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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_param.cec2005;

import es.udc.gii.common.eaf.benchmark.LoadFromFile;
import java.util.Arrays;

/**
 * CEC 2005 F5: Schwefel’s Problem 2.6 with Global Optimum on Bounds
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class F05 extends ShiftedObjectiveFunction {

    protected double[][] A;
    protected double[] B;

    public F05(int dimension) {
        super(dimension);
    }

    public F05() {
    }

    @Override
    protected void initData(int dimension) {
        super.initData(dimension);
        A = new double[dimension][dimension];
        B = new double[dimension];
        
        LoadFromFile.loadMatrixFromResourceFile(prefix + "f5_m100.txt",
                dimension, dimension, A);

        int d4 = dimension / 4 + (dimension % 4 > 0 ? 1 : 0);
        int d34 = 3 * dimension / 4;

        for (int i = 0; i < d4; i++) {
            o[i] = -100;
        }

        for (int i = d34 - 1; i < dimension; i++) {
            o[i] = 100;
        }

        for (int r = 0; r < dimension; r++) {
            for (int c = 0; c < dimension; c++) {
                B[r] += A[r][c] * o[c];
            }
        }
    }

    @Override
    public final int getNumber() {
        return 5;
    }

    @Override
    public final double lowerBound() {
        return -100;
    }

    @Override
    public final double upperBound() {
        return 100;
    }

    private final double[] getAxMinusB(double[] values) {
        double[] AxMinusB = new double[values.length];
        for (int r = 0; r < values.length; r++) {
            for (int c = 0; c < values.length; c++) {
                AxMinusB[r] += A[r][c] * values[c];
            }
            AxMinusB[r] = Math.abs(AxMinusB[r] - B[r]);
        }
        return AxMinusB;
    }

    @Override
    public double doEvaluate(double[] values) {
        double[] AxMinusB = getAxMinusB(denormalize(values));
        Arrays.sort(AxMinusB);
        return AxMinusB[values.length - 1];
    }
}
