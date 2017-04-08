package com.udaan.footballpuzzler.logic;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

public class Achievements {
	private static final String TAG = "Achievements";
	
	private DBAdapter dbAdapter;
	private String data[][];
	
	public Achievements(Context context) {
		dbAdapter = new DBAdapter(context);
		String sql = "SELECT " + DBAdapter.ACHIEVEMENTS_SCORE + ", " + DBAdapter.ACHIEVEMENTS_TIME + " FROM " + DBAdapter.ACHIEVEMENTS_TABLE + " ORDER BY " + DBAdapter.ACHIEVEMENTS_SCORE + " DESC," + DBAdapter.ACHIEVEMENTS_TIME + " ASC";
		data = dbAdapter.getRows(dbAdapter.getReadableDatabase(), sql);
	}

	public int getCount() {
		return data.length;
	}
	
	public String getCell (int row, int column) {
		Log.d(TAG, "row column:" + row + " " + column);
		if (data != null && row < data.length) {
			Log.d(TAG, "data: " + data[row][column]);
			if (column == 1) return "" + formatTime(Long.parseLong(data[row][column]));
			return data[row][column];
		}
		else if (column == 0) {
			Log.d(TAG, "returning none");
			return "Empty";
		}
		else {
			Log.d(TAG, "returning blank");
			return "";
		}
	}
	
	public void addRecord (String score, String time) {
		ContentValues values = new ContentValues();
		values.put(DBAdapter.ACHIEVEMENTS_SCORE, score);
		values.put(DBAdapter.ACHIEVEMENTS_TIME, time);
		dbAdapter.insert(dbAdapter.getWritableDatabase(), DBAdapter.ACHIEVEMENTS_TABLE, null, values);
	}

	private CharSequence formatTime(long millisUntilFinished) {
		int timer100mili = (int) (millisUntilFinished / 100);
		int timerSec = timer100mili / 10;
		int timerMin = timerSec / 60;
		
		return timerMin + ":" + String.format("%02d", timerSec % 60) + "." + (timer100mili % 10);
	}
	
	public boolean isAchieved(int row) {
		return data[row][4].equals("0") ? false : true;
	}

	public String getMedal(int row) {
		return data[row][3];
	}

	public CharSequence getAchievementDesc(int row) {
		return data[row][2];
	}
	
	public CharSequence getAchievementName(int row) {
		return data[row][1];
	}

	public void close() {
		dbAdapter.close();
		data = null;
	}
}
