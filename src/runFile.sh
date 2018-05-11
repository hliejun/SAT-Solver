rm *.csv
rm -rf previous-result
mkdir previous-result
mv *.txt previous-result/
if [ -z "$1" ] && [ -z "$2" ]
  then
  	echo -e "./runFile.sh <Strategy> <File>"
    echo -e "Example : ./runFile.sh DPLL ../test/testcases/benchmark/50V_218C_sat/1.cnf"
  else
  	javac -cp ".:../jars/super-csv-2.3.1.jar" SATSolver.java
  	java -cp ".:../jars/super-csv-2.3.1.jar" SATSolver $1 $2
fi