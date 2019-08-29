package benchmark;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

import scheduler.Config;

import java.io.IOException;

/**
 * This class is responsible for generating suitable unique benchmark data to
 * represent real world data and write all the data to a text file to be used by
 * any algorithm
 * 
 * @author Alex
 *
 */
public class benchmarkGenerator {

	// Constraint variables, change these to change the generations
	private final static int STUDENTNUM = 240;
	private final static int SUPERVISORNUM = 44;
	private final static int PMNUM = 28;

	/**
	 * Randomly assign supervisors to students given both parameters randomly create
	 * an email for each student select panel members from supervisor list and write
	 * it all to a file
	 * 
	 * @param args
	 */
	public static void main(String args[]) {

		// Decelerations
		int randValue = 0;
		String[] supAssignment = new String[STUDENTNUM];
		int[] supUse = new int[SUPERVISORNUM];
		int supAlloc = 0;
		String[] PMs = new String[PMNUM];

		Arrays.fill(supUse, (STUDENTNUM/SUPERVISORNUM) + 1);

		// Do for all students
		while (supAlloc < STUDENTNUM) {
			// Get a random supervisor
			randValue = (int) (Math.random() * SUPERVISORNUM);

			//Check assignment is legal
			if (supUse[randValue] > 0) {
				supAssignment[supAlloc] = supAlloc + " " + randValue;
				supUse[randValue]--;
				supAlloc++;
			}
		}

		// Decelerations
		Random r = new Random();
		String alpha = "qwertyuiopasdfghjklzxcvbnm";
		String randEmail = " psy";
		// Randomly select 3 letters for the email
		for (int i = 0; i < STUDENTNUM; i++) {
			for (int j = 0; j < 3; j++) {
				randEmail += alpha.charAt(r.nextInt(alpha.length()));
			}
			supAssignment[i] = supAssignment[i] + randEmail;
			randEmail = " psy";
		}

		// Randomly select PMs from the supervisor list
		int[] ints = new Random().ints(0, SUPERVISORNUM - 1).distinct().limit(PMNUM).toArray();
		for (int i = 0; i < PMNUM; i++) {
			PMs[i] = "" + ints[i];
		}

		// Benchmark file path
		String path = "C:\\Users\\User\\Desktop\\Diss\\benchmark" + STUDENTNUM + ".txt";
		writeToFile(supAssignment, path, STUDENTNUM);

		path = "C:\\Users\\User\\Desktop\\Diss\\benchmarkPM" + PMNUM + ".txt";
		writeToFile(PMs, path, PMNUM);
	}

	/**
	 * Writes all the data to a file given the file path
	 * 
	 * @param data
	 * @param path
	 * @param size
	 */
	private static void writeToFile(String[] data, String path, int size) {

		// Writes the benchmark data to a file
		Boolean append_to_file = false;

		try {
			FileWriter write = new FileWriter(path, append_to_file);
			PrintWriter print_line = new PrintWriter(write);

			// Write data to file
			for (int i = 0; i < size; i++) {
				print_line.printf("%s" + "%n", data[i]);
			}

			print_line.close();

		} catch (IOException e) {
			System.out.println("Could not write to file");
		}
	}

}
