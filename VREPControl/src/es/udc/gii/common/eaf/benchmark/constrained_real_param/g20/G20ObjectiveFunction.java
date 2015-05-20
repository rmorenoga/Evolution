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

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g20;

import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import java.util.List;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class G20ObjectiveFunction extends ObjectiveFunction {
    
    /** Creates a new instance of G02ObjectiveFunction */
    public G20ObjectiveFunction() {
    }
    
    public void reset() {
    }
    
    public double evaluate(double[] values) {
        
        double fitness = Double.MAX_VALUE;
        
        double[] A = {0.0693, 0.0577, 0.05, 0.2, 0.26, 0.55, 0.06, 0.1,
        0.12, 0.18, 0.1, 0.09
                , 0.0693, 0.0577, 0.05, 0.2, 0.26, 0.55, 0.06, 0.1,
        0.12, 0.18, 0.1, 0.09};
        double[] norm_values;

        norm_values = G20Function.normalize(values);
        
        fitness = 0.0;
        for (int j = 0; j < 24; j++) {
            fitness = fitness + A[j] * norm_values[j];
        }
        
        return fitness;
        
    }
    
}
