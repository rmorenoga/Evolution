/*
 * EDHMOR - Evolutionary designer of heterogeneous modular robots
 * <https://bitbucket.org/afaina/edhmor>
 * Copyright (C) 2016 GII (UDC) and REAL (ITU)
 * Modified by 
 * <http://github.com/rmorenoga>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package simvrep;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.SystemUtils;

import coppelia.remoteApi;
import simvrep.SimulationConfiguration;
import mpi.MPI;
import simvrep.StreamGobbler;

public class Simulator {

	Process simulator = null;
    StreamGobbler errorGobbler = null;
    StreamGobbler outputGobbler = null;
    private int rank = 0, clientID;
    private remoteApi vrepApi = null;

    public void start() {
        this.start(19997);
    }
    
public void start(int jobId) {
        
        if (SimulationConfiguration.isUseMPI()) {
            rank = MPI.COMM_WORLD.Rank();
        }

        if (simulator == null) {
            List<String> processArguments = new ArrayList<String>();

            if (SystemUtils.IS_OS_WINDOWS) {
                processArguments.add("vrep.exe");
            } else {
                //SystemUtils.IS_OS_LINUX
                if (SimulationConfiguration.isUseMPI()) {
                    processArguments.add("xvfb-run");
                    processArguments.add("-a");
                }
                processArguments.add("vrep.sh");
            }
            int initPort = 4;//(int) (jobId % 316);
            SimulationConfiguration.setVrepStartingPort(initPort * 144 + 20000);
            int port = SimulationConfiguration.getVrepStartingPort();
            if (SimulationConfiguration.isUseMPI()) {
                port += MPI.COMM_WORLD.Rank();
            }
            processArguments.add("-h");
            processArguments.add("-gREMOTEAPISERVERSERVICE_" + port + "_FALSE_TRUE");

            String vrepPath = SimulationConfiguration.getVrepPath();
            System.out.println("vrepPath (" + rank + "): " + vrepPath);
            System.out.println(" (" + rank + ")Setting the port to: " + port);

            /*Initialize a v-rep simulator based on the starNumber parameter */
            try {

                ProcessBuilder qq = new ProcessBuilder(processArguments);//, "-h"/home/rodr/EvolWork/Modular/Maze/MazeBuilderR01.ttt");

                qq.directory(new File(vrepPath));
                qq.redirectErrorStream(true); //Error abd std output redirected to the same pipe

                //Vrep error and standart output redirected to the output of the 
                //program. To redirect to a file use these line 
                //File log = new File("vrep_output.txt");
                //qq.redirectOutput(ProcessBuilder.Redirect.appendTo(log));
//                qq.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                simulator = qq.start();
                outputGobbler = new StreamGobbler(simulator.getInputStream(), "VREP_" + port + "_OUTPUT");
                errorGobbler = new StreamGobbler(simulator.getErrorStream(), "VREP_" + port + "_ERROR");
                // kick them off
                errorGobbler.start();
                outputGobbler.start();

//            if (MPI.COMM_WORLD.Rank() == 0) {
                Thread.sleep(10000); //wait for the vrepApi simulators
//            }

            } catch (IOException e) {
                System.out.println(e.toString());
            } catch (InterruptedException e) {
                System.out.println(e.toString());
            }
        }
        
        connect2Vrep();
    }
	
public void connect2Vrep(){
    //System.out.println("Program started");
        vrepApi = new remoteApi();
        vrepApi.simxFinish(-1); // just in case, close all opened connections

        int port = SimulationConfiguration.getVrepStartingPort();
        if (SimulationConfiguration.isUseMPI()) {
            port -= MPI.COMM_WORLD.Rank();
        }

        clientID = vrepApi.simxStart("127.0.0.1", port, true, true, 5000, 5);
        int iter =0; 
        while (clientID == -1 && iter < 10) {
            System.out.println("(" + port + ")Failed connecting to remote API server on port " + port +  "; rank: " + rank + "; attempt: " + iter);
            clientID = vrepApi.simxStart("127.0.0.1", port, true, true, 5000, 5);
            iter++;
        }
        if(clientID == -1){
            System.out.println("(" + port + ")Failed connecting to remote API server on port " + port +  "; rank: " + rank + " after 10 attempts");
            System.out.println("(" + port + ")Program ended");
            MPI.COMM_WORLD.Abort(-1);//Try to close all the programs
            System.exit(-1);
        }
        
        //vrepApi.simxSynchronous(clientID, true); //We want the simulator to run by itself
    }
    
	public synchronized void Disconnect() {
		// Close connection with the simulator
		vrepApi.simxFinish(clientID);
	}

public void stop() {
    
    // First close the connection to V-REP:	
    vrepApi.simxFinish(clientID);
            
    int exitStatus = -1000;
    if (simulator != null) {
        try {
            if (!SystemUtils.IS_OS_WINDOWS) {
                System.out.println(" (" + rank + ")Ps ux 1: ");
//                if (rank == 0)
//                    getPsUX();
                killUnixProcess(simulator);
                //killall("vrepApi");
//                killall("vrepApi.sh");
//                killall("xvfb-run");

            }
            simulator.destroy();
            simulator.getErrorStream().close();
            simulator.getInputStream().close();
            simulator.getOutputStream().close();
            if (simulator != null) {
                try {
                    exitStatus = simulator.waitFor();
                    if (!SystemUtils.IS_OS_WINDOWS) {

//                        killUnixProcess(simulator);
                    }

                } catch (InterruptedException ex) {
                    Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    simulator = null;
    System.out.println(" (" + rank + ")Vrep finished with code: " + exitStatus);
    if (!SystemUtils.IS_OS_WINDOWS && MPI.COMM_WORLD.Rank() == 0) {
        System.out.println(" (" + rank + ")Ps ux 2: ");
//        getPsUX();
    }
}

public int getUnixPID(Process process) throws Exception {
    System.out.println(process.getClass().getName());
    if (process.getClass().getName().equals("java.lang.UNIXProcess")) {
        Class cl = process.getClass();
        Field field = cl.getDeclaredField("pid");
        field.setAccessible(true);
        Object pidObject = field.get(process);
        return (Integer) pidObject;
    } else {
        throw new IllegalArgumentException("Needs to be a UNIXProcess");
    }
}

public int killUnixProcess(Process process) throws Exception {
    int pid = getUnixPID(process);
    System.out.println(" (" + rank + ")PID of the vrep is: " + pid);
    //return Runtime.getRuntime().exec("pkill -TERM -P " + pid).waitFor();
    //return Runtime.getRuntime().exec("kill -- -$(ps -o pgid= "+ pid +" | grep -o '[0-9]*')").waitFor();
    int port = SimulationConfiguration.getVrepStartingPort();
        if (SimulationConfiguration.isUseMPI()) {
            port += MPI.COMM_WORLD.Rank();
        }
    System.out.println(" (" + rank + ")Stopping vrep at port : " + port);
    return Runtime.getRuntime().exec("./removeVrep.sh "+ port ).waitFor();
}

public int killall(String command) throws Exception {
    return Runtime.getRuntime().exec("killall " + command).waitFor();
}

public void getPsUX() {

    String str = "";
    try {

        List<String> processArguments = new ArrayList<String>();
        processArguments.add("ps");
        processArguments.add("ux");
        ProcessBuilder qq = new ProcessBuilder(processArguments);
        Process p = qq.start();
        p.waitFor();
        BufferedReader brStdOut = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader brStdErr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        String strErr = "", strOut = "";
        System.out.println("\n");
        while (brStdOut.ready()) {
            strOut = "(" + rank + ")";
            strOut += brStdOut.readLine();
            System.out.println(strOut);
        }
        System.out.println("\n");
        while (brStdErr.ready()) {
            strErr= "(" + rank + ")";
            strErr += brStdErr.readLine();
            System.err.println(strOut);
        }
        //str = strOut + strErr;
        brStdOut.close();
        brStdErr.close();

    } catch (IOException ex) {
        Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InterruptedException ex) {
        Logger.getLogger(Simulator.class.getName()).log(Level.SEVERE, null, ex);
    }
}

public int getClientID() {
    return clientID;
}

public remoteApi getVrepApi() {
    return vrepApi;
}

	
}

/*Code taken from http://www.javaworld.com/article/2071275/core-java/when-runtime-exec---won-t.html*/
class StreamGobbler extends Thread {

    InputStream is;
    String type;

    StreamGobbler(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }

    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            for (;;) {
                while ((line = br.readLine()) != null) {
                    //System.out.println(type + ">" + line);
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(StreamGobbler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}