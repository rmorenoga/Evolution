#!/bin/bash

for i in {8..10}
do
    echo "Here we go again for the $i time"
    echo "Here we go again for the $i time" >> lout.dat
    date >> lout.dat
    mpjrun.sh -np 32 -dev niodev -Djava.library.path=/home/rodr/EvolWork/Tests/ -jar EvolJEAFAvrgR.jar
    cp ./Testout/BestAvrg.txt ./Out/BestAvrg$i.txt
    cp ./Testout/TestAvrg.txt ./Out/TestAvrg$i.txt
	mkdir ./Out/Avrg$i/
	for j in {0..31}
	do
		cp ./Testout/Indiv$j.txt ./Out/Avrg$i/Indiv$j.txt
	done
    rm ./Testout/Indiv*
done

echo "Finish" >> lout.dat
date >> lout.dat

echo "done"

