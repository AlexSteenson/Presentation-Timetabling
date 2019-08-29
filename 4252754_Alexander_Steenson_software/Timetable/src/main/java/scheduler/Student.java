package scheduler;

/**
 * Student object, contsints all the data needed about the student and the
 * demonstration. Add to here to add details about the student
 * 
 * @author Alex
 *
 */
public class Student implements java.io.Serializable {

	//Decelerations
	private static final long serialVersionUID = 7461943604693795801L;
	private int studentID;
	private String sup_s;
	private int sup;
	private String email;
	private String pm_s;
	private int pm;
	private int timeslot;
	private int compNum;

	//Constructor
	public Student(int studentID, String email, int pm, int sup, String pm_s, String sup_s) {
		this.studentID = studentID;
		this.email = email;
		this.pm = pm;
		this.sup = sup;
		this.pm_s = pm_s;
		this.sup_s = sup_s;

	}

	// Returns the students ID
	public int getStudentID() {
		return studentID;
	}

	// Returns the Students Supervisor as name
	public String getStudentSupervisor() {
		return sup_s;
	}

	// Returns the Students Supervisor ID
	public int getStudentSupervisorAsInt() {
		return sup;
	}

	// Sets the panel member ID
	public void setPM(int pm) {
		this.pm = pm;
		this.pm_s = Integer.toString(pm);
	}

	// Returns the panel member ID
	public int getPm() {
		return pm;
	}

	//Returns the panel members name
	public String getPmAsString() {
		return pm_s;
	}

	//Returns students email
	public String getEmail() {
		return email;
	}

	//Return time slot assigned to the student
	public int getTimeslot() {
		return timeslot;
	}
	//Sets the time slot for the student
	public void setTimeslot(int timeslot) {
		this.timeslot = timeslot;
	}

	//Returns computer assigned to student
	public int getCompNum() {
		return compNum;
	}

	//Sets computer assigned to student
	public void setCompNum(int compNum) {
		this.compNum = compNum;
	}

	// Overrides to string to display the student details
	@Override
	public String toString() {
		return "Student ID: " + this.getStudentID() + ", Email: " + this.getEmail() + ", Supervisor: "
				+ this.getStudentSupervisor() + ", PMs " + this.getPm();

	}

}
