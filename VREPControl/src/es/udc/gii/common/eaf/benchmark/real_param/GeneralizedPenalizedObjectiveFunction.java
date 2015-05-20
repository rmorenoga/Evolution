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
 * GeneralizedPenalizedObjectiveFunction.java
 *
 * Created on 5 de julio de 2007, 19:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.real_param;

import es.udc.gii.common.eaf.benchmark.BenchmarkObjectiveFunction;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 */
public abstract class GeneralizedPenalizedObjectiveFunction extends BenchmarkObjectiveFunction {
    
    /** Creates a new instance of GeneralizedPenalizedObjectiveFunction */
    public GeneralizedPenalizedObjectiveFunction() {
    }
    
    protected double u(double xi, double a, double k, double m) {
        
        double u = 0.0;
        
        if (xi > a) {
            
            u = k*Math.pow(xi-a,m);
            
        } else if (-a <= xi && xi<= a) {
            u = 0.0;
        } else {
            u = k*Math.pow(-xi-a,m);
        }
        
        return u;
        
    }
    
    protected double y(double xi) {
        
        double y = 0.0;
        
        y = 1.0 + 0.25*(xi+1.0);
        
        return y;
        
    }
    
}
