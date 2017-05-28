#!/bin/bash
#
# Setups simulator folders in Ubuntu. A V-REP folder containing a V-REP version 3.4 must exist in /home/rodr/
#

for i in {0..7}
do

cp -r V-REP_PRO_EDU_V3_4_0_Linux Vrep$i
mv ./Vrep$i/vrep ./Vrep$i/vrep$i
mv ./Vrep$i/vrep.sh ./Vrep$i/vrep$i.sh
num=$((19997 - $i))
echo $num
com='s/19997/'$num'/g'
echo $com
sed -i -e $com ./Vrep$i/remoteApiConnections.txt

done 
