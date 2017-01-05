package simvrep;

import coppelia.CharWA;
import coppelia.remoteApi;
import mpi.MPI;

public class SceneBuilder {


	private char[] maze;
	private CharWA strSeq;
	private int clientID;
	private remoteApi vrep;
	private float width;
	private float bheight = 0.2f;
	private int rank = 0;

	public SceneBuilder(remoteApi vrep, int clientID, char[] maze, float width) {
		this.maze = maze;
		this.vrep = vrep;
		this.clientID = clientID;
		this.width = width;
	}

	public void loadScene() {
		String scenePath = "scenes/Maze/defaultm.ttt";


		if (SimulationConfiguration.isUseMPI()) {
			rank = MPI.COMM_WORLD.Rank();
			//System.out.println("Got rank: "+rank+" when loading scene");
		}

		int nAttemps = SimulationConfiguration.getnAttempts();
		int ret;
		for (int i = 0; i < nAttemps; i++) {
			ret = vrep.simxLoadScene(clientID, scenePath, 0, remoteApi.simx_opmode_blocking);
			if (ret == remoteApi.simx_return_ok) {
				// System.out.format("Scene loaded correctly: \n");
				strSeq = new CharWA(maze.length);
				System.arraycopy(maze, 0, strSeq.getArray(), 0, maze.length);

				int result = vrep.simxSetStringSignal(clientID, "Maze", strSeq, vrep.simx_opmode_blocking);
				result = vrep.simxSetFloatSignal(clientID, "MazeW", width, vrep.simx_opmode_blocking);
				result = vrep.simxSetFloatSignal(clientID, "MazeBH", bheight, vrep.simx_opmode_blocking);
				return;
			} else {
				System.err.format(
						"VrepCreateRobot (" + rank
								+ "). Error loading the scene: Remote API function call returned with error code: %d\n",
						ret);
				System.err.println(
						"VrepCreateRobot. Check that the vrep simulator is running and listening in the correct port.");
				System.err.println("VrepCreateRobot. Check also the scene path: " + scenePath);

			}
		}
		if (SimulationConfiguration.isUseMPI())
			MPI.COMM_WORLD.Abort(-1);// Try to close all the programs
		System.exit(-1);

	}

	public int getRank() {
		return rank;
	}

	public void closeScene(){
		 int iter = 0;
				 int ret = vrep.simxCloseScene(clientID,remoteApi.simx_opmode_blocking);
				 while(ret != remoteApi.simx_return_ok && iter < 100){
				 ret = vrep.simxCloseScene(clientID,remoteApi.simx_opmode_blocking);
				 iter++;
				 }
				 if(ret != remoteApi.simx_return_ok)
				 System.err.println("The scene has not been closed after 100 trials.");
	}

}
