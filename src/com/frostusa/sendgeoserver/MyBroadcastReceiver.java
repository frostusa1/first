package com.frostusa.sendgeoserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.v("Tim","in on receive");
        Intent startServiceIntent = new Intent(context, GetAndForwardLocation.class);
        context.startService(startServiceIntent);
	}

}
