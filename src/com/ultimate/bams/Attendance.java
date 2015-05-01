package com.ultimate.bams;

public class Attendance {


		//private variables
		int _id;
		String rollNo;
		String date;
		String attendance;
		
		public Attendance(){

		}
		
		public Attendance(String rollNo, String date, String attendance){
			this.rollNo = rollNo;
			this.date = date;
			this.attendance = attendance;

		}

		// constructor
		public Attendance(int id,String rollNo, String date, String attendance){
			this._id = id;
			this.rollNo = rollNo;
			this.date = date;
			this.attendance = attendance;

		}

	
		public int getID() {
			return _id;
		}
		public void setID(int _id) {
			this._id = _id;
		}
		
		
		public String getRollNo() {
			return rollNo;
		}
		public void setRollNo(String rollNo) {
			this.rollNo = rollNo;
		}
		
		
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		
		
		public String getAttendance(){
			return this.attendance ;
		}
		public void setAttendance(String attendance){
			this.attendance = attendance;
		}

	
}
