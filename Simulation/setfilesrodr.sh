#!/bin/bash
#
# Copies the simulation files in the corresponding simulators folders
#



for i in {0..3}
do

cp -r ./Maze /home/rodr/V-REP/Vrep$i/scenes/
cp -r ./corridors /home/rodr/V-REP/Vrep$i/models/
cp -r ./Module /home/rodr/V-REP/Vrep$i/models/
cp -r ./modular /home/rodr/V-REP/Vrep$i/lua/ 

done 
