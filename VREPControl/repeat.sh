#!/bin/bash

for i in {0..4}
do
    echo "Here we go again SRand3 for the $i time"
    echo "Here we go again SRand3 for the $i time" >> lout.dat
    date >> lout.dat
    mpjrun.sh -np 11 -dev niodev -Djava.library.path=/home/morenoja/Evolution/ -jar EvolJEAFP.jar 0 evolJEAFP.xml
    cp ./Testout/BestSRand3.txt ./Out/BestSRand3$i.txt
    cp ./Testout/TestSRand3.txt ./Out/TestSRand3$i.txt
	mkdir ./Out/SRand$i/
	for j in {0..10}
	do
		cp ./Testout/Indiv$j.txt ./Out/SRand$i/Indiv$j.txt
	done
    rm ./Testout/Indiv*
done

echo "Finish $i time in SRand3" >> lout.dat
date >> lout.dat

echo "done"

