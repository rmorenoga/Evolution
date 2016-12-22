package represent;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


import simvrep.SimulationConfiguration;
import util.InconsistentDataException;



public class RobotTree {

	private RobotNode rootNode = null;

	public RobotNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(RobotNode rootNode) {
		this.rootNode = rootNode;
	}
	
	public List<RobotNode> getNodeList(){
		return createNodeList();
	}
	
	   private List<RobotNode> createNodeList() {
	        int maxModules = SimulationConfiguration.getMaxModules();
	        List<RobotNode> nodes = new ArrayList<RobotNode>();
	        nodes.add(rootNode);
	        rootNode.setConstructionOrder(0);
	        rootNode.setLevel(0);
	        
	        for (int j = 0; j < nodes.size(); j++) {
	            RobotNode node = nodes.get(j);
	            node.setConstructionOrder(j);
	            if (node.getDad() != null) {
	                node.setLevel(node.getDad().getLevel() + 1);
	            }
	            for (int i = 0; i < node.getNConnections(); i++) {
	                if (nodes.size() < maxModules) {
	                    nodes.add(node.getChildren().get(i));
	                } else {
	                    try {
	                        throw new InconsistentDataException("Este arbol tiene excesivos nodos: MaxModules: " + maxModules);
	                    } catch (InconsistentDataException ex) {
	                        System.err.print(this.detailedToString(nodes));
	                        Logger.getLogger(RobotTree.class.getName()).log(Level.SEVERE, null, ex);
	                        System.exit(-1);
	                    }

	                }
	            }
	        }
	        return nodes;
	    }
	
	   public String detailedToString(List<RobotNode> nodes) {
	        String str = "\nTree:";
	        str += "\nNodes of the tree:\n";
	        for (RobotNode node : nodes) {
	            str += "\n" + node.toString();
	        }
	        str += "\n";
	        return str;
	    }
}
