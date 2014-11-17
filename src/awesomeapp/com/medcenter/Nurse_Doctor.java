package awesomeapp.com.medcenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Nurse_Doctor extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_nurse__doctor);
		
		
		//Button Initialization
		Button search = (Button) findViewById(R.id.b_ps_confirm);
		Button logout = (Button) findViewById(R.id.b_dh_logout);
		//TextView initialization (similar to doctor_home)
		final TextView patient_name3 = (TextView) findViewById(R.id.patient_name3);
		
		//Search event handler
		search.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent shiftToPatientInfo = new Intent (v.getContext(), PatientInfoFinal.class);
			startActivityForResult(shiftToPatientInfo, 0);
		}
			
		});//end search event handler
		
		//log out event handler
		logout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Intent shiftToPatientInfo = new Intent (v.getContext(), MainActivity.class);
			startActivityForResult(shiftToPatientInfo, 0);
		}
			
		});//end logout event handler
	}
}
