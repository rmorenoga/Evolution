# Evolution
Evolution of modular robots in different environments

The goal of this project is to evolve controllers for modular robots using evolutionary algorithms.

The Evolutionary framework used is JEAF (https://github.com/GII/JEAF) and the modular robots are simulated using V-REP(http://coppeliarobotics.com/)

## Setting Up

To set up the evolutionary system the simulation in V-REP and the Java Evolutionary framework (JEAF) should be downloaded and set up. This guide will concentrate on setting up the evolution on an Ubuntu 14.04 64 bit computer. This project uses java so a jre or jdk version 1.7 or newer should be installed on the pc.

### Simulator

The V-REP simulator can be downloaded at http://www.coppeliarobotics.com/downloads.html, for this guide we are going to use the Linux 64 bit Pro edu version. The simulator comes packaged into a compressed file that can be put anywhere on the file system, we recommend to uncompress it under the /home/username/V-REP/ folder.

Several copies of the simulator can be created; to copy the simulator just copy all the files contained in the compressed file and put them on a different folder, for example copy them to /home/../V-REP/Vrep0/ and /home/../V-REP/Vrep1/. The numbering makes them easier to call later from the main evolutionary algorithms in java.

Three files should be modified inside the simulators files in order for them to run properly with this project, for example for the simulator in .../V-REP/Vrep0/ the files "vrep" and "vrep.sh" should be changed to "vrep0" and "vrep0.sh" also the field's "portIndex1_port" value inside the "remoteApiConnections.txt" file should be changed depending on the number of the simulator; for the simulator on ../Vrep0/ the value should be 19997, for ../Vrep1/ it should be 19996 which is 19997-1 and so forth. 

### Simulation

The simulation scene files can be found in the maze folder (/VREPControl/Maze/).

The file MazeBuilder01.ttt contains all that is needed for the evolution of a robot with no sensors to take place. This file receives the parameters from the main evolutionary algorithms in java, builds a maze for the robot, runs the simulation and returns a fitness to the main algorithm, for it to work properly, the file should be opened with V-REP and the script associated with the InitControl object should be modified. 

There are four lines that load the basic parts of the maze like this:

    shandle = simLoadModel("/home/.../V-REP/corridors/StraigthCorr.ttm")

and should be modified to reflect the location of the model files in the file system, the model files can be found inside the corridors folder under the Maze folder (/VREPControl/Maze/).

### MPJ

In order for the JEAF framework to work the mpj library should be installed, to do this first download it from the mpj-express project page http://mpj-express.org/download.php as the V-REP simulator the mpj library comes in a compressed folder and can be put anywhere on the file system, we recommend to uncompress it in a folder under the /home/ folder (for example /home/username/MPJ/). The following lines should be added to the .bashrc file (for version 0.43 of mpj):

    export MPJ_HOME=/home/username/MPJ/mpj-v0_43
    export PATH=$MPJ_HOME/bin:$PATH

and they should go before the line that says:

    If not running interactively, don't do anything
    
After restarting the user session in Ubuntu mpj should be ready to work.

### JEAF









An example of an evolutionary run can be seen by examining the files:

Evolution/VREPControl/src/evolutionJEAFParallelRemote/EvolJEAFMazeR.java    (Main)
Evolution/VREPControl/src/evolutionJEAFParallelRemote/CalcFitnessMazeRS234.java  (Objective Function)
Evolution/VREPControl/EvolconfigMazeRS234.xml (Configuration file)

The simulation files can be found on the Maze folder, the file location should be adjusted in the files above in order for the program to work

This is part of a Ph.D thesis project developed at the Alife research group at Universidad Nacional de Colombia, Bogota, Colombia, with the help of the REAL group at the IT University of Copenhagen
