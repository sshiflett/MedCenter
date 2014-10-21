package awesomeapp.com.medcenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class PatientInfoFinal extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_info_final);
		
		Button next = (Button) findViewById(R.id.button1);
		next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent shiftToPatientInfo = new Intent (v.getContext(), PrescriptionSubmission.class);
			startActivityForResult(shiftToPatientInfo, 0);
		}
			
		});
		
		Button next1 = (Button) findViewById(R.id.button3);
		next1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent shiftToPatientInfo = new Intent (v.getContext(), PatientVitalsFinal.class);
			startActivityForResult(shiftToPatientInfo, 0);
			}
			
		});
		
		Button next2 = (Button) findViewById(R.id.button2);
		next2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent shiftToPatientInfo = new Intent (v.getContext(), Nurse_Doctor.class);
			startActivityForResult(shiftToPatientInfo, 0);
			}
			
		});
}
}
