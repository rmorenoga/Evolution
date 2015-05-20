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
 * Sch1_Objective_1.java
 *
 * Created on 18 de diciembre de 2006, 17:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package es.udc.gii.common.eaf.benchmark.multiobjective.sch1;

import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import es.udc.gii.common.eaf.util.ConfWarning;
import org.apache.commons.configuration.Configuration;

/**
 * Schaffer, 1984
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public class Sch1_Objective_1 extends ObjectiveFunction {

    /**
     * The range of the gen value: <code>-MAX &lt;= x &lt;= MAX</code>.
     */
    private double MAX = 10;

    /** Creates a new instance of Sch1_Objective_1 */
    public Sch1_Objective_1() {
    }

    @Override
    public double evaluate(double[] values) {
        final double x = values[0] * MAX;
        return x * x;
    }

    @Override
    public void configure(Configuration conf) {
        super.configure(conf);
        if (conf.containsKey("MAX")) {
            this.MAX = conf.getDouble("MAX");
        } else {
            (new ConfWarning("Sch1_Objective_1.MAX", MAX)).warn();
        }
    }

    @Override
    public String toString() {
        return "Sch1_Objective_1";
    }

    @Override
    public void reset() {
    }
    
    @Override
    public int getDimension() {
        return 1;
    }    
}
