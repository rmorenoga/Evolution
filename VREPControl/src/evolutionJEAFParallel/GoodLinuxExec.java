package evolutionJEAFParallel;

import java.util.*;
import java.io.*;


public class GoodLinuxExec {

	GoodLinuxExec(String command){
		try
        {  
        	System.out.println("Executing "+command);
        	Process qq = new ProcessBuilder(command,"-h").start();
        		// any error message?
        		StreamGobbler errorGobbler = new StreamGobbler(qq.getErrorStream(), "ERROR");            

        		// any output?
        		StreamGobbler outputGobbler = new StreamGobbler(qq.getInputStream(), "OUTPUT");
 
        		// kick them off
        		errorGobbler.start();
        		outputGobbler.start();
                     
        		// any error???
        		//int exitVal = qq.waitFor();
        		//System.out.println("ExitValue: " + exitVal);  
        		Thread.sleep(3000);
        		} catch (Throwable t)
				{
        			t.printStackTrace();
				}
		}
	}
	
	
	

