/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.real_param.soco;

import es.udc.gii.common.eaf.benchmark.BenchmarkObjectiveFunction;
import es.udc.gii.common.eaf.benchmark.LoadFromFile;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.math.util.DoubleArray;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="www.gii.udc.es">www.gii.udc.es</a>)
 */
public abstract class SOCOObjectiveFunction extends BenchmarkObjectiveFunction {

    /**
     * Prefix of the path where the data files are stored.
     */
    public final static String prefix = "/socodata/";
    /** All the 25 function's biases. */
    private static final double[] bias;
    /** This function's bias. */
    protected final double fbias;
    /** This functions' dimension. */
    protected int dimension;
    /** A bias is added to every function. If BIAS_FITNESS = 0.0, no bias is 
     * added. */
    protected double BIAS_FITNESS = 0.0;
    /** Absolute error. */
    public static final double epsilon = 1e-6;

    public SOCOObjectiveFunction() {
        this.fbias = bias[getNumber() - 1];
    }

    static {
        /* Load all bias of the functions. */
        bias = new double[19];
        LoadFromFile.loadRowVectorFromResourceFile(prefix + "fbias.txt", 19, bias);
    }

    public SOCOObjectiveFunction(int dimension) {
        this.fbias = bias[getNumber() - 1];
        this.dimension = dimension;
        initData(dimension);
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
        
        if (this.dimension == 0) {
            return -Integer.MAX_VALUE;
        }
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
     * Normalize a value
     * @param value
     * @return normalized value
     */
    public final double normalize(double value) {
        return (value - lowerBound()) / (upperBound() - lowerBound()) * 2.0 - 1.0;
    }

    /**
     * Normalizes a vector.
     * @param values
     * @return Normalized vector.
     */
    public final double[] normalize(double[] values) {
        double[] z = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            z[i] = normalize(values[i]);
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
    public double evaluate(double[] values) {
        return doEvaluate(values) + BIAS_FITNESS * fbias;
    }

    public double sphere(double[] x) {
        double sum = 0.0d;
        for (int i = 0; i < x.length; i++) {
            sum += x[i] * x[i];
        }
        return sum;
    }

    public double schwefel_1_2(double[] x) {
        double sum = 0.0d;
        double sum2 = 0.0d;

        for (int d = 0; d < x.length; d++) {
            sum2 = 0.0d;
            for (int i = 0; i < d; i++) {
                sum2 += x[i];
            }
            sum += sum2 * sum2;
        }

        return sum;
    }

    public double schwefel_2_21(double[] x) {

        double max = -Double.MAX_VALUE;

        for (int d = 0; d < x.length; d++) {
            
            max = Math.max(max, Math.abs(x[d]));
        }

        return max;

    }

    public double rosenbrock(double[] x) {
        double sum = 0.0d;
        for (int i = 0; i < x.length - 1; i++) {
            sum += 100 * (x[i] * x[i] - x[i + 1]) * (x[i] * x[i] - x[i + 1]) + (x[i] - 1) * (x[i] - 1);
        }
        return sum;
    }

    public double rastrigins(double[] x) {
        double sum = 0.0d;
        for (int i = 0; i < x.length; i++) {
            sum += x[i] * x[i] - 10.0 * Math.cos(2 * Math.PI * x[i]) + 10.0;
        }
        return sum;
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

        return -20 * Math.exp(-0.2 * Math.sqrt(1.0 / x.length * s1))
                - Math.exp(1.0 / x.length * s2) + 20 + Math.E;
    }
    
    public double schwefel_2_22(double[] x) {
    
        double sum = 0.0;
        double prod = 1.0;
        
        for (int i = 0; i < x.length; i++) {
            sum += Math.abs(x[i]);
            prod *= Math.abs(x[i]);
        }
        
        return (sum + prod);
        
    }
    
    public double extended_f10(double[] x) {
    
        double sum = 0.0;
        
        for (int i = 0; i < x.length - 1; i++) {
            sum += f10(x[i], x[i+1]);
        }
        
        sum += f10( x[x.length-1], x[0]);
        
        return sum;
        
    }
    
    public double f10(double x, double y) {
    
        double f10 = 0.0;
        double p, z, t;
        
        p = x*x + y*y;
        z = Math.pow(p, 0.25);
        t = Math.sin(50.0*Math.pow(p, 0.1));
        t = t*t + 1.0;
        
        
        f10 = z*t;
        
        return f10;
    
    }
    
    public double bohackevsky(double[] x) {
        
        double sum = 0.0;
        
        for (int i = 0; i < x.length - 1; i++) {
            
            sum += x[i]*x[i] + 2.0*x[i+1]*x[i+1] - 0.3*Math.cos(3.0*Math.PI*x[i]) - 
                    0.4*Math.cos(4.0*Math.PI*x[i+1]) + 0.7;
        
        }
        
        return sum;
        
    }
    
    public double schaffer(double[] x) {
    
        double sum = 0.0;
        
        for (int i = 0; i < x.length - 1; i++) {
        
            sum += f10(x[i], x[i+1]);
        
        }
        
        return sum;
        
    }
}
