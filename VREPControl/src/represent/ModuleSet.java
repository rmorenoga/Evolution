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

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * ModuleSet is the base abstract class for storing all the properties of one
 * set of modules.
 * <p>
 * This class provides the declaration of all the needed variables to define a
 * set of modules. In addition, it provides the methods to return the stored
 * values. This functions guarantee that the stored values are immutable.
 * <p>
 * To create a module set, inherit this class and set the value of the variables
 * in the constructor. See @see modules.OldEdhmorModuleSet.java for an example.
 *
 * ModuleSetFactory.java Created on 18/10/2015
 *
 * @author Andres Faiña <anfv  at itu.dk>
 */
public abstract class ModuleSet {

    /**
     * The name of this set
     */
    protected String moduleSetName;

    /**
     * The name of the different modules in this set
     */
    protected String[] moduleName;

    /**
     * The Number of different type of modules in this set
     */
    protected int modulesTypeNumber;

    /**
     * The maximum number of faces in one module 
     */
    protected int maxFaceNumberInOneModule;

    /**
     * The number of connection faces for each type of modules
     */
    protected int modulesFacesNumber[];

    /**
     * The number of connection faces which belong to the first body of the 
     * module (base part of the module), the others belong to the actuators part 
     * (the part linked by a joint with the base part)
     */
    protected int modulesBaseFacesNumber[];

    /**
     * The number of posible orientations for the module
     */
    protected int[] moduleOrientations;

    /**
     * The face where the module is connected to its parent for each type of 
     * module and for each orientation  
     */
    protected int[][] connectionFaceForEachOrientation;

    /**
     * The angle that the module is rotated about the normal vector of its 
     * connection face for each type of module and for each orientation
     */
    protected double[][] rotationAboutTheNormalForEachOrientation;

    /**
     * The vector from the origin of the module to the center of the face for 
     * each type of module and for each face of the module
     */
    protected Vector3D[][] originFaceVector;

    /**
     * The normal face vector for each type of module and for each face of the 
     * module
     */
    protected Vector3D[][] normalFaceVector;
    
    /**
     * The symmetric face of one face. Take care, it can return -1 if no 
     * symmetric face exists. 
     */
    protected int[][] symmetricFace;

    /**
     * The mass for each type of module
     */
    protected double modulesMass[];

    //Control parameters
    
    /**
     * The maximum amplitude for each type of module
     */
    protected double[] modulesMaxAmplitude;

    /**
     * The maximum angular frequency for each type of module
     */
    protected double[] modulesMaxAngularFrequency;

    /**
     * The size of boundingBox for each type of module, it is represented as (width,height,longth).Units:meter
     */
    protected Vector3D[] boundingBox;
    
    /**
     * ModuleSet creates the arrays for the variables. It has to be called in 
     * the firts line of the constructor in the classes which inherit form this 
     * class. 
     * 
     * @param modulesTypeNumber             the number of different types of 
     *                                      modules
     * @param maxFaceNumberInOneModule      the maximum number of faces in one 
     *                                      module
     * @param maxOrientations               the maximum number of orientations 
     *                                      in one module
     */
    protected ModuleSet(int modulesTypeNumber, int maxFaceNumberInOneModule, int maxOrientations) {
        this.modulesTypeNumber = modulesTypeNumber;
        this.maxFaceNumberInOneModule = maxFaceNumberInOneModule;
        moduleName = new String[modulesTypeNumber];
        modulesFacesNumber = new int[modulesTypeNumber];
        modulesBaseFacesNumber = new int[modulesTypeNumber];
        moduleOrientations = new int[modulesTypeNumber];
        connectionFaceForEachOrientation = new int[modulesTypeNumber][maxOrientations];
        rotationAboutTheNormalForEachOrientation = new double[modulesTypeNumber][maxOrientations];
        modulesMass = new double[modulesTypeNumber];
        originFaceVector = new Vector3D[modulesTypeNumber][maxFaceNumberInOneModule];
        normalFaceVector = new Vector3D[modulesTypeNumber][maxFaceNumberInOneModule];
        modulesMaxAmplitude = new double[modulesTypeNumber];
        modulesMaxAngularFrequency = new double[modulesTypeNumber];
        boundingBox = new Vector3D[modulesTypeNumber];
        symmetricFace = new int[modulesTypeNumber][maxFaceNumberInOneModule];
    }

    public int getModulesBaseFacesNumber(int type) {
        return modulesBaseFacesNumber[type];
    }

    public int getModulesFacesNumber(int type) {
        return modulesFacesNumber[type];
    }

    public int getModuleOrientations(int type) {
        return moduleOrientations[type];
    }

    public int getConnectionFaceForEachOrientation(int type, int orientation) {
        return connectionFaceForEachOrientation[type][orientation];
    }

    public double getRotationAboutTheNormalForEachOrientation(int type, int orientation) {
        return rotationAboutTheNormalForEachOrientation[type][orientation];
    }

    public int getModulesTypeNumber() {
        return modulesTypeNumber;
    }

    public Vector3D getNormalFaceVector(int type, int face) {
        return new Vector3D(normalFaceVector[type][face].getX(),
                normalFaceVector[type][face].getY(),
                normalFaceVector[type][face].getZ());
    }

    public Vector3D getOriginFaceVector(int type, int face) {
        return new Vector3D(originFaceVector[type][face].getX(),
                originFaceVector[type][face].getY(),
                originFaceVector[type][face].getZ());
    }

    public double[] getModulesMass() {
        return modulesMass;
    }

    public double getModulesMass(int type) {
        return modulesMass[type];
    }

    public double getModulesMaxAmplitude(int type) {
        return modulesMaxAmplitude[type];
    }

    public double getModulesMaxAngularFrequency(int type) {
        return modulesMaxAngularFrequency[type];
    }

    public String getModuleSetName() {
        return moduleSetName;
    }

    public String getModuleName(int type) {
        return moduleName[type];
    }

    public boolean faceBelongsToBasePart(int moduleType, int faceNumber) {
        return faceNumber < getModulesBaseFacesNumber(moduleType);
    }
    
    public Vector3D getboundingBox(int type) {
        return new Vector3D(boundingBox[type].getX(),
                boundingBox[type].getY(),
                boundingBox[type].getZ());
    }
    
    public int getSymmetricFace(int type, int face){
        return symmetricFace[type][face];
    }
}
