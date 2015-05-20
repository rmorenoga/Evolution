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
package es.udc.gii.common.eaf.benchmark.real_param.soco;


/**
 * CEC 2005 F9: Shifter extended f10
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class F09 extends ShiftedObjectiveFunction {

    public F09(int dimension) {
        super(dimension);
    }

    public F09() {
        super();
    }

    @Override
    public int getNumber() {
        return 9;
    }

    @Override
    public double lowerBound() {
        return -100;
    }

    @Override
    public double upperBound() {
        return 100;
    }

    @Override
    public double doEvaluate(double[] values) {
        double[] z = shift(denormalize(values));
        return extended_f10(z);
    }
}
