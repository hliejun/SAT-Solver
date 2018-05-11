init=false
isError=false

if [ -z "$1" ] && [ -z "$2" ];
	then
		init=true
		echo -e "Running Einstein puzzle"
		./default.sh
fi

if [ "$init" = false ] && [ $1 = "BENCHMARK" ];
	then
		if [ -z "$2" ];
			then
				init=true
				echo -e "Running BENCHMARK"
				./benchmark.sh
			else
				isError=true
				echo -e "Invalid command. To run benchamrk, ./run.sh BENCHMARK"
		fi
fi

if [ "$isError" = false ] && [ "$init" = false ]
	then
		./runFile.sh $1 $2
		init=true
 fi

if [ "$isError" = false ] && [ "$init" = true ]
	then
	rm *.class
	cd DataStructures
	rm *.class
	cd ..
	cd Solvers
	rm *.class
	cd CDCL
	rm *.class
	cd ..
	cd DPLL
	rm *.class
	cd ../..
	cd ..
fi