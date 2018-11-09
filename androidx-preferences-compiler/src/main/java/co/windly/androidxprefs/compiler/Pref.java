package co.windly.androidxprefs.compiler;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Pref {

  private final String fieldName;
  private final String prefName;
  private final PrefType type;
  private final String defaultValue;
  private final String comment;

  Pref(String fieldName, String prefName, PrefType type, String defaultValue, String comment) {
    this.fieldName = fieldName;
    this.prefName = prefName;
    this.type = type;
    this.defaultValue = defaultValue;
    this.comment = comment;
  }

  public String getFieldName() {
    return fieldName;
  }

  public String getFieldNameUpperCase() {
    return fieldName.replaceAll("([A-Z]+)", "\\_$1").toUpperCase();
  }

  public String getPrefName() {
    return prefName;
  }

  public PrefType getType() {
    return type;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public String getComment() {
    return comment;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("fieldName", fieldName)
      .append("prefName", prefName)
      .append("type", type)
      .append("defaultValue", defaultValue)
      .append("comment", comment)
      .toString();
  }
}
