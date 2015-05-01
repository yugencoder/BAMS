package com.ultimate.bams; 

import java.io.*;
import java.nio.ByteBuffer;

import com.ultimate.bams.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import SecuGen.FDxSDKPro.*;

public class Register_Student extends Activity
        implements View.OnClickListener, java.lang.Runnable, SGFingerPresentEvent {

    private static final String TAG = "SecuGen USB";

   
    private Button mButtonRegister,
				   mCapture;
    private ImageView mImageViewRegister;
	private EditText studentname,
					 studentrollno;
    
	private TextView mTextViewResult,mNote;
   
    private PendingIntent mPermissionIntent;
   
	private byte[] mRegisterImage,
				   mRegisterTemplate;				   	
	private int[] mMaxTemplateSize,
				   grayBuffer;
	private int mImageWidth,
				mImageHeight;
    private Bitmap grayBitmap;
    private IntentFilter filter; 
    private SGAutoOnEventNotifier autoOn;
   
  
    private JSGFPLib sgfplib;
    private Course student;
    private DBHelper dbhelper;
	private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    
	
	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
    	public void onReceive(Context context, Intent intent) {
    		String action = intent.getAction();
    		if (ACTION_USB_PERMISSION.equals(action)) {
    			synchronized (this) {
    				UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
    				if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
    					if(device != null){
    						Log.e(TAG, "mUsbReceiver.onReceive() Device is not  null");  
    					}
    					else
        					Log.e(TAG, "mUsbReceiver.onReceive() Device is null");    						
    				} 
    				else
    					Log.e(TAG, "mUsbReceiver.onReceive() permission denied for device " + device);    				
    			}
    		}
    	}
    };  
    
    public Handler fingerDetectedHandler = new Handler(){ 
    	// @Override
	    public void handleMessage(Message msg) {
	       //Handle the message
			CaptureFingerPrint();
			EnableControls();		
	    	
	    }
    };

	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register_student);
       
        student = new Course();
        dbhelper = new DBHelper(this);	
        
		studentname = (EditText)findViewById(R.id.studentname);
		studentname.setText(student.name);//
        
		studentrollno = (EditText)findViewById(R.id.studentrollno);
		studentrollno.setText(student.rollNo);//
		mNote = (TextView)findViewById(R.id.note2);
		mButtonRegister = (Button)findViewById(R.id.registerstudent);
		mButtonRegister.setOnClickListener(this);

		
        mCapture = (Button)findViewById(R.id.capture);
        mCapture.setOnClickListener(this);
        
		mTextViewResult = (android.widget.TextView)findViewById(R.id.done);
        mImageViewRegister = (ImageView)findViewById(R.id.registerimage);
     
        grayBuffer = new int[JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES*JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES];
        
		for (int i=0; i<grayBuffer.length; ++i)
        	grayBuffer[i] = android.graphics.Color.GRAY;
        grayBitmap = Bitmap.createBitmap(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES, Bitmap.Config.ARGB_8888);
        
		grayBitmap.setPixels(grayBuffer, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, 0, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES); 
       // mImageViewFingerprint.setImageBitmap(grayBitmap);

        int[] sintbuffer = new int[(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES/2)*(JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES/2)];
        for (int i=0; i<sintbuffer.length; ++i)
        	sintbuffer[i] = android.graphics.Color.GRAY;
        Bitmap sb = Bitmap.createBitmap(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES/2, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES/2, Bitmap.Config.ARGB_8888);
        sb.setPixels(sintbuffer, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES/2, 0, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES/2, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES/2); 
        mImageViewRegister.setImageBitmap(grayBitmap);
      
        
        mMaxTemplateSize = new int[1];

        //USB Permissions
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
       	filter = new IntentFilter(ACTION_USB_PERMISSION);
       	registerReceiver(mUsbReceiver, filter);       	        
        sgfplib = new JSGFPLib((UsbManager)getSystemService(Context.USB_SERVICE));
   
        
	//	debugMessage("jnisgfplib version: " + sgfplib.Version() + "\n");
	
		autoOn = new SGAutoOnEventNotifier (sgfplib, this);
	
    }

    @Override
    public void onPause() {
    	Log.d(TAG, "onPause()");	
		autoOn.stop();
		EnableControls();
    	sgfplib.CloseDevice();
    	unregisterReceiver(mUsbReceiver);
    	mRegisterImage = null;
    	//mVerifyImage = null;
    	mRegisterTemplate = null;
    	//mVerifyTemplate = null;
   
        mImageViewRegister.setImageBitmap(grayBitmap);
    
        super.onPause(); 
    }
    
    @Override
    public void onResume(){
    	Log.d(TAG, "onResume()");	
        super.onResume();
       	registerReceiver(mUsbReceiver, filter);       	        
        long error = sgfplib.Init( SGFDxDeviceName.SG_DEV_AUTO);
        if (error != SGFDxErrorCode.SGFDX_ERROR_NONE){
        	AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        	if (error == SGFDxErrorCode.SGFDX_ERROR_DEVICE_NOT_FOUND)
        		dlgAlert.setMessage("The attached fingerprint device is not supported on Android");
        	else
        		dlgAlert.setMessage("Fingerprint device initialization failed!");
        	dlgAlert.setTitle("SecuGen Fingerprint SDK");
        	dlgAlert.setPositiveButton("OK",
        			new DialogInterface.OnClickListener() {
        		      public void onClick(DialogInterface dialog,int whichButton){
        		        	finish();
        		        	return;        		    	  
        		      }        			
        			}
        	);
        	dlgAlert.setCancelable(false);
        	dlgAlert.create().show();        	
        }
        else {
	        UsbDevice usbDevice = sgfplib.GetUsbDevice();
	        if (usbDevice == null){
	        	AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
	        	dlgAlert.setMessage("SDU04P or SDU03P fingerprint sensor not found!");
	        	dlgAlert.setTitle("SecuGen Fingerprint SDK");
	        	dlgAlert.setPositiveButton("OK",
	        			new DialogInterface.OnClickListener() {
	        		      public void onClick(DialogInterface dialog,int whichButton){
	        		        	finish();
	        		        	return;        		    	  
	        		      }        			
	        			}
	        	);
	        	dlgAlert.setCancelable(false);
	        	dlgAlert.create().show();
	        }
	        else {
		        sgfplib.GetUsbManager().requestPermission(usbDevice, mPermissionIntent);
		        error = sgfplib.OpenDevice(0);
	//			debugMessage("OpenDevice() ret: " + error + "\n");
		        SecuGen.FDxSDKPro.SGDeviceInfoParam deviceInfo = new SecuGen.FDxSDKPro.SGDeviceInfoParam();
		        error = sgfplib.GetDeviceInfo(deviceInfo);
	//			debugMessage("GetDeviceInfo() ret: " + error + "\n");
		    	mImageWidth = deviceInfo.imageWidth;
		    	mImageHeight= deviceInfo.imageHeight;
		        sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400);
				sgfplib.GetMaxTemplateSize(mMaxTemplateSize);
	//			debugMessage("TEMPLATE_FORMAT_SG400 SIZE: " + mMaxTemplateSize[0] + "\n");
		        mRegisterTemplate = new byte[mMaxTemplateSize[0]];
		        //mVerifyTemplate = new byte[mMaxTemplateSize[0]];
		
		        	sgfplib.WriteData((byte)5, (byte)1);
		     
		        	autoOn.start();
		        	DisableControls();
		        
		        //Thread thread = new Thread(this);
		        //thread.start();
	        }
        }
    }

    @Override
    public void onDestroy() {
    	Log.d(TAG, "onDestroy()");
    	sgfplib.CloseDevice();
    	mRegisterImage = null;
    	//mVerifyImage = null;
    	mRegisterTemplate = null;
    	//mVerifyTemplate = null;
    	sgfplib.Close();
        super.onDestroy();
    }
	
	public void EnableControls(){
		this.mCapture.setClickable(true);
		this.mCapture.setTextColor(getResources().getColor(android.R.color.white));	
		this.mButtonRegister.setClickable(true);
		this.mButtonRegister.setTextColor(getResources().getColor(android.R.color.white));		
		
	}

	public void DisableControls(){
		this.mCapture.setClickable(false);
		this.mCapture.setTextColor(getResources().getColor(android.R.color.black));		
		this.mButtonRegister.setClickable(false);
		this.mButtonRegister.setTextColor(getResources().getColor(android.R.color.black));		
	
	}
    //Converts image to grayscale (NEW)
    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {        
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();    
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int y=0; y< height; ++y) {
            for (int x=0; x< width; ++x){
            	int color = bmpOriginal.getPixel(x, y);
            	int r = (color >> 16) & 0xFF;
            	int g = (color >> 8) & 0xFF;
            	int b = color & 0xFF;           	
            	int gray = (r+g+b)/3;
            	color = Color.rgb(gray, gray, gray);
            	//color = Color.rgb(r/3, g/3, b/3);
            	bmpGrayscale.setPixel(x, y, color); 
            }
        }
        return bmpGrayscale;
    }
 
    //Converts image to binary (OLD)
    public Bitmap toBinary(Bitmap bmpOriginal)
    {        
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();    
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public void SGFingerPresentCallback (){
		autoOn.stop();
		fingerDetectedHandler.sendMessage(new Message());
    }
    
	public void CaptureFingerPrint(){
	

		//long dwTimeStart = 0, dwTimeEnd = 0, dwTimeElapsed = 0;

	    byte[] buffer = new byte[mImageWidth*mImageHeight];
	   // dwTimeStart = System.currentTimeMillis();          
	    long result = sgfplib.GetImage(buffer);
	   
	   // dwTimeEnd = System.currentTimeMillis();
	   // dwTimeElapsed = dwTimeEnd-dwTimeStart;
	   
		  Bitmap b = Bitmap.createBitmap(mImageWidth,mImageHeight, Bitmap.Config.ARGB_8888);
	    b.setHasAlpha(false);
	    int[] intbuffer = new int[mImageWidth*mImageHeight];
	    for (int i=0; i<intbuffer.length; ++i)
	    	intbuffer[i] = (int) buffer[i];
	    b.setPixels(intbuffer, 0, mImageWidth, 0, 0, mImageWidth, mImageHeight);
	    mImageViewRegister.setImageBitmap(this.toGrayscale(b));
	}  
	 public void onClick(View v) {
			
 			 if (v == this.mCapture) {
		        	//DEBUG r.d(TAG, "Pressed CAPTURE");
		            //Toast.makeText(getApplicationContext(),"Clicked",0).show();
		        	CaptureFingerPrint();
		        }
			 if (v == this.mButtonRegister) {
		        	//DEBUG Log.d(TAG, "Clicked REGISTER");
					//    debugMessage("Clicked REGISTER\n");
		            if (mRegisterImage != null)
		            	mRegisterImage = null;
		            mRegisterImage = new byte[mImageWidth*mImageHeight];

		     //   	this.mCheckBoxMatched.setChecked(false);
		            ByteBuffer byteBuf = ByteBuffer.allocate(mImageWidth*mImageHeight);
		                    
		            long result = sgfplib.GetImage(mRegisterImage);
		       //     DumpFile("register.raw", mRegisterImage);
		           
		       //     debugMessage("GetImage() ret:" + result + " [" + dwTimeElapsed + "ms]\n");
		            Bitmap b = Bitmap.createBitmap(mImageWidth,mImageHeight, Bitmap.Config.ARGB_8888);
		            byteBuf.put(mRegisterImage);
		            int[] intbuffer = new int[mImageWidth*mImageHeight];
		            for (int i=0; i<intbuffer.length; ++i)
		            	intbuffer[i] = (int) mRegisterImage[i];
		            b.setPixels(intbuffer, 0, mImageWidth, 0, 0, mImageWidth, mImageHeight); 
		            //DEBUG Log.d(TAG, "Show Register image");
		       
		            result = sgfplib.SetTemplateFormat(SecuGen.FDxSDKPro.SGFDxTemplateFormat.TEMPLATE_FORMAT_SG400);
		           
		            
		//            debugMessage("SetTemplateFormat(SG400) ret:" +  result + " [" + dwTimeElapsed + "ms]\n");
		            SGFingerInfo fpInfo = new SGFingerInfo();
		            for (int i=0; i< mRegisterTemplate.length; ++i)
		            	mRegisterTemplate[i] = 0;
		                    
		            result = sgfplib.CreateTemplate(fpInfo, mRegisterImage, mRegisterTemplate);
		           
		           
		    //        debugMessage("CreateTemplate() ret:" + result + " [" + dwTimeElapsed + "ms]\n");
		            this.mImageViewRegister.setImageBitmap(this.toGrayscale(b));  
		        	
		            
		           //use database  --modded
		            
		           
		            student.setRollNo(studentrollno.getEditableText().toString().trim());
		            student.setName(studentname.getEditableText().toString().trim());
		            student.setTemplate(mRegisterTemplate);
		            int no=dbhelper.findaRollno(studentrollno.getEditableText().toString().trim());	
		            if(studentrollno.getEditableText().toString().trim().length()<=0)
		            {
		            Toast.makeText(getApplicationContext(),"Please enter rollno",0).show();}
		            else    if(studentname.getEditableText().toString().trim().length()<=0)
		            {
		            Toast.makeText(getApplicationContext(),"Please enter name",0).show();}
		             
		            else    if(mRegisterTemplate == null)
		            {
		            Toast.makeText(getApplicationContext(),"Please register your fingerprint",0).show();}
		             
		            else	if(no>0){  Toast.makeText(getApplicationContext(),"Entry already exists !",0).show(); }
					else { dbhelper.insertCourseInfo(student);	 
					
		            Toast.makeText(getApplicationContext(),"Successfully Registered !",0).show();

					mTextViewResult.setText("Registered !!");
					
					} 

		            
		       
		            
		        }
	 }

@Override
public void run() {
	
	Log.d(TAG, "Enter run()");
  
    while (true) {
    
    }
}
}