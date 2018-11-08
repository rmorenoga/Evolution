package logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import evolHAEA.EmP;
import maze.Maze;
import maze.SelectableMaze;
import simvrep.Simulation;
import simvrep.SimulationSettings;
import util.ChromoConversion;

public class LogIndividual {
	
	public static int Nsim;
	
	static int individuallength = 234;

	public static void main(String[] args) {
		
		String path = "/home/rodr/Desktop/Results/LogTest";
		String fileHeader = "HillEnv0R";
		String logPath = path+"/"+fileHeader;
		
		//Snake morphology
		String morpho = "[(0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0 , 3.0, 1.0, 3.0, 1.0, 3.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]";
		//T with shoulder module morphology
		//String morpho = "[(0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,3.0, 1.0, 2.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0, 1.0, 2.0, 1.0, 1.0, 2.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0 , 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0]";
		double[] morphology = ChromoConversion.str2double(morpho);
		
		launchSimulator(args);
		
		Maze maze = new Maze(new char[]{'l','r','b'},0.4f,0.088f,1);
		SimulationSettings settings = new SimulationSettings(5,"defaultmhs.ttt",180,false,false,logPath);
		
		Simulation sim = connectToSimulator(Nsim);
		
		double [][] indiv = ReadTXTFiles(path,fileHeader,individuallength,10);
		//double [][] indiv=ReadJsonFiles(path,fileHeader,individuallength,10);
		
		for (int i = 0; i < indiv.length; i++) {
			settings.setLogPath(logPath+i);
			System.out.println(logPath+i);
			RunLogger(indiv[i],morphology, sim,settings,maze);
		}
			
		sim.Disconnect();
		
		stopSimulator();
				
	}
	
	static double RunLogger(double[] indv, double[] morphology, Simulation sim, SimulationSettings settings, Maze maze) {
				
		EmP function = new EmP(sim,morphology,maze,settings);
		double fitnessD = function.apply(indv);
		
		return fitnessD;
	}
	
	private static double[][] ReadJsonFiles(String Folderpath, String fileheader, int indivlength, int numberOfReplicas) {

		double[][] individuals = new double[numberOfReplicas][indivlength];
		
			for (int l = 0;l<numberOfReplicas;l++){
				try {
					 Object obj = new JSONParser().parse(new FileReader(Folderpath+"/"+fileheader+l+".json"));
					 JSONObject jo = (JSONObject) obj;
					 JSONObject best = (JSONObject)jo.get("solution");
					 //double fitness = (double) bestO.get("best_fitness");
					 //System.out.println(fitness); 
					 JSONArray ja = (JSONArray) best.get("best_individual");
					 
					 Iterator itr = ja.iterator();
					 
					 int j=0;
					 while (itr.hasNext()) 
				        {
						 		individuals[l][j] = (double)itr.next();
						 		//System.out.println(individuals[i][j]);
						 		j++;
				        }
					 
					// System.out.println(individuals[i].length);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
				
		
		//System.out.println(individuals.length);
		return individuals;
		
	}
	
private static double[][] ReadTXTFiles(String Folderpath, String fileheader, int indivlength, int numberOfReplicas) {
		
		List<String> list = new ArrayList<String>();
		
		for (int l = 0;l<numberOfReplicas;l++){
			
			try {
				BufferedReader in = new BufferedReader(new FileReader(Folderpath+"/"+fileheader+l+".txt"));
				String str;

				while ((str = in.readLine()) != null) {
					list.add(str);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//System.out.println(list.get(i));
		}
		
		
		
		double[][] individuals = new double[list.size()][indivlength];
		
		for (int i = 0;i<list.size();i++){
			//System.out.println(list.get(i));
			String[] sep = list.get(i).split(",");
			for (int j=0;j<indivlength;j++){
				individuals[i][j] = Double.parseDouble(sep[j]); //How to ensure that the last (blank) element is not used?
				//System.out.print(individuals[i][j]+",");
			}
			//System.out.println(individuals[i].length);
		}

		return individuals;
	}

	static Simulation connectToSimulator(int simulatorNumber) {
	Simulation sim = new Simulation(simulatorNumber);
	// Retry if there is a simulator crash
	for (int i = 0; i < 5; i++) {
		if (sim.Connect()) {
		} else {
			// No connection could be established
			System.out.println("Failed connecting to remote API server");
			System.out.println("Trying again for the " + i + " time in " + simulatorNumber);
			continue;
		}
		break;
	}
	return sim;
}
	
	static void stopSimulator() {
		
		// Stop Simulator
		
			// kill all the v-rep processes
			try {
				ProcessBuilder qq = new ProcessBuilder("killall", "vrep" + Nsim);
				File log = new File("Simout/log");
				qq.redirectErrorStream(true);
				qq.redirectOutput(Redirect.appendTo(log));
				Process p = qq.start();
				int exitVal = p.waitFor();
				System.out.println("Terminated vrep" + Nsim + " with error code " + exitVal);
			} catch (Exception e) {
				System.out.println(e.toString());
				e.printStackTrace();
			}
		
		
	}
	
	static void launchSimulator(String[] args) {
		Nsim = 0;

		if (args.length > 0) {
			try {
				// for(int j=0;j<args.length;j++){
				// System.out.println("Argument "+j+" = "+args[j]);
				// }
				if (args.length >= 1) {
					Nsim = Integer.parseInt(args[0]);
				} else {
					System.err.println("Provide a simulator number");
					System.exit(1);
				}
			} catch (NumberFormatException e) {
				System.err.println("Nsim " + args[0] + " must be an integer.");
				System.exit(1);
			}
		} else {
			System.err.println("Missing arguments");
			System.exit(1);
		}

		
		System.out.println("Using simulator: "+Nsim);
		// Start Simulator

			String vrepcommand = new String("./vrep" + Nsim + ".sh");

			// Initialize a v-rep simulator based on the Nsim parameter
			try {
				// ProcessBuilder qq = new ProcessBuilder(vrepcommand,
				// "-h",
				// "scenes/Maze/MRun.ttt"); //Snake
				// ProcessBuilder qq = new ProcessBuilder("xvfb-run","-a",vrepcommand, "-h",
				// "scenes/Maze/defaultmhs.ttt");
				ProcessBuilder qq = new ProcessBuilder(vrepcommand,"-h", "scenes/Maze/defaultmhs.ttt");
				qq.directory(new File("/home/rodr/V-REP/Vrep" + Nsim + "/"));
				File log = new File("Simout/log");
				qq.redirectErrorStream(true);
				qq.redirectOutput(Redirect.appendTo(log));
				qq.start();
				Thread.sleep(10000);
			} catch (Exception e) {
				System.out.println(e.toString());
				e.printStackTrace();
			}

	}

}
