package scheduler.metaheuristics;

import java.util.Random;

import scheduler.Comparator;
import scheduler.Config;
import scheduler.Student;
import scheduler.ifs.Tuple;
import scheduler.ifs.termination.TerminationCondition;

/**
 * Performs simulated annealing on a complete input solution to try find a
 * better solution
 * 
 * @author Alex
 *
 */
public class SASearch {

	/**
	 * Finds a neighbouring solution and calculated the probability of accepting it
	 * runs whilst the termination conditions allow it to
	 * 
	 * @param solution
	 * @param t0
	 * @return
	 */
	public Student[][] applySASearch(Student[][] solution, double t0, int iter) {

		// Initial decelerations
		double temp = t0;
		int currentIteration = 0;
		Student[][] best = new Student[solution.length][solution[0].length];
		Student[][] sStar = new Student[solution.length][solution[0].length];

		// Copy from initial solution
		best = copyArray(solution, best);
		sStar = copyArray(solution, sStar);

		TerminationCondition TC = new TerminationCondition();

		while (TC.canContinue(currentIteration, Comparator.getValue(sStar, 0)) == true) {
			// find the direction of the permutation e.g. comp 3 and time 3 to comp 3 and
			// time 4
			int neighbourMoveX = new Random().nextInt(3) - 1;
			int neighbourMoveY = new Random().nextInt(3) - 1;
			boolean valid = false;
			Tuple randomSelection = null;

			// Select a variable that isn't null
			while (valid == false) {
				randomSelection = new Tuple((int) (Math.random() * solution.length),
						(int) (Math.random() * solution[0].length));

				if (sStar[randomSelection.x][randomSelection.y] != null) {
					valid = true;
				}
			} // end while

			try {
				Student[][] sPrime = new Student[sStar.length][sStar[0].length];

				// perform the permutation to get the neighbour
				sPrime = copyArray(sStar, sPrime);
				Student var = sStar[randomSelection.x][randomSelection.y];
				sPrime[randomSelection.x][randomSelection.y] = sStar[randomSelection.x
						+ neighbourMoveX][randomSelection.y + neighbourMoveY];
				sPrime[randomSelection.x + neighbourMoveX][randomSelection.y + neighbourMoveY] = var;

				double r = Math.random();
				int delta = Comparator.getValue(sPrime, 0) - Comparator.getValue(sStar, 0);

				// Checks the change doesn't violate any hard constraints
				boolean safe = Comparator.checkSafe(sPrime);

				// Check probability of accepting solution
				if ((delta < 0 || (r < boltzmanProb(delta, temp)) && safe)) {
					sStar = copyArray(sPrime, sStar);
				}
			} catch (ArrayIndexOutOfBoundsException e) {
			}

			// If new solution is better than the best update the best
			if (Comparator.getValue(sStar, 0) < Comparator.getValue(best, 0)) {
				best = copyArray(sStar, best);
			}

			// Cool the temp
			temp *= Config.SACOOLING;
			currentIteration++;

		} // end while

		System.out.println("Iter: " + (currentIteration + iter));
		return best;
	}

	/**
	 * The boltzman probability is used to calculate a number between 0 and 1 of how
	 * likely it it the new solution will be accepted The lower delta is the higher
	 * the likelihood
	 * 
	 * @param delta
	 * @param temp
	 * @return
	 */
	public double boltzmanProb(int delta, double temp) {
		return Math.exp((-delta) / temp);
	}

	/**
	 * Performs a deep copy of the solution rather then creating a reference to it
	 * 
	 * @param src
	 * @param dest
	 * @return
	 */
	public Student[][] copyArray(Student[][] src, Student[][] dest) {
		for (int i = 0; i < src.length; i++)
			dest[i] = src[i].clone();

		return dest;
	}
}
