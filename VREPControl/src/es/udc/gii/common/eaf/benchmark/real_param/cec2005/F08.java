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
 * CEC 2005 F8: Shifted Rotated Ackley’s Function with Global Optimum on Bounds
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class F08 extends ShiftedRotatedObjectiveFunction {

    public F08(int dimension) {
        super(dimension);
    }

    public F08() {
    }

    @Override
    public final int getNumber() {
        return 8;
    }

    @Override
    public final double lowerBound() {
        return -32;
    }

    @Override
    public final double upperBound() {
        return 32;
    }

    @Override
    protected void initData(int dimension) {
        super.initData(dimension);

        int d2 = dimension / 2;

        for (int j = 1; j <= d2; j++) {
            o[2 * j - 2] = -32;
        }
    }

    @Override
    public double doEvaluate(double[] values) {
        double[] z = shiftAndRotate(denormalize(values));
        return ackley(z);
    }
}
