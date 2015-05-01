package com.ultimate.bams;



import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;

public class Pickdate extends Activity
{
		protected static final String TAG = "sss";
		Button btnCalendar,btnShowpresent,btnShowabsent; Button btnClosePopup;
		static TextView mDateDisplay, mPresentStudents,mAbsentStudents, atPercent;
		static DBHelper dbHelper;
		static List<Attendance> attendanceList;
		static List<Course> courseList;
		
		// Variable for storing current date and time
		private int mYear, mMonth, mDay, mHour, mMinute;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pickdate);
		
		btnCalendar = (Button) findViewById(R.id.btnCalendar);
		btnShowabsent = (Button) findViewById(R.id.showlistabsent);
		btnShowpresent = (Button) findViewById(R.id.showlistpresent);
		
		atPercent = (TextView) findViewById(R.id.attendancepercent);
		mDateDisplay = (TextView) findViewById(R.id.pickadate);
		mPresentStudents = (TextView) findViewById(R.id.presentstudents);
		mAbsentStudents = (TextView) findViewById(R.id.absentstudents);
		dbHelper = new DBHelper(this);
		btnCalendar.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		// showDialog(DATE_DIALOG_ID);
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
}


});

btnShowabsent.setOnClickListener(new View.OnClickListener() {
@Override
	public void onClick(View v) {
	String datevalue=mDateDisplay.getText().toString() ;
	Log.e(TAG, datevalue);
	
	
	initiatePopupWindow(datevalue);
}
private PopupWindow pwindo;

private void initiatePopupWindow(String date) {


AlertDialog dialog;
String datevalue=mDateDisplay.getText().toString() ;
courseList = dbHelper.studentsAbsentOnaDate(datevalue);
Log.e(TAG, datevalue);

// TODO Auto-generated method stub
AlertDialog.Builder builder=new AlertDialog.Builder(Pickdate.this);
builder.setTitle("Absent Students");
ListView list=new ListView(Pickdate.this);
list.setAdapter(new AbsAdapter(Pickdate.this,courseList));


builder.setView(list);
dialog=builder.create();
dialog.show();



}

});

btnShowpresent.setOnClickListener(new View.OnClickListener() {
@Override
	public void onClick(View v) {
		String datevalue=mDateDisplay.getText().toString() ;
		Log.e(TAG, datevalue);
		
		
		initiatePopupWindow(datevalue);
	}
private PopupWindow pwindo;

private void initiatePopupWindow(String date) {


		AlertDialog dialog;
		String datevalue=mDateDisplay.getText().toString() ;
		attendanceList = dbHelper.getAttendancelist(datevalue);
		Log.e(TAG, datevalue);
		
		// TODO Auto-generated method stub
		AlertDialog.Builder builder=new AlertDialog.Builder(Pickdate.this);
		builder.setTitle("Present Students");
		ListView list=new ListView(Pickdate.this);
		list.setAdapter(new AttenAdapter(Pickdate.this,attendanceList));
		
		
		builder.setView(list);
		dialog=builder.create();
		dialog.show();



}

});
}





public static class DatePickerFragment extends DialogFragment
implements DatePickerDialog.OnDateSetListener {

public EditText editText;
DatePicker dpResult;

public Dialog onCreateDialog(Bundle savedInstanceState) {
// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

return new DatePickerDialog(getActivity(), this, year, month, day);
}

public void onDateSet(DatePicker view, int year, int month, int day) {


		//dpResult.getDatePicker().setMaxDate(System.currentTimeMillis());
		mDateDisplay .setText(String.valueOf(day) + "-"
		+ String.valueOf(month + 1) + "-" + String.valueOf(year));
		String datevalue2=mDateDisplay.getText().toString() ;
		
		Log.e(TAG, datevalue2);
		int countvalue= dbHelper.getPresentCount(datevalue2);
		Log.e(TAG, ""+countvalue);
		int countRegistered= dbHelper.getTotalStudentCount();
		Log.e(TAG, ""+countRegistered);
		if(countvalue>=0)
		{ mPresentStudents.setText("Present students: "+countvalue); 
		     atPercent.setText("Attendance Percentage: "+ (countvalue*100/countRegistered)+"%");}
		if(countRegistered>=countvalue)
		{ mAbsentStudents.setText("Absent students: "+(countRegistered-countvalue));}
		}
		
		}


@Override
public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
int id = item.getItemId();
if (id == R.id.action_settings) {
return true;
}
return super.onOptionsItemSelected(item);
}


}