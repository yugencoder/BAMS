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

public class CourseAdapterV extends ArrayAdapter<Course> {
	Context context;
	List<Course> Courses;
	DBHelper dbhelper ;
	
	public CourseAdapterV(Context context, List<Course> Courses) {
		super(context, android.R.id.content, Courses);
		this.context = context;
		this.Courses = Courses;
		
	}

		@SuppressLint({ "ViewHolder", "InflateParams" }) 
		public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
        View view = vi.inflate(R.layout.basic_item_v, null);
	
        Course Course = Courses.get(position);
        dbhelper = new DBHelper(context);
        TextView tv = (TextView) view.findViewById(R.id.nameTextV);
        tv.setText(position+1+":  "+Course.getName());

        tv = (TextView) view.findViewById(R.id.rollnoTextV);
        tv.setText(Course.getRollNo());
        
        //boolean val = dbhelper.isPresent(Course.getRollNo());
        ImageView iv = (ImageView) view.findViewById(R.id.imageView2);
        	
        //String rollno = 
         if (dbhelper.isPresent(Course.getRollNo())) {
    		iv.setImageResource(R.drawable.p_icon);
         } 
         else
     		iv.setImageResource(R.drawable.a_icon);
        


         int imageResource = context.getResources().getIdentifier(
         		Course.getName().replaceAll(" ", "_").toLowerCase(), "drawable", context.getPackageName());
         if (imageResource != 0) {
         	ImageView iv2 = (ImageView) view.findViewById(R.id.imageViewV);
         	iv2.setImageResource(imageResource);
         }
         
        return view;
	}

}