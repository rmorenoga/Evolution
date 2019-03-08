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
	static int numberofindividuals = 10;
	static int individuallength = 234;
	static double maxrandomval = 10;
	static double minrandomval = -10;

	public static void main(String[] args) {
		
		String path = "/home/rodr/Desktop/Results";
		String fileHeader = "HAEAEnv1R";
		String fileName = "GenTestHAEA.txt";
		String fileNameCsv = "TableHAEA.csv";
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
		
//		char[][] structures = new char[][]{
//			{'l','b','r'},
//			{'l','r','b'},
//			{'r','b','l'},
//			{'r','l','b'},
//			{'b','l','r'},
//			{'b','r','l'}
//		};
		
	SimulationSettings settings = new SimulationSettings(5,"defaultmhs.ttt",180,false,false);
	SelectableMaze maze = new SelectableMaze(structures, 0, 0.4f, 0.088f);
	//Snake morphology
	String morpho = "[(0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0 , 3.0, 1.0, 3.0, 1.0, 3.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]";
	//T with shoulder module morphology
	//String morpho = "[(0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,3.0, 1.0, 2.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0, 1.0, 2.0, 1.0, 1.0, 2.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0 , 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0]";
	double[] morphology = ChromoConversion.str2double(morpho);		

		 //double [][] indiv = ReadTXTFiles(path,fileHeader,individuallength,numberofindividuals); 
		//double [][] indiv=ReadJsonFiles(path,fileHeader,individuallength,numberofindividuals,"Evol");
		 //double [][] indiv = GenerateRandomIndividuals(numberofindividuals,individuallength, maxrandomval,minrandomval);
		double [][] indiv = new double[][]{{-2.4526502740759, -1.4104002502822, -2.5306678185546, -0.58357808180598, -2.394712095483, -1.6709319782369, -3.1370519037285, -1.3685149324202, -3.0173907149385, -0.97435585681504, -2.033352819368, -1.4954199531833, 0.58280703129512, -2.8406285588735, 3.3870866594266, -2.3195042122943, -0.31249545924027, -1.1718955488796, -2.0642986048117, -2.7009071122959, -8.444682057846, 0.15439433760652, -9.7845478416465, -0.90972978055234, 1.9857339600173, -1.5925090031626, -8.7257795173865, -5.9630668440754, 1.0660079083058, 0.059915014921899, -5.379308911099, 0.15588391909184, 1.337835612351, 4.0444503190563, -4.2657189165105, 4.9682943278506, 5.4829896927901, -1.221871798317, 0.81747456321588, 0.59596519540455, 4.8129118075178, 4.90751390207, -5.0195007079813, 1.1145320432571, -2.5208909712775, -1.1398644313259, -8.5359879123818, -4.8165440180096, -9.0854898221788, 2.3210249348149, -0.41073933530542, -2.2415009691188, -8.2059755832805, -2.0930434611588, -5.3043960729275, -2.1072370563741, 2.4687135111373, -2.8122276290777, 0.22774207969282, 3.1427280747866, 10.799076170303, 8.6066030708783, -5.7763789801321, -4.6517457288536, -8.1417873889986, -5.3713730364376, 0.3334611251236, -0.96919903089929, -0.64151932166198, -0.28648397114895, -0.65131488875752, -2.457010390845, -1.6322309541258, 0.0013680814064509, -1.2767754702961, -0.17770532000371, -3.0305951313338, 1.8064918274769, -5.0002882910291, -3.4919144749099, -8.0484318307739, -2.7248467393261, 0.45816294932532, 1.0581626557618, 2.1300591864497, 5.294171064194, 9.4311018287229, 4.8793718598183, 0.4506633362896, -4.7017502384791, -7.9972936660582, 0.41521142266164, 3.8205575647736, 3.1590357996632, -1.8179561328323, -4.7654228019637, 2.5151787279468, 0.014002208627233, -6.0236177734023, -12.871966789619, -4.5674912459085, -1.3973977047249, -0.2514912431051, 1.0310462190847, -6.6293216082035, -0.56163566967181, -5.0663901714048, -0.6083541148093, 3.3684763437321, -2.1150635392848, 1.8908261186002, 0.99540948036262, -1.9274764091911, 1.8015027237934, 5.5538162275956, 0.90988220001302, 2.891601465705, -4.6440900606361, 0.15183850825884, -0.22667244860331, -0.1787448341934, -0.083878981260045, 0.2439295692809, 0.42707460340785, -0.94191659078037, -2.6752818492745, -0.013851513225934, -0.60480670373069, 0.68974963984197, -2.8834906989826, -5.0046555791503, -3.9426276153752, -0.68213385690197, 2.4944440880809, -2.4580092272278, -0.17322062226464, -2.7895813108466, 3.3496342572331, -4.8098506021588, -5.9333623079393, 1.7857923452407, -1.7373580629692, -0.49873955498788, -11.139048450288, -8.9668487775705, -2.0172169706682, -6.4270471790774, 2.1712484293454, 1.5006955396723, 0.57380077618768, 1.0573830609897, -1.4327106799487, 7.8807453287085, 12.901618032665, 0.51621331144605, 2.135416038898, -7.2081646201452, 2.7803572374467, 2.0994626819533, -1.5883087661279, 0.43968186555135, -0.98349937652507, -2.280381085095, 0.392389599845, -1.5431657763288, -1.0506293339242, -0.42923827887234, -0.98926712258901, 0.62200021294621, -0.14038489629984, 1.5553308640698, -0.057921059261943, 0.002399213724366, -0.0013129091295887, 0.0062188889749861, 1.0160060643478, -0.0080283250996743, 0.0078713614255718, 0.0057036113846009, -2.6118086636042, -0.012702969896129, -0.0034734346142113, 0.53550144133253, -1.0432074617544, 0.015123761390096, -0.010410617421632, -0.0054713671279343, 0.0063204815733829, -1.8451104370879, -0.012978786842187, 0.012039440468318, -0.0022250432138821, -2.0259184027601, -0.0083875783826776, 0.00027977396062715, -0.52303168237081, 1.7822042396578, 0.020173821140179, 0.0037325056785079, 0.0034163058117714, 0.0016474845202616, -1.9558586872439, -0.0003365928664821, -0.00088044601363153, 0.0051248030086939, -0.93946416458224, -0.0079032775983377, -0.0032392964276999, -0.39637589075223, -1.380909248813, 2.2921971618243, 2.8071620282335, 1.4812489724774, -2.7745101798766, 0.22347373683727, 1.879679498685, -2.2186121079137, 0.80837971897151, 0.43570366350075, 0.28967111663943, -1.8029470407046, -0.18554363157117, -1.208834549995, 4.7615492554633, -2.7017458552029, 0.84794890670524, -2.9922192839973, 0.80373083136065, 2.4045166422315, -1.4218893717017, -2.9564907380905, -0.86294210791006, 4.2890153637065, 2.2502219817212}};
		
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
	
	private static double[][] ReadJsonFiles(String Folderpath, String fileheader, int indivlength, int numberOfReplicas,String test) {

		double[][] individuals = new double[numberOfReplicas][indivlength];
		JSONArray ja = new JSONArray();
		String number;
		
			for (int l = 0;l<numberOfReplicas;l++){
				try {
					 Object obj = new JSONParser().parse(new FileReader(Folderpath+"/"+fileheader+l+".json"));
					 JSONObject jo = (JSONObject) obj;
					 
					 switch (test) {
						
						case "Evol":
							JSONObject best = (JSONObject)jo.get("solution");
							//double fitness = (double) bestO.get("best_fitness");
							//System.out.println(fitness); 
							ja = (JSONArray) best.get("best_individual");
							break;
						case "ShortSep":
							JSONObject lastChallenge = (JSONObject)jo.get("challenge29");
							number = lastChallenge.get("fitnessEvol").toString();
							if(!number.equals("-1")) {
								ja = (JSONArray) lastChallenge.get("lastBestEvol");
							}else {
								ja = (JSONArray) lastChallenge.get("lastBest");
							}
							break;
						case "ShortComb":
							lastChallenge = (JSONObject)jo.get("challenge33");
							number = lastChallenge.get("fitnessEvol").toString();
							if(!number.equals("-1")) {
								ja = (JSONArray) lastChallenge.get("lastBestEvol");
							}else {
								ja = (JSONArray) lastChallenge.get("lastBest");
							}
							break;
						case "ShortBump":
							lastChallenge = (JSONObject)jo.get("challenge9");
							number = lastChallenge.get("fitnessEvol").toString();
							if(!number.equals("-1")) {
								ja = (JSONArray) lastChallenge.get("lastBestEvol");
							}else {
								ja = (JSONArray) lastChallenge.get("lastBest");
							}
							break;
						case "ShortDeactivated":
							lastChallenge = (JSONObject)jo.get("challenge211");
							number = lastChallenge.get("fitnessEvol").toString();
							if(!number.equals("-1")) {
								ja = (JSONArray) lastChallenge.get("lastBestEvol");
							}else {
								ja = (JSONArray) lastChallenge.get("lastBest");
							}							
							break;
						default:
							System.out.println("Type of json test not recognized");
							break;					 
					 }
					 
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
