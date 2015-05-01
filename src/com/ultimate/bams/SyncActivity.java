package com.ultimate.bams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ultimate.bams.LoginActivity;
import com.ultimate.bams.Course;
import com.ultimate.bams.DBHelper;
import com.ultimate.bams.R;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class SyncActivity extends Activity implements
		ActionBar.TabListener, View.OnClickListener{
	
	public static final String TAG = "SYNC ACTIVITY";
	List<Course> courses;
	List<Attendance> attendances;

	Course course_obj;
	Attendance attendance_obj;
	DBHelper dbhelper,dbhelper2;
	private Button backup,
				   restore,
				   backup_atten,
				   restore_atten;
	private static int t_rno;
	private static int f_rno;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	    //requestWindowFeature(Window.FEATURE_PROGRESS);


		setContentView(R.layout.activity_sync);
		
		 //setProgressBarVisibility(true);
		    
		ParseAnalytics.trackAppOpened(getIntent());
		

		backup = (Button)findViewById(R.id.Backup);
        backup.setOnClickListener(this);

		restore = (Button)findViewById(R.id.Restore);
		restore.setOnClickListener(this);
		
		backup_atten = (Button)findViewById(R.id.Backup_attendance);
		backup_atten.setOnClickListener(this);

		restore_atten = (Button)findViewById(R.id.Restore_attendance);
		restore_atten.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		 if (v == this.backup) {
			 	populateparse();
			 
		 	}
		 
		 if (v == this.restore) {
			 	populatesql();
			 }

		 if (v == this.backup_atten) {
			 populateparse_atten();
			 }
		 
		 if (v == this.restore_atten) {
			 	populatesql_atten();
			 }
		 }

		
	
private void populateparse() {
	
setProgressBarIndeterminateVisibility(true);
	t_rno = 1;
	dbhelper = new DBHelper(this);
	f_rno = dbhelper.getTotalStudentCount();
	courses = dbhelper.getAllCourseValues();
	 
	 for (Course obj : courses) {
		
		ParseObject course = new ParseObject("Course");
		course.put("id",obj._id);
		course.put("name",obj.name );
		course.put("rollno", obj.rollNo);
		//course.put("temp2	", obj.template+".min");
		course.saveInBackground(new SaveCallback() {
	        @Override
	        public void done(ParseException e) {
	           
	            Log.d("Error","Completed Done");
	            if (e==null){
	                t_rno++;
	               //Toast.makeText(MainActivity.this,"Obj : "+ t_rno +" out of " + f_rno +" Sent!",Toast.LENGTH_SHORT).show();
                	
	                if(t_rno == f_rno)
	                {
	                	//Toast.makeText(MainActivity.this,"Obj : "+ t_rno +" out of " + f_rno +" Sent!",Toast.LENGTH_SHORT).show();
	                	Toast.makeText(SyncActivity.this,"Data Backed Up!",Toast.LENGTH_LONG).show();
	        		 	setProgressBarIndeterminateVisibility(false);


	                }
	             
	            } else {
	                Log.d("TAG",e.getMessage());
	                Log.d("TAG",e.getCode() + "error code");

	            }
	        }
	    });
		}
//setProgressBarIndeterminateVisibility(false);
}

private void populateparse_atten() {
	
	setProgressBarIndeterminateVisibility(true);
		t_rno = 1;
		dbhelper = new DBHelper(this);
		f_rno = dbhelper.getTotalAttendanceCount();
		attendances = dbhelper.getAllAttendance();
		 
		 for (Attendance obj : attendances) {
			
			ParseObject attendance = new ParseObject("Attendance");
			//attendance.put("id",obj._id);
			attendance.put("rollno",obj.rollNo );
			attendance.put("date", obj.date);
			
			attendance.saveInBackground(new SaveCallback() {
		        @Override
		        public void done(ParseException e) {
		           
		            Log.d("Error","Completed Done");
		            if (e==null){
		                t_rno++;
		               //Toast.makeText(MainActivity.this,"Obj : "+ t_rno +" out of " + f_rno +" Sent!",Toast.LENGTH_SHORT).show();
	                	
		                if(t_rno == f_rno)
		                {
		                	//Toast.makeText(MainActivity.this,"Obj : "+ t_rno +" out of " + f_rno +" Sent!",Toast.LENGTH_SHORT).show();
		                	Toast.makeText(SyncActivity.this,"Data Backed Up!",Toast.LENGTH_LONG).show();
		        		 	setProgressBarIndeterminateVisibility(false);


		                }
		             
		            } else {
		                Log.d("TAG",e.getMessage());
		                Log.d("TAG",e.getCode() + "error code");

		            }
		        }
		    });
			}
//setProgressBarIndeterminateVisibility(false);
	}

	protected void populatesql() {
	
		setProgressBarIndeterminateVisibility(true);
		
		course_obj =  new Course();
		dbhelper2 = new DBHelper(this);

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Course" );
		//query.whereEqualTo(ParseConstants.KEY_RECIPIENT_IDS, ParseUser.getCurrentUser().getObjectId());
		//query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> p_courses, ParseException e) {
				
				
				if (e == null) {
					// We found data!
					//p_courses;

					int i = 0;
					
					for(ParseObject p_course : p_courses) {
						
						//course_obj.setID(p_course.getInt("id"));
						course_obj.setRollNo(p_course.getString("rollno"));
						course_obj.setName(p_course.getString("name"));
						//course_obj.setTemplate(p_course.getBytes("template"));
						
						if(dbhelper2.findaRollno(course_obj.getRollNo())==0)
							{
							Toast.makeText(SyncActivity.this,course_obj.name+" restored",Toast.LENGTH_LONG).show();

							dbhelper2.insertCourseInfo(course_obj);
							}
						
						
						i++;
					}
					Toast.makeText(SyncActivity.this,"Data Restored!",Toast.LENGTH_LONG).show();
				 	setProgressBarIndeterminateVisibility(false);
 	
				}
			}
		});
		
		}
	
	
	protected void populatesql_atten() {
		
		setProgressBarIndeterminateVisibility(true);
		
		attendance_obj =  new Attendance();
		dbhelper2 = new DBHelper(this);

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Attendance" );
		//query.whereEqualTo(ParseConstants.KEY_RECIPIENT_IDS, ParseUser.getCurrentUser().getObjectId());
		//query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> p_attens, ParseException e) {
				
				
				if (e == null) {
					// We found data!
					//p_courses;

					int i = 0;
					
					for(ParseObject p_atten : p_attens) {
						
						//attendance_obj.setID(p_course.getInt("id"));
						attendance_obj.setRollNo(p_atten.getString("rollno"));
						attendance_obj.setDate(p_atten.getString("date"));
						attendance_obj.setAttendance(p_atten.getString("JUNK"));

						//attendance_obj.setTemplate(p_course.getBytes("template"));
						
						if(dbhelper2.findaRollno(attendance_obj.getRollNo(),attendance_obj.getDate())==0)
							{
							Toast.makeText(SyncActivity.this,attendance_obj.rollNo+" restored",Toast.LENGTH_LONG).show();

							dbhelper2.insertAttendanceInfo(attendance_obj);
							}
						
						
						i++;
					}
					Toast.makeText(SyncActivity.this,"Data Restored!",Toast.LENGTH_LONG).show();
				 	setProgressBarIndeterminateVisibility(false);
 	
				}
			}
		});
		
		}
		
/*		ParseQuery p_course query = ParseQuery.getQuery("Course");
			 query.findInBackground(new FindCallback<ParseObject>() {
			   public void done(List<ParseObject> results, ParseException e) {
				   if (e == null) {
					   //ArrayList<HashMap<String, String>> articles = new ArrayList<HashMap<String, String>>();
						for (ParseObject result : results) {
							//HashMap<String, String> article = new HashMap<String, String>();
							
							if(dbhelper.ifnotpresent(result.getString("rollno")))
							{
							Course objCourse = new Course();
							
							objCourse.setRollNo(result.getString("rollno"));
							objCourse.setName(result.getString("name"));
							dbhelper.insertCourseInfo(objCourse);
		
							}
						}
						Toast.makeText(MainActivity.this,"Data Restored from Server!",Toast.LENGTH_LONG).show();
		                
	    			 	setProgressBarIndeterminateVisibility(false);

					
				   } else {
						Log.e(TAG, "Exception caught!", e);
					}
			   }
			 });*/
			 
			 
			 
		/*ParseQuery query = new ParseQuery(AddLinkActivity.COURSE);
		query.setLimit(100);
		query.orderByDescending("createAt");
		query.findInBackground(new FindCallback() {
			public void done(List<ParseObject> results, ParseException e) {
					if (e == null) {
						ArrayList<HashMap<String, String>> articles = new ArrayList<HashMap<String, String>>();
						for (ParseObject result : results) {
							HashMap<String, String> article = new HashMap<String, String>();
							article.put(AddLinkActivity.ROLLNO,
									result.getString(AddLinkActivity.ROLLNO));
							article.put(AddLinkActivity.NAME,
									result.getString(AddLinkActivity.NAME));
							articles.add(article);
						}
						
					} else {
						Log.e(TAG, "Exception caught!", e);
					}
				}
		});*/
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		
		if (itemId == R.id.action_logout) {
			ParseUser.logOut();
			navigateToLogin();
		}
		
		return super.onOptionsItemSelected(item);
	}

	private void navigateToLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	
}