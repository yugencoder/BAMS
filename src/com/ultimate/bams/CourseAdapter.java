package com.ultimate.bams;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
//import android.widget.ImageView;
import android.widget.TextView;

import com.ultimate.bams.Course;

public class CourseAdapter extends ArrayAdapter<Course> {
	Context context;
	List<Course> Courses;
	
	public CourseAdapter(Context context, List<Course> Courses) {
		super(context, android.R.id.content, Courses);
		this.context = context;
		this.Courses = Courses;
	}

		@SuppressLint({ "ViewHolder", "InflateParams" }) 
		public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
        View view = vi.inflate(R.layout.basic_item, null);
	
        Course Course = Courses.get(position);
        
        TextView tv = (TextView) view.findViewById(R.id.nameText);
        tv.setText(++position+": "+Course.getName());

        tv = (TextView) view.findViewById(R.id.rollnoText);
        tv.setText(Course.getRollNo());
        
        
        int imageResource = context.getResources().getIdentifier(
        		Course.getName().replaceAll(" ", "_").toLowerCase(), "drawable", context.getPackageName());
        if (imageResource != 0) {
        	ImageView iv = (ImageView) view.findViewById(R.id.imageView);
        	iv.setImageResource(imageResource);
        }
        
        return view;
	}

}