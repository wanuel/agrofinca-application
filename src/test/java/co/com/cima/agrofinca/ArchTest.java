package co.com.cima.agrofinca;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("co.com.cima.agrofinca");

        noClasses()
            .that()
            .resideInAnyPackage("co.com.cima.agrofinca.service..")
            .or()
            .resideInAnyPackage("co.com.cima.agrofinca.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..co.com.cima.agrofinca.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
