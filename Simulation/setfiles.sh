#!/bin/bash
#
#Copies the simulation files in the corresponding simulators folders
#


for i in {0..11}
do

cp -r ./Maze /home/morenoja/V-REP/Vrep$i/scenes/
cp -r ./Maze/corridors /home/morenoja/V-REP/Vrep$i/models/

done
