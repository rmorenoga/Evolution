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
 * Wfg_Objective.java
 *
 * Created on November 21, 2007, 12:53 PM
 *
 */
package es.udc.gii.common.eaf.benchmark.multiobjective.wfg;

import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import org.apache.commons.configuration.Configuration;

/**
 * CEC 2007 Testsuite: WFG common functions.
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public abstract class Wfg_Objective extends ObjectiveFunction {

    protected final double EPSILON = 1.0e-10;
    protected int objNumber = 0;
    protected int numberOfObjectives = 0;

    /** Creates a new instance of Wfg_Objective */
    public Wfg_Objective() {
    }

    @Override
    public void configure(Configuration conf) {
        super.configure(conf);

        this.objNumber = conf.getInt("ObjectiveNumber");
        this.numberOfObjectives = conf.getInt("NumberOfObjectives");
    }

    protected double s_linear(double y, double A) {
        return correct_to_01(Math.abs(y - A) / Math.abs(Math.floor(A - y) + A), EPSILON);
    }

    protected double correct_to_01(double aa, double epsilon) {
        double min = 0.0, max = 1.0;
        double min_epsilon = min - epsilon;
        double max_epsilon = max + epsilon;

        if (aa <= min && aa >= min_epsilon) {
            return min;
        } else if (aa >= max && aa <= max_epsilon) {
            return max;
        } else {
            return aa;
        }
    }

    protected double b_flat(double y, double A, double B, double C) {
        double tmp1 = min_double(0.0, Math.floor(y - B)) * A * (B - y) / B;
        double tmp2 = min_double(0.0, Math.floor(C - y)) * (1.0 - A) * (y - C) / (1.0 - C);

        return correct_to_01(A + tmp1 - tmp2, EPSILON);
    }

    protected double min_double(double d, double d0) {
        return Math.min(d, d0);
    }

    protected double b_poly(double y, double alpha) {
        return correct_to_01(Math.pow(y, alpha), EPSILON);
    }

    protected double r_sum(double[] y, double[] w, int ny) {

        int i;
        double numerator = 0.0;
        double denominator = 0.0;

        for (i = 0; i < ny; i++) {

            numerator += w[i] * y[i];
            denominator += w[i];
        }

        return correct_to_01(numerator / denominator, EPSILON);

    }

    protected double convex(double[] x, int m, int M) {
        int i;
        double result = 1.0;
        for (i = 1; i <= M - m; i++) {
            result *= 1.0 - Math.cos(x[i - 1] * Math.PI / 2.0);
        }

        if (m != 1) {
            result *= 1.0 - Math.sin(x[M - m] * Math.PI / 2.0);
        }

        return correct_to_01(result, EPSILON);
    }

    protected double mixed(double[] x, int A, double alpha) {
        double tmp = 2.0 * A * Math.PI;
        return correct_to_01(
                Math.pow(1.0 - x[0] - Math.cos(tmp * x[0] + Math.PI / 2.0) / tmp, alpha), EPSILON);
    }

    protected int vector_in_01(double[] x, int nx) {
        int i;
        for (i = 0; i < nx; i++) {
            if (x[i] < 0.0 || x[i] > 1.0) {
                return 0;
            }
        }
        return 1;
    }

    protected double linear(double[] x, int m, int M) {
        int i;
        double result = 1.0;
        for (i = 1; i <= M - m; i++) {
            result *= x[i - 1];
        }

        if (m != 1) {
            result *= 1 - x[M - m];
        }

        return correct_to_01(result, EPSILON);
    }

    protected double concave(double[] x, int m, int M) {
        int i;
        double result = 1.0;

        for (i = 1; i <= M - m; i++) {
            result *= Math.sin(x[i - 1] * Math.PI / 2.0);
        }

        if (m != 1) {
            result *= Math.cos(x[M - m] * Math.PI / 2.0);
        }

        return correct_to_01(result, EPSILON);
    }

    protected double disc(double[] x, int A, double alpha, double beta) {
        double tmp1 = A * Math.pow(x[0], beta) * Math.PI;
        return correct_to_01(1.0 - Math.pow(x[0], alpha) *
                Math.pow(Math.cos(tmp1), 2.0), EPSILON);

    }

    protected double b_param(double y, double u, double A, double B, double C) {
        double v = A - (1.0 - 2.0 * u) * Math.abs(Math.floor(0.5 - u) + A);
        return correct_to_01(Math.pow(y, B + (C - B) * v), EPSILON);
    }

    protected double s_decept(double y, double A, double B, double C) {
        double tmp1 = Math.floor(y - A + B) * (1.0 - C + (A - B) / B) / (A - B);
        double tmp2 = Math.floor(A + B - y) * (1.0 - C + (1.0 - A - B) / B) / (1.0 - A - B);

        return correct_to_01(1.0 + (Math.abs(y - A) - B) * (tmp1 + tmp2 + 1.0 / B), EPSILON);
    }

    protected double s_multi(double y, int A, double B, double C) {
        double tmp1 = Math.abs(y - C) / (2.0 * (Math.floor(C - y) + C));
        double tmp2 = (4.0 * A + 2.0) * Math.PI * (0.5 - tmp1);

        return correct_to_01((1.0 + Math.cos(tmp2) +
                4.0 * B * Math.pow(tmp1, 2.0)) / (B + 2.0), EPSILON);
    }

    protected double r_nonsep(double[] y, int A, int ny) {
        int y_len = ny;
        int j;
        double numerator = 0.0;

        for (j = 0; j < y_len; j++) {
            int k;
            numerator += y[j];

            for (k = 0; k <= A - 2; k++) {
                numerator += Math.abs(y[j] - y[(j + k + 1) % y_len]);
            }
        }

        {
            double tmp = Math.ceil(A / 2.0);
            double denominator = y_len * tmp * (1.0 + 2.0 * A - 2.0 * tmp) / A;

            return correct_to_01(numerator / denominator, EPSILON);
        }
    }
}
