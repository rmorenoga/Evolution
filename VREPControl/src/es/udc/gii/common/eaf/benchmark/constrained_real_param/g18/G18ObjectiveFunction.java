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

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g18;

import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import java.util.List;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class G18ObjectiveFunction extends ObjectiveFunction {
    
    /** Creates a new instance of G02ObjectiveFunction */
    public G18ObjectiveFunction() {
    }
    
    public void reset() {
    }
    
    public double evaluate(double[] values) {
        
        double fitness = Double.MAX_VALUE;
        double[] norm_values;

        norm_values = G18Function.normalize(values);
          

        fitness = -0.5 * (norm_values[0] * norm_values[3]
                - norm_values[1] * norm_values[2]
                + norm_values[2] * norm_values[8]
                - norm_values[4] * norm_values[8]
                + norm_values[4] * norm_values[7]
                - norm_values[5] * norm_values[6]);

        return fitness;
        
    }
    
}
