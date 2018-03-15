| Name | Matric Number |
| :---: | :---: |
| Tiong YaoCong | A0139922Y |
| Huang Lie Jung | A0123994W |


# CS4244 SATSolver Draft 1

## Our approach 
Our team has decided to design different types of SATSolver to compare the efficiency of each solver and attempt to improve on it.

## Current progress
- Iterative Davis–Putnam–Logemann–Loveland (DPLL)
	- Tested with benchmark sat & unsat cnf  (Refer to appendix `1`)
- Recursive Davis–Putnam–Logemann–Loveland (RDPLL)
	- Tested with benchmark sat & unsat cnf (Refer to appendix `1`)
- CDCL (80%)
	- Our team is still resolving some bugs that occured during resolution.
	- Able to solve some cases in benchmark.

### Results
Our team is still in the midst of running through all the benchmark test cases of our implementation. Currently, we have the following results :

- DPLL (Refer to appendix `2`)

## Next stage
- Finish up CDCL
	- Resolve the bugs
	- Finish testing CDCL
- Attempt heuristic to improve efficiency of choose branching variables
	1. Variable State Independent Decaying Sum
	2. Exponential Recency Weighted Average
- Model SATSolver in Einstein puzzle


## SETTING UP
1. Unzip the folder `CS4244-SATSolver.zip` by typing `unzip CS4244-SATSolver.zip`
2. Use the provided script `run.sh` to run the source code or running  the source code manually

### Using `run.sh` script
- To use the script, there are two different type of commands
	1. `./run.sh [CNF FOLDER PATH] [STRATEGY]`  
For example, to run `/test/testcases/benchmark/25V_91C_sat/` with `DPLL`.  
It should be `./run.sh /test/testcases/benchmark/25V_91C_sat/ DPLL`
	2. `./run.sh` to run through all benchmark testcases with the default strategy, which is `DPLL`
- Result of the selected cnf will be in the `result` folder

### Running source code manually
- After unzipping the folder, `javac SATSolver.java` to compile the whole project.
- To run the source code,
	1. `java SATSolver [CNF FOLDER PATH] [STRATEGY]` will run through the selected CNF folder with the selected strategy 
	2. `java SATSolver` will run through all testcases in the benchmark and create the `.txt` files in the folder

## Appendix
1. Benchmark testcases - Folder path : `/test/testcases/benchmark/`
2. Result of DPLL - Folder path : `/benchmark-result`
3. Source codes - Folder path : `/src`