package com.foa.driver.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.foa.driver.dialog.LoadingDialog;
import com.foa.driver.network.response.LoginData;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Date;

public final class Helper
{

	public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	public static SimpleDateFormat dateSQLiteFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat dateTimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	public static SimpleDateFormat dateTimeformat2 = new SimpleDateFormat("HH:mm yyyy-MM-dd");
	public static DecimalFormat decimalformat = new DecimalFormat("#.###");

	public static String getTimeFormUTC(String dateInString){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm.sss'Z'", Locale.ENGLISH);
		formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));

		Date date = null;
		try {
			date = formatter.parse(dateInString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateTimeformat2.format(date);
	}



	public static String formatMoney(long monney){
		Locale localeVN = new Locale("vi", "VN");
		NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
		return currencyVN.format(monney);
	}

	public static String formatDistance(float distance){
		DecimalFormat df = new DecimalFormat("#.00");
		 return df.format(distance) + " Km";
	}

	public static void showFailNotification(Context context, LoadingDialog loading, LinearLayout wrapper, String message){
		loading.dismiss();
		YoYo.with(Techniques.Shake).duration(700).playOn(wrapper);
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public static void setTimeout(Runnable runnable, int delay){
		new Thread(() -> {
			try {
				Thread.sleep(delay);
				runnable.run();
			}
			catch (Exception e){
				System.err.println(e);
			}
		}).start();
	}
	
}

