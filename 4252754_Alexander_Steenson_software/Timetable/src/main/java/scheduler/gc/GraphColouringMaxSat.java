package scheduler.gc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import scheduler.Comparator;
import scheduler.Config;
import scheduler.Student;
import scheduler.fileHandlers.DatabaseReader;
import scheduler.fileHandlers.PMAssigner;
import scheduler.metaheuristics.SASearch;
import scheduler.metaheuristics.TabuSearch;

public class GraphColouringMaxSat {

	private int colourNum = 0;
	private int colour[];
	private int matrixSize;
	private int[][] matrix;

	private final int TABU = 1;
	private final int SA = 2;

	/**
	 * Main function for the graph colouring Collects the student data, creates a
	 * solution and applies the corresponding meta heuristic
	 * 
	 * @param type
	 * @return
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 * @throws EncryptedDocumentException 
	 */
	public Student[][] createTimetable(int type) throws EncryptedDocumentException, InvalidFormatException, IOException {

		// Collects student data from either benchmarking or read data
		// Create an instance of the class
		GraphColouringMaxSat gc = new GraphColouringMaxSat();

		// Benchmark data
		//Config.STUDENTNUM = 240;
		//Config.PMNUM = 28;
		//Config.SUPNUM = 44;
		// Assign Panel members
		//PMAssigner assigner = new PMAssigner();
		//assigner.assignPanelMembers();
		//ArrayList<Student> studentList = assigner.studentList;

		// Read data
		DatabaseReader db = new DatabaseReader();
		ArrayList<Student> studentList = db.readDataFile();

		// Create adjacency matrix
		gc.matrixAssigner(studentList);
		// Perform graph colouring
		gc.graphCol();
		// Convert to standard solution format
		Student[][] solution = gc.ConvertSolution(studentList);
		System.out.println("Before: " + Comparator.getValue(solution, 0));

		// Apply meta heuristic
		if (type == TABU) {
			TabuSearch tabu = new TabuSearch();
			solution = tabu.applyTabuSearch(solution, 0);
		} else if (type == SA) {
			SASearch SA = new SASearch();
			solution = SA.applySASearch(solution, 200, 0);
		}

		// gc.printSolution(solution);
		System.out.println("Comp num: " + solution.length + " Timeslots: " + solution[0].length);
		System.out.println("After: " + Comparator.getValue(solution, 0));

		return solution;
	}

	/**
	 * Applies graph colouring Finds highest sat mode to colour and assigns it a
	 * colour
	 */
	public void graphCol() {

		// Fill colours
		Arrays.fill(colour, 0);

		// While there are nodes to still colour
		while (findHighestSatDegree() != -1) {
			// Apply colour to highest sat node
			int node = findHighestSatDegree();
			findColourAndSet(node, 1);
		}
	}

	/**
	 * Find the node with the most different coloured connecting nodes
	 * 
	 * @return node with highest sat
	 */
	public int findHighestSatDegree() {

		int highestColSat = -1;
		int highestUnColSat = -1;
		int highestSatIndex = -1;

		// Look through y axis of adjacency matrix
		for (int i = 0; i < matrixSize; i++) {
			int currentUnColSat = -1;
			int currentColSat = -1;
			ArrayList<Integer> currentCols = new ArrayList<Integer>();
			// x axis
			for (int j = 0; j < matrixSize; j++) {
				// If found a connected node and current node is uncoloured
				if (matrix[i][j] == 1 && colour[i] == 0) {
					// If connected node is uncoloured
					if (colour[j] == 0) {
						currentUnColSat++;
						// If array list is empty add colour
					} else if (currentCols.isEmpty()) {
						currentColSat++;
						currentCols.add(colour[j]);
					} else { // Search for colour in array list
						boolean add = true;
						for (int col : currentCols) {
							// If colour is there no need to do anything as its accounted for
							if (col == colour[j]) {
								add = false;
								break;
							}
						}
						// If colours not there add it to the list and inc sat
						if (add) {
							currentColSat++;
							currentCols.add(colour[j]);
						}
					}
				}
			}

			// Update best node
			if (currentColSat > highestColSat) {
				highestColSat = currentColSat;
				highestUnColSat = currentUnColSat;
				highestSatIndex = i;
			} else if (currentColSat == highestColSat && currentUnColSat > highestUnColSat) {// If coloured is the same
																								// goto uncoloured
				highestColSat = currentColSat;
				highestUnColSat = currentUnColSat;
				highestSatIndex = i;
			}

		}

		return highestSatIndex;
	}

	/**
	 * Finds the first colour a node can have
	 * 
	 * @param node
	 * @param c
	 */
	public void findColourAndSet(int node, int c) {

		// List of colours a node can't have
		ArrayList<Integer> blackListColour = new ArrayList<Integer>();

		// Add colours of connecting nodes to list
		for (int i = 0; i < matrixSize; i++) {
			if (matrix[node][i] == 1) {
				blackListColour.add(colour[i]);
			}
		}

		// Find the first colour the node can accept
		for (int i = 0; i < blackListColour.size(); i++) {
			if (blackListColour.get(i) == c) {
				c++;
				findColourAndSet(node, c);
				return;
			}
		}

		// If another colour was used increment count
		colourNum = (c > colourNum) ? c : colourNum;
		// Assign colour
		colour[node] = c;
	}

	/**
	 * Creates the matrix assigner to represent the connect vertices in an
	 * undirected graph in the example below vertex 1 is connected to vertex 2, 2 to
	 * 1 and 4, 3 to 4 and 4 to 2 and 3 0100 1001 0001 0110
	 * 
	 * @param studentList
	 * @return
	 */
	public int[][] matrixAssigner(ArrayList<Student> studentList) {

		matrixSize = Config.STUDENTNUM;
		matrix = new int[matrixSize][matrixSize];
		colour = new int[matrixSize];

		// Fills matrix with 0's
		for (int i = 0; i < matrix.length; i++) {
			Arrays.fill(matrix[i], 0);
		}

		// Set connecting edges in the adjacency matrix with 1
		for (int i = 0; i < matrixSize; i++) {
			int pm = studentList.get(i).getPm();
			int sup = studentList.get(i).getStudentSupervisorAsInt();

			for (int j = i + 1; j < matrixSize; j++) {
				Student studentCompare = studentList.get(j);
				// Connected so make connection in matrix
				if (pm == studentCompare.getPm() || sup == studentCompare.getStudentSupervisorAsInt()) {
					matrix[i][j] = 1;
					matrix[j][i] = 1;
					// Connected so make connection in matrix
				} else if (pm == studentCompare.getStudentSupervisorAsInt() || sup == studentCompare.getPm()) {
					matrix[i][j] = 1;
					matrix[j][i] = 1;
				}
			} // end inner for
		} // end outer for

		return matrix;

	}

	/**
	 * Converts colour assignments to nodes to a 2D array Colours represent the
	 * computer and the number assignment to a colour is the time slot on that
	 * computer
	 * 
	 * @param studentList
	 * @return
	 */
	public Student[][] ConvertSolution(ArrayList<Student> studentList) {

		int highestCount = 0;

		// Finds the colour with the most allocations to calculate the highest
		// number of time slots are on a computer
		for (int i = 0; i < colourNum; i++) {
			int tempCount = 0;
			// iterate over all the students for a colour
			for (int j = 0; j < matrixSize; j++) {
				// if the student is assigned the colour
				if (colour[j] == i + 1) {
					tempCount++;
				}
			}
			// If the colour has more assignments than the previous colour
			if (tempCount > highestCount) {
				highestCount = tempCount;
			}
		}
		// Creates a new solution with the highest number of time slots and computers
		// needed
		Student[][] solution = new Student[highestCount][colourNum];

		//For all the students
		for (int i = 0; i < matrixSize; i++) {
			int j = 0;
			try {
				//Find the next available slot
				while (solution[j][colour[i] - 1] != null) {
					if (j < highestCount) {
						j++;
					} else {
						System.out.println("Slots full");
						break;
					}
				}
				//Assign student to timeslot and colour
				solution[j][colour[i] - 1] = studentList.get(i);
			} catch (ArrayIndexOutOfBoundsException e) {
			}
		}
		return solution;
	}

}
