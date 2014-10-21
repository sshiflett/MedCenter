package awesomeapp.com.medcenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button next = (Button) findViewById(R.id.button1);
		next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent shiftToHome = new Intent (v.getContext(), Nurse_Doctor.class);
			startActivityForResult(shiftToHome, 0);
		}
			
		});
		
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