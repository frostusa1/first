package com.frostusa.sendgeoserver;




import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;



public class GetAndForwardLocation extends Service {
	LocationManager locman = null;
	LocationListener locListen = null;
	private Criteria criteria;
	List<String> providers;
	String provider;
	

	public GetAndForwardLocation() {
		// TODO Auto-generated constructor stub
	
		
	}
	

	public void onCreate() {
	    super.onCreate();
	    Log.v("PCTIM","In On Create");
	    Toast.makeText(getApplicationContext(),"onCreate Service", Toast.LENGTH_SHORT).show();
	    Log.v("PCTIM","In On Create 2");
	    locListen = new LocationListener() {
	    	   public void onLocationChanged(Location location) {
	    		   processLocation(location);
	    	   }
	    	   public void onStatusChanged(String provider, int status, Bundle extras) {}
	    	   public void onProviderEnabled(String provider) {}
	    	   public void onProviderDisabled(String provider) {
	    		     // CheckEnableGPS();
	    	   }
			 	 
	       };
	       Log.v("PCTIM","In On Create 3");
	       
	       // Register the listener with the Location Manager to receive location updates
          criteria = new Criteria(); 
          locman = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
          Log.v("PCTIM","In On Create 4");
          criteria.setAccuracy(Criteria.ACCURACY_FINE);
          Log.v("PCTIM","In On Create 5");
          providers = locman.getProviders(criteria, true);
          Log.v("PCTIM","In On Create 6");
          for (String provider : providers) {
              
             Log.d("Providers",provider);
          }
          Log.v("PCTIM","In On Create 7");
          provider = locman.getBestProvider(criteria, true);
          Log.v("PCTIM","In On Create 8");
          locman.requestLocationUpdates(provider, 0, 0, locListen);
          Log.v("PCTIM","In On Create 9");
	      Location  locCurr = locman.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	      Log.v("PCTIM","In On Create 10");
	     
          if (null == locCurr) {
	    	   Log.v("XXX","Current = " + "Is Null");
	      } else {
	    	   Log.v("XXX","Current = " + locCurr.toString());
	      }
	   
	}

	

	@Override
	 public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	 public void processLocation(Location location){
		 
			Time tmpTime = new Time();
			tmpTime.setToNow();
			
			String lat = Double.toString(location.getLatitude());
			String lon = Double.toString(location.getLongitude());
			TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
			  telephonyManager.getDeviceId();
			sendString(telephonyManager.getDeviceId()+","+lon+","+lat);
			Toast.makeText(getBaseContext(), lon+","+lat , Toast.LENGTH_LONG).show();
//			send data to server
		 }
	
	 
	 private void sendString(String strIn){
		
		
		 String response = "";
		 String ulParam = "";
		 
		 String url = "http://192.168.1.13/handle_location.php?name=";
		 try{
		    ulParam = URLEncoder.encode(strIn, "utf-8").replace("+", "%20");
		 } catch (Exception e) {
             e.printStackTrace();
             Log.v("Tim","Error Here 1");
         }
		 url = url+ulParam;
		 Log.v("Tim","Url = "+url);
		 new doUpdate().execute(url);
//		 DefaultHttpClient client = new DefaultHttpClient();
//         HttpGet httpGet = new HttpGet(url);
//         try {
//        	 Log.v("Tim","Error Here A "+httpGet);
//             HttpResponse execute = client.execute(httpGet);
//             Log.v("Tim","Error Here B");
//             InputStream content = execute.getEntity().getContent();
//             Log.v("Tim","Error Here C");
//             BufferedReader buffer = new BufferedReader(
//                     new InputStreamReader(content));
//             Log.v("Tim","Error Here D");
//             String s = "";
//             Log.v("Tim","Error Here E");
//             while ((s = buffer.readLine()) != null) {
//                 response += s;
//             }
//
//         } catch (Exception e) {
//             e.printStackTrace();
//             Log.v("Tim","Error Here 2");
//         }
//		Log.v("Tim",response);

	 }
		
	 private class doUpdate extends AsyncTask<String, Integer, Long> {
		 protected Long doInBackground(String... url1){
			 String response = "";
			 DefaultHttpClient client = new DefaultHttpClient();
	         HttpGet httpGet = new HttpGet(url1[0]);
	         try {
	        	 Log.v("Tim","Error Here A "+httpGet);
	             HttpResponse execute = client.execute(httpGet);
	             Log.v("Tim","Error Here B");
	             InputStream content = execute.getEntity().getContent();
	             Log.v("Tim","Error Here C");
	             BufferedReader buffer = new BufferedReader(
	                     new InputStreamReader(content));
	             Log.v("Tim","Error Here D");
	             String s = "";
	             Log.v("Tim","Error Here E");
	             while ((s = buffer.readLine()) != null) {
	                 response += s;
	             }

	         } catch (Exception e) {
	             e.printStackTrace();
	             Log.v("Tim","Error Here 2");
	         }
			Log.v("Tim",response);
			return null;

			 
		 }
	 }
	 
		 
}