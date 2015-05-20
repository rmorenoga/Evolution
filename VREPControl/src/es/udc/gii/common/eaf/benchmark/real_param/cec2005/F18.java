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
 * CEC 2005 F18: Rotated Hybrid Composition Function
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class F18 extends CEC2005CompositionFunction {

    public F18(int dimension) {
        super(dimension);
    }

    public F18() {
    }

    @Override
    public int getNumber() {
        return 18;
    }

    @Override
    public final double lowerBound() {
        return -5;
    }

    @Override
    public final double upperBound() {
        return 5;
    }

    @Override
    protected void initData(int dimension) {
        super.initData(dimension);

        Arrays.fill(o[9], 0.0d);
        
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
        lambda[0] = 10.0 / 32.0;
        lambda[1] = 5.0 / 32.0;
        lambda[2] = 2.0;
        lambda[3] = 1.0;
        lambda[4] = 10.0 / 100.0;
        lambda[5] = 5.0 / 100.0;
        lambda[6] = 20.0;
        lambda[7] = 10.0;
        lambda[8] = 10.0 / 60.0;
        lambda[9] = 5.0 / 60.0;

        sigma = new double[dimension];
        sigma[0] = 1.0;
        sigma[1] = 2.0;
        sigma[2] = 1.5;
        sigma[3] = 1.5;
        sigma[4] = 1.0;
        sigma[5] = 1.0;
        sigma[6] = 1.5;
        sigma[7] = 1.5;
        sigma[8] = 2.0;
        sigma[9] = 2.0;
    }

    @Override
    protected final double[] calculateFit(double[] z) {
        double[] fit = new double[z.length];
        double[] fMax = new double[z.length];

        fit[0] = ackley(transform(z, o[0], lambda[0], M[0]));
        fit[1] = ackley(transform(z, o[1], lambda[1], M[1]));
        fit[2] = rastrigins(transform(z, o[2], lambda[2], M[2]));
        fit[3] = rastrigins(transform(z, o[3], lambda[3], M[3]));
        fit[4] = sphere(transform(z, o[4], lambda[4], M[4]));
        fit[5] = sphere(transform(z, o[5], lambda[5], M[5]));
        fit[6] = weierstrass(transform(z, o[6], lambda[6], M[6]), 0.5, 3, 20);
        fit[7] = weierstrass(transform(z, o[7], lambda[7], M[7]), 0.5, 3, 20);
        fit[8] = griewank(transform(z, o[8], lambda[8], M[8]));
        fit[9] = griewank(transform(z, o[9], lambda[9], M[9]));

        fMax[0] = ackley(transform(y, null, lambda[0], M[0]));
        fMax[1] = ackley(transform(y, null, lambda[1], M[1]));
        fMax[2] = rastrigins(transform(y, null, lambda[2], M[2]));
        fMax[3] = rastrigins(transform(y, null, lambda[3], M[3]));
        fMax[4] = sphere(transform(y, null, lambda[4], M[4]));
        fMax[5] = sphere(transform(y, null, lambda[5], M[5]));
        fMax[6] = weierstrass(transform(y, null, lambda[6], M[6]), 0.5, 3, 20);
        fMax[7] = weierstrass(transform(y, null, lambda[7], M[7]), 0.5, 3, 20);
        fMax[8] = griewank(transform(y, null, lambda[8], M[8]));
        fMax[9] = griewank(transform(y, null, lambda[9], M[9]));

        for (int i = 0; i < N; i++) {
            fit[i] = C * fit[i] / fMax[i];
        }

        return fit;
    }
}
