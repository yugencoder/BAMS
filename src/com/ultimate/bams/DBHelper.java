package com.ultimate.bams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.ultimate.bams.Course;
import com.ultimate.bams.Attendance;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{
	
	private static final String DB_NAME = "BAMS_ULTIMATE";
	private static final String COURSE_TABLE = "SA_Table";
	private static final String ATTENDANCE_TABLE = "Attendance";

	//Database version
	private static int DB_VERSION = 1;
	// Course Table Columns
	private static final String ID = "id";
	private static final String ROLLNO = "rollno";
	private static final String NAME = "name";
	private static final String TEMPLATE = "template";

	
	// Attendance Table Columns
	private static final String ATTENDANCE_ID = "id";
	private static final String ATTENDANCE_ROLLNO = "rollno";
	private static final String ATTENDANCE_DATE = "date";
	private static final String ATTENDANCE_ATTENDANCE = "attendance";
	
	private static final String TAG="DB Handler class";
	
	Context context;
	
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context=context;
		Log.i(TAG, " in DBHelper()");

	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG, "in onCreateDB");
		String CREATE_COURSE_TABLE = "CREATE TABLE " 
				+COURSE_TABLE 
				+ "("  
				+ID + " INTEGER PRIMARY KEY," 
			    + ROLLNO + " TEXT,"
				+ NAME + " TEXT,"
			    +  TEMPLATE + " BLOB" 
				+ ")";
		
		String CREATE_ATTENDANCE_TABLE = "CREATE TABLE " 
				+ ATTENDANCE_TABLE 
				+ "("
				+ ATTENDANCE_ID + " INTEGER PRIMARY KEY," 
				+ ATTENDANCE_ROLLNO + " TEXT," 
				+  ATTENDANCE_DATE + " TEXT," 
				+ ATTENDANCE_ATTENDANCE + " TEXT" 
				+ ")";
		
		
		db.execSQL(CREATE_COURSE_TABLE);
		db.execSQL(CREATE_ATTENDANCE_TABLE);
		
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "in onUpgrade");
		db.execSQL("DROP TABLE IF EXISTS " + COURSE_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + ATTENDANCE_TABLE);
		onCreate(db);
	}
	
	// Adding new CourseS info
		int insertCourseInfo(Course course) {
			int x=0;
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(ROLLNO, course.getRollNo()); // Customer Name
			values.put(NAME, course.getName()); // Customer email
			values.put(TEMPLATE, course.getTemplate());
		
			// Inserting row of values
			db.insert(COURSE_TABLE, null, values);
			db.close(); // Closing database connection
			x=1;
		 return x;
		}
		

		
		
		// Getting All Infos
		public List<Course> getAllCourseValues() {
			List<Course> courseList = new ArrayList<Course>();
			String selectQuery = "SELECT  * FROM " + COURSE_TABLE +" ORDER BY "+ROLLNO;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					Course course = new Course();
					byte[] temp;
					course.setID(Integer.parseInt(cursor.getString(0)));
					course.setRollNo(cursor.getString(cursor.getColumnIndex(ROLLNO)));
					course.setName(cursor.getString(cursor.getColumnIndex(NAME)));
					temp = cursor.getBlob(cursor.getColumnIndex(TEMPLATE));
					course.setTemplate(temp);
	
					// Adding contact to list
					courseList.add(course);
				} while (cursor.moveToNext());
			}//closing cursor properly.
			cursor.close();
			db.close();
			return courseList;
		}	
	
		
		public int updateCourseInfo(Course course) {
			//opening database as writable
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(ROLLNO, course.getRollNo());
			values.put(NAME, course.getName());
			values.put(TEMPLATE, course.getTemplate());

			// updating
			return db.update(COURSE_TABLE, values, ROLLNO + " = ?",
					new String[] { String.valueOf(course.getRollNo()) });//MOdded to rollno
		}
		
		public int updateCourseInfo(Course course, String oldrollno) {
			//opening database as writable
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(ROLLNO, course.getRollNo());
			values.put(NAME, course.getName());
			values.put(TEMPLATE, course.getTemplate());

			// updating
			return db.update(COURSE_TABLE, values, ROLLNO + " = ?",
					new String[] { String.valueOf(oldrollno) });//MOdded to rollno
		}
		
		
		byte[] fetchTemplate(String rollno){
			String selectQuery = "SELECT  * FROM " + COURSE_TABLE + "WHERE " + ROLLNO + " = " + rollno;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			
			byte[] temp;
			temp = cursor.getBlob(cursor.getColumnIndex(TEMPLATE));
			cursor.close();
			db.close();
			return temp;
			
		}
		
		// Deleting single Customer  ------NOT NEEDED HERE

		// Getting Students in the course Count
		public int getTotalStudentCount() {
			String countQuery = "SELECT  * FROM " + COURSE_TABLE;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			int count=cursor.getCount();
			cursor.close();
			db.close();
			return count;
		}
		
		void insertAttendanceInfo(Attendance attendance) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(ATTENDANCE_ROLLNO, attendance.getRollNo()); // Customer Name
			values.put(ATTENDANCE_DATE, attendance.getDate());
			values.put(ATTENDANCE_ATTENDANCE, attendance.getAttendance());

		
			// Inserting row of values
			db.insert(ATTENDANCE_TABLE, null, values);
			db.close(); // Closing database connection
		}
		
		
		//Getting particular Attendance object
				
		
	
		public List<Attendance> getAllAttendance() {
			List<Attendance> attendanceList = new ArrayList<Attendance>();
			String selectQuery = "SELECT  * FROM " + ATTENDANCE_TABLE +" ORDER BY "+ATTENDANCE_ROLLNO;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					Attendance attendance = new Attendance();
					attendance.setID(Integer.parseInt(cursor.getString(0)));
					attendance.setRollNo(cursor.getString(1));
					attendance.setDate(cursor.getString(2));
					attendance.setAttendance(cursor.getString(3));

					// Adding contact to list
					attendanceList.add(attendance);
				} while (cursor.moveToNext());
			}//closing cursor properly.
			cursor.close();
			db.close();
			return attendanceList;
		}	
	
		// Updating  Attendance Info  ---NOT NEEDED HERE
		public int updateAttendanceInfo(Attendance attendance) {
			//opening database as writable
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(ATTENDANCE_ROLLNO, attendance.getRollNo());
			values.put(ATTENDANCE_DATE, attendance.getDate());
			values.put(ATTENDANCE_ATTENDANCE, attendance.getAttendance());

			// updating
			return db.update(ATTENDANCE_TABLE, values, ATTENDANCE_ID + " = ?",
					new String[] { String.valueOf(attendance.getID()) });
		}
		
		
		
		// Deleting single Customer  ------NOT NEEDED HERE

		// Getting Students in the attendance Count
		public int getTotalAttendanceCount() {
			String countQuery = "SELECT  * FROM " + ATTENDANCE_TABLE;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			int count=cursor.getCount();
			cursor.close();
			db.close();
			return count;
		}
		
		public Course GetPartCourseInfo(int rollno) {
			//opening database as writable
			SQLiteDatabase db = this.getWritableDatabase();
			Course course= new Course();
			String selectQuery = "SELECT  * FROM " + COURSE_TABLE +"WHERE" + ROLLNO +" = "+ rollno;
			Cursor cursor = db.rawQuery(selectQuery, null);

		
					course.setID(Integer.parseInt(cursor.getString(0)));
					course.setRollNo(cursor.getString(1));
					course.setName(cursor.getString(2));
					course.setTemplate(cursor.getBlob(cursor.getColumnIndex(TEMPLATE)));
					cursor.close();
					db.close();
					return course;
					
			}//closing cursor properly.
			
		public Course GetPartCourseInfo(String rollno) {
			//opening database as writable
			SQLiteDatabase db = this.getWritableDatabase();
			Course course= new Course();
			String selectQuery = "SELECT  * FROM " + COURSE_TABLE +"WHERE" + ROLLNO +" = '"+ rollno+"'";
			Cursor cursor = db.rawQuery(selectQuery, null);

		
					course.setID(Integer.parseInt(cursor.getString(0)));
					course.setRollNo(cursor.getString(1));
					course.setName(cursor.getString(2));
					course.setTemplate(cursor.getBlob(cursor.getColumnIndex(TEMPLATE)));
					cursor.close();
					db.close();
					return course;
					
			}//closing cursor properly.
				
		public boolean isPresent(String rollno){
			
			boolean flag = true;
			String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
			
			String selectQuery = "SELECT DISTINCT "+ ATTENDANCE_ROLLNO + " FROM " + ATTENDANCE_TABLE  + " WHERE " + ATTENDANCE_DATE + "  =  "+ " '"+ timeStamp+"'"+" AND "+ ATTENDANCE_ROLLNO +" = "+" '"+rollno+"'" ;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if(cursor.getCount()==0)
				flag = false;
			cursor.close();
			db.close();
			return flag;
		}
		
	//finding a rollno in course table
			public int findaRollno(String rollno) {
				String countQuery = "SELECT  * FROM " + COURSE_TABLE+" where "+ROLLNO+" = "+"'"+rollno+"'";
				SQLiteDatabase db = this.getReadableDatabase();
				Cursor cursor = db.rawQuery(countQuery, null);
				int count=cursor.getCount();
				cursor.close();
				db.close();
				return count;
			}
			
			public int findaRollno(String rollno,String date) {
				String countQuery = "SELECT  * FROM " + ATTENDANCE_TABLE+" where "+ATTENDANCE_ROLLNO+" = "+"'"+rollno+"' AND "+ ATTENDANCE_DATE+" = "+"'"+date+"'";
				SQLiteDatabase db = this.getReadableDatabase();
				Cursor cursor = db.rawQuery(countQuery, null);
				int count=cursor.getCount();
				cursor.close();
				db.close();
				return count;
			}


		//finding attendence of a student on a date
				public int findAttendanceOnadate(String rollno, String date) {
					String countQuery = "SELECT  * FROM " + ATTENDANCE_TABLE +" where "+ATTENDANCE_ROLLNO+" = '"+rollno+"' and "+ATTENDANCE_DATE+" = '"+date+"'" +" ORDER BY "+ATTENDANCE_ROLLNO;;
					SQLiteDatabase db = this.getReadableDatabase();
					Cursor cursor = db.rawQuery(countQuery, null);
					int count=cursor.getCount();
					cursor.close();
					db.close();
					return count;
				}

				
				//getting no of present students
				public int getPresentCount(String date) {
					String countQuery = "SELECT DISTINCT "+ ATTENDANCE_ROLLNO + " FROM " + ATTENDANCE_TABLE  + " WHERE " + ATTENDANCE_DATE + "  =  "+ " '"+ date+"'" ;
					SQLiteDatabase db = this.getReadableDatabase();
					Cursor cursor = db.rawQuery(countQuery, null);
					int count=cursor.getCount();
					cursor.close();
					db.close();
					return count;
				}
				
				//returning student name for a particular roll no.
				public String getName(String rollno)
				{
					String name = "Empty" ;
					String Query = "SELECT * FROM " + COURSE_TABLE+" where "+ROLLNO+" = "+"'"+rollno+"'";
					SQLiteDatabase db = this.getReadableDatabase();
					
					Cursor cursor = db.rawQuery(Query, null);
					
					if (cursor.moveToFirst()) {
						do {
							name = cursor.getString(2);
						} while (cursor.moveToNext());
					}//closing cursor properly.
					cursor.close();
					
					return name;
					
					
				}
				
				// student on a date
				public List<Attendance> getAttendancelist(String date){
					
					List<Attendance> attendanceList = new ArrayList<Attendance>();
					String selectQuery = "SELECT * FROM " + ATTENDANCE_TABLE  + " WHERE " + ATTENDANCE_DATE + "  =  "+ " '"+ date+"'" +" ORDER BY "+ATTENDANCE_ROLLNO; ;

					SQLiteDatabase db = this.getWritableDatabase();
					Cursor cursor = db.rawQuery(selectQuery, null);

					if (cursor.moveToFirst()) {
						do {
							Attendance attendance = new Attendance();
						    attendance.setID(Integer.parseInt(cursor.getString(0)));
							attendance.setRollNo(cursor.getString(1));
							attendance.setDate(cursor.getString(2));
							attendance.setAttendance(cursor.getString(3));

							// Adding contact to list
							attendanceList.add(attendance);
						} while (cursor.moveToNext());
					}//closing cursor properly.
					cursor.close();
					db.close();
					
					return attendanceList;
				}
				
				// delete a student rollno from registered
				  public void deleteStudent(String rollno) {
			            SQLiteDatabase db = this.getWritableDatabase();

			            db.delete(COURSE_TABLE, ROLLNO+" = '"+rollno+"'" , null);
			             
			            db.close(); // Closing database connection
			        }
				//student absent on  a date

				  public List<Course> studentsAbsentOnaDate(String date) {
				  List<Course> courseList = new ArrayList<Course>();
				  String selectQuery = "SELECT * FROM " + COURSE_TABLE+" WHERE "+ROLLNO+" NOT IN ( SELECT "+ATTENDANCE_ROLLNO+" FROM " + ATTENDANCE_TABLE + " WHERE " + ATTENDANCE_DATE + " = "+ " '"+ date+"' )" +" ORDER BY "+ATTENDANCE_ROLLNO; ;

				  SQLiteDatabase db = this.getWritableDatabase();
				  Cursor cursor = db.rawQuery(selectQuery, null);

				  if (cursor.moveToFirst()) {
				  do {
				  Course course = new Course();
				  byte[] temp;
				  course.setID(Integer.parseInt(cursor.getString(0)));
				  course.setRollNo(cursor.getString(cursor.getColumnIndex(ROLLNO)));
				  course.setName(cursor.getString(cursor.getColumnIndex(NAME)));
				  temp = cursor.getBlob(cursor.getColumnIndex(TEMPLATE));
				  course.setTemplate(temp);

				  //Adding contact to list
				  courseList.add(course);
				  } while (cursor.moveToNext());
				  }//closing cursor properly.
				  cursor.close();
				  db.close();
				  return courseList;
				  }
}

		

