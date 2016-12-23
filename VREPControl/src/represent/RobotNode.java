package represent;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


import util.InconsistentDataException;





public class RobotNode {
	
	private int type;
	private int handler = -1;
	private RobotNode dad;
	private List<RobotNode> children = new ArrayList<RobotNode>();
	private List<Integer> connectedhandlers;
	private List<Connection> connections = new ArrayList<Connection>();
	private List<Integer> usedFaces = new ArrayList<Integer>();
	private int nModulesSubTree = 1;
	private int level = 0;
	private int constructionOrder;
	private int nFaces;
	
	
	
	public RobotNode(int type, RobotNode dad) {
        this.type = type;
        this.dad = dad;
        nFaces = ModuleSetFactory.getModulesSet().getModulesFacesNumber(type);
	}
	
	
	/**
     * Set a face where a child module will be connected. This function just
     * checks that that face is not used.
     *
     * @param face the face where the module will be attached
     * @return the face where the module will be attached (it doesnt change)
     */
    public int setFaceParent(int face) {
        for (Integer usedFace : usedFaces) {
            if (face == usedFace) {
                try {
                    throw new InconsistentDataException("We are trying to add a child in a face which is already used.");
                } catch (InconsistentDataException ex) {
                    System.err.println("Face already used: " + face);
                    System.err.println("Node: " + this.toString());
                    Logger.getLogger(RobotNode.class.getName()).log(Level.SEVERE, null, ex);
                    System.exit(-1);
                }
            }
        }
        usedFaces.add(face);
        return face;
    }
    
    /**
     * Sets an orientation for a child module. It sets the used face as used.
     *
     * @param orientation the orientation to set
     * @return the orientation set
     */
    public int setChildrenOrientation(int orientation) {

        int trueFace = ModuleSetFactory.getModulesSet().getConnectionFaceForEachOrientation(type, orientation);

        //We have to guarantee that the first value in the list usedFaces is the face which is used to connect to its parent module 
        if (usedFaces.size() > 0) {
            usedFaces.set(0, trueFace);
        } else {
            usedFaces.add(trueFace);
        }

        return orientation;
    }
    
    public boolean isFaceFree(int face) {
        return !this.usedFaces.contains(face);
    }
    
    public void addChildren(RobotNode children, Connection conn) {
        this.children.add(children);
        connections.add(conn);
    }
    
    public int getNumberModulesBranch() {
        int number = 0;
        for (RobotNode child : children) {
            number += child.getNumberModulesBranch();
        }
        return number + 1;
    }
    
    public void setModulesSubTree() {
        nModulesSubTree = 0;
        this.addModulesSubTree();
        for (int i = 0; i < this.children.size(); i++) {
            this.children.get(i).setModulesSubTree();
        }
    }
    
    public void addModulesSubTree() {
        this.nModulesSubTree++;
        if (this.dad != null) {
            dad.addModulesSubTree();
        }
    }
    
    public int getNConnections() {
        if (connections != null) {
            return connections.size();
        } else {
            return 0;
        }
    }
    
    public Connection getConnection(RobotNode child) {

        for (int i = 0; i < children.size(); i++) {
            RobotNode node = children.get(i);
            if (node == child) {
                return connections.get(i);
            }
        }
        return null;
    }
    
    public List<RobotNode> getChildren() {
        return children;
    }
    
    public String toString() {

        String strIni = "";
        for (int i = 0; i < this.getLevel(); i++) {
            strIni += "\t\t";
        }

        String str = "\n" + strIni + "Node: " + this.constructionOrder;
        String tipoStr = ModuleSetFactory.getModulesSet().getModuleName(type);
        str += "\n" + strIni + "Type: " + tipoStr;
        if (this.dad != null) {
            str += "\n" + strIni + "Parent: " + this.dad.constructionOrder;
        } else {
            str += "\n" + strIni + "No parent";
        }
        str += "\n" + strIni + "Number of children: " + this.children.size();
        str += "\n" + strIni + "Faces occupied: ";
        for (int f : usedFaces) {
            str += f + " ";
        }
        str += "\n" + strIni + "Handler: "+handler;
        if (this.dad != null) {
            if (this.getDad().getConnection(this) != null) {
                str += "\n" + strIni + "Dad face: " + this.getDad().getConnection(this).getDadFace();
                str += ",  Orientation: " + this.getDad().getConnection(this).getChildrenOrientation();
            }
        }

        str += "\n";
        return str;
    }
    
    public int getLevel() {
        return level;
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
    
    public void setConstructionOrder(int constructionOrder) {
        this.constructionOrder = constructionOrder;
    }
    
    public int getConstructionOrder() {
        return this.constructionOrder;
    }
    
    public RobotNode getDad() {
        return dad;
    }
    
    public void askforhandlers(){
    	connectedhandlers = new ArrayList<Integer>();
    	int handler;
    	if (dad == null) {
    		connectedhandlers.add(-1);
    		for (int face = 0;face <nFaces;face++){
    			handler = -1;
    			for (int index =0; index<connections.size();index++){
    				if (face == connections.get(index).getDadFace()){
    					handler = children.get(index).getHandler();
    					children.get(index).askforhandlers();
    				}
    			}
    			connectedhandlers.add(handler);   			
    		}
    	}else{
    		handler = dad.getHandler();
    		connectedhandlers.add(handler);
    		for (int face = 1;face <nFaces;face++){
    			handler = -1;
    			for (int index =0; index<connections.size();index++){
    				if (face == connections.get(index).getDadFace()){
    					handler = children.get(index).getHandler();
    					children.get(index).askforhandlers();
    				}
    			}
    			connectedhandlers.add(handler);   			
    		}
    		
    	}
    	//System.out.println(connectedhandlers);
    	
    	
    	
    }


	public List<Integer> getConnectedhandlers() {
		return connectedhandlers;
	}


	public int getHandler() {
		return handler;
	}


	public void setHandler(int handler) {
		this.handler = handler;
	}
    
	public String branchToString() {
        String str = this.toString();
        for (RobotNode child : children) {
            str += "\n" + child.branchToString();
        }
        return str;
    }
    
}
