package com.udaan.footballpuzzler.logic;

import java.util.Arrays;
import java.util.Collections;

import android.content.Context;
import android.util.Log;

/**
 * This class is the main class for the logic behind this quiz game.
 *
 */
public class Board {
	public static final int MAX_CHOICE = 4;
	private static final String TAG = "Board";
	
	public static String category = "football";
	private String[][] data;
	private DBAdapter dbAdapter;
	private Integer[] randInt;
	//private List<Integer> list;
	private int current = 0;
	private int score;
	
	/**
	 * Reads the flag data from the assets/data.csv and stores in the data[][] for quick retrieval
	 */
	public Board(Context context) {
		dbAdapter = new DBAdapter(context);
		//flag_col_name = context.getResources().getString(R.string.flag_table_col_name);
		Log.i(TAG,"Loading data...");
		
		String sql = "SELECT " + DBAdapter.FLAG_COL_FLAG + "," + DBAdapter.FLAG_COL_NAME + 
				" FROM " + DBAdapter.FLAG_TABLE + 
				" WHERE " + DBAdapter.FLAG_COL_CATEGORY + "='" + category + "';";
		
		data = dbAdapter.getRows(dbAdapter.getReadableDatabase(), sql);
		
		Log.i(TAG,"Loading data complete. Total rows: " + data.length);
		
		randInt = new Integer[data.length];
		for (int i = 0; i < data.length; i++) randInt[i] = i;
		//list = new LinkedList<Integer>(Arrays.asList(randInt));
		Collections.shuffle(Arrays.asList(randInt));
		
		score = 0;
	}
	
	public void close() {
		dbAdapter.close();
	}

	public String getFlagRoot() {
		return data[randInt[current]][0];
	}

	public String[] getOptions() {
		String[] options = new String[MAX_CHOICE];
		options[0] = data[randInt[current]][1];
		options[1] = data[randInt[(current + 1) % data.length]][1];
		options[2] = data[randInt[(current + 2) % data.length]][1];
		options[3] = data[randInt[(current + 3) % data.length]][1];
		
		return options;
	}
	
	public String getName(String flag) {
		String sql = "SELECT " + DBAdapter.FLAG_COL_NAME + " FROM " + DBAdapter.FLAG_TABLE +
				" WHERE " + DBAdapter.FLAG_COL_FLAG + "='" + flag + "';";
		String[] name = dbAdapter.getOneRow(dbAdapter.getReadableDatabase(), sql);
		return name[0];
	}
	
	/*
	public boolean removeFlag() {
		Log.d(TAG, "Removing flag index: " + data[randInt[current]][0] + " randInt:" + randInt[current]);
		Log.d(TAG, "Removed:" + list.remove(current));
		nextImage();
		//Collections.shuffle(list);
		return true;
	}
	*/
	
	public boolean nextImage() {
		if (current < data.length - 1) {
			current++;
			Log.d(TAG, "current image is " + getFlagRoot());
			return true;
		}
		else {
			return false;
		}
	}

	public String getCurrentImage() {
		return data[randInt[current]][1];
	}

	public String getScore() {
		Log.d(TAG, "current score: " + score);
		return "" + score;
	}
	
	public void setScore(int score) {
		Log.d(TAG, "score: " + score);
		this.score += score;
	}
}
