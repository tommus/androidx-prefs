Prefs
===

This little tool generates wrappers for your SharedPreferences, so you can benefit from compile time
verification and code completion in your IDE.  You also get nice singletons for free.

Usage
---

### 1/ Add the dependencies to your project

```groovy
dependencies {
    /* ... */
    annotationProcessor 'org.jraf:prefs-compiler:1.2.2' // or kapt if you use Kotlin
    implementation 'org.jraf:prefs:1.2.2'
}
```


### 2/ Define your preferences

Use the `@Prefs` annotation on any plain old Java object.  All its (non static) fields will be considered a preference.

For instance:

```java
@Prefs
public class Main {
    /**
     * User login.
     */
    String login;

    /**
     * User password.
     */
    String password;

    @DefaultBoolean(false)
    Boolean isPremium;

    @Name("PREF_AGE")
    Integer age;
}
```

Currently, the accepted types are:
- Boolean
- Float
- Integer
- Long
- String
- Set\<String\>

Optionally, use `@DefaultXxx` and `@Name` annotations (the default default is `null`, and the default name is the name of your field).

You can pass a file name and mode (as per [Context.getSharedPreference()](http://developer.android.com/reference/android/content/Context.html#getSharedPreferences(java.lang.String, int))) like this:
```java
@Prefs(fileName = "settings", fileMode = Context.MODE_PRIVATE)
```

If you don't, `PreferenceManager.getDefaultSharedPreferences(Context)` will be used.


### 3/ Be a winner!

A class named `<YourClassName>Prefs` will be generated in the same package (at compile time).  Use it like this:

```java
        MainPrefs mainPrefs = MainPrefs.get(this);

        // Put a single value (apply() is automatically called)
        mainPrefs.putAge(42);

        // Put several values in one transaction
        mainPrefs.edit().putLogin("john").putPassword("p4Ssw0Rd").apply();

        // Check if a value is set
        if (mainPrefs.containsLogin()) doSomething();

        // Remove a value
        mainPrefs.removeAge();
        // Or (this has the same effect)
        mainPrefs.putAge(null);

        // Clear all values!
        mainPrefs.clear();
```
