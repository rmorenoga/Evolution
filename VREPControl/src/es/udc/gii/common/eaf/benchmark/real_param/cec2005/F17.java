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

import es.udc.gii.common.eaf.util.EAFRandom;

/**
 * CEC 2005 F17: Rotated Hybrid Composition Function with Noise in Fitness
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class F17 extends F16 {

    public F17(int dimension) {
        super(dimension);
    }

    public F17() {
    }

    @Override
    public final int getNumber() {
        return 17;
    }

    @Override
    public final double doEvaluate(double[] values) {
        return (super.doEvaluate(values) - super.getBias()) * (1 + NOISE * 0.2 * Math.abs(EAFRandom.nextGaussian())) + fbias;
    }
}
