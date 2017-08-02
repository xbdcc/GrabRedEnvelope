//package com.example.qianghongbao.jpush;
//
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.text.format.DateFormat;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.TimePicker;
//import android.widget.Toast;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import cn.jpush.android.api.InstrumentedActivity;
//import cn.jpush.android.api.JPushInterface;
// import  com.example.qianghongbao.R;
//
//public class SettingActivity extends InstrumentedActivity implements OnClickListener {
//	TimePicker startTime;
//	TimePicker endTime;
//	CheckBox mMonday ;
//	CheckBox mTuesday ;
//	CheckBox mWednesday;
//	CheckBox mThursday;
//	CheckBox mFriday ;
//	CheckBox mSaturday;
//	CheckBox mSunday ;
//	Button mSetTime;
//	SharedPreferences mSettings;
//	Editor mEditor;
//
//	@Override
//	public void onCreate(Bundle icicle) {
//		super.onCreate(icicle);
//		setContentView(R.layout.set_push_time);
//		init();
//		initListener();
//	}
//
//   @Override
//    public void onStart() {
//	    super.onStart();
//	    initData();
//   }
//
//	private void init(){
//		startTime = (TimePicker) findViewById(R.id.start_time);
//		endTime = (TimePicker) findViewById(R.id.end_time);
//		startTime.setIs24HourView(DateFormat.is24HourFormat(this));
//		endTime.setIs24HourView(DateFormat.is24HourFormat(this));
//		mSetTime = (Button)findViewById(R.id.bu_setTime);
//		mMonday = (CheckBox)findViewById(R.id.cb_monday);
//		mTuesday = (CheckBox)findViewById(R.id.cb_tuesday);
//		mWednesday = (CheckBox)findViewById(R.id.cb_wednesday);
//	    mThursday = (CheckBox)findViewById(R.id.cb_thursday);
//		mFriday = (CheckBox)findViewById(R.id.cb_friday);
//		mSaturday = (CheckBox)findViewById(R.id.cb_saturday);
//		mSunday = (CheckBox)findViewById(R.id.cb_sunday);
//	}
//
//    private void initListener(){
//	   mSetTime.setOnClickListener(this);
//    }
//
//    private void initData(){
//	  mSettings = getSharedPreferences(ExampleUtil.PREFS_NAME, MODE_PRIVATE);
//	  String days = mSettings.getString(ExampleUtil.PREFS_DAYS, "");
//		if (!TextUtils.isEmpty(days)) {
//			initAllWeek(false);
//			String[] sArray = days.split(",");
//			for (String day : sArray) {
//				setWeek(day);
//			}
//		} else {
//			initAllWeek(true);
//		}
//
//	  int startTimeStr = mSettings.getInt(ExampleUtil.PREFS_START_TIME, 0);
//	  startTime.setCurrentHour(Integer.valueOf(startTimeStr));
//	  int endTimeStr = mSettings.getInt(ExampleUtil.PREFS_END_TIME, 23);
//	  endTime.setCurrentHour(Integer.valueOf(endTimeStr));
//   }
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.bu_setTime:
//			v.requestFocus();
//			v.requestFocusFromTouch();
//			setPushTime();
//			break;
//		}
//	}
//
//	/**
//	 *设置允许接收通知时间
//	 */
//	private void setPushTime(){
//		int startime = startTime.getCurrentHour();
//		int endtime = endTime.getCurrentHour();
//		if (startime > endtime) {
//			Toast.makeText(SettingActivity.this, "开始时间不能大于结束时间", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		StringBuffer daysSB = new StringBuffer();
//		Set<Integer> days = new HashSet<Integer>();
//		if (mSunday.isChecked()) {
//			days.add(0);
//			daysSB.append("0,");
//		}
//		if (mMonday.isChecked()) {
//			days.add(1);
//			daysSB.append("1,");
//		}
//		if (mTuesday.isChecked()) {
//			days.add(2);
//			daysSB.append("2,");
//		}
//		if (mWednesday.isChecked()) {
//			days.add(3);
//			daysSB.append("3,");
//		}
//		if (mThursday.isChecked()) {
//			days.add(4);
//			daysSB.append("4,");
//		}
//		if (mFriday.isChecked()) {
//			days.add(5);
//			daysSB.append("5,");
//		}
//		if (mSaturday.isChecked()) {
//			days.add(6);
//			daysSB.append("6,");
//		}
//
//
//		//调用JPush api设置Push时间
//		JPushInterface.setPushTime(getApplicationContext(), days, startime, endtime);
//
//		mEditor = mSettings.edit();
//		mEditor.putString(ExampleUtil.PREFS_DAYS, daysSB.toString());
//		mEditor.putInt(ExampleUtil.PREFS_START_TIME, startime);
//		mEditor.putInt(ExampleUtil.PREFS_END_TIME, endtime);
//		mEditor.commit();
//		Toast.makeText(SettingActivity.this, R.string.setting_su, Toast.LENGTH_SHORT).show();
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK){
//			finish();
//		}
//		return super.onKeyDown(keyCode, event);
//	}
//
//	private void setWeek(String day){
//		   int dayId = Integer.valueOf(day);
//		   switch (dayId) {
//			case 0:
//				mSunday.setChecked(true);
//				break;
//			case 1:
//				mMonday.setChecked(true);
//				break;
//			case 2:
//				mTuesday.setChecked(true);
//				break;
//			case 3:
//				mWednesday.setChecked(true);
//				break;
//			case 4:
//				mThursday.setChecked(true);
//				break;
//			case 5:
//				mFriday.setChecked(true);
//				break;
//			case 6:
//				mSaturday.setChecked(true);
//				break;
//			}
//	   }
//
//	private void initAllWeek(boolean isChecked) {
//		mSunday.setChecked(isChecked);
//		mMonday.setChecked(isChecked);
//		mTuesday.setChecked(isChecked);
//		mWednesday.setChecked(isChecked);
//		mThursday.setChecked(isChecked);
//		mFriday.setChecked(isChecked);
//		mSaturday.setChecked(isChecked);
//	}
//}