package co.windly.androidxprefs.compiler;

import java.util.Arrays;
import java.util.Optional;
import javax.lang.model.type.TypeMirror;
import org.apache.commons.lang3.StringUtils;

public enum PrefType {

  BOOLEAN(Boolean.class.getName(), Boolean.class.getSimpleName(), "Boolean", "false"),
  FLOAT(Float.class.getName(), Float.class.getSimpleName(), "Float", "0f"),
  INTEGER(Integer.class.getName(), Integer.class.getSimpleName(), "Int", "0"),
  LONG(Long.class.getName(), Long.class.getSimpleName(), "Long", "0L"),
  STRING(String.class.getName(), String.class.getSimpleName(), "String", "null"),
  STRING_SET("java.util.Set<java.lang.String>", "Set<String>", "StringSet", "null"),;

  private final String fullName;
  private final String simpleName;
  private final String methodName;
  private final String defaultValue;

  PrefType(String fullName, String simpleName, String methodName, String defaultValue) {
    this.fullName = fullName;
    this.simpleName = simpleName;
    this.methodName = methodName;
    this.defaultValue = defaultValue;
  }

  public String getFullName() {
    return fullName;
  }

  public String getSimpleName() {
    return simpleName;
  }

  public String getMethodName() {
    return methodName;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public boolean isCompatible(TypeMirror type) {
    return getFullName().equals(type.toString());
  }

  public static PrefType from(TypeMirror fieldType) {
    final String fullName = fieldType.toString();
    final Optional<PrefType> type = Arrays
      .stream(values())
      .filter(it -> it.getFullName().equals(fullName))
      .findFirst();
    if (type.isPresent()) {
      return type.get();
    } else {
      throw new IllegalArgumentException("Unsupported type: " + fullName);
    }
  }

  public static boolean isAllowedType(TypeMirror fieldType) {
    final String fullName = fieldType.toString();
    return Arrays
      .stream(values())
      .anyMatch(it -> it.getFullName().equals(fullName));
  }

  public static String getAllowedTypes() {
    return Arrays
      .stream(values())
      .map(PrefType::getFullName)
      .reduce(StringUtils::join)
      .orElse(null);
  }
}
