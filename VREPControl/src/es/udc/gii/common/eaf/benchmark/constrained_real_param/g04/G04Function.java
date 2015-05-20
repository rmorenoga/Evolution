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

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g04;

import es.udc.gii.common.eaf.benchmark.constrained_real_param.g03.*;
import es.udc.gii.common.eaf.benchmark.constrained_real_param.g02.*;
import es.udc.gii.common.eaf.benchmark.constrained_real_param.g01.*;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class G04Function {

    /**
     * x1 ~ [78, 102]
     * x2 ~ [33, 45]
     * x3, x4, x5 ~ [27, 45]
     * @param values
     */
    public static double[] normalize(double[] values) {

        double[] norm_values = new double[values.length];

        norm_values[0] = values[0]*12.0 + 90.0;
        norm_values[1] = 6.0 * values[1] + 39.0;
        norm_values[2] = 9.0 * values[2] + 36.0;
        norm_values[3] = 9.0 * values[3] + 36.0;
        norm_values[4] = 9.0 * values[4] + 36.0;

        return norm_values;

    }

}
