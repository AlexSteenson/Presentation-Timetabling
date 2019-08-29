package scheduler.ifs.heuristics;

import scheduler.Comparator;
import scheduler.Config;
import scheduler.Student;
import scheduler.ifs.TabuList;
import scheduler.ifs.TabuValue;
import scheduler.ifs.Tuple;
import scheduler.ifs.solution.Solution;

/**
 * Selects the best value placement for a value
 * 
 * @author Alex
 *
 */
public class ValueSelect {

	// Tabu list to avoid cycling
	TabuList tabulist = new TabuList();

	public Tuple valSelect(Student var, Solution sol) {

		// Temp solution to obtain the solution value of the variable in a position
		Solution temp = new Solution(sol);
		Tuple bestCo = null;
		int bestValue = Integer.MAX_VALUE;

		Solution original = new Solution(temp);

		//For all the values
		for (int i = 0; i < Config.COMPUTERNUM; i++) {
			for (int j = 0; j < Config.NUMTIMESLOTS; j++) {
				//Check if value is Tabu
				if (tabulist.checkList(i, j, var)) {
					Student[][] timetable = temp.getSolution();
					int varVecSize = Integer.MAX_VALUE;
					//Calculate the variable vector size after placement
					//If null nothing will be unassigned so x
					if (timetable[i][j] == null) {
						varVecSize = temp.getVarVectorSize();
					} else { //something was unassigned so x + 1
						varVecSize = temp.getVarVectorSize() + 1;
					}
					//add the placement
					timetable[i][j] = var;
					int value = Comparator.getValue(timetable, varVecSize);

					//If this placement is better than the best update best
					if (value < bestValue) {
						bestValue = value;
						bestCo = new Tuple(i, j);
					}

					//Go back to original to try next placement
					temp = new Solution(original);
				}
			}
		}

		//Add placement to Tabu list
		TabuValue tabu = new TabuValue(var.getStudentID(), bestCo.x, bestCo.y);
		tabulist.addTabu(tabu);
		return bestCo;

	}
}
