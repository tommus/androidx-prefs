package co.windly.androidxprefs;

import android.content.SharedPreferences;
import java.util.Set;

public class EditorWrapper implements SharedPreferences.Editor {

  //region Editor

  private final SharedPreferences.Editor editor;

  //endregion

  //region Constructor

  public EditorWrapper(SharedPreferences.Editor wrapped) {
    editor = wrapped;
  }

  //endregion

  //region Interface

  public EditorWrapper putString(String key, String value) {
    editor.putString(key, value);
    return this;
  }

  public EditorWrapper putStringSet(String key, Set<String> values) {
    editor.putStringSet(key, values);
    return this;
  }

  public EditorWrapper putInt(String key, int value) {
    editor.putInt(key, value);
    return this;
  }

  public EditorWrapper putLong(String key, long value) {
    editor.putLong(key, value);
    return this;
  }

  public EditorWrapper putFloat(String key, float value) {
    editor.putFloat(key, value);
    return this;
  }

  public EditorWrapper putBoolean(String key, boolean value) {
    editor.putBoolean(key, value);
    return this;
  }

  public EditorWrapper remove(String key) {
    editor.remove(key);
    return this;
  }

  public EditorWrapper clear() {
    editor.clear();
    return this;
  }

  public boolean commit() {
    return editor.commit();
  }

  public void apply() {
    editor.apply();
  }

  //endregion
}
