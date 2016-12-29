/*
 * EDHMOR - Evolutionary designer of heterogeneous modular robots
 * <https://bitbucket.org/afaina/edhmor>
 * Copyright (C) 2016 GII (UDC) and REAL (ITU)
 * Modified for
 * <http://github.com/rmorenoga/Evolution>
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

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import simvrep.SimulationConfiguration;
import util.ModuleRotation;


/**
 * CalculateModulePositions.java Created on 25/03/2016
 * 
 * @author Andres Faiña <anfv at itu.dk> Modified By
 * @author Rodrigo Moreno
 */
public class RobotFeatureCalculator {

	private int nModulesMax;
	private int nModules = 1;
	private int[] chromosomeInt;
	private double[] chromosomeDouble;
	private int[] moduleType;
	private int[] dadFace;
	private String pathBase = "/home/fai/svn/tesis/programacion/gazebo/tesis/";
	private String worldbase = "base.world";
	private int[] childOrientation;
	private int[] parentModule;
	private int[][] occupiedFaces;

	private Point3d minPos, maxPos, dimensions;
	private Vector3D com;
	private List<Vector3D> supportFaces;
	private double robotMass = 0.0;
	private double initialHeight = 0.0;
	private boolean useSupport = false;

	private Vector3D inertia;
	private double averageConnectionsPerModule;
	private double dispersionConnectionsPerModule;

	private Vector3D[] modulePosition;
	private Rotation[] moduleRotation;
	private ModuleSet moduleSet;
	private RobotTree tree;

	/*    *//**
			 * Class constructor to calculate the position and orientation of
			 * the modules based on the chromosome. This class is used to build
			 * the robot in the simulator, find the features of the robot and
			 * check the overlapping between modules
			 * <p>
			 * 
			 * @param tree
			 *            the tree individual where the morphology and the
			 *            control parameters are stored
			 *
			 *//*
				 * public CalculateModulePositions(TreeIndividual tree){
				 * 
				 * //Load the module set moduleSet =
				 * ModuleSetFactory.getModulesSet(); this.nModulesMax =
				 * (tree.getChromosomeAt(0).length + 3) / 9; this.nModules =
				 * tree.getRootNode().getNumberModulesBranch();
				 * 
				 * initArrays();
				 * 
				 * treeAnalysis(tree); }
				 */

	/**
	 * Class constructor to calcualte the position and orientation of the
	 * modules based on the chromosome. This class is used to bould the robot in
	 * the simulator, find the features of the robot and check the overlapping
	 * between modules
	 * <p>
	 * 
	 * @param chromo
	 *            the chromosome where the morphology and the control parameters
	 *            are stored as doubles
	 *
	 */
	public RobotFeatureCalculator(double[] chromo) {

		// Load the module set
		moduleSet = ModuleSetFactory.getModulesSet();

		// Convert the doubles to integers
		chromosomeInt = new int[chromo.length];
		for (int i = 0; i < chromo.length; i++) {
			chromosomeInt[i] = (int) Math.floor(chromo[i]);
		}

		// Calculate the maximum number of modules that this chromosome allows.
		if ((chromo.length + 3) % 8 == 0) {
			this.nModulesMax = 2*(chromo.length + 3) / 8;
		} else {
			System.err.println("VrepCreateRobot: Error in the lenght of the chromosome; length: " + chromo.length);
			for (int i = 0; i < chromo.length; i++) {
				System.err.print(chromo[i] + ", ");
			}
			System.exit(-1);
		}

		if (SimulationConfiguration.isDebug()) {
			for (int i = 0; i < this.chromosomeInt.length; i++) {
				System.out.print(this.chromosomeInt[i] + ", ");
			}
		}

		this.chromosomeDouble = chromo;

		initArrays();

		// Now, analyze the chromosome
		this.chromosomeAnalysis();

		// calculate the rotation and position of the modules and the force
		// sensors. Load them in Vrep simulator. We also calculate the
		// dimensions
		// of the robot, the center of mass and other useful features of the
		// robot
		calculate();

	}

	private void initArrays() {
		this.moduleType = new int[nModulesMax];

		this.dadFace = new int[nModulesMax - 1];
		this.childOrientation = new int[nModulesMax - 1];
		this.parentModule = new int[nModulesMax];
		this.occupiedFaces = new int[nModulesMax][14];

		this.maxPos = new Point3d(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
		this.minPos = new Point3d(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
		this.dimensions = new Point3d(0, 0, 0);
		this.com = new Vector3D(0, 0, 0);
		this.supportFaces = new ArrayList<Vector3D>();
	}

	private void calculate() {

		// Start with the first module
		int type = this.moduleType[0];
		this.robotMass += moduleSet.getModulesMass(type);

		Vector3D facePos;
		for (int face = 0; face < moduleSet.getModulesFacesNumber(type); face++) {
			facePos = moduleSet.getOriginFaceVector(type, face);

			if (this.maxPos.x < facePos.getX()) {
				this.maxPos.x = facePos.getX();
			}
			if (this.maxPos.y < facePos.getY()) {
				this.maxPos.y = facePos.getY();
			}
			if (this.maxPos.z < facePos.getZ()) {
				this.maxPos.z = facePos.getZ();
			}

			if (this.minPos.x > facePos.getX()) {
				this.minPos.x = facePos.getX();
			}
			if (this.minPos.y > facePos.getY()) {
				this.minPos.y = facePos.getY();
			}
			if (this.minPos.z > facePos.getZ()) {
				this.minPos.z = facePos.getZ();
				supportFaces.clear();
			}
			// Add supports
			if (facePos.getZ() - this.minPos.z < 0.15) {
				supportFaces.add(facePos);
			}
		}

		modulePosition = new Vector3D[nModules];
		moduleRotation = new Rotation[nModules];
		modulePosition[0] = new Vector3D(0, 0, 0);
		moduleRotation[0] = Rotation.IDENTITY;

		for (int module = 1; module < nModules; module++) {

			int modType = moduleType[module];
			int parentModuleType = moduleType[parentModule[module]];
			int conectionFace = dadFace[module - 1] % moduleSet.getModulesFacesNumber(parentModuleType);
			int orientation = childOrientation[(module - 1)] % moduleSet.getModuleOrientations(modType);

			// Check that the face of the parent where we are going to attach
			// the
			// child is available
			while (occupiedFaces[parentModule[module]][conectionFace] == 1) {
				System.err.println("Error in VrepCreateRobot: face already occupied. Individual:");
				for (int i = 0; i < chromosomeInt.length; i++) {
					System.out.print(chromosomeInt[i] + " ");
				}
				for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
					System.out.println(ste);
				}
				System.exit(-1);
				conectionFace = ++conectionFace % moduleSet.getModulesFacesNumber(parentModuleType);
			}
			occupiedFaces[parentModule[module]][conectionFace] = 1;

			// Get the vector which is normal to the face of the parent
			Vector3D normalParentFace = moduleSet.getNormalFaceVector(parentModuleType, conectionFace);

			// Calculate the rotation of the child to align both normals and
			// roate them according to the orientation of the child
			ModuleRotation rot = new ModuleRotation(modType, orientation, normalParentFace);

			// Get the vector from the origin of the module to the connection
			// face (in the parent) Origin parent -> Face parent (OFP)
			Vector3D ofp = moduleSet.getOriginFaceVector(parentModuleType, conectionFace);

			// Face of the child to attach the module
			int childFace = moduleSet.getConnectionFaceForEachOrientation(modType, orientation);

			// Get the vector from the origin of the module to the connection
			// face (in the child) Origin child -> Face child (OFC)
			Vector3D ofc = moduleSet.getOriginFaceVector(modType, childFace);
			occupiedFaces[module][childFace] = 1; // Set that face occupied

			// Rotate the ofc vector
			Vector3D ofcRotated = rot.getRotation().applyTo(ofc);
			ofcRotated = ofcRotated.negate(); // Negate it

			// Obtain the position of the chid in the coord. system of the
			// parent
			Vector3D posVector = ofcRotated.add(ofp);

			// Rotation and pos of the module in the coord. system of the world
			moduleRotation[module] = moduleRotation[parentModule[module]].applyTo(rot.getRotation());
			modulePosition[module] = moduleRotation[parentModule[module]].applyTo(posVector);
			modulePosition[module] = modulePosition[module].add(modulePosition[parentModule[module]]);

			// Calculate the center of mass of the robot
			Vector3D aux = new Vector3D(modulePosition[module].getX(), modulePosition[module].getY(),
					modulePosition[module].getZ());
			double s = moduleSet.getModulesMass(modType);
			this.robotMass += s;
			aux = aux.scalarMultiply(s);
			this.com = this.com.add(aux);

			if (SimulationConfiguration.isDebug()) {
				System.out.println("Total mass of the robot: " + this.robotMass + "; cdm: " + this.com);
			}

			// We check the position in wold coodinates for all the faces of the
			// modules. We find the lowest position in Z axis and the we added
			// this distance to the root module. Then, all the modules are above
			// the ground level and at least the robot has one point of support.
			// FIXME: Hinge modules in Edhmor and Rodrigo´s modules in one
			// orientation dont have faces in all the directions, then they
			// could
			// be partially buried in the ground.
			int nFacesToLook = moduleSet.getModulesFacesNumber(modType);
			for (int face = 0; face < nFacesToLook; face++) {
				ofc = moduleSet.getOriginFaceVector(modType, face);
				ofcRotated = moduleRotation[module].applyTo(ofc);
				ofcRotated = ofcRotated.add(modulePosition[module]);

				if (this.maxPos.x < ofcRotated.getX()) {
					this.maxPos.x = ofcRotated.getX();
				}
				if (this.maxPos.y < ofcRotated.getY()) {
					this.maxPos.y = ofcRotated.getY();
				}
				if (this.maxPos.z < ofcRotated.getZ()) {
					this.maxPos.z = ofcRotated.getZ();
				}

				if (this.minPos.x > ofcRotated.getX()) {
					this.minPos.x = ofcRotated.getX();
				}
				if (this.minPos.y > ofcRotated.getY()) {
					this.minPos.y = ofcRotated.getY();
				}
				if (this.minPos.z > ofcRotated.getZ()) {
					this.minPos.z = ofcRotated.getZ();
					supportFaces.clear();
				}
				// Add supports
				if (ofcRotated.getZ() - this.minPos.z < 0.15) {
					supportFaces.add(ofcRotated);
				}
			}

		}

		this.dimensions.add(this.maxPos);
		this.dimensions.sub(this.minPos);
		this.com = this.com.scalarMultiply(1 / this.robotMass);

		// Calculate the moment of inertia
		double Ix = 0, Iy = 0, Iz = 0;
		for (int module = 1; module < nModules; module++) {
			Vector3D posModule = new Vector3D(modulePosition[module].getX(), modulePosition[module].getY(),
					modulePosition[module].getZ());

			double axisZdistance = Math.pow(posModule.getX() - this.com.getX(), 2)
					+ Math.pow(posModule.getY() - this.com.getY(), 2);
			axisZdistance = Math.sqrt(axisZdistance);
			double axisXdistance = Math.pow(posModule.getY() - this.com.getY(), 2)
					+ Math.pow(posModule.getZ() - this.com.getZ(), 2);
			axisXdistance = Math.sqrt(axisXdistance);
			double axisYdistance = Math.pow(posModule.getX() - this.com.getX(), 2)
					+ Math.pow(posModule.getZ() - this.com.getZ(), 2);
			axisYdistance = Math.sqrt(axisYdistance);

			Ix += axisXdistance * moduleSet.getModulesMass(moduleType[module]);
			Iy += axisYdistance * moduleSet.getModulesMass(moduleType[module]);
			Iz += axisZdistance * moduleSet.getModulesMass(moduleType[module]);

		}
		inertia = new Vector3D(Ix, Iy, Iz);

		if (SimulationConfiguration.isDebug()) {
			System.out.println(
					"min_pos: " + this.minPos + "; max_pos: " + this.maxPos + "; dimensions: " + this.dimensions);
			System.out.println("Total mass of the robot: " + this.robotMass + "; cdm: " + this.com);
		}

		initialHeight = (Math.abs(this.minPos.z) + 0.01);

	}

	// private void treeAnalysis(TreeIndividual tree) {
	// List<Node> nodes = tree.getListNode();
	//
	// int count = 0;
	// for (Node node : nodes) {
	// moduleType[count] = node.getType();
	//
	// if(node.getDad() != null){
	// dadFace[count - 1] = node.getDad().getConnection(node).getDadFace();
	// childOrientation[count - 1] =
	// node.getDad().getConnection(node).getChildrenOrientation();
	// }
	//
	// amplitudeControl[count] = node.getControlAmplitude();
	// angularFreqControl[count] = node.getControlAngularFreq();
	// amplitudeModulation[count] = node.getAmplitudeModulator();
	// phaseControl[count] = (int) node.getControlPhase();
	// frequencyModulation[count] = node.getFrequencyModulator();
	// count++;
	// }
	//
	// }

	private void chromosomeAnalysis() {

		for (int i = 0; i < nModulesMax; i++) {
			moduleType[i] = chromosomeInt[i];
		}
		int[] connections;
		connections = new int[nModulesMax - 1];
		for (int i = 0; i < (nModulesMax - 1); i++) {
			connections[i] = chromosomeInt[nModulesMax + i];
		}

		for (int i = 0; i < (nModulesMax - 1); i++) {
			dadFace[i] = chromosomeInt[2 * nModulesMax - 1 + i];
		}

		for (int i = 0; i < (nModulesMax - 1); i++) {
			childOrientation[i] = chromosomeInt[3 * nModulesMax - 2 + i];
		}


		// Calculating the level of each module
		int[] levelModuleNumber = new int[nModulesMax];
		levelModuleNumber[0] = 1;
		int i = 0;

		nModules = 1;
		int[] level;
		level = new int[nModulesMax];
		for (int currentLevel = 0; currentLevel < nModulesMax; currentLevel++) {
			for (int j = 0; j < levelModuleNumber[currentLevel] && i < nModulesMax; j++) {
				level[i] = currentLevel;
				if (i < nModulesMax - 1) // To avoid the array overflow
				{
					levelModuleNumber[currentLevel + 1] += connections[i];
					nModules += connections[i];
				}
				i++;
			}

		}
		if (nModules > nModulesMax) {
			nModules = nModulesMax;
		}

		// calculate the parent module
		int k = 0;
		for (int ii = 0; ii < nModules - 1; ii++) {
			for (int j = 0; j < connections[ii] && k < (nModules - 1); j++) {
				parentModule[1 + k++] = ii;
			}
		}

		// calculate connection features
		calculateConnectionFeatures(connections);

	}
	
	public void generateRobotTree(){
		tree = new RobotTree();
		List<RobotNode> nodes = new ArrayList<RobotNode>();
		int type;
		int dadnumber;
		int orientation;
		int dadface;
		RobotNode root = new RobotNode(moduleType[0], null);
		nodes.add(root);
		
		RobotNode newNode;
		RobotNode parent;
		Connection conn; 
		for (int j = 1; j < nModules; j++) {
	           type = moduleType[j];
	           dadnumber  = parentModule[j];
	           parent = nodes.get(dadnumber);
	           newNode = new RobotNode(type,parent);
	           dadface = dadFace[j-1];
	           orientation = childOrientation[j-1];
	           conn = new Connection(parent,newNode,dadface,orientation);
	           parent.addChildren(newNode, conn);
	           nodes.add(newNode);
	           
		}
		
		tree.setRootNode(root);
	}

	public RobotTree getTree() {
		return tree;
	}

	public void setTree(RobotTree tree) {
		this.tree = tree;
	}

	private void calculateConnectionFeatures(int[] connections) {

		int[] moduleConnections = new int[nModules];
		moduleConnections[0] = connections[0];
		// If there is more than one module, add the missing connection to the
		// other modules
		if (this.nModules > 1) {
			for (int i = 1; i < nModules - 1; i++) {
				moduleConnections[i] = connections[i] + 1;
			}
			moduleConnections[nModules - 1] = 1;
		}
		this.averageConnectionsPerModule = this.average(moduleConnections);
		this.dispersionConnectionsPerModule = this.standardDeviation(moduleConnections,
				this.averageConnectionsPerModule);
	}

	private double average(int[] vector) {
		double average = 0;
		for (int i = 0; i < this.nModules; i++) {
			average += vector[i];
		}
		return average / this.nModules;
	}

	private double standardDeviation(int[] vector, double mean) {
		double deviation = 0;
		for (int i = 0; i < this.nModules; i++) {
			deviation += Math.pow(vector[i] - mean, 2);
		}
		return Math.sqrt(deviation) / this.nModules;
	}


	public int[] getModuleType() {
		return moduleType;
	}

	public int[] getParentModule() {
		return parentModule;
	}

	public int[] getDadFace() {
		return dadFace;
	}

	public int[] getChildOrientation() {
		return childOrientation;
	}

	public Vector3D[] getModulePosition() {
		return modulePosition;
	}

	public Rotation[] getModuleRotation() {
		return moduleRotation;
	}

	public int getnModules() {
		return nModules;
	}

	public Point3d getMinPos() {
		return minPos;
	}

	public Point3d getMaxPos() {
		return maxPos;
	}

	public Point3d getDimensions() {
		return dimensions;
	}

	public Vector3D getCom() {
		return com;
	}

	public double getRobotMass() {
		return robotMass;
	}

	public double getAverageConnectionsPerModule() {
		return averageConnectionsPerModule;
	}

	public double getDispersionConnectionsPerModule() {
		return dispersionConnectionsPerModule;
	}

	public Vector3D getInertia() {
		return inertia;
	}

}
