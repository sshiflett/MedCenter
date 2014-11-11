package awesomeapp.com.medcenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class Nurse_Doctor extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_nurse__doctor);
		
		Button next = (Button) findViewById(R.id.button1);
		next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent shiftToPatientInfo = new Intent (v.getContext(), PatientInfoFinal.class);
			startActivityForResult(shiftToPatientInfo, 0);
		}
			
		});
		
		Button next1 = (Button) findViewById(R.id.button3);
		next1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent shiftToPatientInfo = new Intent (v.getContext(), MainActivity.class);
			startActivityForResult(shiftToPatientInfo, 0);
		}
			
		});
	}
}
