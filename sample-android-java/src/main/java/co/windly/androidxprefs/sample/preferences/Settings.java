package co.windly.androidxprefs.sample.preferences;

import android.content.Context;
import co.windly.androidxprefs.annotation.DefaultInt;
import co.windly.androidxprefs.annotation.DefaultStringSet;
import co.windly.androidxprefs.annotation.Prefs;
import java.util.Set;

@Prefs(fileName = "settings", fileMode = Context.MODE_PRIVATE)
public class Settings {

  @DefaultInt(0xFFBB00DD)
  Integer preferredColor;

  /**
   * The week days that the user prefers.
   */
  @DefaultStringSet({ "Friday", "Saturday" })
  Set<String> weekDays;
}
