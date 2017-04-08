package com.udaan.footballpuzzler.logic;

import java.nio.charset.Charset;

import com.csvreader.CsvReader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter extends SQLiteOpenHelper{
	private static final int DATABASE_VER = 1;
	private static final String DATABASE_NAME = "Zoo.db";
	
	public static final String TAG = "DBAdapter";
	public static final String FLAG_TABLE = "Flags";
	public static final String FLAG_TABLE_PK = "Flag_PK";
	public static final String FLAG_COL_FLAG = "Flag";
	public static final String FLAG_COL_NAME = "Name";
	public static final String FLAG_COL_CATEGORY = "Category";
	public static final String FLAG_COL_NAME_JA = "Name_JA";
	public static final String FLAG_COL_NAME_RU = "Name_RU";
	
	public static final String ACHIEVEMENTS_TABLE = "Achievements";
	//public static final String ACHIEVEMENTS_PK = "Achievements_PK";
	public static final String ACHIEVEMENTS_ID = "Id";
	public static final String ACHIEVEMENTS_SCORE = "Score";
	public static final String ACHIEVEMENTS_TIME = "Time";
	
	private Context context;

	public DBAdapter(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VER);
		this.context = context;
		getReadableDatabase();
		close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String FLAG_TABLE_CREATE = "CREATE TABLE " + FLAG_TABLE + "(" + FLAG_COL_FLAG + " TEXT NOT NULL, " + 
				FLAG_COL_NAME + " TEXT NOT NULL, " + FLAG_COL_CATEGORY + " TEXT NOT NULL, " + 
				//FLAG_COL_NAME_JA + " TEXT NOT NULL, " +
				//FLAG_COL_NAME_RU + " TEXT NOT NULL, " +
				"CONSTRAINT " + FLAG_TABLE_PK + " PRIMARY KEY (" + FLAG_COL_FLAG + "," + FLAG_COL_CATEGORY + "));";
		
		String ACHIVEMENTS_TABLE_CREATE = "CREATE TABLE " + ACHIEVEMENTS_TABLE + "(" + ACHIEVEMENTS_ID + " INTEGER PRIMARY KEY, " +
				ACHIEVEMENTS_SCORE + " NUMBER NOT NULL, " + ACHIEVEMENTS_TIME + " NUMBER NOT NULL" + ");";
		
		Log.d(TAG, FLAG_TABLE_CREATE);
		Log.d(TAG, ACHIVEMENTS_TABLE_CREATE);
		
		db.execSQL(FLAG_TABLE_CREATE);
		db.execSQL(ACHIVEMENTS_TABLE_CREATE);
		
		loadFlagData(db);
		//loadAchievementsData(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (isTableExists(db, ACHIEVEMENTS_TABLE)) {
			db.execSQL("CREATE TABLE TEMP_" + ACHIEVEMENTS_TABLE + " AS SELECT * FROM " + ACHIEVEMENTS_TABLE);
		}
		
		db.execSQL("DROP TABLE IF EXISTS "+ FLAG_TABLE);
		db.execSQL("DROP TABLE IF EXISTS "+ ACHIEVEMENTS_TABLE);
		
		onCreate(db);
		
		String ACHIEVEMENTS_STATUS = "INSERT INTO " + ACHIEVEMENTS_TABLE + " SELECT * FROM TEMP_" + ACHIEVEMENTS_TABLE + ";";
		
		if (isTableExists(db, "TEMP_" + ACHIEVEMENTS_TABLE)) {
			db.execSQL("DELETE FROM " + ACHIEVEMENTS_TABLE);
			db.execSQL(ACHIEVEMENTS_STATUS);
			db.execSQL("DROP TABLE TEMP_" + ACHIEVEMENTS_TABLE);
		}
		
		Log.d(TAG, "Database upgrade complete");
	}
	
	public void execSQL(SQLiteDatabase db, String sql) {
		Log.d(TAG, "sql:" + sql);
		db.execSQL(sql);
	}
	
	public void insert(SQLiteDatabase db, String table, String nullColumnHack, ContentValues values) {
		Log.d(TAG, "Inserted rows: " + db.insert(table, nullColumnHack, values));
	}
	
	public String[] getOneRow(SQLiteDatabase db, String sql) {
		String[] whereArgs = null;
		
		Log.d(TAG, "sql:" + sql);
		Cursor result = db.rawQuery(sql, whereArgs);

		String data[] = new String[result.getColumnCount()];
		
		if (result.getCount() != 0) {
			result.moveToFirst();
			for(int c = 0; c < result.getColumnCount(); c++) {
				data[c] = result.getString(c);
			}
		}
		else {
			data = null;
		}
		result.close();
		return data;
	}
	
	public int getRowCount(SQLiteDatabase db, String table, String where) {
		String sql = "SELECT * FROM " + table + where;
		Cursor result = db.rawQuery(sql, null);
		return result.getCount();
	}
	
	public String[][] getRows(SQLiteDatabase db, String sql) {
		String[] whereArgs = null;
		
		Log.d(TAG, "sql:" + sql);
		Cursor result = db.rawQuery(sql, whereArgs);
		
		String data[][] = new String[result.getCount()][result.getColumnCount()];
		Log.d(TAG, "Returning " + result.getCount() + " rows");
		if (result.getCount() != 0) {
			result.moveToFirst();
			for(int r = 0; r < result.getCount(); r++) {
				for(int c = 0; c < result.getColumnCount(); c++) {
					data[r][c] = result.getString(c);
				}
				result.moveToNext();
			}
		}
		else {
			data = null;
		}
		result.close();
		return data;
	}

	/**
	 * Checks if the table exists
	 */
	private boolean isTableExists(SQLiteDatabase db, String tableName)
	{
	    if (tableName == null || db == null || !db.isOpen())
	    {
	        return false;
	    }
	    Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", tableName});
	    if (!cursor.moveToFirst())
	    {
	        return false;
	    }
	    int count = cursor.getInt(0);
	    if (count > 0) Log.d(TAG, "The table exists: " + tableName);
	    cursor.close();
	    return count > 0;
	}
	
	private boolean loadFlagData(SQLiteDatabase db) {
		boolean result = false;
		String values[] = null;
		ContentValues row = new ContentValues();
		Log.i(TAG,"Loading Flag table data...");

		try {
			CsvReader file = new CsvReader(context.getAssets().open("data.csv"), Charset.defaultCharset());
			file.readHeaders();
			
			while(file.readRecord()) {
				values = file.getValues();
				
				row.put(FLAG_COL_FLAG, values[0]);
				row.put(FLAG_COL_NAME, values[1]);
				row.put(FLAG_COL_CATEGORY, values[2]);
				//row.put(FLAG_COL_NAME_JA, values[3]);
				//row.put(FLAG_COL_NAME_RU, values[4]);
				
				Log.d(TAG, "Loading: " + values[1]);

				if (db.insert(FLAG_TABLE, null, row) != -1) {
					result = true;
				}
				else {
					result = false;
					break;
				}
			}
			file.close();
		} catch (Exception e) {
			Log.e(TAG,e.toString());
			result = false;
		}
		
		Log.i(TAG,"Loading Flag table complete");
		return result;
	}
}
