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

/**
 * CEC 2005 F12: Schwefel’s Problem 2.13
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class F12 extends ShiftedObjectiveFunction {

    private double[] a;
    private double[][] A;
    private double[][] B;

    public F12(int dimension) {
        super(dimension);
    }

    public F12() {
    }

    @Override
    protected void initData(int dimension) {
        super.initData(dimension);

        a = new double[dimension];
        A = new double[dimension][dimension];
        B = new double[dimension][dimension];

        LoadFromFile.loadMatrixFromResourceFile(prefix + "f12_a.txt", dimension, dimension, A);
        LoadFromFile.loadMatrixFromResourceFile(prefix + "f12_b.txt", dimension, dimension, B);

        for (int r = 0; r < dimension; r++) {
            for (int c = 0; c < dimension; c++) {
                a[r] += A[r][c] * Math.sin(o[c]) + B[r][c] * Math.cos(o[c]);
            }
        }
    }

    @Override
    public final int getNumber() {
        return 12;
    }

    @Override
    public final double lowerBound() {
        return -Math.PI;
    }

    @Override
    public final double upperBound() {
        return Math.PI;
    }

    @Override
    public double doEvaluate(double[] values) {
        double bix = 0.0d;
        double sum = 0.0d;

        double[] z = denormalize(values);

        for (int r = 0; r < dimension; r++) {
            bix = 0.0d;
            for (int c = 0; c < dimension; c++) {
                bix += A[r][c] * Math.sin(z[c]) + B[r][c] * Math.cos(z[c]);
            }
            sum += (a[r] - bix) * (a[r] - bix);
        }

        return sum;
    }
}
