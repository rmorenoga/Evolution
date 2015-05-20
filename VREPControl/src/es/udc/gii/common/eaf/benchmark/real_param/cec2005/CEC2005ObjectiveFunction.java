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

import es.udc.gii.common.eaf.benchmark.BenchmarkObjectiveFunction;
import es.udc.gii.common.eaf.benchmark.LoadFromFile;
import org.apache.commons.configuration.Configuration;

/**
 * Base class for all functions defined for the CEC 2005 special session on real
 * parameter optimization.
 * 
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public abstract class CEC2005ObjectiveFunction extends BenchmarkObjectiveFunction {

    /**
     * Prefix of the path where the data files are stored.
     */
    public final static String prefix = "/cec2005data/";
    /** All the 25 function's biases. */
    private static final double[] bias;
    /** This function's bias. */
    protected final double fbias;
    /** This functions' dimension. */
    protected int dimension;
    /** Some functions introduce some noise. Having NOISE = 0.0 disables the
     * introduction of this noise (for example for testing the fuctions).
     * NOISE = 1 enables the introduction of noise.
     */
    protected double NOISE = 1.0;
    /** A bias is added to every function. If BIAS_FITNESS = 0.0, no bias is 
     * added. */
    protected double BIAS_FITNESS = 1.0;
    /** Absolute error. */
    public static final double epsilon = 1e-6;
    

    static {
        /* Load all bias of the functions. */
        bias = new double[25];
        LoadFromFile.loadRowVectorFromResourceFile(prefix + "fbias.txt", 25, bias);
    }

    public CEC2005ObjectiveFunction() {
        this.fbias = bias[getNumber() - 1];
    }

    public CEC2005ObjectiveFunction(int dimension) {
        this.fbias = bias[getNumber() - 1];
        this.dimension = dimension;
        initData(dimension);
    }

    /**
     * Disables the introduction of noise if this function does introduce some.
     */
    public void disableNoise() {
        NOISE = 0.0;
    }

    /**
     * Enables the introduction of noise if this function does introduce some.
     */
    public void enableNoise() {
        NOISE = 1.0;
    }

    /**
     * Tests if noise is enebled.
     */
    public boolean isNoiseEnabled() {
        return NOISE == 1.0;
    }

    /**
     * Disables the addition of the bias to the final result of this function.
     */
    public void disableBias() {
        BIAS_FITNESS = 0.0;
    }

    /**
     * Enables the addition of the bias to the final result of this function.
     */
    public void enableBias() {
        BIAS_FITNESS = 1.0;
    }

    /**
     * Tests if the addiotion of the bias is enabled.
     */
    public boolean isBiasEnabled() {
        return BIAS_FITNESS == 1.0;
    }

    @Override
    public void configure(Configuration conf) {
        super.configure(conf);

        /* Dimension of the parameter space. */
        this.dimension = conf.getInt("Dimension");

        initData(this.dimension);
    }

    /**
     * Initializes the data needed for this function.
     * @param dimension Dimension of the function (2 &lt;= dimension &lt;= 50).
     */
    protected abstract void initData(int dimension);

    /**
     * Returns the number of this function as defined in the CEC 2005.
     * @return function number
     */
    public abstract int getNumber();

    /**
     * @return The lower bound for the coordinates of the vectors passed to this function.
     */
    public abstract double lowerBound();

    /**
     * @return The upper bound for the coordinates of the vectors passed to this function.
     */
    public abstract double upperBound();

    /**
     * @return The defined bias for this function.
     */
    public final double getBias() {
        return fbias;
    }

    /**
     * @return The dimension of this function.
     */
    @Override
    public int getDimension() {
        return this.dimension;
    }

    /**
     * Denormalizes a value.
     * @param value
     * @return Denormalized value.
     */
    public final double denormalize(double value) {
        return (upperBound() - lowerBound()) * (value + 1) / 2 + lowerBound();
    }

    /**
     * Denormalizes a vector.
     * @param values
     * @return Denormalized vector.
     */
    public final double[] denormalize(double[] values) {
        double[] z = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            z[i] = denormalize(values[i]);
        }
        return z;
    }

    /**
     * Returns the bias of a function given by its function number.
     * @param f Function number
     * @return The bias of the function associated with number {@code f}.
     */
    public final static double getBias(int f) {
        return bias[f - 1];
    }

    /**
     * Rounds a value to a integer value. The rouding is performed as defined by
     * the CEC 2005 technical report.
     * @param x
     * @return {@code x} rounded to an integer value.
     */
    public final double round(double x) {
        return Math.floor(x + 0.5);
    }

    /**
     * @return Returns {@code false} iif the vectors in parameter space can skip
     * from the initialization region. Returns {@code true} otherwise.
     */
    public boolean checkBounds() {
        return true;
    }
    
    /**
     * Especific evaluation of this function.
     */
    protected abstract double doEvaluate(double[] values);

    /**
     * Evaluates this function.
     * @param values Normalized vector in parameter space.
     * @return Fitness value associated with the given vector for this function.
     */
    @Override
    public final double evaluate(double[] values) {
        return doEvaluate(values) + BIAS_FITNESS * fbias;
    }

    /*------ Common functions ------*/
    public double rastrigins(double[] x) {
        double sum = 0.0d;
        for (int i = 0; i < x.length; i++) {
            sum += x[i] * x[i] - 10.0 * Math.cos(2 * Math.PI * x[i]) + 10.0;
        }
        return sum;
    }

    public double weierstrass(double[] x, final double a, final double b, final int kMax) {
        double sum = 0.0d;
        double sum2 = 0.0d;
        double sum3 = 0.0d;

        for (int i = 0; i < x.length; i++) {
            sum2 = 0.0d;
            for (int k = 0; k <= kMax; k++) {
                sum2 += Math.pow(a, k) * Math.cos(2 * Math.PI * Math.pow(b, k) * (x[i] + 0.5));
            }
            sum += sum2;
        }

        for (int k = 0; k <= kMax; k++) {
            sum3 += Math.pow(a, k) * Math.cos(2 * Math.PI * Math.pow(b, k) * 0.5);
        }

        return sum - x.length * sum3;
    }

    public double griewank(double[] x) {
        double sum = 0.0d;
        double prod = 1.0d;

        for (int i = 0; i < x.length; i++) {
            sum += x[i] * x[i];
            prod *= Math.cos(x[i] / Math.sqrt(i + 1.0));
        }
        return 1.0 + sum / 4000 - prod;
    }

    public double ackley(double[] x) {

        double s1 = 0.0d;
        double s2 = 0.0d;

        for (int i = 0; i < x.length; i++) {
            s1 += x[i] * x[i];
            s2 += Math.cos(2 * Math.PI * x[i]);
        }

        return -20 * Math.exp(-0.2 * Math.sqrt(1.0 / x.length * s1)) -
                Math.exp(1.0 / x.length * s2) + 20 + Math.E;
    }

    public double sphere(double[] x) {
        double sum = 0.0d;
        for (int i = 0; i < x.length; i++) {
            sum += x[i] * x[i];
        }
        return sum;
    }

    public double F6(double[] x) {
        double temp1, temp2, res = 0;

        for (int i = 0; i < x.length - 1; i++) {
            temp1 = Math.pow((Math.sin(Math.sqrt(Math.pow(x[i], 2) + Math.pow(x[i + 1], 2)))), 2);
            temp2 = 1.0 + 0.001 * (Math.pow(x[i], 2) + Math.pow(x[i + 1], 2));
            res += 0.5 + (temp1 - 0.5) / (Math.pow(temp2, 2));
        }

        temp1 = Math.pow((Math.sin(Math.sqrt(Math.pow(x[x.length - 1], 2) + Math.pow(x[0], 2)))), 2);
        temp2 = 1.0 + 0.001 * (Math.pow(x[x.length - 1], 2) + Math.pow(x[0], 2));
        res += 0.5 + (temp1 - 0.5) / (Math.pow(temp2, 2));

        return res;
    }

    public double F8F2(double[] x) {
        double temp = 0.0d;
        double res = 0.0d;
        double a, b;
        for (int i = 0; i < x.length - 1; i++) {
            a = x[i] * x[i] - x[i + 1];
            b = x[i] - 1.0;
            temp = 100 * a * a + b * b;
            res += (temp * temp) / 4000 - Math.cos(temp) + 1.0;
        }
        a = x[x.length - 1] * x[x.length - 1] - x[0];
        b = x[x.length - 1] - 1.0;
        temp = 100 * a * a + b * b;
        res += (temp * temp) / 4000 - Math.cos(temp) + 1.0;

        return res;
    }

    public double elliptic(double[] x) {
        double sum = 0.0d;
        for (int i = 0; i < x.length; i++) {
            sum += x[i] * x[i] * Math.pow(1e6, i / (x.length - 1.0));
        }
        return sum;
    }

    public double schwefel_1_2(double[] x) {
        double sum = 0.0d;
        double sum2 = 0.0d;

        for (int d = 0; d < x.length; d++) {
            sum2 = 0.0d;
            for (int i = 0; i <= d; i++) {
                sum2 += x[i];
            }
            sum += sum2 * sum2;
        }

        return sum;
    }

    public double rosenbrock(double[] x) {
        double sum = 0.0d;
        for (int i = 0; i < x.length - 1; i++) {
            sum += 100 * (x[i] * x[i] - x[i + 1]) * (x[i] * x[i] - x[i + 1]) + (x[i] - 1) * (x[i] - 1);
        }
        return sum;
    }
}
