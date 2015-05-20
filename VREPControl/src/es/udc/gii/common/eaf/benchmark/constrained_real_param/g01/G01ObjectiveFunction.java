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
 * G01ObjectiveFunction.java
 *
 * Created on 27 de agosto de 2007, 19:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g01;

import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import org.apache.commons.math.stat.StatUtils;


/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class G01ObjectiveFunction extends ObjectiveFunction {
    
    public G01ObjectiveFunction() {
    }

    @Override
    public void reset() {
    }

    /**
     * 
     * @param values
     * f(x) = 5 * sum(xi,1,4) - 5 * sum(xi*xi,1,4) - sum(xi,5,13)
     * 0 <= xi <= 1 (i = 1,...,9)
     * 0 <= xi <= 100 (i = 10,11,12)
     * 0 <= x13 <= 1
     * @return
     */
    @Override
    public double evaluate(double[] values) {
        
        double[] norm_values;

        norm_values = G01Function.normalize(values);

        return 5.0 * StatUtils.sum(norm_values, 0, 4)
            - 5.0 * StatUtils.sumSq(norm_values, 0, 4) - StatUtils.sum(norm_values, 4, 9);
        
    }
    
}
