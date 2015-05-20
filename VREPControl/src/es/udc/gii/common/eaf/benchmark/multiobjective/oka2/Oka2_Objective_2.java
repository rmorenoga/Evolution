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
 * Oka2_Objective_2.java
 *
 * Created on November 20, 2007, 11:46 AM
 *
 */
package es.udc.gii.common.eaf.benchmark.multiobjective.oka2;

import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;

/**
 * CEC 2007 Testsuite: Oka2
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class Oka2_Objective_2 extends ObjectiveFunction {

    /** Creates a new instance of Oka2_Objective_2 */
    public Oka2_Objective_2() {
    }

    @Override
    public double evaluate(double[] values) {

        final double x1 = Math.PI * values[0];
        final double x2 = 5 / 2 * values[1] + 5 / 2;
        final double x3 = 5 / 2 * values[2] + 5 / 2;

        double a = (1 / (4 * Math.PI * Math.PI)) * (x1 + Math.PI) * (x1 + Math.PI);
        double b = Math.pow(Math.abs(x2 - 5 * Math.cos(x1)), 1.0 / 3.0);
        double c = Math.pow(Math.abs(x3 - 5 * Math.sin(x1)), 1.0 / 3.0);

        return 1 - a + b + c;
    }

    @Override
    public void reset() {
    }

    @Override
    public String toString() {
        return "Oka2_Objective_2";
    }
    
    @Override
    public int getDimension() {
        return 3;
    }    
}
