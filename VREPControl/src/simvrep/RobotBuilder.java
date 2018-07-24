/* 
 * EDHMOR - Evolutionary designer of heterogeneous modular robots
 * <https://bitbucket.org/afaina/edhmor>
 * Copyright (C) 2015 GII (UDC) and REAL (ITU)
 * Modified for 
 * <http://gtihub.com/rmorenoga/Evolution>
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
package simvrep;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import coppelia.FloatWA;
import coppelia.IntW;
import coppelia.remoteApi;
import simvrep.SimulationConfiguration;
import util.ModuleRotation;
import mpi.MPI;
import represent.ModuleSet;
import represent.ModuleSetFactory;
import represent.RobotFeatureCalculator;
import represent.RobotNode;
import represent.RobotTree;

public class RobotBuilder {
	private String worldbase = "base.world";

	private remoteApi vrep;
	private int clientID;
	private List<Integer> moduleHandlers;
	private List<Integer> forceSensorHandlers;
	private ModuleSet moduleSet;
	private RobotFeatureCalculator robotFeatures;
	private RobotTree tree;
	private String controllername;

	private static final int WORLD_COORD = -1;

	/**
	 * Class constructor to build a robot in the Vrep simulator based on the
	 * chromosome
	 * <p>
	 *
	 * @param vrep
	 *            the remote API library of the Vrep simulator
	 * @param clientID
	 *            the Vrep client to communicate
	 * @param chromosomeDouble
	 *            the chromosome where the morphology and the control parameters
	 *            are stored as doubles
	 * @param scene
	 *            the scene to load in the simulator
	 * 
	 */

	RobotBuilder(remoteApi vrep, int clientID, double[] chromosomeDouble) {
		this(clientID, chromosomeDouble);
		this.vrep = vrep;
		this.clientID = clientID;
	}

	/**
	 * Class constructor to build a robot in the Vrep simulator based on the
	 * chromosome
	 * <p>
	 * 
	 * @param clientID
	 *            the Vrep client to communicate
	 * @param chromo
	 *            the chromosome where the morphology and the control parameters
	 *            are stored as doubles
	 * @param scene
	 *            the scene to load in the simulator
	 *
	 */
	RobotBuilder(int clientID, double[] chromo) {

		// Load the module set
		moduleSet = ModuleSetFactory.getModulesSet();
		controllername = SimulationConfiguration.getController();
		robotFeatures = new RobotFeatureCalculator(chromo);

		robotFeatures.generateRobotTree();

		tree = robotFeatures.getTree();

	}

	/**
	 * Creates the robot in the Vrep simulator based on the chromosome array.
	 * <p>
	 */
	public int createRobot() {

		// calculate the rotation and position of the modules and the force
		// sensors. Load them in Vrep simulator. We also calcualte the
		// dimensions
		// of the robot, the center of mass and other useful features of the
		// robot
		int returncode = robotAssembly();
		return returncode;
	}


	private int robotAssembly() {
		List<RobotNode> nodes = tree.getNodeList();
		Vector3D[] modulePosition = robotFeatures.getModulePosition();
		Rotation[] moduleRotation = robotFeatures.getModuleRotation();
		moduleHandlers = new ArrayList<Integer>();
		forceSensorHandlers = new ArrayList<Integer>();

		int rootModuleHandler = addModule(0);
		if(rootModuleHandler ==-1){
			return -1;
		}
		moduleHandlers.add(rootModuleHandler);
		nodes.get(0).setHandler(rootModuleHandler);

		for (int module = 1; module < robotFeatures.getnModules(); module++) {

			int moduleHandler = addModule(module);
			if(moduleHandler ==-1){
				return -1;
			}
			moduleHandlers.add(moduleHandler);
			nodes.get(module).setHandler(moduleHandler);

			int[] moduleType = robotFeatures.getModuleType();
			int[] parentModule = robotFeatures.getParentModule();
			int modType = moduleType[module];
			int parentModuleType = moduleType[parentModule[module]];
			int conectionFace = robotFeatures.getDadFace()[module - 1]
					% moduleSet.getModulesFacesNumber(parentModuleType);
			int orientation = robotFeatures.getChildOrientation()[(module - 1)]
					% moduleSet.getModuleOrientations(modType);

			// Get the vector which is normal to the face of the parent
			Vector3D normalParentFace = moduleSet.getNormalFaceVector(parentModuleType, conectionFace);

			// Get the vector from the origin of the module to the connection
			// face (in the parent) Origin parent -> Face parent (OFP)
			Vector3D ofp = moduleSet.getOriginFaceVector(parentModuleType, conectionFace);

			// Face of the child to attach the module
			int childFace = moduleSet.getConnectionFaceForEachOrientation(modType, orientation);

			// Rotate module to the correct orientation in Vrep
			rotateModule(moduleHandler, WORLD_COORD, new ModuleRotation(moduleRotation[module]).getEulerAngles());

			// Move module to the correct position in Vrep
			moveModule(moduleHandler, WORLD_COORD, modulePosition[module]);

			// Add Force Sensor in Vrep
			int forceSensor = addForceSensor();
			if(forceSensor == -1){
				return -1;
			}
			
			forceSensorHandlers.add(forceSensor);

			if (normalParentFace.getZ() == 0) {
				double[] forceSensorOrientation = { 0, 0, 0 };
				if (normalParentFace.getY() == 0) {
					forceSensorOrientation[1] = Math.PI / 2;
				} else {
					forceSensorOrientation[0] = Math.PI / 2;
				}
				// Rotate Force Sensor in Vrep
				rotateModule(forceSensor, moduleHandlers.get(parentModule[module]), forceSensorOrientation);
			}
			// Move Force Sensor in Vrep
			double posForceSensor[] = ofp.toArray();
			moveModule(forceSensor, moduleHandlers.get(parentModule[module]), posForceSensor);

			// Set the force sensor as a child of the parent module
			// FIXME: This code will not work in modules with 2 or more dof
			int offset = 0;
			if (!moduleSet.faceBelongsToBasePart(moduleType[parentModule[module]], conectionFace)) {
				offset = 2;
			}
			setObjectParent(forceSensor, moduleHandlers.get(parentModule[module]) + 1 + offset);

			// Set the child module as a child of the foce sensor
			// FIXME: This code will not work in modules with 2 or more dof
			if (moduleSet.faceBelongsToBasePart(modType, childFace)) {
				setObjectParent(moduleHandler + 1, forceSensor);
			} else {
				setObjectParent(moduleHandler + 3, forceSensor);
				setObjectParent(moduleHandler + 2, moduleHandler + 3);
				setObjectParent(moduleHandler + 1, moduleHandler + 2);
				setObjectParent(moduleHandler, moduleHandler + 1);
			}
			
		}

		double initialHeight = (Math.abs(robotFeatures.getMinPos().z) + 0.001);

		if (this.worldbase.contains("sueloRugoso")) {
			initialHeight += 0.15;
		}

		if (this.worldbase.contains("manipulator")) {
			initialHeight = 1;
		}

		//System.out.println("initialHeight: " + initialHeight);
		if (initialHeight < (0.055 / 2))
			initialHeight += (0.055 / 2);
		// Move the robot up
		double[] posIni = { 0, 0, initialHeight };
		moveModule(moduleHandlers.get(0), -1, posIni);
		
		return 1;
		
		
	}

	public RobotTree getTree() {
		return tree;
	}

	private int addModule(int moduleNumber) {
		String modelPath = modelPath(robotFeatures.getModuleType()[moduleNumber]);

		IntW moduleHandle = new IntW(0);
		// clientID,final String modelPathAndName, int options, IntW baseHandle,
		// int operationMode
		int ret = vrep.simxLoadModel(clientID, modelPath, 0, moduleHandle, remoteApi.simx_opmode_blocking);

		if (ret == remoteApi.simx_return_ok) {
			// System.out.format("Model loaded correctly: %d\n",
			// parentModuleHandle.getValue());
			return moduleHandle.getValue();
		} else {
			System.err.format(
					"VrepCreateRobot, addModule Function: Remote API function call returned with error code: %d\n",
					ret);
			System.err
					.println("VrepCreateRobot, addModule Function: Check that the model path is correct: " + modelPath);
			//System.exit(-1);
			return -1;
		}

	}

	private int addForceSensor() {
		String modelPath = forceSensorPath();

		IntW forceSensorHandle = new IntW(0);
		// clientID,final String modelPathAndName, int options, IntW baseHandle,
		// int operationMode
		int ret = vrep.simxLoadModel(clientID, modelPath, 0, forceSensorHandle, remoteApi.simx_opmode_blocking);

		if (ret == remoteApi.simx_return_ok) {
			// System.out.format("Model loaded correctly: %d\n",
			// parentModuleHandle.getValue());
			return forceSensorHandle.getValue();
		} else {
			System.out.format(
					"VrepCreateRobot, addForceSensor Function: Remote API function call returned with error code: %d\n",
					ret);
			System.err.println("VrepCreateRobot, addForceSensor Function: Check that the force sensor path is correct: "
					+ modelPath);
			//System.exit(-1);
			return -1;
		}

	}

	private void rotateModule(int moduleHandler, int parentHandler, double[] rotation) {
		// int simxSetObjectPosition(int clientID,int objectHandle, int
		// relativeToObjectHandle, final FloatWA position, int operationMode)

		FloatWA rot = new FloatWA(3);
		rot.getArray()[0] = (float) (rotation[0]);
		rot.getArray()[1] = (float) (rotation[1]);
		rot.getArray()[2] = (float) (rotation[2]);

		int ret = vrep.simxSetObjectOrientation(clientID, moduleHandler, parentHandler, rot,
				remoteApi.simx_opmode_oneshot);
	}

	private void moveModule(int moduleHandler, int parentHandler, double[] position) {
		// int simxSetObjectPosition(int clientID,int objectHandle, int
		// relativeToObjectHandle, final FloatWA position, int operationMode)
		FloatWA pos = new FloatWA(3);
		pos.getArray()[0] = (float) position[0];
		pos.getArray()[1] = (float) position[1];
		pos.getArray()[2] = (float) position[2];

		int ret = vrep.simxSetObjectPosition(clientID, moduleHandler, parentHandler, pos,
				remoteApi.simx_opmode_oneshot);
	}

	private void moveModule(int moduleHandler, int parentHandler, Vector3D vec) {
		double pos[] = vec.toArray();
		moveModule(moduleHandler, parentHandler, pos);
	}

	private void setObjectParent(int moduleHandler, int parentHandler) {
		// int simxSetObjectParent(int clientID,int objectHandle,int
		// parentObject,boolean keepInPlace,int operationMode);

		int ret = vrep.simxSetObjectParent(clientID, moduleHandler, parentHandler, true, remoteApi.simx_opmode_oneshot);
	}

	private String modelPath(int moduleType) {
		String path = "models/Module/";
		path += moduleSet.getModuleSetName() + "/"; // moduleSetName
		path += controllername + "/";
		path += moduleSet.getModuleName(moduleType) + ".ttm"; // moduleName
		return path;
	}

	private String forceSensorPath() {
		String path = "models/Module/";
		path += moduleSet.getModuleSetName() + "/"; // moduleSetName
		path += "forceSensor.ttm"; // moduleName
		return path;
	}

	public List<Integer> getModuleHandlers() {
		return moduleHandlers;
	}
	
	public int[] getModuleHandlersint(){
		Integer[] dummy  = new Integer[moduleHandlers.size()];
		int[] dummyint = new int[moduleHandlers.size()];
		moduleHandlers.toArray(dummy);
		for (int i = 0; i<moduleHandlers.size();i++){
			dummyint[i] = dummy[i];
			//System.out.println(dummyint[i]);
		}
		return dummyint;
	}

	public List<Integer> getForceSensorHandlers() {
		return forceSensorHandlers;
	}

	

	public int[] getModuleType() {
		return robotFeatures.getModuleType();
	}
}
