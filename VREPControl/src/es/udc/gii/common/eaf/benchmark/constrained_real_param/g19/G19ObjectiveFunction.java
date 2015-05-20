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
 * G02ObjectiveFunction.java
 *
 * Created on 28 de agosto de 2007, 11:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g19;

import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import java.util.List;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class G19ObjectiveFunction extends ObjectiveFunction {
    
    /** Creates a new instance of G02ObjectiveFunction */
    public G19ObjectiveFunction() {
    }
    
    public void reset() {
    }
    
    public double evaluate(double[] values) {
        
        double fitness = Double.MAX_VALUE;

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

        for (int i = 0; i < 10; i++) {
            sum1 = sum1 + B[i] * norm_values[i];
        }

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                sum2 = sum2 + C[i][j] * norm_values[10 + i] * norm_values[10 + j];
            }
        }

        for (int i = 0; i < 5; i++) {
            sum3 = sum3 + D[i] * Math.pow(norm_values[10 + i], 3.0);
        }

        fitness = -sum1 + sum2 + 2.0 * sum3;

        return fitness;
        
    }
    
}
