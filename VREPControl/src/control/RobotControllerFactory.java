package control;

import coppelia.remoteApi;
import represent.EmergeModuleSet;
import represent.ModuleSet;
import simvrep.RobotBuilder;
import simvrep.SimulationConfiguration;

public class RobotControllerFactory {
	
    /**
     * This is the current controller being used.
     */
    static RobotController controller;
    
    
    /**
    * reloadController reloads the controller if this has been changed. As an 
    * example, this function is called in the GUI to change the set as required.
    */
    public static void reloadController(remoteApi vrep,int clientID, RobotBuilder robot, float[] parameters){
        controller = loadController(vrep,clientID,robot,parameters);
    }
    
    /**
    * loadController loads the controller. It reads the string stored in  
    * SimulationConfiguration and creates a new controller according to this 
    * value.
    */
    private static RobotController loadController(remoteApi vrep,int clientID, RobotBuilder robot, float[] parameters){
        RobotController Cont = null;
        
        if(SimulationConfiguration.getController().contentEquals("CPG")){
            Cont = new CPGController(vrep,clientID,robot,parameters);
        }else if (SimulationConfiguration.getController().contentEquals("CPGH")){
        	Cont = new CPGHController(vrep,clientID,robot,parameters);
        }
        
        if (Cont == null){
            System.out.println("Failed Controller initialization. Check the Controller property in the configuration file.");
            System.exit(-1);
        }
        
        return Cont;
        
            
    }
    
    /**
     * getModulesSet returns the current module set. 
     * @return      the current module set
     */
     public final static RobotController getController(){
         return controller;
     }

}
