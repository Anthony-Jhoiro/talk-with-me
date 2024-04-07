package fr.anthonyquere.talkwithme.arch;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "fr.anthonyquere.talkwithme")
public class DomainRulesArchTest {
  @ArchTest
  public static final ArchRule domainsClasses = classes()
    .that().resideInAnyPackage("..domains..")
    .should().beRecords()
    .orShould().beEnums()
    .orShould().beAssignableTo(RuntimeException.class)
    .orShould().beAssignableTo(Exception.class)
    .andShould().onlyDependOnClassesThat()
    .resideInAnyPackage("..domains..", "java..");
}
