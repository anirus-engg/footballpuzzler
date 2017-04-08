package com.udaan.footballpuzzler.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.udaan.footballpuzzler.logic.Achievements;

public class AchievementsActivity extends Activity{
	private TextView r1c0, r1c1, r2c0, r2c1, r3c0, r3c1, r4c0, r4c1, r5c0, r5c1;
	private TextView r6c0, r6c1, r7c0, r7c1, r8c0, r8c1, r9c0, r9c1, r0c0, r0c1;
	private Achievements achieve;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_achievements);
		
		achieve = new Achievements(this);
		r0c0 = (TextView)findViewById(R.id.r0c0);
		r0c1 = (TextView)findViewById(R.id.r0c1);
		r1c0 = (TextView)findViewById(R.id.r1c0);
		r1c1 = (TextView)findViewById(R.id.r1c1);
		r2c0 = (TextView)findViewById(R.id.r2c0);
		r2c1 = (TextView)findViewById(R.id.r2c1);
		r3c0 = (TextView)findViewById(R.id.r3c0);
		r3c1 = (TextView)findViewById(R.id.r3c1);
		r4c0 = (TextView)findViewById(R.id.r4c0);
		r4c1 = (TextView)findViewById(R.id.r4c1);
		r5c0 = (TextView)findViewById(R.id.r5c0);
		r5c1 = (TextView)findViewById(R.id.r5c1);
		r6c0 = (TextView)findViewById(R.id.r6c0);
		r6c1 = (TextView)findViewById(R.id.r6c1);
		r7c0 = (TextView)findViewById(R.id.r7c0);
		r7c1 = (TextView)findViewById(R.id.r7c1);
		r8c0 = (TextView)findViewById(R.id.r8c0);
		r8c1 = (TextView)findViewById(R.id.r8c1);
		r9c0 = (TextView)findViewById(R.id.r9c0);
		r9c1 = (TextView)findViewById(R.id.r9c1);
		
		r0c0.setText(achieve.getCell(0, 0));
		r0c1.setText(achieve.getCell(0, 1));
		r1c0.setText(achieve.getCell(1, 0));
		r1c1.setText(achieve.getCell(1, 1));
		r2c0.setText(achieve.getCell(2, 0));
		r2c1.setText(achieve.getCell(2, 1));
		r3c0.setText(achieve.getCell(3, 0));
		r3c1.setText(achieve.getCell(3, 1));
		r4c0.setText(achieve.getCell(4, 0));
		r4c1.setText(achieve.getCell(4, 1));
		r5c0.setText(achieve.getCell(5, 0));
		r5c1.setText(achieve.getCell(5, 1));
		r6c0.setText(achieve.getCell(6, 0));
		r6c1.setText(achieve.getCell(6, 1));
		r7c0.setText(achieve.getCell(7, 0));
		r7c1.setText(achieve.getCell(7, 1));
		r8c0.setText(achieve.getCell(8, 0));
		r8c1.setText(achieve.getCell(8, 1));
		r9c0.setText(achieve.getCell(9, 0));
		r9c1.setText(achieve.getCell(9, 1));
	}
	
	public void onDestroy() {
		super.onDestroy();
		achieve.close();
	}
}
