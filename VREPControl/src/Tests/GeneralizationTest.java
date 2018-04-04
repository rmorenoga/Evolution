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
import evolHAEA.HEmP;
import simvrep.Simulation;

public class GeneralizationTest {

	// public float alpha = 0.7f;// Look into the Run simulation method to set
	// alpha
	static int numberofindividuals = 10;
	static int individuallength = 132;
	static double maxrandomval = 3;
	static double minrandomval = -3;

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
		
		
		
		
		

		double[] result = new double[6];

		// float[][] indiv=ReadFile("IndivGen.txt");

		double[][] indiv = GenerateRandomIndividuals(numberofindividuals,individuallength, maxrandomval,minrandomval);
	//new double[][] { { -1.0f, -0.34856381681107756f, -0.7018207570662021f }, };

		for (int i = 0; i < indiv.length; i++) {

			result = RunTest(indiv[0],morpho, sim);
			//WResultsFile(indiv[i], result);
			for (int j = 0;j<6;j++){
				System.out.println(result[j]);
			}
			System.out.println("+++++++++++++++++++++++++++++++++++++");

		}
		
		for (int i = 0;i<6;i++){
			System.out.println(result[i]);
		}

	}

	private static void WResultsFile(float[] indiv, float[] result) {
		FileWriter file = null;
		PrintWriter pw = null;
		try {
			file = new FileWriter("IndGenResult.txt", true);
			pw = new PrintWriter(file);

			// pw.println(indiv[0]+","+indiv[1]+","+indiv[2]+","+result[0]+","+result[1]+","+result[2]+","+result[3]+","+result[4]+","+result[5]);

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

			while ((str = in.readLine()) != null) {
				list.add(str);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		float[][] values = new float[list.size()][3];
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
			String[] sep = list.get(i).split(",");
			values[i][0] = Float.parseFloat(sep[0]);
			values[i][1] = Float.parseFloat(sep[1]);
			values[i][2] = Float.parseFloat(sep[2]);
		}

		// for (int i = 0; i < values.length ;i++ ){
		// System.out.println(values[i][0]+"*"+values[i][1]+"*"+values[i][2]);
		// }
		return values;
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
