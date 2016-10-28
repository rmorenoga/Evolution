#!/bin/bash

	stdbuf -oL /home/rodr/V-REP/Vrep0/vrep0.sh -h scenes/Maze/MDebug.ttt >>/home/rodr/git/Evolution/VREPControl/Simout/log0 2>&1 &
	stdbuf -oL /home/rodr/V-REP/Vrep1/vrep1.sh -h scenes/Maze/MDebug.ttt >>/home/rodr/git/Evolution/VREPControl/Simout/log1 2>&1 &
	
