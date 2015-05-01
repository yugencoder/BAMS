package com.ultimate.bams;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ultimate.bams.Course;

public class AbsAdapter extends ArrayAdapter<Course> {
	Context context;
	List<Course> Courses;
	DBHelper dbhelper ;
	//String name;
	
	public AbsAdapter(Context context, List<Course> Courses) {
		super(context, android.R.id.content, Courses);
		this.context = context;
		this.Courses = Courses;
		
	}

		@SuppressLint({ "ViewHolder", "InflateParams" }) 
		public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
        View view = vi.inflate(R.layout.abs_basic_item, null);
        dbhelper = new DBHelper(context);
        
        Course course = Courses.get(position);
       
        //name = dbhelper.getName(course.getRollNo());
        
        TextView tv = (TextView) view.findViewById(R.id.nameText_a);
        tv.setText(++position+": "+course.getName());
        
        tv = (TextView) view.findViewById(R.id.rollnoText_a);
        tv.setText("   "+course.getRollNo());
        
        /*ImageView iv = (ImageView) view.findViewById(R.id.imageView1);
        int imageResource = context.getResources().getIdentifier(
        		Course.getImage(), "drawable", context.getPackageName());
        if (imageResource != 0) {
        	iv.setImageResource(imageResource);
        }*/
        
        return view;
	}

}