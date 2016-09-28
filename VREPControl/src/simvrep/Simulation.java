package simvrep;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;

import coppelia.CharWA;
import coppelia.FloatWA;
import coppelia.IntW;
import coppelia.IntWA;
import coppelia.remoteApi;
import represent.Robot;

public class Simulation {

	/**
	 * Siumlator server number to connect to
	 */
	public int simnumber;

	/**
	 * Maximum simulation time allowed
	 */
	public int MaxTime = 10;

	/**
	 * Client Id assigned by the simulator server
	 */
	public int clientID;

	/**
	 * Object that implements all v-rep's remote api
	 */
	public remoteApi vrep;

	/**
	 * Signals to set in the simulator
	 */
	public CharWA strCP;
	public CharWA strNO;
	public CharWA strSeq;

	/**
	 * Constructor of the SimulationTest Class
	 * 
	 * @param simnumber
	 * @param MaxTime
	 * @param robot
	 */

	public Simulation(int simnumber, int MaxTime, Robot robot) {
		this.simnumber = simnumber;
		this.MaxTime = MaxTime;

		FloatWA ControlParam = new FloatWA(robot.getControlParam().length);
		System.arraycopy(robot.getControlParam(),0,ControlParam.getArray(),0,robot.getControlParam().length);
		char[] p = ControlParam.getCharArrayFromArray();
		strCP = new CharWA(p.length);
		System.arraycopy(p,0,strCP.getArray(),0,p.length);
		
		int nmodules = robot.getNumberofmodules();
		int[] orientation = robot.getOrientation();
		// Pack Integers into one String data signal
		IntWA NumberandOri = new IntWA(nmodules + 2);
		int[] NO = new int[nmodules+ 2];
		NO[0] = nmodules;
		NO[1] = MaxTime;
		for (int i = 2; i < nmodules + 2; i++) {
			NO[i] = orientation[i - 2];
		}
		System.arraycopy(NO,0,NumberandOri.getArray(),0,NO.length);
		char[] p2 = NumberandOri.getCharArrayFromArray();
		strNO = new CharWA(p2.length);
		System.arraycopy(p2,0,strNO.getArray(),0,p2.length);
		

	}

	/**
	 * Connects to the simulator server specified by simnumber
	 * 
	 * @return whether the simulator returns a valid clientID
	 */
	public boolean Connect() {
		vrep = new remoteApi(); // Create simulator control object
		//vrep.simxFinish(-1); // just in case, close all opened connections
		// Connect with the corresponding simulator remote server
		clientID = vrep.simxStart("127.0.0.1", 19997 - simnumber, true, true, 5000, 5);
		System.out.println("Sim "+simnumber+" ClientID "+clientID);
		return clientID != -1;
	}

	/**
	 * Closes the connection with the simulator
	 */
	public void Disconnect() {
		// Close connection with the simulator
		vrep.simxFinish(clientID);
	}

	/**
	 * Sets the maze sequence signal in the simulator
	 * 
	 * @param sequence
	 *            a char array containing the maze sequence
	 */
	public void SendMaze(char[] sequence) {
		strSeq = new CharWA(sequence.length);
		System.arraycopy(sequence,0,strSeq.getArray(),0,sequence.length);

		vrep.simxSetStringSignal(clientID, "Maze", strSeq, vrep.simx_opmode_oneshot_wait);
	}
	
	public void SendMaze(char[] sequence, float width){
		SendMaze(sequence);
		vrep.simxSetFloatSignal(clientID, "MazeW", width, vrep.simx_opmode_oneshot_wait);
	}

	public void SendSignals() {
		// Set Simulator signal values
		vrep.simxSetStringSignal(clientID, "NumberandOri", strNO, vrep.simx_opmode_oneshot_wait);
		vrep.simxSetStringSignal(clientID, "ControlParam", strCP, vrep.simx_opmode_oneshot_wait);
	}

	/**
	 * Runs a simulation in a previously openede scene in V-Rep
	 * 
	 * @param alpha
	 *            the weight of the distance in the fintess calculation
	 * @return an array with whether the simulation was succesful, the fitness
	 *         and whether the robot could get out of the maze
	 * @throws InterruptedException
	 */
	public float[] RunSimulation(float alpha) throws InterruptedException {


		long startTime = System.currentTimeMillis();
		long stopTime = 0;
		long elapsedTime = 0;
		// Create all output variables
		IntW out = new IntW(0);
		CharWA datastring = new CharWA(1);
		FloatWA out2 = new FloatWA(3);
		float[] fitnessout = new float[3];

		// Start Simulation
		int ret = vrep.simxStartSimulation(clientID, vrep.simx_opmode_oneshot_wait);
		System.out.println("Start: " + ret + " sim: " + simnumber);
		// Setting up and waiting for finished flag
		vrep.simxGetIntegerSignal(clientID, "finished", out, vrep.simx_opmode_streaming);
		out.setValue(0);

		while (out.getValue() == 0) {

			Thread.sleep(10);

			if (vrep.simxGetIntegerSignal(clientID, "finished", out, vrep.simx_opmode_buffer) == vrep.simx_return_ok) {
				// We received a fitness signal and everything is ok
				// System.out.println("Retrieved Signal:
				// "+Integer.toString(out.getValue()));
			} else {
				// We have not received the fitness message and out may contain
				// garbage so we set it up to 0 again
				out.setValue(0);
				stopTime = System.currentTimeMillis();
				elapsedTime = stopTime - startTime;

				if (elapsedTime > 300000) {
					System.out.println("Too much time has passed attempting to restart simulator");
					fitnessout[0] = -1; // This signals that the output is not
					// ok and the simulator should be
					// restarted
					fitnessout[1] = 1000;
					return fitnessout;
				}
			}
		}

		// Stop listening to the finished signal
		vrep.simxGetIntegerSignal(clientID, "finished", out, vrep.simx_opmode_discontinue);

		// Stop simulation
		vrep.simxStopSimulation(clientID, vrep.simx_opmode_oneshot_wait);

		// Read simulation results
		ret = vrep.simxGetStringSignal(clientID, "Position", datastring, vrep.simx_opmode_oneshot_wait);
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

		float[] fitness = new float[2];
		fitness = Calcfitness(out2, alpha);
		fitnessout[1] = fitness[0];
		fitnessout[2] = fitness[1];

		// Return the fitness of the run
		return fitnessout;

	}

	/**
	 * Restarts the simulator by killing the process and re-launching it
	 * 
	 * @param j
	 *            the attempt number
	 */
	public void RestartSim(int j, String scene) {

		int simnumber = this.simnumber;

		// Create the command to open the corresponding simulator
		String vrepcommand = new String("./vrep" + simnumber + ".sh");

		System.out.println("Restarting simulator and trying again for the " + j + " time in " + simnumber);

		try {
			// Command to kill corresponding simulator
			ProcessBuilder qq = new ProcessBuilder("killall", "vrep" + simnumber);
			// Specify output file for command line messages
			File log = new File("Simout/log");
			qq.redirectErrorStream(true);
			qq.redirectOutput(Redirect.appendTo(log));
			// Start command process
			Process p = qq.start();
			// Wait for process to finish
			int exitVal = p.waitFor();
			System.out.println("Terminated vrep" + simnumber + " with error code " + exitVal);

			// Command to open a simulator with no window
			// qq = new ProcessBuilder(vrepcommand,"-h");
			qq = new ProcessBuilder(vrepcommand,"-h", "scenes/Maze/"+scene);
			// qq = new
			// ProcessBuilder("xvfb-run","--auto-servernum","--server-num=1",vrepcommand,
			// "-h");
			// Open the simulator from its own directory
			qq.directory(new File("/home/rodr/V-REP/Vrep" + simnumber + "/"));
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

	/**
	 * Calculate fitness based on the output coming from the simulator
	 * 
	 * @param output
	 *            the output coming from the simulator
	 * @param alpha
	 *            the weight of the distance of in the fitness calculation
	 * @return an array with the calculated fitness and whether the robot could
	 *         get out of the maze
	 */
	private float[] Calcfitness(FloatWA output, float alpha) {

		float[] fitness = new float[2];
		float beta = 1 - alpha;

		if (output.getArray()[0] == 0) {
			// The robot could get out of the maze so the fitness is the time it
			// spent normalized
			fitness[0] = beta * output.getArray()[1] / MaxTime;
			fitness[1] = 1;
		} else if (output.getArray()[1] == 0) {
			// The robot could not get out of the maze so the fitness is the
			// distance to goal + the maximum time allowed
			fitness[0] = alpha * output.getArray()[0] + beta * 1.0f;
			fitness[1] = 0;
		}

		return fitness;

	}

}
