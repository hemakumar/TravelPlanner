/**
 * @author Shilpi Agarwal
 * @author Hema Kumar
 */
/** This file is part of TravelPlanner.

TravelPlanner is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

TravelPlanner is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Travelplanner. If not, see <http://www.gnu.org/licenses/>.
*/

package com.travelplanner;

import java.util.Calendar;

import com.travelplanner.db.DBAdapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NewPlan extends Activity implements OnClickListener{
	
	private TextView mDateDisplay;
    private Button mPickDate;
    private int mYear;
    private int mMonth;
    private int mDay;

    static final int DATE_DIALOG_ID = 0;
    private static final String[] MONTHS = {"JAN","FEB","MAR","APR","MAY","JUN","JULY","AUG","SEP","OCT","NOV","DEC"};
	
	private DBAdapter db = null;
	
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.newplan);
	    mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
        mPickDate = (Button) findViewById(R.id.pickDate);

        // add a click listener to the button
        mPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        // get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // display the current date (this method is below)
        updateDisplay();
        
        Spinner travelType = (Spinner) findViewById(R.id.travelTypeSpinner);
        ArrayAdapter travelTypeAdapter = ArrayAdapter.createFromResource(
                this, R.array.TravelTypes, android.R.layout.simple_spinner_item);
        travelTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        travelType.setAdapter(travelTypeAdapter);
        
        Spinner travelMode = (Spinner) findViewById(R.id.travelModeSpinner);
        ArrayAdapter travelModeAdapter = ArrayAdapter.createFromResource(
                this, R.array.TravelMode, android.R.layout.simple_spinner_item);
        travelModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        travelMode.setAdapter(travelModeAdapter);
        
        Spinner travelTime = (Spinner) findViewById(R.id.travelTimeSpinner);
        ArrayAdapter travelTimeAdapter = ArrayAdapter.createFromResource(
                this, R.array.TravelTime, android.R.layout.simple_spinner_item);
        travelTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        travelTime.setAdapter(travelTimeAdapter);
	    final Button addButton = (Button) findViewById(R.id.addTravelPlanButton);
	    addButton.setOnClickListener(this);
	    final Button cancleButton = (Button) findViewById(R.id.cancelTravelPlanButton);
	    cancleButton.setOnClickListener(this);
	    db = new DBAdapter(this);
	}
	
	private void updateDisplay() {
        mDateDisplay.setText(
            new StringBuilder()
                    // Month is 0 based so add 1
                    .append(MONTHS[mMonth]).append(" ")
                    .append(mDay).append(", ")
                    .append(mYear).append(" "));
    }
	
	// the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, 
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };
            
            @Override
            protected Dialog onCreateDialog(int id) {
                switch (id) {
                case DATE_DIALOG_ID:
                    return new DatePickerDialog(this,
                                mDateSetListener,
                                mYear, mMonth, mDay);
                }
                return null;
            }
	    
	    @Override
		public void onClick(View v) {
			
				switch (v.getId()) {
				case R.id.addTravelPlanButton:
			    String name = (((EditText) findViewById(R.id.travelNameEntry)).getText().toString()).toUpperCase();
			    String mode = ((String)((Spinner) findViewById(R.id.travelModeSpinner)).getSelectedItem()).toUpperCase();
			    String type = ((String)((Spinner) findViewById(R.id.travelTypeSpinner)).getSelectedItem()).toUpperCase();
			    String date = ((TextView) findViewById(R.id.dateDisplay)).getText().toString();
			    String time = (String)((Spinner) findViewById(R.id.travelTimeSpinner)).getSelectedItem();
			    String from = (((EditText) findViewById(R.id.travelFromEntry)).getText().toString()).toUpperCase();
			    String to = (((EditText) findViewById(R.id.travelToEntry)).getText().toString()).toUpperCase();
			    long id;
			    
			    id = db.insertTravelPlan(name, mode, type, date, time, from, to);
			    			    
				Intent addNewPlanCheckIntent = new Intent(this, AddNewPlanCheck.class);
				Bundle bundle = new Bundle();
				bundle.putLong("id", id);
								  
				addNewPlanCheckIntent.putExtras(bundle);
				startActivity(addNewPlanCheckIntent);
				break;
				case R.id.cancelTravelPlanButton:
					Intent existingIntent = new Intent(this, ExistingPlan.class);
					startActivity(existingIntent);
					break;
				
				// More buttons go here (if any) ...
				}
				}
}
