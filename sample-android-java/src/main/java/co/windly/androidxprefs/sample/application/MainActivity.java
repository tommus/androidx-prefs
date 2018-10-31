package co.windly.androidxprefs.sample.application;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import co.windly.androidxprefs.sample.R;
import co.windly.androidxprefs.sample.preferences.MainPrefs;
import co.windly.androidxprefs.sample.preferences.SettingsPrefs;

public class MainActivity extends Activity {

  private static final String TAG = MainActivity.class.getName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    MainPrefs mainPrefs = MainPrefs.get(this);
    mainPrefs.edit().putLogin("john").putPassword("p4Ssw0Rd").commit();
    mainPrefs.putAge(null);
    Log.d(TAG, "login=" + mainPrefs.getLogin());
    Log.d(TAG, "age=" + mainPrefs.getAge());
    Log.d(TAG, "premium=" + mainPrefs.getPremium());
    Log.d(TAG, "premium=" + mainPrefs.isPremium());
    Log.d(TAG, "mainPrefs=" + mainPrefs.getAll());

    SettingsPrefs settingsPrefs = SettingsPrefs.get(this);
    settingsPrefs.putPreferredColor(0xFFFFFF);
    Log.d(TAG, "preferredColor=" + settingsPrefs.getPreferredColor());
    Log.d(TAG, "settingsPrefs=" + settingsPrefs.getAll());
  }
}
