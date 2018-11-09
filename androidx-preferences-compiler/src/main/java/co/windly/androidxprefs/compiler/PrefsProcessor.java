package co.windly.androidxprefs.compiler;

import co.windly.androidxprefs.annotations.DefaultBoolean;
import co.windly.androidxprefs.annotations.DefaultFloat;
import co.windly.androidxprefs.annotations.DefaultInt;
import co.windly.androidxprefs.annotations.DefaultLong;
import co.windly.androidxprefs.annotations.DefaultString;
import co.windly.androidxprefs.annotations.DefaultStringSet;
import co.windly.androidxprefs.annotations.Name;
import co.windly.androidxprefs.annotations.Prefs;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes({
  "co.windly.androidxprefs.annotations.DefaultBoolean",
  "co.windly.androidxprefs.annotations.DefaultFloat",
  "co.windly.androidxprefs.annotations.DefaultInt",
  "co.windly.androidxprefs.annotations.DefaultLong",
  "co.windly.androidxprefs.annotations.DefaultString",
  "co.windly.androidxprefs.annotations.DefaultStringSet",
  "co.windly.androidxprefs.annotations.Mode",
  "co.windly.androidxprefs.annotations.Name",
  "co.windly.androidxprefs.annotations.Prefs"
})
public class PrefsProcessor extends AbstractProcessor {

  private static final String SUFFIX_PREF_WRAPPER = "Prefs";
  private static final String SUFFIX_EDITOR_WRAPPER = "EditorWrapper";
  private static final String SUFFIX_CONSTANTS = "Constants";

  private Configuration freemarkerConfiguration;

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  private Configuration getFreemarkerConfiguration() {
    if (freemarkerConfiguration == null) {
      freemarkerConfiguration = new Configuration(new Version(2, 3, 28));
      freemarkerConfiguration.setClassForTemplateLoading(getClass(), "");
    }
    return freemarkerConfiguration;
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    for (TypeElement annotation : annotations) {
      if (!annotation.getQualifiedName().contentEquals("co.windly.androidxprefs.annotations.Prefs")) {
        // Should never happen - but does with kapt :)
        continue;
      }
      for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
        final TypeElement classElement = (TypeElement) element;
        final PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();

        final String classComment = processingEnv.getElementUtils().getDocComment(classElement);

        final List<Pref> prefList = new ArrayList<Pref>();
        // Iterate over the fields of the class
        for (VariableElement variableElement : ElementFilter.fieldsIn(classElement.getEnclosedElements())) {
          if (variableElement.getModifiers().contains(Modifier.STATIC)) {
            // Ignore constants
            continue;
          }

          final TypeMirror fieldType = variableElement.asType();
          final boolean isAllowedType = PrefType.isAllowedType(fieldType);
          if (!isAllowedType) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
              fieldType + " is not allowed here, only these types are allowed: " + PrefType.getAllowedTypes(),
              variableElement);
            // Problem detected: halt
            return true;
          }

          final String fieldName = variableElement.getSimpleName().toString();
          final Name fieldNameAnnot = variableElement.getAnnotation(Name.class);
          final String prefName = getPrefName(fieldName, fieldNameAnnot);

          final String prefDefaultValue = getDefaultValue(variableElement, fieldType);
          if (prefDefaultValue == null) {
            // Problem detected: halt
            return true;
          }

          final String fieldComment = processingEnv.getElementUtils().getDocComment(variableElement);
          final Pref pref = new Pref(fieldName, prefName, PrefType.from(fieldType), prefDefaultValue, fieldComment);
          prefList.add(pref);
        }

        final Map<String, Object> args = new HashMap<String, Object>();

        // File name (optional - also use 'value' for this)
        final Prefs prefsAnnot = classElement.getAnnotation(Prefs.class);
        String fileName = prefsAnnot.value();
        if (fileName.isEmpty()) {
          fileName = prefsAnnot.fileName();
        }
        if (!fileName.isEmpty()) args.put("fileName", fileName);

        // File mode (must only appear if fileName is defined)
        final int fileMode = prefsAnnot.fileMode();
        if (fileMode != -1) {
          if (fileName.isEmpty()) {
            // File mode set, but not file name (which makes no sense)
            processingEnv.getMessager()
              .printMessage(Diagnostic.Kind.ERROR, "fileMode must only be set if fileName (or value) is also set",
                classElement);
            // Problem detected: halt
            return true;
          }
          args.put("fileMode", fileMode);
        }

        // Disable @Nullable generation
        args.put("disableNullable", prefsAnnot.disableNullable());

        JavaFileObject javaFileObject;
        try {
          args.put("package", packageElement.getQualifiedName());
          args.put("comment", classComment);
          args.put("prefWrapperClassName", classElement.getSimpleName() + SUFFIX_PREF_WRAPPER);
          args.put("editorWrapperClassName", classElement.getSimpleName() + SUFFIX_EDITOR_WRAPPER);
          args.put("constantsClassName", classElement.getSimpleName() + SUFFIX_CONSTANTS);
          args.put("prefList", prefList);

          // SharedPreferencesWrapper
          javaFileObject =
            processingEnv.getFiler().createSourceFile(classElement.getQualifiedName() + SUFFIX_PREF_WRAPPER);
          Template template = getFreemarkerConfiguration().getTemplate("shared_preferences_wrapper.ftl");
          try (Writer writer = javaFileObject.openWriter()) {
            template.process(args, writer);
          }

          // EditorWrapper
          javaFileObject =
            processingEnv.getFiler().createSourceFile(classElement.getQualifiedName() + SUFFIX_EDITOR_WRAPPER);
          template = getFreemarkerConfiguration().getTemplate("editor_wrapper.ftl");
          try (Writer writer = javaFileObject.openWriter()) {
            template.process(args, writer);
          }

          // Constants
          javaFileObject =
            processingEnv.getFiler().createSourceFile(classElement.getQualifiedName() + SUFFIX_CONSTANTS);
          template = getFreemarkerConfiguration().getTemplate("constants.ftl");
          try (Writer writer = javaFileObject.openWriter()) {
            template.process(args, writer);
          }
        } catch (IOException | TemplateException e) {
          processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
            "En error occurred while generating Prefs code " + e.getClass() + e.getMessage(), element);
          e.printStackTrace();
          // Problem detected: halt
          return true;
        }
      }
    }
    return true;
  }

  private String getDefaultValue(VariableElement variableElement, TypeMirror fieldType) {
    Class<? extends Annotation> clazz = DefaultBoolean.class;
    final DefaultBoolean defaultBoolean = (DefaultBoolean) variableElement.getAnnotation(clazz);
    if (defaultBoolean != null) {
      if (!ensureCompatibleAnnotation(PrefType.BOOLEAN, fieldType, clazz, variableElement)) return null;
      return String.valueOf(defaultBoolean.value());
    }

    clazz = DefaultFloat.class;
    final DefaultFloat defaultFloat = (DefaultFloat) variableElement.getAnnotation(clazz);
    if (defaultFloat != null) {
      if (!ensureCompatibleAnnotation(PrefType.FLOAT, fieldType, clazz, variableElement)) return null;
      return String.valueOf(defaultFloat.value()) + "f";
    }

    clazz = DefaultInt.class;
    final DefaultInt defaultInt = (DefaultInt) variableElement.getAnnotation(clazz);
    if (defaultInt != null) {
      if (!ensureCompatibleAnnotation(PrefType.INTEGER, fieldType, clazz, variableElement)) return null;
      return String.valueOf(defaultInt.value());
    }

    clazz = DefaultLong.class;
    final DefaultLong defaultLong = (DefaultLong) variableElement.getAnnotation(clazz);
    if (defaultLong != null) {
      if (!ensureCompatibleAnnotation(PrefType.LONG, fieldType, clazz, variableElement)) return null;
      return String.valueOf(defaultLong.value()) + "L";
    }

    clazz = DefaultString.class;
    final DefaultString defaultString = (DefaultString) variableElement.getAnnotation(clazz);
    if (defaultString != null) {
      if (!ensureCompatibleAnnotation(PrefType.STRING, fieldType, clazz, variableElement)) return null;
      return "\"" + unescapeString(defaultString.value()) + "\"";
    }

    clazz = DefaultStringSet.class;
    final DefaultStringSet defaultStringSet = (DefaultStringSet) variableElement.getAnnotation(clazz);
    if (defaultStringSet != null) {
      if (!ensureCompatibleAnnotation(PrefType.STRING_SET, fieldType, clazz, variableElement)) return null;
      final StringBuilder res = new StringBuilder("new HashSet<String>(Arrays.asList(");
      int i = 0;
      for (String s : defaultStringSet.value()) {
        if (i > 0) res.append(", ");
        res.append("\"");
        res.append(unescapeString(s));
        res.append("\"");
        i++;
      }
      res.append("))");
      return res.toString();
    }

    // Default default value :)
    return "null";
  }

  private boolean ensureCompatibleAnnotation(PrefType prefType, TypeMirror fieldType, Class<?> annotClass,
    VariableElement variableElement) {
    if (!prefType.isCompatible(fieldType)) {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
        annotClass.getSimpleName() + " annotation is only allowed on " + prefType.getSimpleName() + " fields",
        variableElement);
      return false;
    }
    return true;
  }

  private static String getPrefName(String fieldName, Name fieldNameAnnot) {
    return (fieldNameAnnot != null)
      ? fieldNameAnnot.value()
      : fieldName;
  }

  private String unescapeString(String s) {
    final StringBuilder sb = new StringBuilder();
    for (int i = 0, count = s.length(); i < count; i++)
      switch (s.charAt(i)) {
        case '\t':
          sb.append("\\t");
          break;
        case '\b':
          sb.append("\\b");
          break;
        case '\n':
          sb.append("\\n");
          break;
        case '\r':
          sb.append("\\r");
          break;
        case '\f':
          sb.append("\\f");
          break;
        case '\'':
          sb.append("\\'");
          break;
        case '\"':
          sb.append("\\\"");
          break;
        case '\\':
          sb.append("\\\\");
          break;
        default:
          sb.append(s.charAt(i));
      }
    return sb.toString();
  }
}
