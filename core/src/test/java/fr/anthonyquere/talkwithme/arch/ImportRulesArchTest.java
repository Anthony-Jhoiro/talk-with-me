package fr.anthonyquere.talkwithme.arch;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "fr.anthonyquere.talkwithme")
public class ImportRulesArchTest {

  @ArchTest
  public static final ArchRule hexaImports = classes()
    .that().resideInAnyPackage("..hexa..")
    .should().onlyAccessClassesThat()
    .resideInAnyPackage("java..", "..hexa..");
}
