#!/bin/bash

for i in {0..1}
do
    stdbuf -oL /home/rodr/V-REP/Vrep$i/vrep$i.sh -h scenes/Maze/MDebug.ttt >>./Simout/log$i 2>&1 &	
done
echo "Finished starting simulators"

sleep 20s

#echo "Starting java program"

#java -Djava.library.path=/home/rodr/Evolution/ -jar HAEAP.jar 2

#killall -r vrep

#echo "Finished"
