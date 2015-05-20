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

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g22;

import es.udc.gii.common.eaf.benchmark.constrained_real_param.g21.*;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class G22Function {

    /**
     * 0 ≤ x1 ≤ 20000, 0 ≤ x2 , x3 , x4 ≤ 1 × 10^6, 0 ≤ x5 , x6 , x7 ≤ 4 × 10^7
     * , 100 ≤ x8 ≤ 299.99, 100 ≤ x9 ≤ 399.99, 100.01 ≤ x10 ≤ 300, 100 ≤ x11 ≤ 400,
     * 100 ≤ x12 ≤ 600, 0 ≤ x13 , x14 , x15 ≤ 500, 0.01 ≤ x16 ≤ 300, 0.01 ≤ x17 ≤ 400,
     * −4.7 ≤ x18 , x19 , x20 , x21 , x22 ≤ 6.25.
     * @param values
     */
    public static double[] normalize(double[] values) {

       double[] norm_values = new double[values.length];

       norm_values[0] = (values[0]+1.0)*10000.0;
       norm_values[1] = (values[1]+1.0)*500000.0;
       norm_values[2] = (values[2]+1.0)*500000.0;
       norm_values[3] = (values[3]+1.0)*500000.0;
       norm_values[4] = (values[4]+1.0)*20000000.0;
       norm_values[5] = (values[5]+1.0)*20000000.0;
       norm_values[6] = (values[6]+1.0)*20000000.0;
       norm_values[7] = (values[7]+1.0)*99.995 + 100.0;
       norm_values[8] = (values[8]+1.0)*149.995 + 100.0;
       norm_values[9] = (values[9]+1.0)*99.95 + 100.01;
       norm_values[10] = (values[10] + 1.0)*150.0 + 100.0;
       norm_values[11] = (values[11]+1.0)*250.0 + 100.0;
       norm_values[12] = (values[12]+1.0)*250.0;
       norm_values[13] = (values[13]+1.0)*250.0;
       norm_values[14] = (values[14]+1.0)*250.0;
       norm_values[15] = (values[15] + 1.0)*149.995 + 0.01;
       norm_values[16] = (values[16] + 1.0)*199.995 + 0.01;
       norm_values[17] = (values[17] + 1.0)*5.475 - 4.7;
       norm_values[18] = (values[18] + 1.0)*5.475 - 4.7;
       norm_values[19] = (values[19] + 1.0)*5.475 - 4.7;
       norm_values[20] = (values[20] + 1.0)*5.475 - 4.7;
       norm_values[21] = (values[21] + 1.0)*5.475 - 4.7;

       return norm_values;
    }

}
