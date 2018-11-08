# AndroidX Preferences

This library uses annotation processing to ensure the compile time verification for user-defined shared preferences.

## Usage

1. Add dependencies.

Add dependencies to the *Java-based* project:

```groovy
dependencies {
    implementation "co.windly:androidx-preferences:1.0.5"
    annotationProcessor "co.windly:androidx-preferences-compiler:1.0.5"
}
```

Add dependencies to the *Kotlin-based* project:

```groovy
dependencies {
    implementation "co.windly:androidx-preferences:1.0.5"
    kapt "co.windly:androidx-preferences-compiler:1.0.5"
}
```

3. Define shared preferences.

Use the `@Prefs` annotation on any POJO. All (non static) fields will be considered a preference.

For example:

```java
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
```

Accepted shared preference field types are:

* Boolean
* Float
* Integer
* Long
* String
* Set\<String\>

4. Use generated wrapper class.

A class named `<YourClassName>Prefs` will be generated in the same package (at compile time).  Use it like this:

```java
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
      .apply();

    // Check if a value is set.
    if (cache.containsFirstName()) {
      Log.d(TAG, "First name is set.");
    };

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
```
