package scheduler.ifs.termination;

import java.util.Arrays;

import scheduler.Config;

/**
 * Determines if an algorithm is allowed to stop based on a set of criteria: max
 * iterations, if the solution value hasn't improved or if the solution value is
 * 0
 * 
 * @author Alex
 *
 */
public class TerminationCondition {

	private int iMaxIter = 0;
	private int lastSolutions[] = new int[300];

	public TerminationCondition() {
		iMaxIter = Config.MAXITER;

		Arrays.fill(lastSolutions, Integer.MAX_VALUE);
	}

	public boolean canContinue(int currentIter, int solutionValue) {
		// Add solution value to array
		addSolution(solutionValue);

		// If the max iterations has been reached stop
		if (iMaxIter >= 0 && currentIter >= iMaxIter) {
			return false;
		}

		// If the perfect solution is found stop
		if (solutionValue == 0) {
			return false;
		}

		for (int i = lastSolutions.length - 2; i >= 0; i--) { // If an improvement is made
			if (solutionValue < lastSolutions[i]) {
				return true;
			}
		}

		// If an improvement hasn't been made
		return false;
	}

	/**
	 * Adds a solution to the array. If the array is full remove the entry thats
	 * been on there the longest and add the new one
	 * 
	 * @param solutionValue
	 */
	public void addSolution(int solutionValue) {

		// If the solution value list has a free slot to enter the solutions value
		for (int i = lastSolutions.length - 1; i >= 0; i--) {
			if (lastSolutions[i] == Integer.MAX_VALUE) {
				lastSolutions[i] = solutionValue;
				return;
			}
		}

		int[] temp = new int[lastSolutions.length];
		Arrays.fill(temp, -1);

		// If the solution value list is full make space for the value
		for (int i = lastSolutions.length - 1; i >= 1; i--) {
			temp[i - 1] = lastSolutions[i];
		}
		lastSolutions = temp;
		lastSolutions[lastSolutions.length - 1] = solutionValue;
	}
}
