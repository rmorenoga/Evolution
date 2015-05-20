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
package es.udc.gii.common.eaf.benchmark.constrained_real_param.g07;

import es.udc.gii.common.eaf.problem.constraint.InequalityConstraint;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class G07ConstraintFunction_g2 extends InequalityConstraint {

    /** Creates a new instance of G01ConstraintFunction_1 */
    public G07ConstraintFunction_g2() {
    }

    @Override
    public double evaluate(double[] values) {

        double[] norm_values;

        norm_values = G07Function.normalize(values);

        //values ~ [-10.0, 10.0]
        return 10.0 * norm_values[0] - 8.0 * norm_values[1] - 17.0 * norm_values[6] +
                2.0 * norm_values[7];
    }
}
