package com.ultimate.bams;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ultimate.bams.Attendance;

public class AttenAdapter extends ArrayAdapter<Attendance> {
	Context context;
	List<Attendance> Attendances;
	DBHelper dbhelper ;
	String name;
	
	public AttenAdapter(Context context, List<Attendance> Attendances) {
		super(context, android.R.id.content, Attendances);
		this.context = context;
		this.Attendances = Attendances;
		
	}

		@SuppressLint({ "ViewHolder", "InflateParams" }) 
		public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
        View view = vi.inflate(R.layout.atten_basic_item, null);
        dbhelper = new DBHelper(context);
        
        Attendance attendance = Attendances.get(position);
       
        name = dbhelper.getName(attendance.getRollNo());
        
        TextView tv = (TextView) view.findViewById(R.id.nameText_a);
        tv.setText(++position+": "+name);
        
        tv = (TextView) view.findViewById(R.id.rollnoText_a);
        tv.setText("   "+attendance.getRollNo());
        
        /*ImageView iv = (ImageView) view.findViewById(R.id.imageView1);
        int imageResource = context.getResources().getIdentifier(
        		Attendance.getImage(), "drawable", context.getPackageName());
        if (imageResource != 0) {
        	iv.setImageResource(imageResource);
        }*/
        
        return view;
	}

}