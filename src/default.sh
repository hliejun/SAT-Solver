rm *.csv
rm -rf previous-result
mkdir previous-result
mv *.txt previous-result/
javac -cp ".:../jars/super-csv-2.3.1.jar" SATSolver.java
java -cp ".:../jars/super-csv-2.3.1.jar" SATSolver