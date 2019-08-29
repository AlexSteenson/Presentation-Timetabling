package scheduler.ifs.heuristics;

import java.util.Random;

import scheduler.Config;
import scheduler.Student;
import scheduler.ifs.Tuple;
import scheduler.ifs.solution.Solution;

/**
 * Find the best variable for placement
 * @author Alex
 *
 */
public class VariableSelection {

	/**
	 * Select the best variable if the unassigned array list isn't empty and the
	 * worst variable placement if it is. The 'best' is the variable who's
	 * supervisors or panel member already has the most placements The 'worst'counts
	 * as the variable that causes the most soft constraints to be violated
	 * 
	 * @param solution
	 * @return
	 */
	public Student varSelect(Solution solution) {

		// If all variables are assigned, select an already assigned variable
		if (solution.getAllVars().isEmpty()) {
			Student[][] sol = solution.getSolution();
			int longestRow = 0;
			Tuple longest = new Tuple(99, 99);
			int row = 1;
			int slots = sol[0].length;
			int rooms = sol.length;

			//
			for (int i = 0; i < rooms; i++) {
				for (int j = 0; j < slots - 2; j++) {
					while (sol[i][j + 1] != null && sol[i][j] != null) {
						if (sol[i][j].getPm() == sol[i][j + 1].getPm()) {
							row++;
							if (j < slots - 2) {
								j++;
							} else {
								break;
							}
						} else {
							break;
						}

					}
					if (row > longestRow) {
						longestRow = row;
						longest.x = i;
						if (longestRow == 1) {
							longest.y = j;
						} else {
							longest.y = j - (int) Math.ceil(longestRow / 2); // Gets the middle of the longest row
						}

					}
					row = 1;
				}
			}
			Student var = sol[longest.x][longest.y];
			Random rand = new Random();

			while (var == null) {
				int x = rand.nextInt(rooms);
				int y = rand.nextInt(slots);
				var = sol[x][y];
				// System.out.println("x: " + x + " y: " + y + " var" + sol[x][y]);
			}
			sol[longest.x][longest.y] = null;

			return var;

		} else { // If there are unassigned variables select the "worst"

			Student temp;
			int[] pmCount = new int[Config.SUPNUM + Config.PMNUM];
			int[] supCount = new int[Config.SUPNUM + Config.PMNUM];
			float pmLowest = Integer.MAX_VALUE;
			float supLowest = Integer.MAX_VALUE;
			int pmIndex = Integer.MAX_VALUE;
			int supIndex = Integer.MAX_VALUE;

			for (int i = 0; i < solution.getVarVectorSize(); i++) { // Get a count for each pm and supervisor not
																	// assigned
				temp = solution.getVar(i);
				pmCount[temp.getPm()]++;
				supCount[temp.getStudentSupervisorAsInt()]++;
			}

			for (int i = 0; i < Config.SUPNUM; i++) { // Find the pm with the least amount of demos
				if (pmLowest > pmCount[i] && pmCount[i] != 0) {
					pmLowest = pmCount[i];
					pmIndex = i;
				}

				if (supLowest > supCount[i] && supCount[i] != 0) { // Find the supervisor with the least amount of demos
					supLowest = supCount[i];
					supIndex = i;
				}
			}

			pmLowest /= Config.STUDENTNUM / Config.PMNUM * 100;
			supLowest /= Config.STUDENTNUM / Config.SUPNUM * 100;

			if (pmLowest < supLowest) {
				for (int i = 0; i < solution.getVarVectorSize(); i++) { // Find the first student with the selected pm
					temp = solution.getVar(i);
					if (temp.getPm() == pmIndex) {
						solution.removeVar(i);
						return temp;
					}
				}
			} else {
				for (int i = 0; i < solution.getVarVectorSize(); i++) { // Find the first student with the selected pm
					temp = solution.getVar(i);
					if (temp.getStudentSupervisorAsInt() == supIndex) {
						solution.removeVar(i);
						return temp;
					}
				}
			}
		}
		return null;
	}
}
