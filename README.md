**Team members :** TIONG YAOCONG (A0139922Y) yaocong@u.nus.edu.sg, HUANG LIE JUN (A0123994W) A0123994 hliejun@u.nus.edu

## CS4244 SATSolver Draft 1

### Our approach 
Our team has decided to design different types of SATSolver to compare the efficiency of each solver and attempt to improve on it.

#### Current progress
- Iterative Davis–Putnam–Logemann–Loveland (DPLL)
	- Tested with benchmark sat & unsat CNF  (Refer to `Appendix 1`)
- Recursive Davis–Putnam–Logemann–Loveland (RDPLL)
	- Tested with benchmark sat & unsat CNF (Refer to `Appendix 1`)
- CDCL (80%)
	- Our team is still resolving some bugs that occured during resolution.
	- Able to solve some cases in benchmark.

#### Results
Our team is still in the midst of running through all the benchmark test cases of our implementation. Currently, we have the following results :

- DPLL (Refer to `Appendix 2`)

### Next stage
- Finish up CDCL
	- Resolve the bugs
	- Finish testing CDCL
- Attempt heuristic to improve efficiency of choose branching variables
	- Variable State Independent Decaying Sum
	- Exponential Recency Weighted Average
- Model SATSolver in Einstein puzzle


### SETTING UP
1. Unzip the folder `CS4244-SATSolver.zip` by typing `unzip CS4244-SATSolver.zip`
2. Use the provided script `run.sh` to run the source code or running  the source code manually

#### Using `run.sh` script
- To use the script, you may need to change the the execution rights of the script by typing `chmod 755 run.sh`

- There are two different types of command
	
	1. `./run.sh [CNF FOLDER PATH] [STRATEGY]`
	
		```
		For example, to run test/50V_218C_sat/ with DPLL  
		It should be ./run.sh test/50V_218C_sat/ DPLL
		```
	
	2. `./run.sh` to run through all testcases with the default strategy, which is `DPLL`
	
- Available CNF Folder Path : (Refer to `Appendix 1`)
- Available Strategy : 
	- `DPLL`
	- `RDPLL` 
	- `CDCL` (Not working as expected)
- Result of the selected `CNF FOLDER PATH` will be in the `result` folder

#### Running source code manually
- After unzipping the folder, `javac SATSolver.java` to compile the whole project.
- To run the source code,
	1. `java SATSolver [CNF FOLDER PATH] [STRATEGY]` will run through the selected CNF folder with the selected strategy 
	2. `java SATSolver` will run through all testcases in the benchmark and create the `.txt` files in the folder

### Appendix
1. Benchmark testcases - Folder path : `test/`
	- `50V_218C_sat`
	- `50V_218C_unsat`
	- `75V_325C_sat`
	- `75V_325C_unsat`
	- `100V_430C_sat`
	- `100V_430C_unsat`
	- `125V_538C_sat`
	- `125V_538C_unsat`
	- `150V_645C_sat`
	- `150V_645C_unsat`
	- `175V_753C_sat`
	- `175V_753C_unsat`
	- `200V_860C_sat`
	- `200V_860C_unsat`
	- `225V_960C_sat`
	- `225V_960C_unsat`
	- `250V_1065C_sat`
	- `250V_1065C_unsat` 

	```
	Testcases folder format : 50V_218C_sat 
	50 Variables , 218 Clauses , Satisfiable
	```  
2. Result of DPLL - Folder path : `benchmark-result`
3. Source codes - Folder path : `src`