# Evolution
Evolution of modular robots in different environments

The goal of this projecto is to evolve controllers for modular robots using evolutionary algorithms.

The Evolutionary framework used is JEAF (https://github.com/GII/JEAF) and the modular robots are simulated using V-REP(http://coppeliarobotics.com/)

An example of an evolutionary run can be seen by examining the files:

Evolution/VREPControl/src/evolutionJEAFParallelRemote/EvolJEAFMazeR.java    (Main)
Evolution/VREPControl/src/evolutionJEAFParallelRemote/CalcFitnessMazeRS234.java  (Objective Function)
Evolution/VREPControl/EvolconfigMazeRS234.xml (Configuration file)

The simulation files can be found on the Maze folder, the file location should be adjusted in the files above in order for the program to work

This is part of a Ph.D thesis project developed at the Alife research group at Universidad Nacional de Colombia, Bogota, Colombia, with the help of the REAL group at the IT University of Copenhagen
