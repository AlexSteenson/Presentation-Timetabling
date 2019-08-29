package scheduler.ifs;

import java.util.Arrays;

import scheduler.Config;
import scheduler.Student;

public class TabuList {
	
	//Tabu list of Tabu values
	private TabuValue[] tabuList = new TabuValue[Config.TABULIST];
	
	/**
	 * Checks the Tabu list to see if an assignment is Tabu
	 * @param x
	 * @param y
	 * @param student
	 * @return
	 */
	public boolean checkList(int x, int y, Student student) {
		
		//For the while list
		for (int i = 0; i < Config.TABULIST; i++) {
			try {
				//If everything matches its tabu
				if (tabuList[i].getRoom() == x && tabuList[i].getTime() == y
						&& student.getStudentID() == tabuList[i].getStudentID()) {
					return false;
				}
			} catch (NullPointerException e) {

			}
		}
		
		return true;
	}
	
	/**
	 * Adds a tabu value to the lists
	 * @param tabu
	 */
	public void addTabu(TabuValue tabu) {

		//If the Tabu list has a free slot to enter the Tabu value
		for (int i = tabuList.length - 1; i >= 0 ; i--) {
			if (tabuList[i] == null) {
				tabuList[i] = tabu;
				return;
			}
		}

		TabuValue[] temp = new TabuValue[tabuList.length];
		Arrays.fill(temp, null);
		
		// If the Tabu list is full make space for the new value
		for (int i = tabuList.length - 1; i >= 1; i--) {
			temp[i - 1] = tabuList[i];
		}
		//add value to the new free slot
		tabuList = temp;
		tabuList[tabuList.length - 1] = tabu;

	}

}
