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
 * Pol_Objective_1.java
 *
 * Created on 18 de diciembre de 2006, 17:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.multiobjective.pol;

import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;

/**
 * Poloni and Pediroda, 1997
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class Pol_Objective_1 extends ObjectiveFunction {

    /** Creates a new instance of Pol_Objective_1 */
    public Pol_Objective_1() {
    }

    @Override
    public String toString() {
        return "Pol_Objective_1";
    }

    @Override
    public void reset() {
    }

    @Override
    public double evaluate(double[] values) {
        final double x = Math.PI * values[0];
        final double y = Math.PI * values[1];
        final double A1 = 0.5 * Math.sin(1) - 2 * Math.cos(1) +
                Math.sin(2) - 1.5 * Math.cos(2);
        final double A2 = 1.5 * Math.sin(1) - Math.cos(1) +
                2 * Math.sin(2) - 0.5 * Math.cos(2);
        final double B1 = 0.5 * Math.sin(x) - 2 * Math.cos(x) + Math.sin(y) -
                1.5 * Math.cos(y);
        final double B2 = 1.5 * Math.sin(x) - Math.cos(x) + 2 * Math.sin(y) -
                0.5 * Math.cos(y);

        return -(1 + (A1 - B1) * (A1 - B1) + (A2 - B2) * (A2 - B2));
    }
    
    @Override
    public int getDimension() {
        return 2;
    }    
}
