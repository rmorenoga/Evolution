package simpletests;

import coppelia.remoteApi;
import mpi.MPI;


public class SimpleParallelControl {

	//public static remoteApi vrep;  
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		
		MPI.Init(args);
		MPI.initThread(MPI.THREAD_MULTIPLE, 0, args);
		int myRank = MPI.COMM_WORLD.Rank();
		
//
//		if(myRank==0){
//		    System.loadLibrary("remoteApiJava");
//		  }
		
		//if(myRank==0){
			//vrep = new remoteApi(); //Class that loads and links the Native C library of the simulator
			//System.loadLibrary("remoteApiJava");
		//} else{
			//try 
				//{
					//Thread.sleep(2000);
			//} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			//}
		//}
		
		SimpleParallelEvaluateTest test = new SimpleParallelEvaluateTest();
	
		test.evaluate(); //The native library is used inside this class
		

		
		
		MPI.Finalize();
		long stopTime = System.currentTimeMillis();
	      long elapsedTime = stopTime - startTime;
	      System.out.println(elapsedTime);
	
	}


}
