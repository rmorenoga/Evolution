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

		
		double[][] indiv = new double[][]{{
			1.0, 1.0, -0.031110454104099072, -0.9947166406861332, -0.9255484041619079, -0.15386625909293, -0.8, 0.9384261329287366, -1.0, -1.0, -1.0, 1.0}};
			
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
				
		result = RunTest(indiv[i],morpho,0,sim);
		WResultsFile(indiv[i],result);
				
		}
		
		
			
	}

	static double[] RunTest(double[] param,String morpho, int extraparam, Simulation sim) {
		
		float alpha = 0.7f;
		double[] res  = new double[30];
		
		float[] fullparam = new float[param.length];
		for (int i = 0; i < param.length; i++) {
			fullparam[i] = (float) param[i];
		}
		
		
		
		//Parameter Mask: Allows control over which parameters are actually sent to the robot depending on its controller, ParameterMask class just sends everything adjusted for max and min values
		// Submask: Helper classes that fix certain parts of the controller
		//ParameterMask parammask = new ParameterMask(extraparam);
		//CPGSingle parammask = new CPGSingle(extraparam,true,true);
		//CPGHSingle parammask = new CPGHSingle(extraparam,true,true);
		CPGHSBase parammask;
		
		char[] subshort = new char[]{'s','b','l','r'};
		float width = 0.5f;
		
		double[] morphoDouble = ChromoConversion.str2double(morpho);
		EvaluatorMT evaluator;
		
		for (int i= 0;i<res.length;i++){
			parammask = new CPGHSBase(extraparam,true,true,true);
			parammask.setandsepParam(fullparam);
			evaluator = new EvaluatorMT(morphoDouble, "defaultmhs.ttt", parammask, sim, alpha, subshort, width);
			res[i] = evaluator.evaluate();
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
