package scheduler.fileHandlers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import scheduler.Config;
import scheduler.Student;

/**
 * Reads benchmark data from text files and assigns a panel member to each
 * student
 * 
 * @author Alex
 *
 */
public class PMAssigner {

	// Number of students
	private int studentNum;
	private int pmNum;
	// Array list of Student objects
	public ArrayList<Student> studentList = new ArrayList<Student>();

	/**
	 * Constructor
	 */
	public PMAssigner() {
		this.studentNum = Config.STUDENTNUM;
		this.pmNum = Config.PMNUM;
	}

	/**
	 * Assigns one panel member to each student making sure the pm isn't also their
	 * sup and all assignments are equal
	 */
	public void assignPanelMembers() {

		int randValue;
		int pmAlloc = 0;
		int pm;
		String[] panelMembers = readPMFile();
		double[] pmUse = new double[Config.PMNUM];
		Student s;

		//Read data from benchmark files
		readStudentSupervisorFile();

		// Fill each row
		Arrays.fill(pmUse, (Config.STUDENTNUM / Config.PMNUM) + 1);

		// Do for all students
		while (pmAlloc < Config.STUDENTNUM) {
			// Get the student that needs allocating a second marker
			s = studentList.get(pmAlloc);

			// Get a random panel member number
			randValue = (int) (Math.random() * Config.PMNUM);
			pm = Integer.parseInt(panelMembers[randValue]);

			//Check assignment is legal
			if (Integer.parseInt(s.getStudentSupervisor()) != pm && pmUse[randValue] > 0) {
				s.setPM(pm);
				pmUse[randValue]--;
				pmAlloc++;
			}
			
		}

	}

	/**
	 * Reads the student supervisor allocations and creates student objects for each student
	 */
	public void readStudentSupervisorFile() {

		//Decelerations
		String path = "C:\\Users\\User\\Desktop\\Diss\\benchmark" + studentNum + ".txt";
		String fileContent[] = readFile(path, studentNum);
		String words[] = new String[3];
		int sup;
		String email = "";
		int ID = 0;

		for (int i = 0; i < fileContent.length; i++) {
			// Split the line up into Student ID and Supervisor, create new student object
			// and add it to the array list

			words = fileContent[i].split("\\W+"); // Split the line
			ID = Integer.parseInt(words[0]); // Convert Student ID to integer
			sup = Integer.parseInt(words[1]);
			email = words[2] + "@nottingham.ac.uk";
			studentList.add(new Student(ID, email, -1, sup, Integer.toString(-1), Integer.toString(sup))); // Add new
		}
	}

	/**
	 * Reads the pm IDs
	 * @return
	 */
	private String[] readPMFile() {

		String path = "C:\\Users\\User\\Desktop\\Diss\\benchmarkPM" + pmNum + ".txt";
		String fileContent[] = readFile(path, pmNum);

		return fileContent;
	}

	/**
	 * General read file function.
	 * Reads the file and returns the contents 
	 * @param path
	 * @param size
	 * @return
	 */
	private String[] readFile(String path, int size) {

		// File path
		String FILENAME = path;
		BufferedReader br = null;
		FileReader fr = null;
		String[] content = new String[size];

		try { // Read file
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			String sCurrentLine;
			int i = 0;

			//Read in data
			while (i < size) {
				sCurrentLine = br.readLine();
				if (sCurrentLine != null) {
					content[i] = sCurrentLine;
				} else {
					break;
				}
				i++;
			}

		} catch (IOException e) { // Can't open file
			e.printStackTrace();
			System.out.println("Can't find file");
		} finally { // Close the file

			try {
				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return content;
	}
}
