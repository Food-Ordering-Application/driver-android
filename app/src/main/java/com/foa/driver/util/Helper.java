package com.foa.driver.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.airbnb.lottie.LottieAnimationView;
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
	public static SimpleDateFormat dateTimeformat2 = new SimpleDateFormat("dd-MM-yyy HH:mm");
	public static SimpleDateFormat onlyDayformat = new SimpleDateFormat("dd/MM/yyyy");
	public static SimpleDateFormat onlyHourformat = new SimpleDateFormat("HH:mm");
	public static SimpleDateFormat fullTimeformat = new SimpleDateFormat("HH:mm - dd/MM/yyyy");
	public static DecimalFormat decimalformat = new DecimalFormat("#.###");

	public static SplitDay getTimeFormUTC(String dateInString)  {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		formatter.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));

		Date date = null;
		try {
			date = formatter.parse(dateInString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new SplitDay(onlyDayformat.format(date),onlyHourformat.format(date),fullTimeformat.format(date));
	}



	public static String formatMoney(long monney){
		Locale localeVN = new Locale("vi", "VN");
		NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
		return currencyVN.format(monney);
	}

	public static String formatMoneyCompact(long monney){
		monney/=1000;
		Locale localeVN = new Locale("vi", "VN");
		NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
		String moneyStr =  currencyVN.format(monney);
		moneyStr = moneyStr.replace(currencyVN.getCurrency().getSymbol(),"K");
		return moneyStr.trim();
	}

	public static String formatDistance(float distance){
		DecimalFormat df = new DecimalFormat("#.00");
		 return df.format(distance/1000) + " Km";
	}

	public static void showFailNotification(Context context, LottieAnimationView loading, LinearLayout wrapper, String message){
		loading.setVisibility(View.GONE);
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

	public static class SplitDay {
		String day;
		String hour;
		String full;

		public SplitDay(String day, String hour,String full) {
			this.day = day;
			this.hour = hour;
			this.full = full;
		}

		public String getDay() {
			return day;
		}

		public void setDay(String day) {
			this.day = day;
		}

		public String getHour() {
			return hour;
		}

		public void setHour(String hour) {
			this.hour = hour;
		}

		public String getFull() {
			return full;
		}

		public void setFull(String full) {
			this.full = full;
		}
	}
	
}

