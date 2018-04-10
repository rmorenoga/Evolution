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
import evolHAEA.HEmP;
import simvrep.Simulation;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class GeneralizationTest {

	// public float alpha = 0.7f;// Look into the Run simulation method to set
	// alpha
	static int numberofindividuals = 10;
	static int individuallength = 132;
	static double maxrandomval = 1;
	static double minrandomval = -1;

	public static void main(String[] args) {
		
		Simulation sim = new Simulation(0, 180);
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
		
	String morpho = "[(0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0 , 3.0, 1.0, 3.0, 1.0, 3.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]";
		
		
		
		
		
		String filename = "GenResultHAEA.txt";
		double[] result = new double[6];
		
//		Data process = new Data("GenResultRand.txt","TableRand.csv",30,"C:/Users/golde_000/Desktop",",");
//		
//		process.GenerateCSV("Rand");

		 //double [][] indiv=ReadTXTFiles("G:/My Drive/2018/Thesis/Results/UbuntuHome/HillClimbing/TurnLeft","HillClimbingResult",numberofindividuals,individuallength);
		double [][] indiv=ReadJsonFiles("G:/My Drive/2018/Thesis/Results/UbuntuHome/HAEA/lbr","testjson",numberofindividuals,individuallength);
			for (int i = 0; i < indiv.length; i++) {
		 
		 			result = RunTest(indiv[i],morpho, sim);
		 			WResultsFile(indiv[i], result, filename);
		 			for (int j = 0;j<6;j++){
		 				System.out.println(result[j]);
		 			}
		 			System.out.println("+++++++++++++++++++++++++++++++++++++");
		 
		 		}
//		 indiv  = null;
//		 indiv = GenerateRandomIndividuals(numberofindividuals,individuallength, maxrandomval,minrandomval);
//		 
//
//		for (int i = 0; i < indiv.length; i++) {
//
//			result = RunTest(indiv[i],morpho, sim);
//			WResultsFile(indiv[i], result, filename);
//			for (int j = 0;j<6;j++){
//				System.out.println(result[j]);
//			}
//			System.out.println("+++++++++++++++++++++++++++++++++++++");
//
//		}
		

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
	
	private static double[][] ReadJsonFiles(String Folderpath, String fileheader, int numberoffiles, int indivlength) {

		double[][] individuals = new double[numberoffiles][indivlength];
		
		for (int i=0;i<numberoffiles;i++){
			try {
				 Object obj = new JSONParser().parse(new FileReader(Folderpath+"/"+fileheader+i+".json"));
				 JSONObject jo = (JSONObject) obj;
				 JSONObject best = (JSONObject)jo.get("solution");
				 //double fitness = (double) bestO.get("best_fitness");
				 //System.out.println(fitness); 
				 JSONArray ja = (JSONArray) best.get("best_individual");
				 
				 Iterator itr = ja.iterator();
				 
				 int j=0;
				 while (itr.hasNext()) 
			        {
					 		individuals[i][j] = (double)itr.next();
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

	private static double[][] ReadTXTFiles(String Folderpath, String fileheader, int numberoffiles, int indivlength) {
		
		List<String> list = new ArrayList<String>();
		for (int i=0;i<numberoffiles;i++){
			
			try {
				BufferedReader in = new BufferedReader(new FileReader(Folderpath+"/"+fileheader+i+".txt"));
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
	
	

	static double[] RunTest(double[] indv, String morpho, Simulation sim) {
		double[] fitnessD = new double[6];		
		HEmP test;
		
		for (int i = 0;i<6;i++){
			test = new HEmP(0.7f, sim, morpho, "GeneralTest",i);
			fitnessD[i] = test.apply(indv);
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
