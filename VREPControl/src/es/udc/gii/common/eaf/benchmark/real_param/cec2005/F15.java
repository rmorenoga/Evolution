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

import java.util.Arrays;

/**
 * CEC 2005 F15: Hybrid Composition Function
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class F15 extends CEC2005CompositionFunction {

    public F15(int dimension) {
        super(dimension);
    }

    public F15() {
    }

    @Override
    protected void initData(int dimension) {
        super.initData(dimension);

        M = new double[N][dimension][dimension];
        for (int n = 0; n < N; n++) {
            for (int d = 0; d < dimension; d++) {
                M[n][d][d] = 1.0d;
            }
        }

        lambda = new double[N];
        lambda[0] = 1.0;
        lambda[1] = 1.0;
        lambda[2] = 10.0;
        lambda[3] = 10.0;
        lambda[4] = 5.0 / 60.0;
        lambda[5] = 5.0 / 60.0;
        lambda[6] = 5.0 / 32.0;
        lambda[7] = 5.0 / 32.0;
        lambda[8] = 5.0 / 100.0;
        lambda[9] = 5.0 / 100.0;

        sigma = new double[N];
        Arrays.fill(sigma, 1.0);
    }

    @Override
    protected final double[] calculateFit(double[] z) {
        double[] fit = new double[N];
        double[] fMax = new double[N];

        fit[0] = rastrigins(transform(z, o[0], lambda[0], M[0]));
        fit[1] = rastrigins(transform(z, o[1], lambda[1], M[1]));
        fit[2] = weierstrass(transform(z, o[2], lambda[2], M[2]), 0.5, 3, 20);
        fit[3] = weierstrass(transform(z, o[3], lambda[3], M[3]), 0.5, 3, 20);
        fit[4] = griewank(transform(z, o[4], lambda[4], M[4]));
        fit[5] = griewank(transform(z, o[5], lambda[5], M[5]));
        fit[6] = ackley(transform(z, o[6], lambda[6], M[6]));
        fit[7] = ackley(transform(z, o[7], lambda[7], M[7]));
        fit[8] = sphere(transform(z, o[8], lambda[8], M[8]));
        fit[9] = sphere(transform(z, o[9], lambda[9], M[9]));

        fMax[0] = rastrigins(transform(y, null, lambda[0], M[0]));
        fMax[1] = rastrigins(transform(y, null, lambda[1], M[1]));
        fMax[2] = weierstrass(transform(y, null, lambda[2], M[2]), 0.5, 3, 20);
        fMax[3] = weierstrass(transform(y, null, lambda[3], M[3]), 0.5, 3, 20);
        fMax[4] = griewank(transform(y, null, lambda[4], M[4]));
        fMax[5] = griewank(transform(y, null, lambda[5], M[5]));
        fMax[6] = ackley(transform(y, null, lambda[6], M[6]));
        fMax[7] = ackley(transform(y, null, lambda[7], M[7]));
        fMax[8] = sphere(transform(y, null, lambda[8], M[8]));
        fMax[9] = sphere(transform(y, null, lambda[9], M[9]));

        for (int i = 0; i < N; i++) {
            fit[i] = C * fit[i] / fMax[i];
        }

        return fit;
    }

    @Override
    public int getNumber() {
        return 15;
    }

    @Override
    public final double lowerBound() {
        return -5;
    }

    @Override
    public final double upperBound() {
        return 5;
    }
}
