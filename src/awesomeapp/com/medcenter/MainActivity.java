package awesomeapp.com.medcenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		final EditText username=(EditText) findViewById(R.id.editText1);
		final EditText password=(EditText) findViewById(R.id.editText2);
	
		Button next = (Button) findViewById(R.id.button1);
		next.setOnClickListener(new View.OnClickListener() {
	
				@Override
				public void onClick(View v) {
				Intent shiftToHome = new Intent (v.getContext(), Nurse_Doctor.class);
				Intent shiftToPharm = new Intent (v.getContext(), PharmacistMainFinal.class);
				
				if(username.getText().toString().equals("doctor1") && password.getText().toString().equals("password")){
				startActivityForResult(shiftToHome, 0);
				}	
				else if(username.getText().toString().equals("pharm1") && password.getText().toString().equals("password")){
				startActivityForResult(shiftToPharm, 0);
				}
				}});
			
		Button next1 = (Button) findViewById(R.id.button2);
		next1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent shiftToPatient = new Intent (v.getContext(), PatientMain.class);
			startActivityForResult(shiftToPatient, 0);
				
			}
		});
			
		}
	 
	
	
}