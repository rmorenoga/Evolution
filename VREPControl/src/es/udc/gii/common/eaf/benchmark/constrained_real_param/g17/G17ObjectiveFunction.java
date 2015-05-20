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

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g17;

import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import java.util.List;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class G17ObjectiveFunction extends ObjectiveFunction {
    
    /** Creates a new instance of G02ObjectiveFunction */
    public G17ObjectiveFunction() {
    }
    
    @Override
    public void reset() {
    }
    
    @Override
    public double evaluate(double[] values) {
        
        double fitness = Double.MAX_VALUE;
        double x1, x2, x3, x4, x5, x6, aux1, aux2, aux5, aux4, f1, f2;
        double[] norm_values;

        norm_values = G17Function.normalize(values);

        x1 = norm_values[0];
        x2 = norm_values[1];
        x3 = norm_values[2];
        x4 = norm_values[3];
        x5 = norm_values[4];
        x6 = norm_values[5];

        f1 = Double.MAX_VALUE;
        f2 = Double.MAX_VALUE;
               
        if (x1 >= 0.0 && x1 < 300.0) {
            f1 = 30.0 * x1;
        } else {
            if (x1 >= 300.0 && x1 <= 400.0) {
                f1 = 31.0 * x1;
            }
        }
        if (x2 >= 0.0 && x2 < 100.0) {
            f2 = 28.0 * x2;
        } else {
            if (x2 >= 100.0 && x2 < 200.0) {
                f2 = 29.0 * x2;
            } else {
                if (x2 >= 200.0 && x2 <= 1000.0) {
                    f2 = 30.0 * x2;
                }
            }
        }
        fitness = f1 + f2;

        return fitness;
        
    }
    
}
