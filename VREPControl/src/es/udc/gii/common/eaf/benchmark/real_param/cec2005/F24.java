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
import es.udc.gii.common.eaf.util.EAFRandom;
import java.util.Arrays;

/**
 * CEC 2005 F24: Rotated Hybrid Composition Function
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class F24 extends CEC2005CompositionFunction {

    public F24(int dimension) {
        super(dimension);
    }

    public F24() {
    }

    @Override
    protected void initData(int dimension) {
        super.initData(dimension);
        double[][] m = new double[N * dimension][dimension];

        if (dimension <= 10) {
            LoadFromFile.loadMatrixFromResourceFile(prefix + "f" +
                    getNumber() + "_m10.txt", N * dimension, dimension, m);
        } else if (dimension > 10 && dimension <= 30) {
            LoadFromFile.loadMatrixFromResourceFile(prefix + "f" +
                    getNumber() + "_m30.txt", N * dimension, dimension, m);
        } else if (dimension > 30 && dimension <= 50) {
            LoadFromFile.loadMatrixFromResourceFile(prefix + "f" +
                    getNumber() + "_m50.txt", N * dimension, dimension, m);
        }

        M = new double[N][dimension][dimension];
        for (int n = 0; n < N; n++) {
            for (int r = 0; r < dimension; r++) {
                M[n][r] = m[n * dimension + r];
            }
        }

        lambda = new double[N];
        lambda[0] = 10.0;
        lambda[1] = 5.0 / 20.0;
        lambda[2] = 1.0;
        lambda[3] = 5.0 / 32.0;
        lambda[4] = 1.0;
        lambda[5] = 5.0 / 100;
        lambda[6] = 5.0 / 50.0;
        lambda[7] = 1.0;
        lambda[8] = 5.0 / 100;
        lambda[9] = 5.0 / 100;

        sigma = new double[dimension];
        Arrays.fill(sigma, 2.0d);
    }

    protected double[] noncontinuous(double[] z) {
        double[] zP = Arrays.copyOf(z, z.length);

        for (int i = 0; i < zP.length; i++) {
            if (Math.abs(z[i]) >= 0.5) {
                zP[i] = round(2.0 * z[i]) / 2.0d;
            }
        }
        return zP;
    }

    @Override
    protected double[] calculateFit(double[] z) {
        double[] fit = new double[z.length];
        double[] fMax = new double[z.length];

        double noise = 1.0 + NOISE * 0.1 * EAFRandom.nextGaussian();
        fit[0] = weierstrass(transform(z, o[0], lambda[0], M[0]), 0.5, 3, 20);
        fit[1] = F6(transform(z, o[1], lambda[1], M[1]));
        fit[2] = F8F2(transform(z, o[2], lambda[2], M[2]));
        fit[3] = ackley(transform(z, o[3], lambda[3], M[3]));
        fit[4] = rastrigins(transform(z, o[4], lambda[4], M[4]));
        fit[5] = griewank(transform(z, o[5], lambda[5], M[5]));
        fit[6] = F6(noncontinuous(transform(z, o[6], lambda[6], M[6])));
        fit[7] = rastrigins(noncontinuous(transform(z, o[7], lambda[7], M[7])));
        fit[8] = elliptic(transform(z, o[8], lambda[8], M[8]));
        fit[9] = sphere(transform(z, o[9], lambda[9], M[9])) * noise;

        fMax[0] = weierstrass(transform(y, null, lambda[0], M[0]), 0.5, 3, 20);
        fMax[1] = F6(transform(y, null, lambda[1], M[1]));
        fMax[2] = F8F2(transform(y, null, lambda[2], M[2]));
        fMax[3] = ackley(transform(y, null, lambda[3], M[3]));
        fMax[4] = rastrigins(transform(y, null, lambda[4], M[4]));
        fMax[5] = griewank(transform(y, null, lambda[5], M[5]));
        fMax[6] = F6(noncontinuous(transform(y, null, lambda[6], M[6])));
        fMax[7] = rastrigins(noncontinuous(transform(y, null, lambda[7], M[7])));
        fMax[8] = elliptic(transform(y, null, lambda[8], M[8]));
        fMax[9] = sphere(transform(y, null, lambda[9], M[9])) * noise;

        for (int i = 0; i < N; i++) {
            fit[i] = C * fit[i] / fMax[i];
        }

        return fit;
    }

    @Override
    public int getNumber() {
        return 24;
    }

    @Override
    public double lowerBound() {
        return -5;
    }

    @Override
    public double upperBound() {
        return 5;
    }
}
