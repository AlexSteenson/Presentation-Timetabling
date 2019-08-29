package scheduler.fileHandlers;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import scheduler.Config;
import scheduler.Student;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Data from external files are read here, such as config and Excel files
 * Reads all the data from an Excel file
 * Work sheet 0 and columns 1, 3, 5 and 6 are read 
 * 
 * All of the config file is read and sets the Value in the config class
 * @author Alex
 */
public class DatabaseReader {

	// private final String XLSX_FILE_PATH =
	// "C:\\Users\\User\\Desktop\\Diss\\Book1.xlsx";
	private int pmCount = 0;
	private int supCount = 0;

	/**
	 * Reads an Excel file from the file path found in the config file
	 * All data needed to timetable is read
	 * PMNUM and SUPNUMis calculated and set in the config file
	 * Assigns IDs to pms and supervisors
	 * @return
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 * @throws EncryptedDocumentException 
	 */
	public ArrayList<Student> readDataFile() throws IOException, EncryptedDocumentException, InvalidFormatException {

		ArrayList<Student> studentList = new ArrayList<Student>();

		// Creating a Workbook from an Excel file (.xls or .xlsx)
		Workbook workbook;
		
			workbook = WorkbookFactory.create(new File(Config.FILEPATH));
			// Getting the Sheet at index zero
			Sheet sheet = workbook.getSheetAt(0);

			// Create a DataFormatter to format and get each cell's value as String
			DataFormatter dataFormatter = new DataFormatter();

			//Array list of names already read
			ArrayList<String> names = new ArrayList<String>();

			//Each row in the work sheet 
			for (Row row : sheet) {
				//Get supervisor name
				String cellValue = dataFormatter.formatCellValue(row.getCell(3));
				//Check if the name has already been read, if not add the name
				if (checkList(names, cellValue, true)) {
					names.add(cellValue);
				}
				//Get pm name
				cellValue = dataFormatter.formatCellValue(row.getCell(6));
				//If name has not been added add it
				if (checkList(names, cellValue, false)) {
					names.add(cellValue);
				}

			}
			
			int i = 0;
			for (Row row : sheet) {
				//Stops the headers in the Excel file being added as a student
				if (i != 0) {
					//Create a new student object
					Student stu;
					//Read student ID
					int studentID = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(1)));
					//Read supervisor name
					String supName = dataFormatter.formatCellValue(row.getCell(3));
					int supID = findID(names, supName);
					//Read student email
					String studentEmail = dataFormatter.formatCellValue(row.getCell(5));
					//Read pm name
					String pmName = dataFormatter.formatCellValue(row.getCell(6));
					int pmID = findID(names, pmName);
					//Set all the data for Student and add to array list
					stu = new Student(studentID, studentEmail, pmID, supID, pmName, supName);
					studentList.add(stu);
				} else {
					i++;
				}

			}

			workbook.close();


		//Set config data
		Config.STUDENTNUM = studentList.size();
		Config.PMNUM = pmCount;
		Config.SUPNUM = supCount;

		return studentList;
	}

	/**
	 * Searches the array list for a name, if its on there return true
	 * Increment the pm or sup cound 
	 * @param list
	 * @param name
	 * @param sup
	 * @return
	 */
	public boolean checkList(ArrayList<String> list, String name, boolean sup) {
		//Iterate over the array list 
		for (String n : list) {
			//If they're the same
			if (n.equals(name)) {
				return false;
			}
		}
		//Theyre not the same so inc count and return true
		if (sup) {
			supCount++;
		} else {
			pmCount++;
		}

		return true;
	}

	/**
	 * Finds the ID of a name in an arraylist of names
	 * @param names
	 * @param name
	 * @return
	 */
	public int findID(ArrayList<String> names, String name) {

		//Iterate over names
		for (int i = 0; i < names.size(); i++) {
			//It a match is found return the index as the ID
			if (names.get(i).equals(name)) {
				return i;
			}
		}
		//Couldn't find a match
		return -1;
	}

	/**
	 * Reads all the data from the config file
	 */
	public void readConfigFile() {

		// The name of the file to open.
		String fileName = "Config.txt";

		// This will reference one line at a time
		String line = null;

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(fileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			ArrayList<String> content = new ArrayList<String>();

			while ((line = bufferedReader.readLine()) != null) {
				content.add(line);
			}

			//Assign values
			Config.FILEPATH = content.get(0);
			Config.PDF_FILEPATH = content.get(1);
			Config.TABULIST = Integer.parseInt(content.get(2));
			Config.MAXITER = Integer.parseInt(content.get(3));
			Config.SACOOLING = Double.parseDouble(content.get(4));
			

			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}

	}

	/**
	 * Writes data to the config file
	 * @param dataFilePath
	 * @param pdfFilePath
	 * @param tabuSize
	 * @param maxIter
	 * @param SACooling
	 */
	public void writeConfigFile(String dataFilePath, String pdfFilePath, String tabuSize, String maxIter, String SACooling) {

		// The name of the file to open.
		String fileName = "Config.txt";

		try {
			// Default encoding.
			FileWriter fileWriter = new FileWriter(fileName);

			// Always wrap FileWriter in BufferedWriter.
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			//Write data, one on each line
			bufferedWriter.write(dataFilePath);
			bufferedWriter.newLine();
			bufferedWriter.write(pdfFilePath);
			bufferedWriter.newLine();
			bufferedWriter.write(tabuSize);
			bufferedWriter.newLine();
			bufferedWriter.write(maxIter);
			bufferedWriter.newLine();
			bufferedWriter.write(SACooling);

			// close files.
			bufferedWriter.close();
		} catch (IOException ex) {
			System.out.println("Error writing to file '" + fileName + "'");
		}
	}

}
