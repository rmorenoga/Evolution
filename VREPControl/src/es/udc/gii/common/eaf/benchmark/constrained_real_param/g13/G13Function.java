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

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g13;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class G13Function {

    /**
     * −2.3 ≤ xi ≤ 2.3 (i = 1, 2) and −3.2 ≤ xi ≤ 3.2 (i = 3, 4, 5)
     * @param values
     */
    public static double[] normalize(double[] values) {

        double[] norm_values = new double[values.length];

        norm_values[0] = values[0]*2.3;
        norm_values[1] = values[1]*2.3;
        norm_values[2] = values[2]*3.2;
        norm_values[3] = values[3]*3.2;
        norm_values[4] = values[4]*3.2;

        return norm_values;

    }

}
