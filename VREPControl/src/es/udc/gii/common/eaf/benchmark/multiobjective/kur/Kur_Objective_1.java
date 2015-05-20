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
 * Kur_Objective_1.java
 *
 * Created on 18 de diciembre de 2006, 17:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.multiobjective.kur;

import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;

/**
 * Kursawe 1991
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class Kur_Objective_1 extends ObjectiveFunction {

    /** Creates a new instance of Kur_Objective_1 */
    public Kur_Objective_1() {
    }

    @Override
    public String toString() {
        return "Kur_Objective_1";
    }

    @Override
    public void reset() {
    }

    @Override
    public double evaluate(double[] values) {
        double sum = 0;
        for (int i = 0; i < values.length - 1; i++) {
            double xi = 5 * values[i];
            double xi_1 = 5 * values[i + 1];
            sum += Math.exp(-0.2 * Math.sqrt(xi * xi + xi_1 * xi_1));
        }

        return -10 * sum;
    }
}
