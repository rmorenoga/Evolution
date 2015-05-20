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
 * MainClass.java
 *
 * Created on 10 de noviembre de 2006, 16:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package es.udc.gii.common.eaf.benchmark.multiobjective;

import es.udc.gii.common.eaf.algorithm.EvolutionaryAlgorithm;
import es.udc.gii.common.eaf.facade.EAFFacade;
import es.udc.gii.common.eaf.stoptest.StopTest;

/**
 *
 * @author Grupo Integrado de Ingeniería (<a href="http://www.gii.udc.es">www.gii.udc.es</a>) 
 * @since 1.0
 *
 */
public class MainClass {
    
    
    public static void main(String[] args) {
        
        EAFFacade facade = new EAFFacade();
        EvolutionaryAlgorithm algorithm;
        StopTest stopTest;
      
        algorithm = facade.createAlgorithm(
                "/home/lucia/svn/eaf/evolutionary_algorithms/devel/src/es/udc" +
                "/gii/common/eaf/examples/multiobjective");
        stopTest = facade.createStopTest(
                "/home/lucia/svn/eaf/evolutionary_algorithms/devel/src/es/udc" +
                "/gii/common/eaf/examples/multiobjective");
        
        facade.resolve(stopTest,algorithm);
             
    }
    
}
