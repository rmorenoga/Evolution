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
package util;

import static java.lang.Math.abs;
import represent.ModuleSet;
import represent.ModuleSetFactory;
import org.apache.commons.math3.geometry.euclidean.threed.CardanEulerSingularityException;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.util.FastMath;

/**
 * ModuleRotation.java Created on 21/11/2015
 *
 * @author Andres Faiña <anfv  at itu.dk>
 */
public class ModuleRotation {

    private final Rotation rotation;

    /**
     * Calculates the rotation that rotates the module in an specific
     * orientation.
     *
     * <p>
     *
     * @param type the type of the selected module
     * @param orientation the orientation to connect this module to its parent
     * @param parentFaceNormal the normal vector to the face of the parent
     * module where this module will be attached
     *
     */
    public ModuleRotation(int type, int orientation, Vector3D parentFaceNormal) {

        //the face of the module has to face the face of the parent, 
        //so we need to negate the normal of the face of the parent
        parentFaceNormal = parentFaceNormal.negate();

        //obtain the module set
        ModuleSet moduleSet = ModuleSetFactory.getModulesSet();

        //get the face where the module will be attached to its parent
        int faceToConnect = moduleSet.getConnectionFaceForEachOrientation(type, orientation);

        //get the normal of the child face where it will be connected 
        Vector3D childFaceNormal = moduleSet.getNormalFaceVector(type, faceToConnect);
        //System.out.println("normalFaceChild: " + normalFaceChild.toString());

        //Obtain a rotation matrix thar rotates the module to align the 
        //child face with the face of the parent
        Rotation firstRotation = new Rotation(childFaceNormal, parentFaceNormal);
                
        //Calculate a new rotation that rotates the module about the normal 
        //of the parent face normal, the degrees are defined by the orientation
        Rotation rotationAboutNormal = new Rotation(parentFaceNormal, moduleSet.getRotationAboutTheNormalForEachOrientation(type, orientation));

        //Combine both rotations 
        rotation = rotationAboutNormal.applyTo(firstRotation);
    }

    /**
     * Calculates the rotation that rotates the module in an specific
     * orientation.
     *
     * <p>
     *
     * @param rot the rotation of the module
     *
     */
    public ModuleRotation(Rotation rot) {
        rotation = rot;
    }

    /**
     * Returns the Euler angles of this rotation.
     *
     * <p>
     *
     * @return the Euler angles to rotate the module
     */
    public double[] getEulerAngles() {
        return getEulerAnglesEuclid();
        //return getEulerAnglesMath3();
    }

    private double[] getEulerAnglesEuclid() {
        double[] angles = new double[3];

        //Get quaternion components
        double w = -1*rotation.getQ0();
        double x = rotation.getQ1();
        double y = rotation.getQ2();
        double z = rotation.getQ3();

        angles[0] = 0;
        angles[1] = 0;
        angles[2] = 0;

        double test = w * y + x * z; //Test for a singularity when test gets near 0.5 or -0.5
        if (test > 0.499) { //Can be adjusted
            angles[0] = 2 * FastMath.atan2(x, w);
            angles[1] = Math.PI / 2;
            angles[2] = 0;
        } else if (test < -0.499) { //Can be adjusted
            angles[0] = 2 * FastMath.atan2(x, w);
            angles[1] = -Math.PI / 2;
            angles[2] = 0;
        } else {
            angles[0] = FastMath.atan2(2 * (w * x - y * z), 1 - 2 * ((x * x) + (y * y)));
            angles[1] = FastMath.asin(2 * (w * y + x * z));
            angles[2] = FastMath.atan2(2 * (w * z - x * y), 1 - 2 * ((y * y) + (z * z)));
        }
//        System.out.println("Quaternion: " + rotation.getQ0() + " " + rotation.getQ1() + " " + rotation.getQ2() + " "
//                + rotation.getQ3());
//        System.out.println("Test = " + test);
//        System.out.println("Euler Angles: " + angles[0]+ " "+ angles[1] + " " + angles[2]);
        return angles;
    }

    /***
     * Deprecated. Calculates the Euler Angles using the library and handles 
     * singularities with a custom code. It has been depreciated as some 
     * modules are not correctly aligned in a few cases
     * @return  euler angles to rotate the module
     */
    private double[] getEulerAnglesMath3() {
        double[] angles = new double[3];

        try {
            //if there are no singuralities, its easy to obtain the angles
            angles = rotation.getAngles(RotationOrder.XYZ);
        } catch (CardanEulerSingularityException singularityException) {
            //If there are singularities, there are a lot of combinations of the 
            //Euler angles that proce this rotation. We select one of these 
            //rotations (generated with the same code as the library is using)

            Vector3D v1 = rotation.applyTo(Vector3D.PLUS_K);
            Vector3D v2 = rotation.applyInverseTo(Vector3D.PLUS_I);

            angles[0] = FastMath.atan2(-v1.getY(), v1.getZ());
            if (abs(v1.getY()) < 0.1 && abs(v1.getZ()) < 0.1) {
                System.out.println("Problem in the first angle: " + v1.getY() + ", " + v1.getZ());
            }

            if (abs(v1.getX()) > 1) {
                System.out.println("Quat. problem.: " + rotation.getQ0() + " " + rotation.getQ1() + " " + rotation.getQ2() + " " + rotation.getQ3());
                if (v1.getX() > 0) {
                    angles[1] = FastMath.asin(1);
                } else {
                    angles[1] = FastMath.asin(-1);
                }
            } else {
                angles[1] = FastMath.asin(v1.getX());
            }

            angles[2] = FastMath.atan2(-(v2.getY()), v2.getX());
            if (abs(v2.getY()) < 0.1 && abs(v2.getX()) < 0.1) {
                System.out.println("Problem in the second angle" + v2.getY() + ", " + v2.getX());
            }

//            angles[2] = FastMath.atan2(-1, 1);
        }

        //Vector3D axis = rotation.getAxis();
        //System.out.println("Axis: " + axis.getX() + ", " + axis.getY() + ", " + axis.getZ());
        //System.out.println("Angle of rotation: " + rotation.getAngle());
        System.out.println("RPY Rotation (new method): " + angles[0] + " " + angles[1] + " " + angles[2]);
        System.out.println("Quaternion: " + rotation.getQ0() + " " + rotation.getQ1() + " " + rotation.getQ2() + " " + rotation.getQ3());

        double[] anglesDgr = new double[3];
        anglesDgr[0] = angles[0] * 180 / Math.PI;
        anglesDgr[1] = angles[1] * 180 / Math.PI;
        anglesDgr[2] = angles[2] * 180 / Math.PI;

        if ((((int) round(anglesDgr[0])) % 90) > 0 || (((int) (round(anglesDgr[1]))) % 90) > 0 || (((int) round(anglesDgr[2])) % 90) > 0) {
            System.out.println("Problem!!!\n");
            System.out.println(round(anglesDgr[0]) + " " + round(anglesDgr[1]) + " " + round(anglesDgr[0]));
        }
        System.out.println(round(anglesDgr[0]) + " " + round(anglesDgr[1]) + " " + round(anglesDgr[0]));

        return angles;
    }

    private double round(double d) {
        if (d > -0.1 && d < 0.1) {
            return 0;
        }
        if (d > 89.9 && d < 90.1) {
            return 90;
        }
        if (d > 179.9 && d < 180.1) {
            return 180;
        }
        if (d > 269.9 && d < 270.1) {
            return 270;
        }
        if (d > 359.9 && d < 360.1) {
            return 0;
        }
        if (d > -90.1 && d < -89.9) {
            return -90;
        }
        if (d > -180.1 && d < -179.9) {
            return -180;
        }
        if (d > -270.1 && d < -269.9) {
            return -270;
        }
        if (d > -360.1 && d < -359.9) {
            return 0;
        }
        return d;

    }

    public Rotation getRotation() {
        return rotation;
    }

}
