#!/bin/bash

for i in {0,1}
do
    stdbuf -oL /home/rodr/V-REP/Vrep$i/vrep$i.sh -h scenes/Maze/MDebug.ttt >>/home/rodr/git/Evolution/VREPControl/Simout/log$i 2>&1 &	
done
echo "Finished starting simulators"
