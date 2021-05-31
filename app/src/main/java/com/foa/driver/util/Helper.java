package com.foa.driver.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.foa.driver.dialog.LoadingDialog;
import com.foa.driver.network.response.LoginData;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

public final class Helper
{
	private static ContextWrapper instance;
	private static SharedPreferences pref;

	public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	public static SimpleDateFormat dateSQLiteFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat dateTimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public static DecimalFormat decimalformat = new DecimalFormat("#.###");
	public static void initialize(Context base)
	{
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
		decimalformat.setDecimalFormatSymbols(otherSymbols);
		
		instance = new ContextWrapper(base);
		pref = instance.getSharedPreferences("com.foa.driver", Context.MODE_PRIVATE);
	}
	public static void write(String key, String value)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, value);
		editor.apply();
	}

	public static void remove(String key)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.remove(key);
		editor.apply();
	}
	
	public static String read(String key)
	{
		return com.foa.driver.util.Helper.read(key, null);
	}
	
	public static String read(String key, String defValue)
	{
		return pref.getString(key, defValue);
	}
	
	public static void clearAll()
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.clear();
		editor.apply();
	}

	static public void setLoginData(LoginData loginData){
		com.foa.driver.util.Helper.write(Constants.DRIVER_ID,loginData.getDriver().getId());
		com.foa.driver.util.Helper.write(Constants.BEARER_ACCESS_TOKEN,loginData.getBearerAccessToken());
	}

	static public void clearLoginData(){
		com.foa.driver.util.Helper.remove(Constants.DRIVER_ID);
		com.foa.driver.util.Helper.remove(Constants.BEARER_ACCESS_TOKEN);
	}

	public static void showFailNotification(Context context, LoadingDialog loading, LinearLayout wrapper, String message){
		loading.dismiss();
		YoYo.with(Techniques.Shake).duration(700).playOn(wrapper);
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	
}

