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
 * Ctp1_Constraint1.java
 *
 * Created on 18 de diciembre de 2006, 11:50
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.multiobjective.ctp1;

import es.udc.gii.common.eaf.problem.constraint.InequalityConstraint;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class Ctp1_Constraint_1 extends InequalityConstraint {

    /** Creates a new instance of Ctp1_Constraint1 */
    public Ctp1_Constraint_1() {
    }

    @Override
    public double evaluate(double[] values) {

        double f1, f2, x, y, g;

        x = 1 / 2 * values[0] + 1 / 2;
        y = 1 / 2 * values[1] + 1 / 2;


        f1 = x;

        g = 1.0 + y;

        f2 = g * Math.exp(-x / g);

        return f2 / (0.858 * Math.exp(-0.541 * f1)) - 1.0;
    }

    @Override
    public String toString() {
        return "Ctp1_Constraint_1";
    }
}
