package com.example.activities;

import com.example.lolme.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/*
 * id
 * summoner name
 * level 1~30 -- normal win-loss
 * if ranked tier,level with icon, division -- ranked win-loss -- ranked solo, ranked 3s (solo/team), ranked 5s (best)
 * last 10 games (drop down - onClick)
 * Total dmg/turrets/etc etc (per game)
 * Win % depending on purple or blue?
 */

public class SummonerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) { // "f20528c8-4c61-429a-8689-30fd14ebd417" 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.summoner_page); // http://ddragon.leagueoflegends.com/cdn/4.4.3/img/profileicon/28.png  /api/lol/static-data/{region}/v1.2/champion/{id}
		
		Intent intent = getIntent();

		TextView userName = (TextView) findViewById(R.id.user_name);
		TextView userId = (TextView) findViewById(R.id.user_id);
		TextView userLevel = (TextView) findViewById(R.id.user_summoner_level);
		TextView userRegion = (TextView) findViewById(R.id.user_region);
		
		userName.setText(intent.getStringExtra("name"));
		userId.setText(intent.getStringExtra("id"));
		userLevel.setText(intent.getStringExtra("summonerLevel"));
		userRegion.setText(intent.getStringExtra("region"));
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
