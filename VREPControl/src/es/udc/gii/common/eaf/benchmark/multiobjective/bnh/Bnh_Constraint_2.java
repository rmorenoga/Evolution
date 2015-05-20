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
 * Bnh_Constraint2.java
 *
 * Created on 18 de diciembre de 2006, 11:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.multiobjective.bnh;

import es.udc.gii.common.eaf.problem.constraint.InequalityConstraint;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class Bnh_Constraint_2 extends InequalityConstraint {

    /** Creates a new instance of Bnh_Constraint2 */
    public Bnh_Constraint_2() {
    }

    @Override
    public double evaluate(double[] values) {


        double x, y;

        x = 5/2 * values[0] + 5/2;
        y = 3/2 * values[1] + 3/2;
        
        return -Math.pow(x - 8.0, 2) - Math.pow((y + 3.0), 2) + 7.7;
    }
    
    @Override
    public String toString() {
        return "Bnh_Constraint_2";
    }    
}