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
 * S_Dtlz3_Objective.java
 *
 * Created on November 21, 2007, 12:08 PM
 *
 */
package es.udc.gii.common.eaf.benchmark.multiobjective.dtlz3;

import es.udc.gii.common.eaf.benchmark.multiobjective.*;
import org.apache.commons.configuration.Configuration;

/**
 * CEC 2007 Testsuite: Extended, shifted DTLZ3 (S_DTLZ3)
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class S_Dtlz3_Objective extends ExtendedShiftedObjectiveFunction {

    private int objNumber = 0;
    private int numberOfObjectives = 0;

    /** Creates a new instance of S_Dtlz3_Objective */
    public S_Dtlz3_Objective() {
    }
    
    /** Creates a new instance of S_Dtlz3_Objective */
    public S_Dtlz3_Objective(int dimension) {
        super(dimension);
    }    

    @Override
    public double evaluate(double[] x) {

        int nx = x.length;

        int i = 0;
        int j = 0;
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
            z[i] = x[i] - o[i];

            if (z[i] < 0) {
                zz[i] = -lambda[i] * z[i];
                p[i] = -z[i] / d[i];
            } else {
                zz[i] = z[i];
                p[i] = 0;
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
        return "S_Dtlz3_Objective";
    }

    @Override
    protected String getDataFile() {
        return "S_DTLZ3.dat";
    }

    @Override
    protected String getBoundsFile() {
        return "S_DTLZ3_bound.dat";
    }
}
