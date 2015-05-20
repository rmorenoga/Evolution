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
 * Base class for all composition functions for the CEC 2005 special session on
 * real-parameter optimization (functions 15-25).
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public abstract class CEC2005CompositionFunction extends CEC2005ObjectiveFunction {

    /** A constant. */
    protected static final double C = 2000;
    /** Number of base functions used to compose the global function. */
    protected static final int N = 10;
    /** Bias of each of the functions. */
    protected static final double[] BIAS = {
        0, 100, 200, 300, 400, 500, 600, 700, 800, 900
    };
    /** Parameters. */
    protected double[] sigma;
    protected double[] lambda;
    /** Shifted optimum in parameter space. */
    protected double[][] o;
    /** Vector for determining the scaling of the output of the base funcions. */
    protected double[] y;
    /** Transformation matrix of each base function. */
    protected double[][][] M;

    public CEC2005CompositionFunction() {
    }

    public CEC2005CompositionFunction(int dimension) {
        this.dimension = dimension;
        initData(dimension);
    }

    @Override
    protected void initData(int dimension) {
        this.dimension = dimension;

        this.o = new double[N][dimension];
        LoadFromFile.loadMatrixFromResourceFile(prefix + "f" + getNumber() +
                "_o.txt", N, dimension, o);

        this.y = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            this.y[i] = 5;
        }
    }

    /**
     * @param dim (irrelevant)
     * @return the optima of this function. The first row is the global optimum.
     */
    @Override
    public double[][] getOptimum(int dim) {
        return o;
    }

    /**
     * Transforms a denormalized vector shifting, scaling and rotating it.
     * @param x Denormalized vector.
     * @param o Shifted optimum in parameter space.
     * @param l Scaling factor.
     * @param m Transformation matrix which performs a rotation.
     * @return
     */
    protected final double[] transform(double[] x, double[] o, double l, double[][] m) {
        double[] z = new double[x.length];
        double[] z2 = new double[x.length];

        if (o == null) {
            for (int i = 0; i < x.length; i++) {
                z[i] = x[i] / l;
            }
        } else {
            for (int i = 0; i < x.length; i++) {
                z[i] = (x[i] - o[i]) / l;
            }
        }

        for (int c = 0; c < x.length; c++) {
            z2[c] = 0;
            for (int r = 0; r < x.length; r++) {
                z2[c] += z[r] * m[r][c];
            }
        }

        return z2;
    }

    /**
     * Computes the fitness of a denormalized vector.
     * @param z Denorlalized vector.
     * @return fitness.
     */
    protected abstract double[] calculateFit(double[] z);

    /**
     * Calculates the weights to ponder the output of each base function.
     * @param x Denormalized vector.
     * @return Array of weights to be aplied to the output of the base functions.
     */
    protected double[] calculateW(double[] x) {
        double[] w = new double[N];
        double sum = 0.0d;
        double maxW = -Double.MAX_VALUE;
        double SW = 0;

        for (int i = 0; i < N; i++) {
            sum = 0.0d;

            for (int k = 0; k < this.dimension; k++) {
                sum += (x[k] - o[i][k]) * (x[k] - o[i][k]);
            }

            w[i] = Math.exp(-sum / (2 * this.dimension * sigma[i] * sigma[i]));

            if (w[i] > maxW) {
                maxW = w[i];
            }
        }

        for (int i = 0; i < N; i++) {

            if (w[i] != maxW) {
                w[i] = w[i] * (1.0 - Math.pow(maxW, 10));
            }
            SW += w[i];
        }

        if (Math.abs(SW) < epsilon) {
            for (int i = 0; i < N; i++) {
                w[i] = 1.0 / N;
            }
        } else {
            for (int i = 0; i < N; i++) {
                w[i] /= SW;
            }
        }

        return w;
    }

    @Override
    public double doEvaluate(double[] values) {

        double[] z = denormalize(values);
        double[] w = calculateW(z);
        double[] fit = calculateFit(z);

        double res = 0.0;
        for (int i = 0; i < N; i++) {
            res += w[i] * (fit[i] + BIAS[i]);
        }

        return res;
    }

    @Override
    public void reset() {
    }
}
