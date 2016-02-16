package Tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

import coppelia.CharWA;
import coppelia.FloatWA;
import coppelia.IntW;
import coppelia.IntWA;
import coppelia.remoteApi;

public class GeneralizationTest {

	// public float alpha = 0.7f;// Look into the Run simulation method to set
	// alpha

	public static void main(String[] args) {


		
		float[] result = new float[6];
		
		//float[][] indiv=ReadFile("IndivGen.txt");
		
		float[][] indiv = new float[][]{
				{0.8231876101554202f,0.3938365318859707f,-0.46775484270457374f},
				{-0.5583003059086579f,-0.09817694862285438f,-0.5303740382467594f},
				{0.9044620383137466f,-0.27357761196792685f,-0.725597730699019f},
				{-0.851349699953341f,0.04913730639336216f,0.3470308523993737f},
				{0.7511159257767415f,0.19193150551373317f,0.24099098615274095f},
				{0.8352718635010198f,-0.28427773832456654f,0.2539889966954673f},
				{0.9573347644699126f,0.0776744853586044f,0.359043665830474f},
				{-0.9679811472273032f,-0.3807192562514106f,-0.7098479165221826f},
				{-0.6509005318349372f,0.278574287255091f,0.21247765375697278f},
				{0.6278656953395899f,0.038620083013802375f,0.30759121055001953f},
				{-0.6752127063155836f,-0.281697011072645f,-0.6694425530843746f},
				{-0.795631412516664f,-6.323246590600035E-5f,0.280598879502242f},
				{0.313856922350894f,-0.4674805016274659f,0.8379004560704518f},
				{-0.7551055183638457f,-0.2638098999167174f,-0.5301696985507128f},
				{0.8349396011993928f,-0.056962449405237356f,0.21151191621747842f},
				{-0.18009033086813545f,0.04106337547385404f,0.9999997808656144f},
				{-0.880452105147841f,0.05920147581839901f,0.2962613696292334f},
				{-0.9703862844244017f,0.39103024004720743f,-0.7240238499686114f},
				{0.872350320877231f,0.0567790537448937f,0.35200170244605666f},
				{0.4343205136390041f,-0.2959409916046962f,-0.8903250143186663f},
				{-1.0f,-0.35963312933775854f,-0.7154144036710405f},
				{1.0f,-0.3422449971171529f,-0.7065578293682835f},
				{-0.8670387046730206f,-0.32161179089752345f,-0.7207622355429326f},
				{1.0f,0.33955005485682027f,-0.7046479071701693f},
				{1.0f,-0.2989448863933685f,-0.7104593288495897f},
				{-0.9993050444883625f,-0.3269876338163299f,-0.7141389763098467f},
				{-1.0f,-0.34631719133020195f,-0.7075578123213507f},
				{1.0f,-0.3524158785354704f,-0.7144879926181861f},
				{-1.0f,-0.3329614165830896f,-0.7063852512591036f},
				{1.0f,-0.3400496163957518f,-0.709139543637078f} };
				
				
				
		for (int i = 0; i < indiv.length ;i++ ){
			
			result = RunTest(indiv[i][0],indiv[i][1],indiv[i][2]);
			WResultsFile(indiv[i],result);
			
		}
		

	}
	private static void WResultsFile(float[] indiv, float[] result) {
		FileWriter file= null;
		PrintWriter pw = null;
		try {
			file = new FileWriter("IndGenResult.txt", true);
			pw = new PrintWriter(file);
			
			pw.println(indiv[0]+","+indiv[1]+","+indiv[2]+","+result[0]+","+result[1]+","+result[2]+","+result[3]+","+result[4]+","+result[5]);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != file)
					file.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
	}
	private static float[][] ReadFile(String filepath) {

		List<String> list = new ArrayList<String>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(filepath));
			String str;

			while((str = in.readLine())!=null){
				list.add(str);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		float[][] values = new float[list.size()][3];
		for (int i = 0; i < list.size(); i++){
			System.out.println(list.get(i));
			String[] sep = list.get(i).split(",");
			values[i][0] = Float.parseFloat(sep[0]);
			values[i][1] = Float.parseFloat(sep[1]);
			values[i][2] = Float.parseFloat(sep[2]);
		}


//		for (int i = 0; i < values.length ;i++ ){
//			System.out.println(values[i][0]+"*"+values[i][1]+"*"+values[i][2]);
//		}
		return values;
	}
		static float[] RunTest (float ampli, float offset, float phase){
		// Use simulator instance 0 or the default remoteAPI server
		// configuration
		int simNumber = 0;
		
		phase = phase * (float) Math.PI;
		
//		ampli = -0.9703862844244017f;
//		offset = 0.39103024004720743f;
//		phase = -0.7240238499686114f * (float) Math.PI;

		// Pack Floats into one String data signal
		FloatWA ControlParam = new FloatWA(3);
		float[] CP = new float[3];
		CP[0] = ampli;
		CP[1] = offset;
		CP[2] = phase;
		ControlParam.setArray(CP);
		CharWA strCP = new CharWA(1);
		strCP.setArray(ControlParam.getCharArrayFromArray());

		// Morphology Parameters
		int Numberofmodules = 8;
		int[] orientation = new int[] { 1, 0, 1, 0, 1, 0, 1, 0 };

		// Simulation Parameters
		int MaxTime = 120;

		// Pack Integers into one String data signal
		IntWA NumberandOri = new IntWA(Numberofmodules + 3);
		int[] NO = new int[Numberofmodules + 3];
		NO[0] = Numberofmodules;
		NO[1] = MaxTime;
		NO[2] = 0;
		for (int i = 3; i < Numberofmodules + 3; i++) {
			NO[i] = orientation[i - 3];
		}
		NumberandOri.setArray(NO);
		CharWA strNO = new CharWA(1);
		strNO.setArray(NumberandOri.getCharArrayFromArray());

		// 3 Sub-environment maze parameters, create a function for this
		char[][] subenvperm = new char[][] {
				{ 's', 'l', 's', 'b', 's', 'r', 's' },
				{ 's', 'l', 's', 's', 'r', 's', 'b' },
				{ 'b', 's', 'l', 's', 's', 'r', 's' },
				{ 'b', 's', 'r', 's', 's', 'l', 's' },
				{ 's', 'r', 's', 's', 'l', 's', 'b' },
				{ 's', 'r', 's', 'b', 's', 'l', 's' } };

		// Maze Parameters (Already a string)
		char[] mazeseq = new char[] { 's' }; // Default Maze Sequence
		CharWA strSeq = new CharWA(1);
		strSeq.setArray(mazeseq);

		// Array that receives fitness from the simulator or signals a crash
		float[] rfitness = new float[3];

		// Fitness to be returned by each combination of subenvironments for a
		// given individual
		float[] fitness = new float[] { 1, 1, 1, 1, 1, 1 };

		// Number of retries in case of simulator crash
		int maxTries = 5;

		// Retry if there is a simulator crash
		for (int j = 0; j < maxTries; j++) {
			// Simulator interaction start
			remoteApi vrep = new remoteApi(); // Create simulator control object
			vrep.simxFinish(-1); // just in case, close all opened connections
			// Connect with the corresponding simulator remote server
			int clientID = vrep.simxStart("127.0.0.1", 19997 - simNumber, true,
					true, 5000, 5);

			if (clientID != -1) {
				// Set Simulator signal values
				vrep.simxSetStringSignal(clientID, "NumberandOri", strNO,
						vrep.simx_opmode_oneshot_wait);
				vrep.simxSetStringSignal(clientID, "ControlParam", strCP,
						vrep.simx_opmode_oneshot_wait);
				vrep.simxSetStringSignal(clientID, "Maze", strSeq,
						vrep.simx_opmode_oneshot_wait);

				try {
					// *******************************************************************************************************************************
					for (int i = 0; i < subenvperm.length; i++) {

						// New Maze Parameters (Already a string)
						mazeseq = subenvperm[i];
						strSeq.setArray(mazeseq);
						vrep.simxSetStringSignal(clientID, "Maze", strSeq,
								vrep.simx_opmode_oneshot_wait);

						// Run Scene in the simulator
						rfitness = RunSimulation(vrep, clientID, MaxTime,
								simNumber);

						if (rfitness[0] == -1) {
							RestartSim(simNumber, j);
							continue;
						}

						// Retrieve the fitness if there is no crash
						fitness[i] = rfitness[1];
					}

					// *******************************************************************************************************************************
				} catch (InterruptedException e) {
					System.err.println("InterruptedException: "
							+ e.getMessage());
				}
				// Close connection with the simulator
				vrep.simxFinish(clientID);

			} else {
				// No connection could be established
				System.out.println("Failed connecting to remote API server");
				System.out.println("Trying again for the " + j + " time");
				continue;
			}

			break;
		}
		for (int i = 0; i < fitness.length; i++) {
			System.out.println(fitness[i]);
		}
		return fitness;

	}

	static float[] RunSimulation(remoteApi vrep, int clientID, int MaxTime,
			int threadnum) throws InterruptedException {

		long startTime = System.currentTimeMillis();
		long stopTime = 0;
		long elapsedTime = 0;
		// Create all output variables
		IntW out = new IntW(0);
		CharWA datastring = new CharWA(1);
		FloatWA out2 = new FloatWA(3);
		float[] fitnessout = new float[3];

		// Start Simulation
		int ret = vrep.simxStartSimulation(clientID,
				vrep.simx_opmode_oneshot_wait);
		System.out.println("Start: " + ret + " sim: " + threadnum);
		// Setting up and waiting for finished flag
		vrep.simxGetIntegerSignal(clientID, "finished", out,
				vrep.simx_opmode_streaming);
		out.setValue(0);

		while (out.getValue() == 0) {

			Thread.sleep(10);

			if (vrep.simxGetIntegerSignal(clientID, "finished", out,
					vrep.simx_opmode_buffer) == vrep.simx_return_ok) {
				// We received a fitness signal and everything is ok
				// System.out.println("Retrieved Signal: "+Integer.toString(out.getValue()));
			} else {
				// We have not received the fitness message and out may contain
				// garbage so we set it up to 0 again
				out.setValue(0);
				stopTime = System.currentTimeMillis();
				elapsedTime = stopTime - startTime;

				if (elapsedTime > 300000) {
					System.out
							.println("Too much time has passed attempting to restart simulator");
					fitnessout[0] = -1; // This signals that the output is not
										// ok and the simulator should be
										// restarted
					fitnessout[1] = 1000;
					return fitnessout;
				}
			}
		}

		// Stop listening to the finished signal
		vrep.simxGetIntegerSignal(clientID, "finished", out,
				vrep.simx_opmode_discontinue);

		// Stop simulation
		vrep.simxStopSimulation(clientID, vrep.simx_opmode_oneshot_wait);

		// Read simulation results
		ret = vrep.simxGetStringSignal(clientID, "Position", datastring,
				vrep.simx_opmode_oneshot_wait);
		if (ret != vrep.simx_return_ok) {
			System.out.println("Position Signal not received");
		}

		out2.initArrayFromCharArray(datastring.getArray());

		if (out2.getArray().length == 0) {
			// If out2 is empty the simulator is not working properly and should
			// be restarted
			System.out.println("out2 is empty");
			fitnessout[0] = -1; // This signals that the output is not ok and
								// the simulator should be restarted
			fitnessout[1] = 1000;
			return fitnessout;
		}

		fitnessout[0] = 0;

		float alpha = 0.7f;
		float beta = 1 - alpha;

		if (out2.getArray()[0] == 0) {
			// The robot could get out of the maze so the fitness is the time it
			// spent normalized
			fitnessout[1] = beta * out2.getArray()[1] / MaxTime;
			fitnessout[2] = 1;
		} else if (out2.getArray()[1] == 0) {
			// The robot could not get out of the maze so the fitness is the
			// distance to goal + the maximum time allowed
			fitnessout[1] = alpha * out2.getArray()[0] + beta * 1.0f;
			fitnessout[2] = 0;
		}

		// Return the fitness of the run
		return fitnessout;

	}

	static void RestartSim(int myRank, int j) {

		// Create the command to open the corresponding simulator
		String vrepcommand = new String("./vrep" + myRank + ".sh");

		System.out.println("Restarting simulator and trying again for the " + j
				+ " time in " + myRank);

		try {
			// Command to kill corresponding simulator
			ProcessBuilder qq = new ProcessBuilder("killall", "vrep" + myRank);
			// Specify output file for command line messages
			File log = new File("Simout/log");
			qq.redirectErrorStream(true);
			qq.redirectOutput(Redirect.appendTo(log));
			// Start command process
			Process p = qq.start();
			// Wait for process to finish
			int exitVal = p.waitFor();
			System.out.println("Terminated vrep" + myRank + " with error code "
					+ exitVal);

			// Command to open a simulator with no window
			// qq = new ProcessBuilder(vrepcommand,"-h");
			qq = new ProcessBuilder(vrepcommand, "-h",
					"/home/rodr/EvolWork/Modular/Maze/MazeBuilderR01.ttt");
			// qq = new
			// ProcessBuilder("xvfb-run","--auto-servernum","--server-num=1",vrepcommand,
			// "-h");
			// Open the simulator from its own directory
			qq.directory(new File("/home/rodr/V-REP/Vrep" + myRank + "/"));
			// Specify output file for command line messages of the simulator
			qq.redirectErrorStream(true);
			qq.redirectOutput(Redirect.appendTo(log));
			// Issue command
			qq.start();
			// Wait for the simulator to open
			Thread.sleep(5000);
			System.out.println("Trying again now");
		} catch (Exception e) {
			// Could not reopen the simulator
			System.out.println(e.toString());
			e.printStackTrace();
		}

	}

}
