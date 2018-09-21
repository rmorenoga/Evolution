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
import java.util.Iterator;
import java.util.List;

import coppelia.CharWA;
import coppelia.FloatWA;
import coppelia.IntW;
import coppelia.IntWA;
import coppelia.remoteApi;
import evolHAEA.EmP;
import evolHAEA.HEmP;
import maze.Maze;
import maze.SelectableMaze;
import simvrep.ShortChallengeSettings;
import simvrep.Simulation;
import simvrep.SimulationSettings;
import unalcol.optimization.OptimizationFunction;
import util.ChromoConversion;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class GeneralizationTest {

	// public float alpha = 0.7f;// Look into the Run simulation method to set
	// alpha
	static int numberofindividuals = 2;
	static int individuallength = 234;
	static double maxrandomval = 10;
	static double minrandomval = -10;

	public static void main(String[] args) {
		
		String path = "G:/My Drive/2018/Thesis/Results/HillClimbing/EnvOrder/";
		String fileName = "GenResultFit.txt";
		String fileNameCsv = "TableHillEnvOrder.csv";
		double[] result = new double[6];
	
		Simulation sim = new Simulation(0);
		// Retry if there is a simulator crash
		for (int i = 0; i < 5; i++) {
			if (sim.Connect()) {
				break;
			} else {
				// No connection could be established
				System.out.println("Failed connecting to remote API server");
				System.out.println("Trying again for the " + i + " time in " + 0);
			}
		}
		
		char[][] structures = new char[][]{
			{'s','l','b','r'},
			{'s','l','r','b'},
			{'s','r','b','l'},
			{'s','r','l','b'},
			{'s','b','l','r'},
			{'s','b','r','l'}
		};	
		
	SimulationSettings settings = new SimulationSettings(5,"defaultmhs.ttt",180,false,false);
	SelectableMaze maze = new SelectableMaze(structures, 0, 0.4f, 0.088f);
	String morpho = "[(0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0 , 3.0, 1.0, 3.0, 1.0, 3.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]";
	double[] morphology = ChromoConversion.str2double(morpho);		

		 double [][] indiv = ReadTXTFiles(path,"Hill",6,individuallength,10); 
		//double [][] indiv=ReadJsonFiles("C:/Users/golde_000/Desktop/Test","HAEA",1,individuallength,10);
		 //double [][] indiv = GenerateRandomIndividuals(numberofindividuals,individuallength, maxrandomval,minrandomval);
			for (int i = 0; i < indiv.length; i++) {
		 
		 			result = RunTest(indiv[i],morphology, sim,settings,maze);
		 			WResultsFile(indiv[i], result, path+fileName);
		 			for (int j = 0;j<6;j++){
		 				System.out.println(result[j]);
		 			}
		 			System.out.println("+++++++++++++++++++++++++++++++++++++");
		 
		 		}
			
			sim.Disconnect();
			
			Data process = new Data(fileName,fileNameCsv,numberofindividuals,path,",");
			
			process.GenerateCSV("HAEA");

	}

	private static void WResultsFile(double[] indiv, double[] result,String filename) {
		FileWriter file = null;
		PrintWriter pw = null;
		try {
			file = new FileWriter(filename, true);
			pw = new PrintWriter(file);
			
			for (int i=0;i<indiv.length;i++){
				if (i==indiv.length-1){
					pw.print(indiv[i]+";  ");
				}else{
					pw.print(indiv[i]+",");
				}
				
			}
			
			pw.println(result[0]+","+result[1]+","+result[2]+","+result[3]+","+result[4]+","+result[5]);

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
	
	private static double[][] ReadJsonFiles(String Folderpath, String fileheader, int numberOfEnv, int indivlength, int numberOfReplicas) {

		double[][] individuals = new double[numberOfEnv*numberOfReplicas][indivlength];
		
		int k = 0;
		for (int i=0;i<numberOfEnv;i++){
			for (int l = 0;l<numberOfReplicas;l++){
				try {
					 Object obj = new JSONParser().parse(new FileReader(Folderpath+fileheader+"Env"+i+"R"+l+".json"));
					 JSONObject jo = (JSONObject) obj;
					 JSONObject best = (JSONObject)jo.get("solution");
					 //double fitness = (double) bestO.get("best_fitness");
					 //System.out.println(fitness); 
					 JSONArray ja = (JSONArray) best.get("best_individual");
					 
					 Iterator itr = ja.iterator();
					 
					 int j=0;
					 while (itr.hasNext()) 
				        {
						 		individuals[k][j] = (double)itr.next();
						 		//System.out.println(individuals[i][j]);
						 		j++;
				        }
					 
					// System.out.println(individuals[i].length);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				k++;
				
			}
				
		}
		//System.out.println(individuals.length);
		return individuals;
		
	}

	private static double[][] ReadTXTFiles(String Folderpath, String fileheader, int numberOfEnv, int indivlength, int numberOfReplicas) {
		
		List<String> list = new ArrayList<String>();
		
		for (int i=0;i<numberOfEnv;i++){
			for (int l = 0;l<numberOfReplicas;l++){
			
			try {
				BufferedReader in = new BufferedReader(new FileReader(Folderpath+fileheader+"Env"+i+"R"+l+".txt"));
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
	
	

	static double[] RunTest(double[] indv, double[] morphology, Simulation sim, SimulationSettings settings, SelectableMaze maze) {
			
		double[] fitnessD = new double[6];		
		EmP function;
		
		for (int i = 0;i<6;i++){
			maze.selectMaze(i);
			function = new EmP(sim,morphology,maze,settings);
			fitnessD[i] = function.apply(indv);
		}
		
		return fitnessD;
	}
	
	static double[][] GenerateRandomIndividuals(int number,int lenght,double maxvalue, double minvalue){
		double[][] individuals  = new double[number][lenght];
		
		for (int i=0;i<number;i++){
			individuals[i] = GenerateRandomIndiv(lenght,maxvalue,minvalue);
		}
		
		return individuals;
	}
	
	static double[] GenerateRandomIndiv(int lenght, double maxvalue, double minvalue){
		double[] individual = new double[lenght];
		
		// NewValue = (((OldValue - OldMin) * (NewMax - NewMin)) / (OldMax -
		// OldMin)) + NewMin
		
		for (int i=0;i<lenght;i++){
			individual[i] =(Math.random()*(maxvalue - minvalue))+minvalue;
		}		
		
		return individual;	
	}
	
	

	

	

}
