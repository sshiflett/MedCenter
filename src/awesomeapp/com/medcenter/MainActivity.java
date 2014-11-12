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
		
		final EditText username=(EditText) findViewById(R.id.et_input1);
		final EditText password=(EditText) findViewById(R.id.et_input2);
	
		Button next = (Button) findViewById(R.id.b_ps_confirm);
		next.setOnClickListener(new View.OnClickListener() {
	
				@Override
				public void onClick(View v) {
				Intent shiftToDoctorHome = new Intent (v.getContext(), Nurse_Doctor.class);
				Intent shiftToPharm = new Intent (v.getContext(), PharmacistMainFinal.class);
				Intent shiftToNurseHome = new Intent (v.getContext(), NurseHome.class);
				Intent shiftToPatientHome = new Intent (v.getContext(), PatientMain.class);
				
				if(username.getText().toString().equals("doctor1") && password.getText().toString().equals("password")){
					startActivityForResult(shiftToDoctorHome, 0);
				}	
				else if(username.getText().toString().equals("pharm1") && password.getText().toString().equals("password")){
					startActivityForResult(shiftToPharm, 0);
				}
				else if(username.getText().toString().equals("nurse1") && password.getText().toString().equals("password")){
					startActivityForResult(shiftToNurseHome, 0);
				}
				else if (username.getText().toString().equals("patient1") && password.getText().toString().equals("password")){
					startActivityForResult(shiftToPatientHome, 0);
				}
				}});
			
		
			
		}
	 
	
	
}