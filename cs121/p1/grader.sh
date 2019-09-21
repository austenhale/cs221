#!/bin/bash
echo "Checking for README"
echo "------------------"
if [ -f "README" ]
then
	echo "README found."
elif [ -f "README.txt" ]
then
	echo "README.txt found (should be named README)."
else
    echo "Missing README."
fi
echo "------------------"

echo "Checking for @author tag"
echo "------------------"
grep @author PlayList.java 
echo "------------------"

echo "Compiling source files"
echo "------------------"
javac *.java
echo "------------------"

echo "==============" Test Results "=============="
if (test -f PlayList.class)
then
	for n in 1 2 3 4 5 6
	do
		echo "----------  Program Input ($n)   ----------"
		cat test/test$n
		echo "----------  Expected Output ($n) ----------"
		cat test/test$n.out
		echo "----------  Your Output ($n)     ----------"
		timeout 10 java PlayList < test/test$n
	done
fi
