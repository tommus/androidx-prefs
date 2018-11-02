package co.windly.androidxprefs;

import android.content.SharedPreferences;
import java.util.Map;
import java.util.Set;

public class SharedPreferencesWrapper implements SharedPreferences {

  //region Preferences

  private final SharedPreferences preferences;

  //endregion

  //region Constructor

  public SharedPreferencesWrapper(SharedPreferences wrapped) {
    preferences = wrapped;
  }

  //endregion

  //region Interface

  public Map<String, ?> getAll() {
    return preferences.getAll();
  }

  public String getString(String key, String defValue) {
    return preferences.getString(key, defValue);
  }

  public Set<String> getStringSet(String key, Set<String> defValues) {
    return preferences.getStringSet(key, defValues);
  }

  public int getInt(String key, int defValue) {
    return preferences.getInt(key, defValue);
  }

  public long getLong(String key, long defValue) {
    return preferences.getLong(key, defValue);
  }

  public float getFloat(String key, float defValue) {
    return preferences.getFloat(key, defValue);
  }

  public boolean getBoolean(String key, boolean defValue) {
    return preferences.getBoolean(key, defValue);
  }

  public boolean contains(String key) {
    return preferences.contains(key);
  }

  public SharedPreferences.Editor edit() {
    return preferences.edit();
  }

  public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
    preferences.registerOnSharedPreferenceChangeListener(listener);
  }

  public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
    preferences.unregisterOnSharedPreferenceChangeListener(listener);
  }

  public void clear() {
    edit().clear().apply();
  }

  //endregion
}
