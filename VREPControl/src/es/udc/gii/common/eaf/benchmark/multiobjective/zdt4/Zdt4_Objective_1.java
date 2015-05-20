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
 * Zdt4_Objective_1.java
 *
 * Created on 18 de diciembre de 2006, 18:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.multiobjective.zdt4;

import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class Zdt4_Objective_1 extends ObjectiveFunction {

    /** Creates a new instance of Zdt4_Objective_1 */
    public Zdt4_Objective_1() {
    }

    @Override
    public double evaluate(double[] values) {

        double h, f1, g, aux;

        f1 = 1 / 2 * values[0] + 1 / 2;
        g = 0.0;

        if (values.length >= 30) {
            for (int i = 0; i < 30; i++) {
                aux = 1 / 2 * values[i] + 1 / 2;
                g += aux * aux - 10.0 * Math.cos(4.0 * Math.PI * aux);
            }

            g += 91.0;
            h = 1.0 - Math.sqrt(f1 / g);
            return g * h;

        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "Zdt4_Objective_1";
    }

    @Override
    public void reset() {
    }
}
