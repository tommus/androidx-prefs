package co.windly.androidxprefs.sample.application;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import co.windly.androidxprefs.sample.R;
import co.windly.androidxprefs.sample.preferences.UserCachePrefs;

public class MainActivity extends Activity {

  private static final String TAG = MainActivity.class.getName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Get access to shared preferences wrapper.
    final UserCachePrefs cache = UserCachePrefs.get(this);

    // Put a single value (apply() is automatically called).
    cache
      .putId(1L);

    // Put several values in one transaction.
    cache
      .edit()
      .putFirstName("John")
      .putLastName("Snow")
      .putPassword("WinterIsComing")
      .putActive(true)
      .commit();

    // Check if a value is set.
    if (cache.containsFirstName()) {
      Log.d(TAG, "First name is set.");
    }

    // Access preferences one by one.
    Log.d(TAG, "id -> " + cache.getId());
    Log.d(TAG, "first name -> " + cache.getFirstName());
    Log.d(TAG, "last name -> " + cache.getLastName());
    Log.d(TAG, "password -> " + cache.getPassword());
    Log.d(TAG, "active -> " + cache.isActive());

    // Access all preferences.
    Log.d(TAG, "cache -> " + cache.getAll());

    // Remove a value.
    cache.removeFirstName();

    // Clear all preferences.
    cache.clear();
  }
}
