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
 * R_Dtlz2_Objective.java
 *
 * Created on November 21, 2007, 11:46 AM
 *
 */
package es.udc.gii.common.eaf.benchmark.multiobjective.dtlz2;

import es.udc.gii.common.eaf.benchmark.multiobjective.ExtendedRotatedObjectiveFunction;
import org.apache.commons.configuration.Configuration;

/**
 * CEC 2007 Testsuite: Extended, rotated DTLZ2 (R_DTLZ2)
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class R_Dtlz2_Objective extends ExtendedRotatedObjectiveFunction {

    private int objNumber = 0;
    private int numberOfObjectives = 0;

    /** Creates a new instance of R_Dtlz2_Objective */
    public R_Dtlz2_Objective() {
    }
    
    /** Creates a new instance of R_Dtlz2_Objective */
    public R_Dtlz2_Objective(int dimension) {
        super(dimension);
    }    

    @Override
    public double evaluate(double[] x) {

        int nx = x.length;

        int i = 0, j = 0;
        int k = nx - numberOfObjectives + 1;
        double g = 0;        

        double[] z = new double[nx];
        double[] zz = new double[nx];
        double[] p = new double[nx];
        double[] psum = new double[numberOfObjectives];


        // denormalize vector:
        for (i = 0; i < nx; i++) {
            x[i] = (bounds[1][i] - bounds[0][i]) / 2 * x[i] +
                    (bounds[1][i] + bounds[0][i]) / 2;
        }

        for (i = 0; i < nx; i++) {
            z[i] = 0;
            for (j = 0; j < nx; j++) {
                z[i] += M[i][j] * x[j];
            }
            if (z[i] >= 0 && z[i] <= 1) {
                zz[i] = z[i];
                p[i] = 0;
            } else if (z[i] < 0) {
                zz[i] = -lambda[i] * z[i];
                p[i] = -z[i];
            } else {
                zz[i] = 1 - lambda[i] * (z[i] - 1);
                p[i] = z[i] - 1;
            }
        }

        for (j = 0; j < numberOfObjectives; j++) {
            psum[j] = 0;
        }

        for (i = nx - k + 1; i <= nx; i++) {
            g += Math.pow(zz[i - 1] - 0.5, 2) - Math.cos(20 * Math.PI * (zz[i - 1] - 0.5));
            for (j = 0; j < numberOfObjectives; j++) {
                psum[j] = Math.sqrt(Math.pow(psum[j], 2) + Math.pow(p[i - 1], 2));
            }
        }

        g = 100 * (k + g);

        double ff = (1 + g);

        for (j = numberOfObjectives - objNumber; j >= 1; j--) {
            ff *= Math.cos(zz[j - 1] * Math.PI / 2.0);
            psum[objNumber - 1] = Math.sqrt(Math.pow(psum[objNumber - 1], 2) +
                    Math.pow(p[j - 1], 2));
        }

        if (objNumber > 1) {
            ff *= Math.sin(zz[(numberOfObjectives - objNumber + 1) - 1] * Math.PI / 2.0);
            psum[objNumber - 1] = Math.sqrt(Math.pow(psum[objNumber - 1], 2) +
                    Math.pow(p[(numberOfObjectives - objNumber + 1) - 1], 2));
        }

        return 2.0 / (1 + Math.exp(-psum[objNumber - 1])) * (ff + 1);
    }

    @Override
    public void reset() {
    }

    @Override
    public void configure(Configuration conf) {
        super.configure(conf);

        this.objNumber = conf.getInt("ObjectiveNumber");
        this.numberOfObjectives = conf.getInt("NumberOfObjectives");
    }

    @Override
    public String toString() {
        return "R_Dtlz2_Objective";
    }

    @Override
    protected String getTransformationMatrixFile() {
        return "R_DTLZ2_M_" + this.dimension + "D.dat";
    }

    @Override
    protected String getScaleFactorVectorFile() {
        return "R_DTLZ2_lamda_" + this.dimension + "D.dat";
    }

    @Override
    protected String getBoundsFile() {
        return "R_DTLZ2_bound_" + this.dimension + "D.dat";
    }
}
