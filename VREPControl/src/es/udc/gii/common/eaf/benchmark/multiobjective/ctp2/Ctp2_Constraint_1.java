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
 * Ctp2_Constraint1.java
 *
 * Created on 18 de diciembre de 2006, 12:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.multiobjective.ctp2;

import es.udc.gii.common.eaf.problem.constraint.InequalityConstraint;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class Ctp2_Constraint_1 extends InequalityConstraint {

    /** Creates a new instance of Ctp2_Constraint1 */
    public Ctp2_Constraint_1() {
    }

    @Override
    public double evaluate(double[] values) {

        double theta, exp1, exp2, f, a, b, c, d, e;
        double x, y, f1, f2, g;


        x = 1 / 2 * values[0] + 1 / 2;
        y = 1 / 2 * values[1] + 1 / 2;

        g = 1.0 + y;
        f1 = x;
        f2 = g * (1.0 - Math.sqrt(x / g));

        //a = 0.2;
        a = 0.1;
        b = 10.0;
        c = 1.0;
        //d = 6.0;
        d = 0.5;
        e = 1.0;
        theta = -0.2 * Math.PI;

        exp1 = (f2 - e) * Math.cos(theta) - f1 * Math.sin(theta);
        exp2 = (f2 - e) * Math.sin(theta) + f1 * Math.cos(theta);
        exp2 = b * Math.PI * Math.pow(exp2, c);
        exp2 = Math.abs(Math.sin(exp2));
        exp2 = a * Math.pow(exp2, d);

        return exp1 / exp2 - 1.0;

    }

    @Override
    public String toString() {
        return "Ctp2_Constraint_1";
    }
}
