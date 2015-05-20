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

package es.udc.gii.common.eaf.benchmark.constrained_real_param.g14;

import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import org.apache.commons.math.stat.StatUtils;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class G14ObjectiveFunction extends ObjectiveFunction {
    
    public G14ObjectiveFunction() {
    }
    
    @Override
    public void reset() {
    }
    
    @Override
    public double evaluate(double[] values) {
        
        double sum, fitness = 0.0;
        double[] norm_values;
        double[] c = { -6.089, -17.164, -34.054, -5.914, -24.721, -14.986, 
                     -24.1, -10.708, -26.662, -22.179};
        
        norm_values = G14Function.normalize(values);
        sum = StatUtils.sum(norm_values);
        for (int i = 0; i < 10; i++) {
            fitness += norm_values[i] * (c[i] + Math.log(norm_values[i] / sum));
        }
        return fitness;
        
    }
    
}
