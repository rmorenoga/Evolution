package Tests;

import java.io.FileWriter;
import java.io.PrintWriter;

import control.CPGHSBase;
import evolHAEA.HEmP;
import simvrep.EvaluatorMT;
import simvrep.Simulation;
import util.ChromoConversion;

public class GenEmTest {

	public static void main(String[] args) {
		Simulation sim = new Simulation(0, 60);
		
		double[] result;
		
		
		String morpho = "[(0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0 , 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]";

		
		//double[][] indiv = new double[][]{{
			//1.0, 1.0, -0.031110454104099072, -0.9947166406861332, -0.9255484041619079, -0.15386625909293, -0.8, 0.9384261329287366, -1.0, -1.0, -1.0, 1.0},{0.7760164602688042, -0.3909846388463969, -0.8641055969947993, -1.0, -1.0, -0.5551028030994317, -1.0, 1.0, -1.0, -0.708158144366758, -0.7679504678050293, 0.9746661541360532}};
		double[][] indiv = new double[][]{{0.7760164602688042, -0.3909846388463969, -0.8641055969947993, -1.0, -1.0, -0.5551028030994317, -1.0, 1.0, -1.0, -0.708158144366758, -0.7679504678050293, 0.9746661541360532}};	
			
			
			for (int i = 0; i < 5; i++) {
				if (sim.Connect()) {
					break;
				} else {
					// No connection could be established
					System.out.println("Failed connecting to remote API server");
					System.exit(-1);
				}
			}	
			
		for (int i = 0; i < indiv.length ;i++ ){
				
		result = RunTest(indiv[i],morpho,sim);
		//WResultsFile(indiv[i],result);
				
		}
		
		
			
	}

	static double[] RunTest(double[] param,String morpho, Simulation sim) {
		
		float alpha = 0.7f;
		double[] res  = new double[10];
		
		float[] fullparam = new float[param.length];
		for (int i = 0; i < param.length; i++) {
			fullparam[i] = (float) param[i];
		}
		
		
		
		//Parameter Mask: Allows control over which parameters are actually sent to the robot depending on its controller, ParameterMask class just sends everything adjusted for max and min values
		// Submask: Helper classes that fix certain parts of the controller
		//ParameterMask parammask = new ParameterMask();
		//CPGSingle parammask = new CPGSingle(true,true);
		//CPGHSingle parammask = new CPGHSingle(true,true);
		CPGHSBase parammask = new CPGHSBase(true,true,true);
		parammask.setParameters(fullparam);
		
		char[] subshort = new char[]{'s','b','l','r'};
		float width = 0.5f;
		
		double[] morphoDouble = ChromoConversion.str2double(morpho);
		EvaluatorMT evaluator;
		
		for (int i= 0;i<res.length;i++){	
			evaluator = new EvaluatorMT(morphoDouble, "defaultmhs.ttt", parammask, sim, alpha, subshort, width);
			res[i] = evaluator.evaluate();
			System.out.println("Fitness: " +res[i]);
		}
				
		return res;
	}

	private static void WResultsFile(double[] indiv, double[] result) {
		FileWriter file = null;
		PrintWriter pw = null;
		try {
			file = new FileWriter("GenEmResult.txt", true);
			pw = new PrintWriter(file);

			for (int i = 0; i < indiv.length; i++) {

				if (i == indiv.length - 1) {
					pw.print(indiv[i] + "--");
				} else {
					pw.print(indiv[i] + ",");
				}
			}
			for (int i = 0; i < result.length; i++) {

				if (i == result.length - 1) {
					pw.println(result[i]+";");
				} else {
					pw.print(result[i] + ",");
				}
			}

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

}
