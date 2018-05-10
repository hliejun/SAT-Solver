rm *.csv
javac -cp ".:../jars/super-csv-2.3.1.jar" SATSolver.java 
java -cp ".:../jars/super-csv-2.3.1.jar" SATSolver DPLL ../test/testcases/benchmark/50V_218C_sat
