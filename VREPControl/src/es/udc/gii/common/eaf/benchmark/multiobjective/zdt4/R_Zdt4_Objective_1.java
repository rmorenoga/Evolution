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
 * R_Zdt4_Objective_1.java
 *
 * Created on November 20, 2007, 2:06 PM
 *
 */
package es.udc.gii.common.eaf.benchmark.multiobjective.zdt4;

import es.udc.gii.common.eaf.benchmark.multiobjective.*;

/**
 * CEC 2007 Testsuite: Extended, rotated ZDT4 (R_ZDT4).
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class R_Zdt4_Objective_1 extends ExtendedRotatedObjectiveFunction {

    /** Creates a new instance of R_Zdt4_Objective_1 */
    public R_Zdt4_Objective_1() {
    }
    
    /** Creates a new instance of R_Zdt4_Objective_1 */
    public R_Zdt4_Objective_1(int dimension) {
        super(dimension);
    }    

    @Override
    public double evaluate(double[] x) {
        int nx = x.length;

        int i = 0, j = 0;
        double[] z = new double[nx];
        double[] zz = new double[nx];
        double[] p = new double[nx];

        double f0 = 0;

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
        }
        if (z[0] >= 0 && z[0] <= 1) {
            zz[0] = z[0];
            p[0] = 0;
            f0 = zz[0] + 1;
        } else if (z[0] < 0) {
            zz[0] = -lambda[0] * z[0];
            p[0] = -z[0];
            f0 = 2.0 / (1 + Math.exp(-p[0])) * (zz[0] + 1);
        } else {
            zz[0] = 1 - lambda[0] * (z[0] - 1);
            p[0] = z[0] - 1;
            f0 = 2.0 / (1 + Math.exp(-p[0])) * (zz[0] + 1);
        }

        return f0;
    }

    @Override
    public void reset() {
    }

    @Override
    public String toString() {
        return "R_Zdt4_Objective_1";
    }
    
    @Override
    protected String getTransformationMatrixFile() {
        return "R_ZDT4_M_" + this.dimension + "D.dat";
    }

    @Override
    protected String getScaleFactorVectorFile() {
        return "R_ZDT4_lamda_" + this.dimension + "D.dat";
    }

    @Override
    protected String getBoundsFile() {
        return "R_ZDT4_bound_" + this.dimension + "D.dat";
    }    
}
