package scheduler.metaheuristics;

import scheduler.Comparator;
import scheduler.Student;
import scheduler.ifs.TabuList;
import scheduler.ifs.TabuValue;
import scheduler.ifs.termination.TerminationCondition;

public class TabuSearch {

	/**
	 * Performed on a complete solution to try find a better solution Performs a
	 * neighbourhoods search and takes the best candidate as the new solution uses a
	 * Tabu list of forbidden moves to avoid cycling
	 * 
	 * @param solution
	 * @return
	 */
	public Student[][] applyTabuSearch(Student[][] solution, int iter) {

		// Decelerations
		TabuList tabuList = new TabuList();
		int currentIteration = 0;
		Student[][] s0 = new Student[solution.length][solution[0].length];
		s0 = copyArray(solution, s0);
		Student[][] bestSol = new Student[solution.length][solution[0].length];
		bestSol = copyArray(solution, bestSol);
		Student[][] bestCand = new Student[solution.length][solution[0].length];
		bestCand = null;

		TerminationCondition TC = new TerminationCondition();
		int bestIter = 0;

		// While termination criterion isn't met
		while (TC.canContinue(currentIteration, Comparator.getValue(s0, 0)) == true) {
			// Select a random value
			int y = (int) (Math.random() * s0[0].length);
			int x = (int) (Math.random() * s0.length);

			// Create a candidate to perform the permutation on
			Student[][] cand = new Student[s0.length][s0[0].length];
			TabuValue tabu = null;

			// Neighbourhood search searching the time slots before and after
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					// Don't perform with no change, otherwise it could be the best
					if (i != 0 || j != 0) {
						cand = copyArray(s0, cand);
						Student variable = s0[x][y];
						try {
							// Add assignment
							cand[x][y] = cand[x + i][y + j];
							cand[x + i][y + j] = variable;

							if (bestCand == null) {
								bestCand = new Student[s0.length][s0[0].length];
								bestCand = copyArray(cand, bestCand);
								tabu = new TabuValue(variable.getStudentID(), x + i, y + j);
								// Check aspiration criteria
							} else if (!tabuList.checkList(x + i, y + j, variable)) {
								if (Comparator.getValue(cand, 0) < Comparator.getValue(bestSol, 0)
										&& Comparator.getValue(cand, 0) <= Comparator.getValue(bestCand, 0)) {
									bestCand = copyArray(cand, bestCand);
								}
								// Compare
								// If s0 is better than current bestCand
							} else if (Comparator.getValue(cand, 0) <= Comparator.getValue(bestCand, 0)) {
								bestCand = copyArray(cand, bestCand);
								tabu = new TabuValue(variable.getStudentID(), x + i, y + j);
							}

						} catch (ArrayIndexOutOfBoundsException e) {
						} catch (NullPointerException e) {

						}
					}

				} // end time for
			} // end computer for

			// Add value for best candidate
			tabuList.addTabu(tabu);

			// If the bestCand is better than the current best
			if (Comparator.getValue(bestCand, 0) <= Comparator.getValue(bestSol, 0)) {
				bestSol = new Student[bestCand.length][bestCand[0].length];
				bestSol = copyArray(bestCand, bestSol);
				bestIter = currentIteration;
			}
			// Set s0 to best cand to continue search from there
			s0 = copyArray(bestCand, s0);
			s0 = checkTimes(s0);
			// Reset best cand otherwise the best results could go wrong
			bestCand = null;

			currentIteration++;
		} // end while

		System.out.println("Num Iter: " + (currentIteration + iter));
		System.out.println("Best Iter: " + bestIter);
		// bestSol = checkTimes(bestSol);
		return bestSol;

	}

	/**
	 * If a computer is no longer being used remove it from the search and less it
	 * better
	 * 
	 * @param s
	 * @return
	 */
	public Student[][] checkTimes(Student[][] s) {
		for (int i = 0; i < s[0].length; i++) {
			if (s[s.length - 1][i] != null) {
				return s;
			}
		}
		Student[][] shorter = new Student[s.length - 1][s[0].length];
		return copyArray(s, shorter);
	}

	/**
	 * Perform a deep copy of the solution instead of setting reference
	 * 
	 * @param src
	 * @param dest
	 * @return
	 */
	public Student[][] copyArray(Student[][] src, Student[][] dest) {
		int length = (src.length <= dest.length) ? src.length : dest.length;

		for (int i = 0; i < length; i++)
			dest[i] = src[i].clone();

		return dest;
	}
}
