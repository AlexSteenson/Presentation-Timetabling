package scheduler.ifs.solution;

import java.util.ArrayList;
import java.util.Arrays;

import scheduler.Config;
import scheduler.Student;

/**
 * Contains all the information needed for the solution
 * @author Alex
 *
 */
public class Solution {

	private ArrayList<Student> unassignedVariables = new ArrayList<Student>();
	private int iteration;
	private Student[][] solution = new Student[Config.COMPUTERNUM][Config.NUMTIMESLOTS];

	// Constructor
	public Solution() {
		iteration = 0;
		// Fill each row
		for (Student[] row : solution)
			Arrays.fill(row, null);
	}

	// Copy constructor
	public Solution(Solution copy) {
		// Copy iteration
		this.iteration = copy.getIteration();

		// Copy solution
		for (int i = 0; i < copy.getSolution().length; i++) {
			for (int j = 0; j < copy.getSolution()[0].length; j++) {
				this.solution[i][j] = copy.getSolutionAtIndex(i, j);
			}
		}

		// Copy unassignedVariables vector
		for (int i = 0; i < copy.getVarVectorSize(); i++) {
			this.unassignedVariables.add(copy.getVar(i));
		}

	}

	// Gets current iteration
	public int getIteration() {
		return iteration;
	}

	// Increments the current iteration
	public void incIteration() {
		iteration++;
	}

	//Getters and setters
	public ArrayList<Student> getAllVars() {
		return unassignedVariables;
	}

	public Student getVar(int i) {
		return unassignedVariables.get(i);
	}

	public void addVar(Student student) {
		unassignedVariables.add(student);
	}

	public int getVarVectorSize() {
		return unassignedVariables.size();
	}

	public void removeVar(int i) {
		unassignedVariables.remove(i);
	}

	public Student[][] getSolution() {
		return solution;
	}

	public Student getSolutionAtIndex(int i, int j) {
		return solution[i][j];
	}

	public void addAssignment(int i, int j, Student var) {
		solution[i][j] = var;
	}

	public void removeAssignment(int i, int j) {
		solution[i][j] = null;
		// System.out.println("Removed");
	}

	//Checks to see if all the variables have been assigned
	public boolean isVarEmpty() {
		if(unassignedVariables.isEmpty()) {
			return true;
		}
		return false;
	}

	//Prints the solution in console
	public void printSolution() {

		int count = 0;
		for (int i = 0; i < Config.COMPUTERNUM; i++) {
			System.out.print("Room " + i + "\n");
			for (int j = 0; j < Config.NUMTIMESLOTS; j++) {
				if (solution[i][j] == null)
					System.out.print("null ");
				else {
					System.out
							.print(solution[i][j].getPm() + "(" + solution[i][j].getStudentSupervisor() + ") ");
					// System.out.print(solution[i][j].getStudentID() + " ");
					count++;
				}

			}
			System.out.print("\n");
		}
		System.out.print(count + "\n");
	}

}
