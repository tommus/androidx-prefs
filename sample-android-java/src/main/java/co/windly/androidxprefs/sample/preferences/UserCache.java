package co.windly.androidxprefs.sample.preferences;

import co.windly.androidxprefs.annotations.DefaultBoolean;
import co.windly.androidxprefs.annotations.DefaultLong;
import co.windly.androidxprefs.annotations.DefaultString;
import co.windly.androidxprefs.annotations.Prefs;

import static android.content.Context.MODE_PRIVATE;

@Prefs(value = "UserCachePreferences", fileMode = MODE_PRIVATE)
public class UserCache {

  //region Basic

  @DefaultLong(0L)
  Long id;

  @DefaultString("")
  String firstName;

  @DefaultString("")
  String lastName;

  @DefaultString("")
  String password;

  @DefaultBoolean(true)
  Boolean active;

  //endregion
}
