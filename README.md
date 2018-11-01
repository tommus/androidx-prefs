# AndroidX Preferences

This library uses annotation processing to ensure the compile time verification for user-defined shared preferences.

## Usage

1. Add Jitpack repository address to the project `build.gradle`:

```groovy
repositories {
    /* (...) */
    maven { url "https://jitpack.io" }
}
```

2. Add dependencies.

To the *Java-based* project:

```groovy
dependencies {
    /* (...) */
    implementation "com.github.tommus.androidx-prefs:androidx-preferences:1.0.0"
    annotationProcessor "com.github.tommus.androidx-prefs:androidx-preferences-compiler:1.0.0"
}
```

To the *Kotlin-based* project:

```groovy
dependencies {
    /* (...) */
    implementation "com.github.tommus.androidx-prefs:androidx-preferences:1.0.0"
    kapt "com.github.tommus.androidx-prefs:androidx-preferences-compiler:1.0.0"
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
      doSomething();
    };

    // Remove a value.
    cache.removeFirstName();

    // Clear all preferences.
    cache.clear();
```
