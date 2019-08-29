package scheduler.ifs;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import scheduler.Comparator;
import scheduler.Config;
import scheduler.Student;
import scheduler.fileHandlers.DatabaseReader;
import scheduler.fileHandlers.PMAssigner;
import scheduler.ifs.heuristics.ValueSelect;
import scheduler.ifs.heuristics.VariableSelection;
import scheduler.ifs.solution.Solution;
import scheduler.ifs.termination.TerminationCondition;
import scheduler.metaheuristics.SASearch;
import scheduler.metaheuristics.TabuSearch;

public class RunnerIFS {

	private final int TABU = 1;
	private final int SA = 2;

	public Student[][] createTimetable(int type) throws EncryptedDocumentException, InvalidFormatException, IOException {
		
		//Read data from benchmarking
		//Config.STUDENTNUM = 160;
		//Config.PMNUM = 11;
		//Config.SUPNUM = 27;
		
		//Assign Panel members
		//PMAssigner assigner = new PMAssigner();
		//assigner.assignPanelMembers();
		//ArrayList<Student> studentList = assigner.studentList;
		//System.out.println("Assigned");

		//Read real data
		DatabaseReader db = new DatabaseReader();
		ArrayList<Student> studentList = db.readDataFile();

		TerminationCondition TC = new TerminationCondition();
		Solution s0 = new Solution();
		
		VariableSelection VS = new VariableSelection();
		ValueSelect val = new ValueSelect();

		// Add all students to student list
		for (Student student : studentList) {
			s0.addVar(student);
		}

		Solution best = new Solution(s0);
		
		while (TC.canContinue(s0.getIteration(), Comparator.getValue(s0.getSolution(), s0.getVarVectorSize())) == true) {

			// Select variable and value
			Student variable = VS.varSelect(s0);
			Tuple value = val.valSelect(variable, s0);

			// Add assignment
			if (s0.getSolutionAtIndex(value.x, value.y) == null) {
				s0.addAssignment(value.x, value.y, variable);
			} else { //If there was a student in the value add them back to the array list 
				s0.addVar(s0.getSolutionAtIndex(value.x, value.y));
				s0.removeAssignment(value.x, value.y);
				s0.addAssignment(value.x, value.y, variable);

			}

			//Update best solution
			best = new Solution(Comparator.compareSolutions(s0, best));
			s0.incIteration();

		} // end while

		System.out.println("Before: " + Comparator.getValue(best.getSolution(), 0));

		Student[][] finalSol;
		//Apply meta heuristic
		if (type == TABU) {
			TabuSearch tabu = new TabuSearch();
			finalSol = tabu.applyTabuSearch(best.getSolution(), s0.getIteration());
		} else if (type == SA) {
			SASearch SA = new SASearch();
			finalSol = SA.applySASearch(best.getSolution(), 200, s0.getIteration());
		}else {
			finalSol = best.getSolution();
		}

		//s0.printSolution();
		System.out.println("After: " + Comparator.getValue(finalSol, 0));
		System.out.println(best.isVarEmpty());

		return finalSol;
	}

}
