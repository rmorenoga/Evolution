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
 * BenchmarkObjectiveFunction.java
 *
 * Created on 4 de julio de 2007, 20:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark;

import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public abstract class BenchmarkObjectiveFunction extends ObjectiveFunction {

    protected double upper_limit;

    protected double lower_limit;

    /** Creates a new instance of BenchmarkObjectiveFunction */
    public BenchmarkObjectiveFunction() {
    }

    public abstract double[][] getOptimum(int dim);

    public double getLower_limit() {
        return lower_limit;
    }

    public void setLower_limit(double lower_limit) {
        this.lower_limit = lower_limit;
    }

    public double getUpper_limit() {
        return upper_limit;
    }

    public void setUpper_limit(double upper_limit) {
        this.upper_limit = upper_limit;
    }

    
}
