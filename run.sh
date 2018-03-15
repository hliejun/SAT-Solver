echo -e "Setting up CS4244 SATSolver ..."
echo -e "****************************************"

filepath=$1
strategy=$2

if [[ "$filepath" != "" && "$strategy" == "" ]]; then
	echo -e "Invalid arguments. Please follow the format given in README"
	exit 1
fi

if [[ "$filepath" == "" && "$strategy" == "" ]]; then
	
	filepath="test/"
	strategy="DPLL"
fi

echo -e "Selected CNF Folder path : " $filepath
echo -e "Selected Strategy : " $strategy

mkdir release
rm -rf result
mkdir result

cp -r src/ release
cp -r test/testcases/benchmark/ release/test
echo -e "Setup complete!"
echo -e "****************************************"

echo -e "Compiling SATSolver ..."
cd release
javac SATSolver.java
echo -e "SATSolver compiled!"
echo -e "****************************************"

echo -e "Running SATSolver ..."
echo -e "****************************************"
java SATSolver $1 $2
cp -r *.txt ../result
echo -e "Finished running test cases from " $1
echo -e "****************************************"

echo -e "Cleaning up..."
cd ..
rm -rf release
echo -e "Cleaning up completed!"
echo -e "Please save a copy of the result folder before re-running the script as it will be overwritten!"
