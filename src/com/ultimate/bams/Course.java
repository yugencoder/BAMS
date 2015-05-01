package com.ultimate.bams;
public class Course {
	//private variables
	int _id;
	String rollNo;
	String name;
	byte[] template;

	public Course(){

	}
	// constructor
	public Course(String rollNo, String name){
		this.rollNo = rollNo;
		this.name = name;

	}

	// constructor
	public Course(String rollNo, String name, byte[] template){
		this.rollNo = rollNo;
		this.name = name;
		this.template = template;
	
	}

	// getting ID
	public int getID(){
		return this._id;
	}

	// setting id
	public void setID(int id){
		this._id = id;
	}
	
	public String getRollNo(){
		return this.rollNo ;
	}

	// setting id
	public void setRollNo(String rollNo){
		this.rollNo = rollNo;
	}
	// getting name
	public String getName(){
		return this.name;
	}

	// setting name
	public void setName(String name){
		this.name = name;
	}
	//setting and getting date
	public byte[] getTemplate(){//getDate
		return this.template ;
	}


	public void setTemplate(byte[] template){
		this.template = template;
	}
	
	
}
//	public int describeContents() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//	
//	public Course(Parcel in) {
//		 Log.i(RegFingerprints_List.LOGTAG, "Parcel constructor");
//		  	 _id = in.readInt();
//	         rollNo = in.readString();
//	         name = in.readString();
//	         //template = new byte[in.readInt()];//added
//	         //in.readByteArray(template);//added
//	         in.readByteArray(template);
//   }
//	
//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//        Log.i(RegFingerprints_List.LOGTAG, "writeToParcel");
//
//         dest.writeInt(_id);
//         dest.writeString(rollNo);
//         dest.writeString(name);
//         //dest.writeInt(template.length);//added
//         dest.writeByteArray(template);
//
//		
//	}
//	
//	 public static final Parcelable.Creator<Course> CREATOR =
//             new Parcelable.Creator<Course>() {
//
//        @Override
//        public Course createFromParcel(Parcel source) {
//            Log.i(RegFingerprints_List.LOGTAG, "createFromParcel");
//
//             return new Course(source);
//        }
//
//        @Override
//        public Course[] newArray(int size) {
//             Log.i(RegFingerprints_List.LOGTAG, "newArray");
//             return new Course[size];
//        }
//
//   };
//	

