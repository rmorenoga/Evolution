package simpletests;

import static java.lang.Math.abs;

import org.apache.commons.math3.geometry.euclidean.threed.CardanEulerSingularityException;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.util.FastMath;

import coppelia.CharWA;
import coppelia.FloatWA;
import coppelia.remoteApi;

public class SimpleRotationTest {

	static Rotation rotation;
	
	public static void main(String[] args) throws InterruptedException {
		double[][] rotationtable = new double[][] { // x,y,z All y angles at 1.571 and -1.571 in the first three rows are singularities
				{ 1, 0, 0, 0 }, // 0;0,0,0
				{ Math.sqrt(2) / 2, 0, Math.sqrt(2) / 2, 0 }, // 1;0,1.571,0 Math3S 0,-1.571,-0.785
				{ 0, 0, 1, 0 }, // 2;-3.142,0,-3.142
				{ Math.sqrt(2) / 2, 0, -Math.sqrt(2) / 2, 0 }, // 3;0,-1.571,0 Math3S 0,1.571,-0.785
				{ Math.sqrt(2) / 2, 0, 0, Math.sqrt(2) / 2 }, // 4;0,0,1.571 Math3 0,0,-1.571
				{ 0.5, 0.5, 0.5, 0.5 }, // 5;1.571,1.571,0 Math3 -1.571,0,-1.571
				{ 0, Math.sqrt(2) / 2, Math.sqrt(2) / 2, 0 }, // 6;-3.142,0,-1.571 
				{ 0.5, -0.5, -0.5, 0.5 }, // 7;-1.571,-1.571,0 Math3 1.571,0,-1.571
				{ Math.sqrt(2) / 2, 0, 0, -Math.sqrt(2) / 2 }, // 8;0,0,-1.571 Math3 0,0,1.571 
				{ 0.5, -0.5, 0.5, -0.5 }, // 9;-1.571,1.571,0 Math3 1.571,0,1.571
				{ 0, -Math.sqrt(2) / 2, Math.sqrt(2) / 2, 0 }, // 10;-3.142,0,1.571 
				{ 0.5, 0.5, -0.5, -0.5 }, // 11;1.571,-1.571,0 Math3 -1.571,0,1.571
				{ Math.sqrt(2) / 2, Math.sqrt(2) / 2, 0, 0 }, // 12;1.571,0,0 Math3 -1.571,0,0
				{ 0.5, 0.5, 0.5, -0.5 }, // 13;1.571,0,-1.571 Math3S 0,-1.571,-0.785
				{ 0, 0, Math.sqrt(2) / 2, -Math.sqrt(2) / 2 }, // 14;1.571,0,-3.142
				{ 0.5, 0.5, -0.5, 0.5 }, // 15;1.571,0,1.571 Math3S 0,1.571,-0.785
				{ 0, 1, 0, 0 }, // 16;-3.142,0,0
				{ 0, Math.sqrt(2) / 2, 0, -Math.sqrt(2) / 2 }, // 17;3.142,-1.571,0 Math3S 0,-1.571,-0.785
				{ 0, 0, 0, 1 }, // 18;0,0,-3.142 
				{ 0, Math.sqrt(2) / 2, 0, Math.sqrt(2) / 2 }, // 19;3.142,1.571,0 Math3S 0,-1.571,-0.785
				{ Math.sqrt(2) / 2, -Math.sqrt(2) / 2, 0, 0 }, // 20;-1.571,0,0 Math3 1.571,0,0
				{ 0.5, -0.5, 0.5, 0.5 }, // 21;-1.571,0,1.571 Math3S 0,-1.571,-0.785
				{ 0, 0, Math.sqrt(2) / 2, Math.sqrt(2) / 2 }, // 22;-1.571,0,-3.142
				{ 0.5, -0.5, -0.5, -0.5 },// 23;-1.571,0,-1.571 Math3S 0,1.571,0.785
				{ 0.146  , 0.854,  -0.354,  -0.354,  }, //24;Half quadrant -3.139,-0.7866,0.7879 Math3 -2.5258,-0.525,0.954
				{       0.280,    -0.676,  -0.261, 0.630}, //25;Near singularity -2.3611,-1.506,-0.001 Math3 1.64,-0.783,-1.470
				{ 0.278, -0.670, -0.263, 0.636} // 26;In Singularity -2.3549,-1.5707,0 Math3 1.622,-1.783,-1.496
		};
		int i = 5;
		remoteApi vrep = new remoteApi(); // Create simulator control object
		vrep.simxFinish(-1); // just in case, close all opened connections
		// Connect with the corresponding simulator remote server
		int clientID = vrep.simxStart("127.0.0.1", 19997, true, true, 5000, 5);
		
		
		//for (int i = 0; i < 24; i++) {
			
			rotation = new Rotation(rotationtable[i][0], rotationtable[i][1], rotationtable[i][2], rotationtable[i][3],
					false);
			// rotation = new Rotation(0.713,0,0.701,0,true);
			double[] angleso = new double[3];
			//angleso = getEulerAnglesMath3();
			angleso = getEulerAnglesEuclid();
			
			System.out.println("Angles " + angleso[0] + ", " + angleso[1] + ", " + angleso[2]);
			// TODO Auto-generated method stub

			CharWA strCP;
			float[] eulerangles = new float[] { (float) angleso[0], (float) angleso[1], (float) angleso[2] };
			FloatWA ControlParam = new FloatWA(3);
			ControlParam.setArray(eulerangles);
			strCP = new CharWA(1);
			strCP.setArray(ControlParam.getCharArrayFromArray());

			vrep.simxSetStringSignal(clientID, "ControlParam", strCP, vrep.simx_opmode_oneshot_wait);

			//vrep.simxStartSimulation(clientID, vrep.simx_opmode_oneshot_wait);
			
			//vrep.simxStopSimulation(clientID, vrep.simx_opmode_oneshot_wait);
			//vrep.simxPauseSimulation(clientID,vrep.simx_opmode_oneshot_wait );
			//Thread.sleep(5000);

			

		//}
		
		vrep.simxFinish(clientID);

	}
	
	public static double[] getEulerAnglesEuclid(){
		double[] angles = new double[3];
		
		double w = rotation.getQ0();
		double x = rotation.getQ1();
		double y = rotation.getQ2();
		double z = rotation.getQ3();

		angles[0] = 0;
		angles[1] = 0;
		angles[2] = 0;

		double test = w * y + x * z;
		if (test > 0.499) {
			angles[0] = 2 * FastMath.atan2(x, w);
			angles[1] = Math.PI / 2;
			angles[2] = 0;
		} else if (test < -0.499) {
			angles[0] = 2 * FastMath.atan2(x, w);
			angles[1] = -Math.PI / 2;
			angles[2] = 0;
		} else {
			 angles[0] = FastMath.atan2(2*(w*x-y*z),1-2*((x*x)+(y*y)));
			 angles[1] = FastMath.asin(2*(w*y+x*z));
			 angles[2] = FastMath.atan2(2*(w*z-x*y),1-2*((y*y)+(z*z)));
		}
		System.out.println("Quaternion: " + rotation.getQ0() + " " + rotation.getQ1() + " " + rotation.getQ2() + " "
				+ rotation.getQ3());
		System.out.println("Test = "+test);
		return angles;
		
	}

	public static double[] getEulerAnglesMath3() {

		double[] angles = new double[3];
		
		double w = rotation.getQ0();
		double x = rotation.getQ1();
		double y = rotation.getQ2();
		double z = rotation.getQ3();
		
		System.out.println("Quaternion: " + rotation.getQ0() + " " + rotation.getQ1() + " " + rotation.getQ2() + " "
				+ rotation.getQ3());

		try {
			// if there are no singuralities, its easy to obtain the angles
		angles = rotation.getAngles(RotationOrder.XYZ);
		} catch (CardanEulerSingularityException singularityException) {
			//If there are singularities, there are a lot of combinations of the 
            //Euler angles that produce this rotation. We select one of these 
            //rotations (generated with the same code as the library is using)

            Vector3D v1 = rotation.applyTo(Vector3D.PLUS_K);
            Vector3D v2 = rotation.applyInverseTo(Vector3D.PLUS_I);
                                                        
            angles[0] = FastMath.atan2(-v1.getY(), v1.getZ());
            if(abs(v1.getY())<0.1 && abs(v1.getZ()) <0.1)
                System.out.println("Problem in the first angle: " + v1.getY() + ", "+ v1.getZ());
            
            if( abs(v1.getX())>1 ){
                System.out.println("Quat. problem.: " + rotation.getQ0() + " "+ rotation.getQ1()+ " " + rotation.getQ2()+" " + rotation.getQ3());
                if(v1.getX()>0){
                    angles[1] = FastMath.asin(1);
                 }else{
                    angles[1] = FastMath.asin(-1);
                }
            }else{  
                    angles[1] = FastMath.asin(v1.getX());      
            }       
            
            angles[2] = FastMath.atan2(-(v2.getY()), v2.getX());
            if(abs(v2.getY())<0.1 && abs(v2.getX()) <0.1)
                System.out.println("Problem in the second angle" + v2.getY() + ", "+ v2.getX());
            
            angles[2] = FastMath.atan2(-1, 1);
            
        

        //Vector3D axis = rotation.getAxis();
        //System.out.println("Axis: " + axis.getX() + ", " + axis.getY() + ", " + axis.getZ());
        //System.out.println("Angle of rotation: " + rotation.getAngle());
        System.out.println("RPY Rotation (new method): " + angles[0] + " "+ angles[1] + " " + angles[2]);
        System.out.println("Quaternion: " + rotation.getQ0() + " "+ rotation.getQ1()+ " " + rotation.getQ2()+" " + rotation.getQ3());
       
        double[] anglesDgr = new double[3];
        anglesDgr[0] = angles[0]*180/Math.PI;
        anglesDgr[1] = angles[1]*180/Math.PI;
        anglesDgr[2] = angles[2]*180/Math.PI;
        
        if ( ( ((int) round(anglesDgr[0]))%90)>0 ||( ((int) (round(anglesDgr[1])))%90)>0 || ( ( (int) round(anglesDgr[2]))%90)>0){
            System.out.println("Problem!!!\n");
            System.out.println( round(anglesDgr[0]) + " " + round(anglesDgr[1]) + " " + round(anglesDgr[0]));
        }
        System.out.println( round(anglesDgr[0]) + " " + round(anglesDgr[1]) + " " + round(anglesDgr[0]));
		}
		
		
		
		return angles;

	}

private static double round(double d){
    if(d > -0.1 && d < 0.1)
        return 0;
    if(d > 89.9 && d < 90.1)
        return 90;
    if(d > 179.9 && d < 180.1)
        return 180;
    if(d > 269.9 && d < 270.1)
        return 270;
    if(d > 359.9 && d < 360.1)
        return 0;
    if(d > -90.1 && d < -89.9)
        return -90;
    if(d > -180.1 && d < -179.9)
        return -180;
    if(d > -270.1 && d < -269.9)
        return -270;
    if(d > -360.1 && d < -359.9)
        return 0;
    return d;
    
}

}
