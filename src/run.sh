if [ -z "$1" ] && [ -z "$2" ]
	then
		echo -e "Running Einstein puzzle"
		./default.sh
	elif [ -z "$2" ]
		then
			echo -e "Running BENCHMARK"
			./benchmark.sh
	else
		./runFile.sh $1 $2
    fi
