package Tests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Data {
	
	private String folderpath;
	private String inputfile;
	private String outputfile;
	private int numberofindividuals;
	private double[] table;
	private String separator;
	
	public Data(String inputfile,String outputfile, int numberofindividuals, String folderpath,String separator){
		this.folderpath = folderpath;
		this.inputfile = inputfile;
		this.outputfile = outputfile;
		this.numberofindividuals = numberofindividuals;	
		this.separator = separator;
	}
	
	
	
	public void GenerateCSV(String type){
		
		double[][] result;
		
		result = Readinputfile(inputfile,folderpath);
		
		Writeoutput(result,outputfile,folderpath,separator,type);
		
	

		
		
				
	}
	
	private double[][] Readinputfile(String inputfile,String folderpath){
		
		List<String> list = new ArrayList<String>();
		
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(folderpath+"/"+inputfile));
			String str;

			while ((str = in.readLine()) != null) {
				list.add(str);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		double[][] results = new double[list.size()][6];
		
		for (int i = 0;i<list.size();i++){
			//System.out.println(list.get(i));
			String[] sep = list.get(i).split(";");
			String[] sep1 = sep[1].split(",");
			
			for (int j=0;j<sep1.length;j++){
				results[i][j] = Double.parseDouble(sep1[j]);
				System.out.println(results[i][j]);
			}
			
		}
				
		return results;
	}
	
	private void Writeoutput(double[][] result, String outputfile, String folderpath,String separator,String type){
		
		String[] env = new String[]{"lbr","lrb","rbl","rlb","blr","brl"};
		
		FileWriter file = null;
		PrintWriter pw = null;
		try {
			file = new FileWriter(folderpath+"/"+outputfile, true);
			pw = new PrintWriter(file);
			
			for (int i=0;i<result.length;i++){
				for (int j=0;j<result[i].length;j++){
					pw.print((i+30)+separator);
					pw.print(env[j]+separator);
					pw.print(type+separator);
					pw.println(result[i][j]);	
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
