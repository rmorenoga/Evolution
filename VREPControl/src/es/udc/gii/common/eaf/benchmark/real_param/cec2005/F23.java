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

/**
 * CEC 2005 F23: Non-Continuous Rotated Hybrid Composition Function
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public final class F23 extends F21 {

    public F23() {
    }

    public F23(int dimension) {
        super(dimension);
    }

    @Override
    public final double doEvaluate(double[] values) {
        double[] z = denormalize(values);

        for (int i = 0; i < z.length; i++) {
            if (Math.abs(z[i]) >= 0.5d) {
                z[i] = round(2.0d * z[i]) / 2.0d;
            }
        }

        double[] w = calculateW(z);
        double[] fit = calculateFit(z);

        double res = 0.0;
        for (int i = 0; i < N; i++) {
            res += w[i] * (fit[i] + BIAS[i]);
        }

        return res;
    }

    @Override
    public final int getNumber() {
        return 23;
    }
}
