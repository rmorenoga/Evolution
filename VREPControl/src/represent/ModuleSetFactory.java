/* 
 * EDHMOR - Evolutionary designer of heterogeneous modular robots
 * <https://bitbucket.org/afaina/edhmor>
 * Copyright (C) 2015 GII (UDC) and REAL (ITU)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package represent;

import simvrep.SimulationConfiguration;

/**
 * ModuleSetFactory returns the current set of modules to use.
 * <p>
 * This static object can be call to obtain the current module set. It loads the
 * module set when the program starts. It stores a copy of the set in this
 * object, which is the object returned when getModulesSet is called.
 * 
 * 
 * ModuleSetFactory.java Created on 18/10/2015
 *
 * @author Andres Faiña <anfv  at itu.dk>
 */
public class ModuleSetFactory {
    
    /**
     * This is the current module set whis is being used.
     */
    static ModuleSet moduleSet = loadModuleSet();

    /**
    * reloadModuleSet reloads the module set if this has been changed. As an 
    * example, this function is called in the GUI to change the set as required.
    */
    public static void reloadModuleSet(){
        moduleSet = loadModuleSet();
    }
    
    /**
    * loadModuleSet loads the module set. It reads the string stored in  
    * SimulationConfiguration and creates a new module set according to this 
    * value.
    */
    private static ModuleSet loadModuleSet(){
        ModuleSet set = null;
        
        if(SimulationConfiguration.getModuleSet().contentEquals("emergeModules"))
            set = new EmergeModuleSet();
        
        if (set == null){
            System.out.println("Failed ModulesSet initialization. Check the ModuleSet property in the configuration file.");
            System.exit(-1);
        }
        
        return set;
        
            
    }
    
    
    /**
    * getModulesSet returns the current module set. 
    * @return      the current module set
    */
    public final static ModuleSet getModulesSet(){
        return moduleSet;
    }
    
    
    
}
