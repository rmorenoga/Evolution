package simpletests;

import java.io.*;
import java.lang.ProcessBuilder.Redirect;



public class SimpleOpenCloseTest {

	public static void main(String[] args) {
		int myRank = 0;
		String vrep0sim = new String("/home/rodrigo/V-REP/Vrep"+myRank+"/vrep"+myRank+".sh"); 
		System.out.println(vrep0sim);
		String vrepcommand = new String("./vrep.sh");
		try {
            //Runtime rt = Runtime.getRuntime();
            //Process pr = rt.exec("cmd /c dir");
            //rt.exec("cd /home/rodrigo/V-REP/V-REP_PRO_EDU_V3_1_3_64_Linux/");
            //rt.exec("export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/home/rodrigo/V-REP/V-REP_PRO_EDU_V3_1_3_64_Linux/.");
            //rt.exec("echo $LD_LIBRARY_PATH");
            //Process pr = rt.exec(vrep0sim);
           // rt.exec("/home/rodrigo/V-REP/Vrep2/vrep2.sh");
			ProcessBuilder qq=new ProcessBuilder(vrepcommand);
			//Map<String, String> env = qq.environment();
			qq.directory(new File("/home/rodrigo/V-REP/V-REP_PRO_EDU_V3_1_3_64_Linux/"));
			//qq.inheritIO();
			File log = new File("Simout/log");
			qq.redirectErrorStream(true);
			qq.redirectOutput(Redirect.appendTo(log));
			qq.start();
         
            /*BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

           String line=null;

            while((line=input.readLine()) != null) {
                System.out.println(line);
            }*/

            //int exitVal = pr.waitFor();
            Thread.sleep(10000);
            System.out.println("Ten seconds passed");
            //rt.exec("killall -r vrep0");
            //Thread.sleep(2000);
            //rt.exec("killall -r vrep2");
            //int exitVal = pr.exitValue();
            
           qq=new ProcessBuilder("killall","-r","vrep");
           qq.redirectErrorStream(true);
		   qq.redirectOutput(Redirect.appendTo(log));
		   Process p = qq.start();
		   int exitVal = p.waitFor();
            
            System.out.println("Exited with error code "+exitVal);

        } catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    
		
    }

	

}
