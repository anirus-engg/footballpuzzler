package com.udaan.footballpuzzler.ui;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.udaan.footballpuzzler.logic.Achievements;
import com.udaan.footballpuzzler.logic.Board;

public class MainActivity extends Activity {
	public static final boolean PAID = false;
	public static final int MAX_PIECES = 16;
	private static final String MY_AD_UNIT_ID = "a152b50089cfac1";
	private static final String TAG = "MainActivity";
	//private static int gameTime = 60000;
	
	private ImageView flag0, flag1, flag2, flag3, flag4, flag5, flag6, flag7;
	private ImageView flag8, flag9, flag10, flag11, flag12, flag13, flag14, flag15;
	private TextView option0, option1, option2, option3;
	private TextView scoreField, scoreText;
	private ImageView help;// settings, barcode;
	private LinearLayout adLL;
	private AdView adView;
	private AdRequest adRequest;
	private Board board;
	private String[] option;
	private int tempScore = 3;
	private boolean settingsClicked;
	private boolean showResumeAlert = false;
	private String flagRoot;
	private long playTime = 0;
	private long startTime = 0;
	private SharedPreferences prefs;
	//private int continuousScore;
	//private int continuousScoreAchieved;
	//private CountDownTimerPausable countDown;
	//private TextView timer;
	//private String longCategory = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		scoreField = (TextView)findViewById(R.id.score);
		//timer = (TextView)findViewById(R.id.timer);
		
		flag0 = (ImageView)findViewById(R.id.flag0);
		flag1 = (ImageView)findViewById(R.id.flag1);
		flag2 = (ImageView)findViewById(R.id.flag2);
		flag3 = (ImageView)findViewById(R.id.flag3);
		flag4 = (ImageView)findViewById(R.id.flag4);
		flag5 = (ImageView)findViewById(R.id.flag5);
		flag6 = (ImageView)findViewById(R.id.flag6);
		flag7 = (ImageView)findViewById(R.id.flag7);
		flag8 = (ImageView)findViewById(R.id.flag8);
		flag9 = (ImageView)findViewById(R.id.flag9);
		flag10 = (ImageView)findViewById(R.id.flag10);
		flag11 = (ImageView)findViewById(R.id.flag11);
		flag12 = (ImageView)findViewById(R.id.flag12);
		flag13 = (ImageView)findViewById(R.id.flag13);
		flag14 = (ImageView)findViewById(R.id.flag14);
		flag15 = (ImageView)findViewById(R.id.flag15);
		
		option0 = (TextView)findViewById(R.id.option0);
		option1 = (TextView)findViewById(R.id.option1);
		option2 = (TextView)findViewById(R.id.option2);
		option3 = (TextView)findViewById(R.id.option3);
		
		scoreText = (TextView)findViewById(R.id.score_text);
		//settings = (ImageView)findViewById(R.id.settings);
		help = (ImageView)findViewById(R.id.help);
		
		if (!PAID) {
			adView = new AdView(this, AdSize.SMART_BANNER, MY_AD_UNIT_ID);
			adLL = (LinearLayout) findViewById(R.id.ad_ll);
		    adLL.addView(adView);
		    adRequest = new AdRequest();
		    adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
		    adView.loadAd(adRequest);
		}
	    
		/*
	    settings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				settingsClicked = true;
				showResumeAlert = false;
				Intent intent = new Intent(v.getContext(), SettingsActivity.class);
				startActivity(intent);
			}
		});
		*/
	    
		help.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), FavoritesActivity.class);
				startActivity(intent);
			}
		});
	    
		alertNewGame();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");
		
		prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	String category = prefs.getString("category", "football");
    	
    	scoreText.setText(R.string.score_text);
    			
    	Board.category = category;
    	
    	if (showResumeAlert) alertResume();
    	if (settingsClicked) {
    		alertNewGame();
    		settingsClicked = false;
    	}
	}
	
	protected void onResume() {
		super.onResume();
		startTime = new Date().getTime();
		Log.d(TAG, "onResume");
	}
	
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "onStop");
	}
	
	protected void onDestroy() {
		super.onDestroy();
	}
	
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
		playTime += new Date().getTime() - startTime;
	}
	
	public void newGame() {
		if (board != null) board.close();
		board = new Board(this);
		
		startTime = new Date().getTime();
		Log.d(TAG, "startime:" + new Date().getTime());
		
		loadBoard();
	}
	
	public void alertNewGame() {
		Log.d(TAG, "alertNewGame");
		//timer.setText(sTimer);
		//continuousScore = 0;
		//continuousScoreAchieved = 0;
		AlertDialog.Builder alertConfirm = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		alertConfirm.setTitle(R.string.new_game);
		alertConfirm.setView(inflater.inflate(R.layout.alert_newgame, null));
		alertConfirm.setCancelable(false);
		alertConfirm.setNeutralButton(getResources().getString(R.string.start), new DialogInterface.OnClickListener() { 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				showResumeAlert = true;
				newGame();
			}
		});
		alertConfirm.show();
	}
	
	public void alertGameOver() {
		Log.d(TAG, "alertGameOver");
		showResumeAlert = false;
		board.close();
		
		Achievements achievement = new Achievements(this);
		achievement.addRecord(board.getScore(), "" + (playTime +(new Date().getTime() - startTime)));
		achievement.close();
		
		Log.d(TAG, "playtime: " + formatTime(playTime +(new Date().getTime() - startTime)));
		TextView gameOverText = new TextView(this);
		gameOverText.setText("You completed the puzzle in " + formatTime(playTime +(new Date().getTime() - startTime)) + " minutes with a score of " + board.getScore());
		
		AlertDialog.Builder alertConfirm = new AlertDialog.Builder(this);
		alertConfirm.setTitle(R.string.times_up);
		alertConfirm.setView(gameOverText);
		//alertConfirm.setView(inflater.inflate(R.layout.alert_gameover, null));
		alertConfirm.setCancelable(false);
		alertConfirm.setNeutralButton(getResources().getString(R.string.close), new DialogInterface.OnClickListener() { 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				alertNewGame();
				Intent intent = new Intent(getApplication(), AchievementsActivity.class);
				startActivity(intent);
			}
		});
		alertConfirm.show();
	}
	
	public void alertResume() {
		showResumeAlert = false;
		AlertDialog.Builder alertConfirm = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		alertConfirm.setTitle(R.string.resume);
		alertConfirm.setView(inflater.inflate(R.layout.alert_resume, null));
		alertConfirm.setCancelable(false);
		alertConfirm.setNeutralButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() { 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				showResumeAlert = true;
				//countDown.start();
			}
		});
		alertConfirm.show();
		Log.d(TAG, "alertResume");
	}
	
	private void loadBoard() {
		flagRoot = board.getFlagRoot();
		option = board.getOptions();
		tempScore = 3;
		scoreField.setText(board.getScore());
		
		Log.d(TAG, "flagroot: " + flagRoot);
		
		//Randomize the image pieces
		Integer[] randInt = new Integer[MAX_PIECES];
		for (int i = 0; i < MAX_PIECES; i++) randInt[i] = i;
		Collections.shuffle(Arrays.asList(randInt));
		
		flag0.setImageResource(getResources().getIdentifier(flagRoot + randInt[0], "drawable", this.getPackageName()));
		flag1.setImageResource(getResources().getIdentifier(flagRoot + randInt[1], "drawable", this.getPackageName()));
		flag2.setImageResource(getResources().getIdentifier(flagRoot + randInt[2], "drawable", this.getPackageName()));
		flag3.setImageResource(getResources().getIdentifier(flagRoot + randInt[3], "drawable", this.getPackageName()));
		flag4.setImageResource(getResources().getIdentifier(flagRoot + randInt[4], "drawable", this.getPackageName()));
		flag5.setImageResource(getResources().getIdentifier(flagRoot + randInt[5], "drawable", this.getPackageName()));
		flag6.setImageResource(getResources().getIdentifier(flagRoot + randInt[6], "drawable", this.getPackageName()));
		flag7.setImageResource(getResources().getIdentifier(flagRoot + randInt[7], "drawable", this.getPackageName()));
		flag8.setImageResource(getResources().getIdentifier(flagRoot + randInt[8], "drawable", this.getPackageName()));
		flag9.setImageResource(getResources().getIdentifier(flagRoot + randInt[9], "drawable", this.getPackageName()));
		flag10.setImageResource(getResources().getIdentifier(flagRoot + randInt[10], "drawable", this.getPackageName()));
		flag11.setImageResource(getResources().getIdentifier(flagRoot + randInt[11], "drawable", this.getPackageName()));
		flag12.setImageResource(getResources().getIdentifier(flagRoot + randInt[12], "drawable", this.getPackageName()));
		flag13.setImageResource(getResources().getIdentifier(flagRoot + randInt[13], "drawable", this.getPackageName()));
		flag14.setImageResource(getResources().getIdentifier(flagRoot + randInt[14], "drawable", this.getPackageName()));
		flag15.setImageResource(getResources().getIdentifier(flagRoot + randInt[15], "drawable", this.getPackageName()));
		
		
		//Randomize the image pieces
		Integer[] randOptions = new Integer[Board.MAX_CHOICE];
		for (int i = 0; i < Board.MAX_CHOICE; i++) randOptions[i] = i;
		Collections.shuffle(Arrays.asList(randOptions));
		
		option0.setText(option[randOptions[0]]);
		option0.setTag(option[randOptions[0]]);
		option0.setOnClickListener(new MyClickListener());
		option1.setText(option[randOptions[1]]);
		option1.setTag(option[randOptions[1]]);
		option1.setOnClickListener(new MyClickListener());
		option2.setText(option[randOptions[2]]);
		option2.setTag(option[randOptions[2]]);
		option2.setOnClickListener(new MyClickListener()); 
		option3.setText(option[randOptions[3]]);
		option3.setTag(option[randOptions[3]]);
		option3.setOnClickListener(new MyClickListener());
		
	}
	
	private void isMatched() {
		//score++;
		//continuousScore++;
		//if(board.removeFlag())
		board.setScore(tempScore);
		scoreField.setText(board.getScore());
		Log.d(TAG, "endtime: " + new Date().getTime());
		if(board.nextImage()) {
			loadBoard();
		}
		else {
			alertGameOver();
		}
	}
	
	private CharSequence formatTime(long millisUntilFinished) {
		int timer100mili = (int) (millisUntilFinished / 100);
		int timerSec = timer100mili / 10;
		int timerMin = timerSec / 60;
		
		return timerMin + ":" + String.format("%02d", timerSec % 60) + "." + (timer100mili % 10);
	}
	
	/*
	private void showAlert(String msg) {
		AlertDialog.Builder alertConfirm = new AlertDialog.Builder(this);
		//alertConfirm.setTitle(R.string.times_up);
		alertConfirm.setMessage(msg);
		alertConfirm.setCancelable(false);
		alertConfirm.setNeutralButton("OK", new DialogInterface.OnClickListener() { 
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		alertConfirm.show();
	}
	*/
	
	private class MyClickListener implements OnClickListener {
			
		@Override
		public void onClick(View v) {
			Log.d(TAG, ""+v.getTag());
			Log.d(TAG, "tempScore:" + tempScore);
			if(v.getTag().equals(board.getCurrentImage())) {
				isMatched();
			}
			else {
				MediaPlayer player = MediaPlayer.create(v.getContext(), R.raw.wrong);
				player.start();
				if (tempScore > 0) tempScore--;
			}
		}
	}
}