# Evolution
Evolution of modular robots in different environments

The goal of this project is to evolve controllers for modular robots using evolutionary algorithms.

The Evolutionary framework used is JEAF (https://github.com/GII/JEAF) and the modular robots are simulated using V-REP(http://coppeliarobotics.com/).

This is part of a Ph.D thesis project developed at the Alife research group at Universidad Nacional de Colombia, Bogota, Colombia, with the help of the REAL group at the IT University of Copenhagen.

## Setting Up

To set up the evolutionary system the simulation in V-REP and the Java Evolutionary framework (JEAF) should be downloaded and set up. This guide will concentrate on setting up the evolution on an Ubuntu 14.04 64 bit computer. This project uses java so a jre or jdk version 1.7 or newer should be installed on the pc. The whole project is set up as an Eclipse (https://eclipse.org/) project.

### MPJ

In order for the JEAF framework to work the mpj library should be installed, to do this first download it from the mpj-express project page http://mpj-express.org/download.php the mpj library comes in a compressed folder and can be put anywhere on the file system, we recommend to uncompress it in a folder under the /home/ folder (for example /home/username/MPJ/). The following lines should be added to the .bashrc file (for version 0.43 of mpj):

    export MPJ_HOME=/home/username/MPJ/mpj-v0_43
    export PATH=$MPJ_HOME/bin:$PATH

and they should go before the line that says:

    If not running interactively, don't do anything
    
After restarting the user session in Ubuntu MPJ should be ready to work.

### Simulator

The V-REP simulator can be downloaded at http://www.coppeliarobotics.com/downloads.html, for this project the Linux 64 bit Pro edu version is used. The simulator comes in a compressed file that can be put anywhere on the file system, we recommend to uncompress it under the /home/username/V-REP/ folder.

Several copies of the simulator can be created; to copy the simulator just copy all the files contained in the compressed file and put them on a different folder, for example copy them to /home/../V-REP/Vrep0/ and /home/../V-REP/Vrep1/. The numbering makes them easier to call later from the main evolutionary algorithms in java.

Three files should be modified inside the simulators files in order for them to run properly with this project, for example for the simulator in .../V-REP/Vrep0/ the files "vrep" and "vrep.sh" should be changed to "vrep0" and "vrep0.sh" respectively, also the field's "portIndex1_port" value inside the "remoteApiConnections.txt" file should be changed depending on the number of the simulator; for the simulator on ../Vrep0/ the value should be 19997, for ../Vrep1/ it should be 19996 (which is 19997-1) and so forth. 

### Simulation

The simulation scene files can be found in the Maze folder (/VREPControl/Maze/).

The file MazeBuilder01.ttt contains all that is needed for the evolution of a robot with no sensors to take place. This file receives the parameters from the main evolutionary algorithms in java, builds a maze for the robot, runs the simulation and returns a fitness to the main algorithm, for it to work properly, the file should be opened with V-REP and the script associated with the InitControl object should be modified. 

Inside the InitControl object script are four lines that load the basic parts of the maze like this:

    shandle = simLoadModel("/home/.../V-REP/corridors/StraigthCorr.ttm")

and should be modified to reflect the location of the model files in the file system, the model files can be found inside the corridors folder under the Maze folder (/VREPControl/Maze/).

### JEAF

The JEAF library jar files are already included under the VREPControl/lib folder and should be included in the path in order for the evolutionary algorithms to work. The simulator file "libremoteApiJava.so" should also be included in the program path to correctly communicate with the simulator. The "libremoteApiJava.so" file can be found in the folder /programming/remoteApiBindings/java/lib/64Bit in the simulator files.

### Evolutionary algortihm

The file:

/VREPControl/src/evolutionJEAFParallelRemote/EvolJEAFMazeR.java    (Main)

is an example of a main class for an evolutionary run. This class initiates the mpj parallel environment, starts the simulators as needed and the evolutionary process based on the parameters of the configuration file. The file should be modified to reflect the location of the simulators in the file system.

    qq.directory(new File("/home/.../V-REP/Vrep" + myRank + "/"));
    
and of the simulation scene file:

    ProcessBuilder qq = new ProcessBuilder(vrepcommand, "-h",
					"/home/.../Maze/MazeBuilder01.ttt");

 For this specific example the configuration file is required as a parameter of the program when executed from the command line. An example configuration file can be found in:

/VREPControl/EvolconfigMazeRS234.xml

The configuration file contains all the parameters needed for the evolutionary algorithm, from the population size, number of generations and type of evaluation to use, to the class that defines the fitness function. The fitness function is implemented in a class that extends the ObjectiveFunction class of the JEAF framework. For the configuration file example provided above the objective function class can be found on:

/VREPControl/src/evolutionJEAFParallelRemote/CalcFitnessMazeRS234.java

This class receives the parameters for the individual to be evaluated, packages and sends them to the corresponding simulator, and runs the robot simulation. When the simulation signals that is done the objective function class receives the results, calculates the fitness and returns it to the evolutionary process in JEAF. If there is an error during the simulation or the simulator stops responding this class also attempts to restart its corresponding simulator and tries again to evaluate the individual. In order to restart the simulator the location of the simulator should also be changed accordingly inside the RestartSim method:
<<<<<<< HEAD
=======

    qq.directory(new File("/home/.../V-REP/Vrep" + myRank + "/"));
    
as well as the location of the scene file to simulate:

    qq = new ProcessBuilder(vrepcommand, "-h",
					"/home/.../Maze/MazeBuilder01.ttt");

### Running

In order to do an evolutionary run, all the files must be "compiled" (put in a jar file for our purposes) and all the libraries that it depends on should also be included in this jar file, this can be done by using Eclipse or any other tool. Once the jar file is generated the program can be run using the following command in a terminal:

    mpjrun.sh -np numberofprecesses -dev niodev -Djava.library.path=pathtojarfile -jar Jarfile.jar

provided that the configuration file and the "libremoteApiJava.so" file are inside the same folder as the jar file. The number of processes indicates the number of simulators that will be used at the same time, for example when using the above defined simulators, Vrep0 and Vrep1, this number will be 2.

Alternatively the program can be run from Eclipse by using an mpj-express plugin, the installation and basic usage of this plugin can be found on the following guide http://mpj-express.org/docs/guides/RunningandDebuggingMPJExpresswithEclipse.pdf All the necessary files and libraries should be included in the project path in order for it to run. 
					
				
					


>>>>>>> refs/remotes/origin/master

    qq.directory(new File("/home/.../V-REP/Vrep" + myRank + "/"));
    
as well as the location of the scene file to simulate:

    qq = new ProcessBuilder(vrepcommand, "-h",
					"/home/.../Maze/MazeBuilder01.ttt");

### Running

In order to do an evolutionary run, all the files must be "compiled" (put in a jar file for our purposes) and all the libraries that it depends on should also be included in this jar file, this can be done by using Eclipse or any other tool. Once the jar file is generated the program can be run using the following command in a terminal:

    mpjrun.sh -np numberofprecesses -dev niodev -Djava.library.path=pathtojarfile -jar Jarfile.jar

provided that the configuration file and the "libremoteApiJava.so" file are inside the same folder as the jar file. The number of processes indicates the number of simulators that will be used at the same time, for example when using the above defined simulators, Vrep0 and Vrep1, this number will be 2.

Alternatively the program can be run from Eclipse by using an mpj-express plugin, the installation and basic usage of this plugin can be found on the following guide http://mpj-express.org/docs/guides/RunningandDebuggingMPJExpresswithEclipse.pdf All the necessary files and libraries should be included in the project path in order for it to run. 

