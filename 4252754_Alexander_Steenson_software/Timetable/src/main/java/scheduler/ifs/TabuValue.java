package scheduler.ifs;

/**
 * Contains all the data for a Tabu value
 * @author Alex
 *
 */
public class TabuValue {

	private int studentID;
	private int room;
	private int time;

	/**
	 * Empty constructor
	 */
	public TabuValue() {
		studentID = -1;
		room = -1;
		time = -1;
	}

	/**
	 * Complete constructor to set values
	 * @param studentID
	 * @param room
	 * @param time
	 */
	public TabuValue(int studentID, int room, int time) {
		this.studentID = studentID;
		this.room = room;
		this.time = time;
	}

	public int getStudentID() {
		return studentID;
	}

	public int getRoom() {
		return room;
	}

	public int getTime() {
		return time;
	}

	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}

	public void setRoom(int room) {
		this.room = room;
	}

	public void setTime(int time) {
		this.time = time;
	}

}
