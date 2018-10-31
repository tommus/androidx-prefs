package co.windly.androidxprefs.compiler;

public class Pref {

  private final String mFieldName;
  private final String mPrefName;
  private final PrefType mType;
  private final String mDefaultValue;
  private final String mComment;

  public Pref(String fieldName, String prefName, PrefType type, String defaultValue, String comment) {
    mFieldName = fieldName;
    mPrefName = prefName;
    mType = type;
    mDefaultValue = defaultValue;
    mComment = comment;
  }

  public String getFieldName() {
    return mFieldName;
  }

  public String getFieldNameUpperCase() {
    return mFieldName.replaceAll("([A-Z]+)", "\\_$1").toUpperCase();
  }

  public String getPrefName() {
    return mPrefName;
  }

  public PrefType getType() {
    return mType;
  }

  public String getDefaultValue() {
    return mDefaultValue;
  }

  public String getComment() {
    return mComment;
  }

  @Override
  public String toString() {
    return "Pref{" +
      "mFieldName='" + mFieldName + '\'' +
      ", mPrefName='" + mPrefName + '\'' +
      ", mType=" + mType +
      ", mDefaultValue='" + mDefaultValue + '\'' +
      ", mComment='" + mComment + '\'' +
      '}';
  }
}
