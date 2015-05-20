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
 * CEC 2005 F7: Shifted Rotated Griewank’s Function without Bounds
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class F07 extends ShiftedRotatedObjectiveFunction {

    public F07(int dimension) {
        super(dimension);
    }

    public F07() {
    }

    @Override
    public final int getNumber() {
        return 7;
    }

    @Override
    public final double lowerBound() {
        return 0;
    }

    @Override
    public final double upperBound() {
        return 600;
    }

    @Override
    public double doEvaluate(double[] values) {
        double[] z = shiftAndRotate(denormalize(values));
        return griewank(z);
    }

    @Override
    public boolean checkBounds() {
        return false;
    }
}
