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
 * G01ConstraintFunction_1.java
 *
 * Created on 27 de agosto de 2007, 19:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g19;

import es.udc.gii.common.eaf.problem.constraint.InequalityConstraint;
import java.util.List;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class G19ConstraintFunction_g1 extends InequalityConstraint {
    
    /** Creates a new instance of G01ConstraintFunction_1 */
    public G19ConstraintFunction_g1() {
    }
    
    public double evaluate(double[] values) {
        
        double constraintValue = Double.MAX_VALUE;
        double sum1 = 0.0;
        double sum2 = 0.0;
        double sum3 = 0.0;
        double[][] A = { { -16.0, 2.0, 0.0, 1.0, 0.0}, {0.0, -2.0, 0.0, 0.4,
                2.0}, { -3.5, 0.0, 2.0, 0.0, 0.0}, {0.0, -2.0, 0.0,
                        -4.0, -1.0}, {0.0, -9.0, -2.0, 1.0, -2.8}, {2.0, 0.0,
                                -4.0, 0.0, 0.0}, { -1.0, -1.0, -1.0, -1.0, -1.0}, { -1.0,
                                        -2.0, -3.0, -2.0, -1.0}, {1.0, 2.0, 3.0, 4.0,
                                                5.0}, {1.0, 1.0, 1.0, 1.0, 1.0}
        };
        double[] B = { -40.0, -2.0, -0.25, -4.0, -4.0, -1.0, -40.0, -
                60.0, 5.0, 1.0};
        double[][] C = { {30.0, -20.0, -10.0, 32.0, -10.0}, { -20.0, 39.0,
                -6.0, -31.0, 32.0}, { -10.0, -6.0, 10.0, -6.0, -10.0},
                {32.0, -31.0, -6.0, 39.0, -20.0}, { -10.0, 32.0,
                        -10.0, -20.0, 30.0}
        };
        double[] D = {4.0, 8.0, 10.0, 6.0, 2.0};
        double[] E = { -15.0, -27.0, -36.0, -18.0, -12.0};
        double[] norm_values;

        norm_values = G19Function.normalize(values);
        
        
        sum1 = 0.0;
        for (int i = 0; i < 5; i++) {
            sum1 = sum1 + C[i][0] * norm_values[10 + i];
        }
        sum2 = 0.0;
        for (int i = 0; i < 10; i++) {
            sum2 = sum2 + A[i][0] * norm_values[i];
        }
        
        constraintValue = -2.0 * sum1 - 3.0 * D[0] * Math.pow(norm_values[10 + 0], 2.0) -
                E[0] + sum2;
        
        
        
        return constraintValue;
    }
    
    
}
