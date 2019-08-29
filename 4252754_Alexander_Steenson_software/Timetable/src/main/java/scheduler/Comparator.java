package scheduler;

import scheduler.ifs.solution.Solution;

/**
 * This class evaluates a solution based on the hard and soft constraints it
 * violates It is capable of returning a solution value, the better of two
 * solution or if a hard constraint is violated. Hard constraints classify as
 * somebody with multiple assignments at the same time. Soft constraints are
 * based on how many assignments somebody has in a row
 * 
 * @author Alex
 *
 */
public class Comparator {

	/**
	 * Checks the hard constraints to see if a hard constraint is being violated, if
	 * it is return false, else true
	 * 
	 * @param s0
	 *            - solution
	 * @return if a HC is being violated
	 */
	public static boolean checkSafe(Student[][] s0) {

		if (checkHardConst(s0) > 0)
			return false;
		else
			return true;

	}

	/**
	 * Evaluates all hard and soft constraints and how many students are still to be
	 * assigned to gain a solution value Soft constraints penalty are based on how
	 * many demonstrations in a row somebody has e.g. 3 in a row will be better than
	 * 2 but not as good as 4
	 * 
	 * @param s0
	 *            - solution
	 * @param varVecSize
	 *            - How many students are still to be assigned
	 * @return - solution value
	 */
	public static int getValue(Student[][] s0, int varVecSize) {

		// Compare togetherness, validity, breaks

		// Decelerations
		int row = 1;
		final int slots = s0[0].length;
		final int computers = s0.length;
		int solutionValue = 0;

		// For all timeslots in each computer
		for (int i = 0; i < computers; i++) {
			for (int j = 0; j < slots - 2; j++) {
				// While the current and next value isn't null
				while (s0[i][j + 1] != null && s0[i][j] != null) {
					// if pms are the same increment row
					if (s0[i][j].getPm() == s0[i][j + 1].getPm()) { // Find how many of a pm are in a
																	// row
						row++;
						// whilst j < number of time slots increment otherwise break the loop as there
						// can't be and more in a row
						if (j < slots - 2) {
							j++;
						} else {
							break;
						}
					} else {
						row++;
						break;
					}
				}
				// Calculate soft constraints based on how many in a row
				if (row < 6) {
					solutionValue += 10 - row;
				} else if (row > 9) {
					solutionValue += row - 10 + 1;
				}
				row = 1;
			}
		}

		row = 1;

		// Same as above but for supervisors
		// TODO put in another function to reduce repeated code
		for (int i = 0; i < computers; i++) {
			for (int j = 0; j < slots - 2; j++) {
				while (s0[i][j + 1] != null && s0[i][j] != null) {
					// Find how many of supervisors are in a row
					if (s0[i][j].getStudentSupervisorAsInt() == s0[i][j + 1].getStudentSupervisorAsInt()) {

						row++;
						if (j < slots - 2) {
							j++;
						} else {
							break;
						}
					} else {
						row++;
						break;
					}
				}
				if (row < 6) {
					solutionValue += 10 - row;
				} else if (row > 9) {
					solutionValue += row - 10 + 1;
				}
				row = 1;
			}
		}

		// Add on hard constraints
		solutionValue += checkHardConst(s0);
		// Add penalty for each unassigned student
		solutionValue += varVecSize * 100;
		// System.out.println("Solution value = " + solutionValue);

		return solutionValue;
	}

	/**
	 * Evaluates the solution to see what hard constraints have been violated
	 * @param s0
	 * @return
	 */
	public static int checkHardConst(Student[][] s0) {

		// Decelerations
		final int slots = s0[0].length;
		final int rooms = s0.length;
		int value = 0;

		// Hard constraints
		// For all the time slots
		for (int i = 0; i < slots; i++) {
			for (int j = 0; j < rooms; j++) {
				for (int k = j + 1; k < rooms; k++) {
					try {
						// If a supervisor or pm has a conflict where they are also a su or pm
						if (s0[j][i].getStudentSupervisorAsInt() == s0[k][i].getStudentSupervisorAsInt()
								|| s0[j][i].getPm() == s0[k][i].getPm()) {
							// Add penalty
							value += 500;
						}
						// If a supervisor or pm has a conflict where they are also a pm or sup
						if (s0[j][i].getStudentSupervisorAsInt() == s0[k][i].getPm()
								|| s0[j][i].getPm() == s0[k][i].getStudentSupervisorAsInt()) {
							// Add penalty
							value += 500;
						}
					} catch (NullPointerException e) {

					}
				}

			}
		}

		return value;

	}

	/**
	 * Compares two solutions and return the better of the two
	 * @param s0
	 * @param candidate
	 * @return
	 */
	public static Solution compareSolutions(Solution s0, Solution candidate) {

		//Get solution values for both solutions
		int s0Value = getValue(s0.getSolution(), s0.getVarVectorSize());
		int candidateValue = getValue(candidate.getSolution(), candidate.getVarVectorSize());

		//Return the better of the two
		return (s0Value < candidateValue) ? s0 : candidate;

	}

}
