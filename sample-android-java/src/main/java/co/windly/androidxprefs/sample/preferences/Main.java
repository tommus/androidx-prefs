package co.windly.androidxprefs.sample.preferences;

import co.windly.androidxprefs.annotation.DefaultBoolean;
import co.windly.androidxprefs.annotation.Name;
import co.windly.androidxprefs.annotation.Prefs;

@Prefs
public class Main {

  private static final String PREF_LOGIN = "PREF_LOGIN";
  private static final String PREF_AGE = "PREF_AGE";

  /**
   * User login.
   */
  @Name(PREF_LOGIN)
  String login;

  /**
   * User password.
   */
  String password;

  @DefaultBoolean(false)
  Boolean premium;

  @Name(PREF_AGE)
  Integer age;
}
