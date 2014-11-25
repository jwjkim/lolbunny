package com.example.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.lolme.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class HomeActivity extends Activity {
	
	String region;
	TextView errorString;

	@Override
	protected void onCreate(Bundle savedInstanceState) { // "f20528c8-4c61-429a-8689-30fd14ebd417"
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_page);

		final EditText et_name = (EditText) findViewById(R.id.et_username);
		final Button summonerEnter = (Button) findViewById(R.id.enter_summoner);
		final Spinner regionSpinner = (Spinner) findViewById(R.id.region_spinner);
		errorString = (TextView) findViewById(R.id.account_not_found);
		
		regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	        	
	            Object item = parent.getItemAtPosition(pos);
	            Log.wtf("OKOKOK", "OKOKOK : " + item.toString());

	        }
	        public void onNothingSelected(AdapterView<?> parent) {
	        }
	    });
		
		et_name.setText("iKimono");

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		region = String.valueOf(regionSpinner.getSelectedItem()).toLowerCase();

		summonerEnter.setOnClickListener(new OnClickListener() {

			@SuppressLint("DefaultLocale")
			@Override
			public void onClick(View v) {
				if (isNetworkAvailable()) {
					if (null == et_name.getText()
							|| et_name.getText().toString().isEmpty()) {
						errorString.setVisibility(View.VISIBLE);
					} else {
						errorString.setVisibility(View.GONE);
						getUserID(et_name.getText().toString(), region);
					}
				}
			}
		});

		// Feedback button
		TextView feedback = (TextView) findViewById(R.id.feedback);
		feedback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent emailIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				String[] recipients = new String[] {
						"lolbunny.feedback@gmail.com", "", };
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
						recipients);
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						"Test");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						"Suggestion...");
				emailIntent.setType("text/plain");
				startActivity(Intent.createChooser(emailIntent, "Send mail..."));
				finish();
			}
		});
	}
	
	public void clickedItem() {
		
	}
	
	public void onClick(View v) {
		
	}

	public void getUserID(String name, String region) {
		try {
			URL url = new URL("https://na.api.pvp.net/api/lol/" + region
					+ "/v1.4/summoner/by-name/" + name
					+ "?" + getString(R.string.api_key)
					+ "?api_key=f20528c8-4c61-429a-8689-30fd14ebd417");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			Log.i("Connection Status", con.getResponseMessage());
			if (con.getResponseMessage().equalsIgnoreCase("not found")) {
				Log.wtf("OKOKOK", "OKOKOK not found");
				errorString.setVisibility(View.VISIBLE);
			} else {
				errorString.setVisibility(View.GONE);
				readStream(con.getInputStream(), con);
			}
		} catch (Exception e) {
			Log.wtf("OKOKOK", "OKOKOK connection error");
			e.printStackTrace();
		}
	}

	private void readStream(InputStream in, final HttpURLConnection con)
			throws JSONException {
		BufferedReader reader = null;
		StringBuilder responseStrBuilder = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = reader.readLine()) != null) {
				responseStrBuilder.append(line);
			}

			JSONObject myObject = new JSONObject(responseStrBuilder.toString());
			JSONObject jUser = new JSONObject(myObject.getString("ikimono"));
			Intent summonerActivity = new Intent(HomeActivity.this, SummonerActivity.class);
			summonerActivity.putExtra("name", jUser.getString("name"));
			summonerActivity.putExtra("id", jUser.getString("id"));
			summonerActivity.putExtra("profileIconId", jUser.getString("profileIconId"));
			summonerActivity.putExtra("summonerLevel", jUser.getString("summonerLevel"));
			summonerActivity.putExtra("region", region);
			
			HomeActivity.this.startActivity(summonerActivity);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
