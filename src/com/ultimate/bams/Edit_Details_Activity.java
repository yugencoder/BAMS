package com.ultimate.bams;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.ultimate.bams.DBHelper;
import com.ultimate.bams.Course;
import com.ultimate.bams.CourseAdapter;


public class Edit_Details_Activity  extends ListActivity {
		
		List<Course> courses;
		DBHelper dbhelper;
		
		protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_details);
		
		
		
		dbhelper = new DBHelper(this);
		courses = dbhelper.getAllCourseValues();
		
		if (courses.size() == 0) {
			//CREATE SOMETHING
			//createData();
			
			courses = dbhelper.getAllCourseValues();
		}
		
		refreshDisplay();
			
		
		};
		
		public void refreshDisplay() {
			
			ArrayAdapter<Course> adapter = new CourseAdapter(this,courses);
			setListAdapter(adapter);
			
		}
		
		private void createData() {
			
			Log.d("Insert: ", "Inserting dummy values ..");
			dbhelper.insertCourseInfo(new Course("14111001","John"));
			dbhelper.insertCourseInfo(new Course("14111003","Tim Bernes"));
			dbhelper.insertCourseInfo(new Course("14111011","Raj"));
			dbhelper.insertCourseInfo(new Course("14111033","Muhammed"));
			dbhelper.insertCourseInfo(new Course("14111022","Maria"));
			dbhelper.insertCourseInfo(new Course("14111055","Elton"));
			dbhelper.insertCourseInfo(new Course("14111111","Javad"));
			dbhelper.insertCourseInfo(new Course("14111009","savani"));
		
		}

		protected void onListItemClick(ListView l, View v, int position, long id) {
			super.onListItemClick(l, v, position, id);
			
			Course course = courses.get(position);
			
			Intent intent = new Intent(this, Edit_Details_Dialog.class);//Change this..
			
			intent.putExtra("_id",course._id);
			intent.putExtra("rollNo",course.rollNo);
			intent.putExtra("name",course.name);
			
//			intent.putExtra("_id", course.getID());
//			intent.putExtra("rollNo", course.getRollNo());
//			intent.putExtra("name", course.getName());
//			intent.putExtra("template", course.getTemplate());
//			
			startActivity(intent);
			
		}
		
		protected void onRestart()
		{
			super.onRestart();
			courses = dbhelper.getAllCourseValues(); // added
			refreshDisplay();
			
		}	
		
}		
