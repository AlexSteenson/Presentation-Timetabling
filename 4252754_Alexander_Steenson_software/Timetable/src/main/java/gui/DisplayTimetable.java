package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import scheduler.Config;
import scheduler.Student;
import scheduler.fileHandlers.DatabaseReader;
import scheduler.gc.GraphColouringMaxSat;
import scheduler.ifs.RunnerIFS;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.CardLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.ScrollPaneConstants;

public class DisplayTimetable extends JFrame {

	/**
	 * 
	 */
	private final int NORMAL = 0;
	private final int TABU = 1;
	private final int SA = 2;

	private Student[][] timetable;
	private Student[] displayTimetable;

	private static final long serialVersionUID = -8514966208423619162L;
	private JPanel contentPane;
	private JTable tblTimetable;
	private JPanel timetablePanel;
	private JScrollPane scrollPane;
	private JPanel mainPanel;
	private JButton btnGenerate;
	private JButton btnTimetableBack;
	private JTextField txtCompNum;
	private JTextField txtTimeSlot;
	private JLabel lblComputers;
	private JLabel lblTimeSlots;
	private JComboBox<String> algorithm;
	private JComboBox<String> metaheuristic;
	private JComboBox<String> displayType;
	private Object[][] table;

	private JButton btnSettings;
	private JButton btnDone;
	private JTextField txtFilePath;
	private JTextField txtTabuListSize;
	private JTextField txtMaxIter;
	private JTextField txtCoolingSA;
	private JPanel settingsPanel;
	private JButton btnSave;
	private JButton btnLoad;
	private JButton btnPdf;
	private JLabel lblPdfFP;
	private JTextField txtPdfFP;
	private JLabel lblDisplayType;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DisplayTimetable frame = new DisplayTimetable();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */

	public DisplayTimetable() {
		setBackground(SystemColor.windowBorder);
		setTitle("Timetable");

		DatabaseReader db = new DatabaseReader();
		db.readConfigFile();

		initComponents();
		mainPanel.setVisible(true);
		timetablePanel.setVisible(false);
		settingsPanel.setVisible(false);

		createEvents();

	}

	/**
	 * Contains all of the code for creating and initialising components
	 */
	private void initComponents() {

		// Initialise components and set their parameters
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1280, 760);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		mainPanel = new JPanel();
		contentPane.add(mainPanel, "name_174284207560615");
		mainPanel.setLayout(null);

		txtCompNum = new JTextField();
		txtCompNum.setBounds(637, 184, 116, 22);
		mainPanel.add(txtCompNum);
		txtCompNum.setColumns(10);

		txtTimeSlot = new JTextField();
		txtTimeSlot.setBounds(637, 250, 116, 22);
		mainPanel.add(txtTimeSlot);
		txtTimeSlot.setColumns(10);

		lblComputers = new JLabel("Computer Num");
		lblComputers.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblComputers.setBounds(450, 175, 175, 31);
		mainPanel.add(lblComputers);

		lblTimeSlots = new JLabel("Time Slots");
		lblTimeSlots.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblTimeSlots.setBounds(450, 241, 175, 31);
		mainPanel.add(lblTimeSlots);

		timetablePanel = new JPanel();
		contentPane.add(timetablePanel, "name_172995545792000");
		timetablePanel.setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(29, 57, 1200, 600);
		timetablePanel.add(scrollPane);

		tblTimetable = new JTable();
		scrollPane.setViewportView(tblTimetable);

		btnGenerate = new JButton("Generate");
		btnGenerate.setBounds(668, 499, 95, 25);
		mainPanel.add(btnGenerate);

		JLabel lblMainMenu = new JLabel("Main Menu");
		lblMainMenu.setBounds(540, 100, 200, 31);
		mainPanel.add(lblMainMenu);
		lblMainMenu.setFont(new Font("Tahoma", Font.PLAIN, 25));

		JLabel lblType = new JLabel("Algorithm Type");
		lblType.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblType.setBounds(450, 301, 175, 31);
		mainPanel.add(lblType);

		String[] algorithms = { "IFS", "Graph Colouring" };
		algorithm = new JComboBox<String>(algorithms);
		algorithm.setSelectedIndex(0);
		algorithm.setBounds(637, 301, 115, 25);
		mainPanel.add(algorithm);

		JLabel lblGCType = new JLabel("Metaheuristic");
		lblGCType.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblGCType.setBounds(450, 361, 175, 31);
		mainPanel.add(lblGCType);

		btnSettings = new JButton("Settings");
		btnSettings.setBounds(450, 499, 97, 25);
		mainPanel.add(btnSettings);

		btnLoad = new JButton("Load");
		btnLoad.setBounds(559, 499, 97, 25);
		mainPanel.add(btnLoad);

		lblDisplayType = new JLabel("Display Type");
		lblDisplayType.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblDisplayType.setBounds(450, 421, 175, 31);
		mainPanel.add(lblDisplayType);

		String[] GCAlgorithms = { "Normal", "Tabu", "Simulated Annealing" };
		metaheuristic = new JComboBox<String>(GCAlgorithms);
		metaheuristic.setSelectedIndex(0);
		metaheuristic.setBounds(637, 361, 115, 25);
		mainPanel.add(metaheuristic);

		String[] displays = { "Grid", "Table" };
		displayType = new JComboBox<String>(displays);
		displayType.setSelectedIndex(0);
		displayType.setBounds(637, 427, 115, 25);
		mainPanel.add(displayType);

		contentPane.setLayout(new CardLayout(0, 0));

		JLabel lblTimetable = new JLabel("Timetable");
		lblTimetable.setBounds(540, 13, 109, 31);
		timetablePanel.add(lblTimetable);
		lblTimetable.setFont(new Font("Tahoma", Font.PLAIN, 25));

		btnTimetableBack = new JButton("Back");
		btnTimetableBack.setBounds(29, 665, 85, 25);
		timetablePanel.add(btnTimetableBack);

		btnSave = new JButton("Save");
		btnSave.setBounds(1132, 670, 97, 25);
		timetablePanel.add(btnSave);

		btnPdf = new JButton("PDF");
		btnPdf.setBounds(1011, 670, 97, 25);
		timetablePanel.add(btnPdf);

		settingsPanel = new JPanel();
		contentPane.add(settingsPanel, "name_664434636972830");
		settingsPanel.setLayout(null);

		JLabel lblSettings = new JLabel("Settings");
		lblSettings.setBounds(540, 100, 200, 31);
		settingsPanel.add(lblSettings);
		lblSettings.setFont(new Font("Tahoma", Font.PLAIN, 25));

		JLabel lblFilePath = new JLabel("Data File Path");
		lblFilePath.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblFilePath.setBounds(425, 157, 172, 31);
		settingsPanel.add(lblFilePath);

		JLabel lblTabulistSize = new JLabel("Tabu List Size");
		lblTabulistSize.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblTabulistSize.setBounds(425, 355, 172, 26);
		settingsPanel.add(lblTabulistSize);

		JLabel lblMaxIterations = new JLabel("Max Iterations");
		lblMaxIterations.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblMaxIterations.setBounds(425, 289, 172, 32);
		settingsPanel.add(lblMaxIterations);

		JLabel lblSaCoolingRate = new JLabel("SA Cooling Rate");
		lblSaCoolingRate.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblSaCoolingRate.setBounds(425, 421, 200, 31);
		settingsPanel.add(lblSaCoolingRate);

		txtFilePath = new JTextField();
		txtFilePath.setBounds(637, 163, 200, 22);
		settingsPanel.add(txtFilePath);
		txtFilePath.setColumns(10);

		txtTabuListSize = new JTextField();
		txtTabuListSize.setColumns(10);
		txtTabuListSize.setBounds(637, 361, 116, 22);
		settingsPanel.add(txtTabuListSize);

		txtMaxIter = new JTextField();
		txtMaxIter.setColumns(10);
		txtMaxIter.setBounds(637, 295, 116, 22);
		settingsPanel.add(txtMaxIter);

		txtCoolingSA = new JTextField();
		txtCoolingSA.setColumns(10);
		txtCoolingSA.setBounds(637, 427, 115, 25);
		settingsPanel.add(txtCoolingSA);

		btnDone = new JButton("Done");
		btnDone.setBounds(559, 499, 97, 25);
		settingsPanel.add(btnDone);

		lblPdfFP = new JLabel("PDF File Path");
		lblPdfFP.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblPdfFP.setBounds(425, 223, 172, 31);
		settingsPanel.add(lblPdfFP);

		txtPdfFP = new JTextField();
		txtPdfFP.setBounds(637, 229, 200, 22);
		settingsPanel.add(txtPdfFP);
		txtPdfFP.setColumns(10);

	}

	/**
	 * Contains all of the code for creating events
	 */
	private void createEvents() {

		// If the generate button is clicked generate a timetable
		btnGenerate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// long tStart = System.currentTimeMillis();

				// Find what algorithm was selected to run
				if (algorithm.getSelectedItem() == "IFS") {
					try {
						// Check if parameters are okay
						if (checkAndSetValues()) {
							mainPanel.setVisible(false);

							// Time the run
							long tStart = System.currentTimeMillis();
							// Run the algorithm

							generateTimetableIFS();

							long tEnd = System.currentTimeMillis();
							// Calculate time
							long tDelta = tEnd - tStart;
							double elapsedSeconds = tDelta / 1000.0;

							System.out.println("Time: " + elapsedSeconds);
							System.out.print("\n");

							timetablePanel.setVisible(true);
						}
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Can't open Excel file");
					}

				} else if (algorithm.getSelectedItem() == "Graph Colouring") {
					try {

						mainPanel.setVisible(false);

						// Start timer
						long tStart = System.currentTimeMillis();
						// Run algorithm
						generateTimetableGraphColouring();
						long tEnd = System.currentTimeMillis();
						// Calculate time
						long tDelta = tEnd - tStart;
						double elapsedSeconds = tDelta / 1000.0;

						System.out.println("Time: " + elapsedSeconds);
						System.out.print("\n");

						// Change panels
						timetablePanel.setVisible(true);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Can't open Excel file");
					}
				}
			}
		});

		// When the settings button is clicked
		btnSettings.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				// fill text boxes with the current configurations
				txtFilePath.setText(Config.FILEPATH);
				txtPdfFP.setText(Config.PDF_FILEPATH);
				txtTabuListSize.setText(Integer.toString(Config.TABULIST));
				txtMaxIter.setText(Integer.toString(Config.MAXITER));
				txtCoolingSA.setText(Double.toString(Config.SACOOLING));

				// Change panels
				mainPanel.setVisible(false);
				settingsPanel.setVisible(true);

			}
		});

		// When the done button is clicked
		btnDone.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				try {
					// Write new configurations to config file
					DatabaseReader db = new DatabaseReader();
					db.writeConfigFile(txtFilePath.getText(), txtPdfFP.getText(), txtTabuListSize.getText(),
							txtMaxIter.getText(), txtCoolingSA.getText());

					// Update config settings
					Config.MAXITER = Integer.parseInt(txtMaxIter.getText());
					Config.TABULIST = Integer.parseInt(txtTabuListSize.getText());
					Config.SACOOLING = Double.parseDouble(txtCoolingSA.getText());
					Config.FILEPATH = txtFilePath.getText();
					Config.PDF_FILEPATH = txtPdfFP.getText();

					// Change panels
					settingsPanel.setVisible(false);
					mainPanel.setVisible(true);

				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Please enter correct values");
				}

			}
		});

		// When the back button is clicked
		btnTimetableBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// Change panels
				timetablePanel.setVisible(false);
				mainPanel.setVisible(true);
			}
		});

		// When the save button is clicked
		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				FileOutputStream fileOut = null;
				ObjectOutputStream out = null;

				try {
					// Save solution object to a file
					fileOut = new FileOutputStream("Save.caz");
					out = new ObjectOutputStream(fileOut);

					out.writeObject(timetable);
					out.close();
					fileOut.close();

					// Tell the user its saved
					JOptionPane.showMessageDialog(null, "Saved");
				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(null, "Couldn't save timetable");
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Couldn't save timetable");
				}
			}
		});

		// Mouse listener for when the PDF button it clicked
		btnPdf.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				try {
					// Open a new document
					Document document = new Document();
					if (Config.PDF_FILEPATH == "") {
						PdfWriter.getInstance(document, new FileOutputStream("timetable.pdf"));
					} else {
						PdfWriter.getInstance(document, new FileOutputStream(Config.PDF_FILEPATH + "timetable.pdf"));
					}
					document.open();

					PdfPTable table = new PdfPTable(6);
					table.setWidthPercentage(100);

					// Add the headers to the PDF
					table.addCell("Student ID");
					table.addCell("Email");
					table.addCell("Supervisor");
					table.addCell("2nd marker");
					table.addCell("Timeslot");
					table.addCell("Computer");

					int compNum = timetable.length;
					int timeslots = timetable[0].length;
					displayTimetable = new Student[compNum * timeslots];

					// Add all the assignments to a 1D array
					for (int i = 0; i < timeslots; i++) {
						for (int j = 0; j < compNum; j++) {
							displayTimetable[compNum * i + j] = timetable[j][i];
						}
					}

					// Add all the assignments
					for (int i = 0; i < displayTimetable.length; i++) {
						Student field = displayTimetable[i];

						if (field != null) {
							table.addCell(Integer.toString(field.getStudentID()));
							table.addCell(field.getEmail());
							table.addCell(field.getStudentSupervisor());
							table.addCell(field.getPmAsString());
							table.addCell(calculateTime(i / compNum + 1));
							table.addCell(Integer.toString(i % compNum + 1));
						}
					}

					// Add table to PDF
					document.add(table);
					document.close();

					JOptionPane.showMessageDialog(null, "Saved to pdf");
				} catch (DocumentException e) {
					JOptionPane.showMessageDialog(null, "Could not save to pdf");
				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(null, "Could not save to pdf (It may be open?)");
				}
			}
		});

		// Mouse listener for load button
		btnLoad.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg0) {

				FileInputStream fis;
				ObjectInputStream ois;
				try {
					// Open file
					fis = new FileInputStream("Save.caz");
					ois = new ObjectInputStream(fis);
					timetable = (Student[][]) ois.readObject();

					fis.close();
					ois.close();

					// Calculate student num
					int stuNum = 0;
					for (Student[] row : timetable) {
						for (Student stu : row) {
							if (stu != null) {
								stuNum++;
							}
						}
					}

					Config.STUDENTNUM = stuNum;

					// Draw timetable depending on what display type is selected
					if (displayType.getSelectedItem() == "Grid") {
						setAndDisplayGrid(timetable);
					} else {
						setAndDisplayTable(timetable);
					}

					// Change panels
					mainPanel.setVisible(false);
					timetablePanel.setVisible(true);

				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(null, "Can't find a save");
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Couldn't open file");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}

		});

	}

	/**
	 * Checks the validity of the vales entered for the computer numbers and time
	 * slots If its not valid then display a pop up box, otherwise allow the entries
	 * and generate a timetable
	 * 
	 * @return
	 */
	private boolean checkAndSetValues() {

		try {
			int compNum = Integer.parseInt(txtCompNum.getText());
			int timeNum = Integer.parseInt(txtTimeSlot.getText());

			// Checking if there's enough time slots to timetable every student
			if (compNum > 0 && timeNum > 0 && compNum * timeNum > Config.STUDENTNUM) {
				Config.COMPUTERNUM = compNum;
				Config.NUMTIMESLOTS = timeNum;
			} else if (compNum * timeNum < Config.STUDENTNUM) { // If there aren't enough time slots
				JOptionPane.showMessageDialog(null, "Not enough timeslots for the number of students");
				return false;
			} else {
				JOptionPane.showMessageDialog(null, "Please enter integer values above 0");
				return false;
			}

		} catch (Exception e) { // If nothing was entered into the text boxes
			JOptionPane.showMessageDialog(null, "Please enter integer values");
			return false;
		}
		return true;
	}

	/**
	 * Generates a timetable using IFS and applies the selected meta heuristic
	 * 
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws EncryptedDocumentException
	 */
	private void generateTimetableIFS() throws EncryptedDocumentException, InvalidFormatException, IOException {

		// Run IFS with the selected meta heuristic
		if (metaheuristic.getSelectedItem() == "Tabu") { // Tabu
			RunnerIFS runner = new RunnerIFS();
			timetable = runner.createTimetable(TABU);
		} else if (metaheuristic.getSelectedItem() == "Simulated Annealing") { // SA
			RunnerIFS runner = new RunnerIFS();
			timetable = runner.createTimetable(SA);
		} else { // Just IFS
			RunnerIFS runner = new RunnerIFS();
			timetable = runner.createTimetable(NORMAL);
		}

		// Display the timetable with the correct display type
		if (displayType.getSelectedItem() == "Grid") {
			setAndDisplayGrid(timetable);
		} else {
			setAndDisplayTable(timetable);
		}

	}

	/**
	 * Run graph colouring with the selected meta heuristic and display the
	 * timetable
	 * 
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws EncryptedDocumentException
	 */
	private void generateTimetableGraphColouring()
			throws EncryptedDocumentException, InvalidFormatException, IOException {

		// Run GC with the selected meta heuristic
		GraphColouringMaxSat runner = new GraphColouringMaxSat();
		if (metaheuristic.getSelectedItem() == "Tabu") {
			timetable = runner.createTimetable(TABU);
		} else if (metaheuristic.getSelectedItem() == "Simulated Annealing") {
			timetable = runner.createTimetable(SA);
		} else {
			timetable = runner.createTimetable(NORMAL);
		}

		// Display the timetable with the selected display type
		if (displayType.getSelectedItem() == "Grid") {
			setAndDisplayGrid(timetable);
		} else {
			setAndDisplayTable(timetable);
		}

	}

	/**
	 * Displays the timetable in the traditional timetable format with time across
	 * the top and computers down the side
	 * 
	 * @param timetable
	 */
	public void setAndDisplayTable(Student[][] timetable) {

		Config.COMPUTERNUM = timetable.length;
		Config.NUMTIMESLOTS = timetable[0].length;
		table = new Object[Config.COMPUTERNUM][Config.NUMTIMESLOTS + 1];
		String[] headers = new String[Config.NUMTIMESLOTS + 1];

		// Model to allow wrapping, multiple lines in one cell
		DefaultTableModel dm = new DefaultTableModel() {
			private static final long serialVersionUID = 5613501162725939403L;

			public Class<String> getColumnClass(int columnIndex) {
				return String.class;
			}
		};

		// For all the computers
		for (int i = 0; i < timetable.length; i++) {
			// Set the computer number going down the side on the left
			table[i][0] = "Computer " + (i + 1);
			// For all the time slots
			for (int j = 1; j <= timetable[0].length; j++) {
				Student stu = timetable[i][j - 1];
				if (stu != null) {
					String input = Integer.toString(stu.getStudentID()) + "\n" + stu.getEmail() + "\n"
							+ stu.getStudentSupervisor() + "\n" + stu.getPmAsString();

					// Add the details for the assignment
					table[i][j] = input;
				} else {
					// Nothing in the time slot
					table[i][j] = "";
				}
			}
		}

		// Add the time headers to another array
		headers[0] = "Computer";
		for (int i = 1; i < headers.length; i++) {
			headers[i] = calculateTime(i);
		}

		// Add headers and timetable
		dm.setDataVector(table, headers);
		// Set timetable settings
		tblTimetable.setModel(dm);
		tblTimetable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblTimetable.setRowHeight(80);
		// Set cell width
		for (int i = 1; i < headers.length; i++) {
			tblTimetable.getColumnModel().getColumn(i).setPreferredWidth(157);
		}
		// Wrapping
		tblTimetable.setDefaultRenderer(String.class, new MultiLineTableCellRenderer());
	}

	/**
	 * Displays the timetable in a table with just headers at the top
	 * 
	 * @param timetable
	 */
	public void setAndDisplayGrid(Student[][] timetable) {

		int compNum = timetable.length;
		int timeslots = timetable[0].length;
		displayTimetable = new Student[compNum * timeslots];

		// Convert solution to 1D array
		for (int i = 0; i < timeslots; i++) {
			for (int j = 0; j < compNum; j++) {
				displayTimetable[compNum * i + j] = timetable[j][i];
			}

		}
		table = new Object[Config.STUDENTNUM][6];

		// For every student add an entry into the table
		for (int i = 0, j = 0; i < displayTimetable.length && j < Config.STUDENTNUM; i++) {
			try {
				// Calculate time slot and computer based on location in the array
				displayTimetable[i].setTimeslot(i / compNum + 1);
				displayTimetable[i].setCompNum(i % compNum + 1);

				// Add student and assignment data to the table
				table[j][0] = displayTimetable[i].getStudentID();
				table[j][1] = displayTimetable[i].getEmail();
				table[j][2] = displayTimetable[i].getStudentSupervisor();
				table[j][3] = displayTimetable[i].getPmAsString();
				table[j][4] = calculateTime(displayTimetable[i].getTimeslot());
				table[j][5] = displayTimetable[i].getCompNum();
				j++;
			} catch (NullPointerException e) {

			}
		}

		// Set the headers and add the table data
		tblTimetable.setModel(new DefaultTableModel(table,
				new String[] { "Student ID", "Email", "Supervisor", "2nd marker", "Time", "Computer" }) {

			// Disable editing of the table
			private static final long serialVersionUID = 5533020091217193462L;
			boolean[] columnEditables = new boolean[] { false, false, false, false, false, true };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		// Set cell dimensions
		tblTimetable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tblTimetable.setRowHeight(15);
		tblTimetable.getColumnModel().getColumn(1).setPreferredWidth(157);
		tblTimetable.getColumnModel().getColumn(3).setPreferredWidth(87);
	}

	/**
	 * Calculates the time based on the position of the assignment. e.g. the first
	 * assignment for computer one is in the first time slot 9:00
	 * 
	 * @param slot
	 * @return
	 */
	public String calculateTime(int slot) {

		try {
			// Calculate the time elapsed since the start time, change the 15 to the number
			// of minutes a demonstration is. e.g. for 30 minutes change to (slot - 1) * 30
			int timeDistance = (slot - 1) * 15;
			// Starting time for the demonstrations
			String myTime = "09:00";
			SimpleDateFormat df = new SimpleDateFormat("HH:mm");
			Date d = df.parse(myTime);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			// Add the time elapsed to stating time to get the time of the demonstration
			cal.add(Calendar.MINUTE, timeDistance);
			String newTime;

			// If the time is later than the last time slot for the day move it to the next
			// day
			if (cal.get(Calendar.HOUR) > 5 && cal.get(Calendar.AM_PM) == 1) { // Later than end time and before 12am
				cal.add(Calendar.HOUR, -9);
				newTime = "Day 2: " + df.format(cal.getTime());
			} else if (cal.get(Calendar.HOUR) < 9 && cal.get(Calendar.AM_PM) == 0) { // Later than end time and later
																						// than 12am
				cal.add(Calendar.HOUR, -9);
				newTime = "Day 2: " + df.format(cal.getTime());
			} else { // In the time frame
				newTime = "Day 1: " + df.format(cal.getTime());
			}

			return newTime;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "";
	}
}