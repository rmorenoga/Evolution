#!/bin/bash

for i in {1..2}
do
    echo "Here we go again 1234 group 0 for the $i time"
    echo "Here we go again 1234 group 0 for the $i time" >> lout.dat
    date >> lout.dat
    mpjrun.sh -np 10 -dev niodev -Djava.library.path=/home/rodr/EvolWork/Tests/ -jar EvolJEAFAvrgR.jar 10
    cp ./Testout/BestS12340.txt ./Out/BestS12340$i.txt
    cp ./Testout/TestS12340.txt ./Out/TestS12340$i.txt
	mkdir ./Out/Avrg$i/
	for j in {0..9}
	do
		cp ./Testout/Indiv$j.txt ./Out/S1234$i/Indiv$j.txt
	done
    rm ./Testout/Indiv*
done

echo "Finish $i time in 1234 group 0" >> lout.dat
date >> lout.dat

echo "done"

