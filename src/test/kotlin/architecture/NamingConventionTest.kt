package architecture

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController


@AnalyzeClasses(packages = ["com.github.anddd7"])
class NamingConventionTest {
  @ArchTest
  val `services should be prefixed` = classes()
      .that().resideInAPackage("..service..").and().areAnnotatedWith(Service::class.java)
      .should().haveSimpleNameEndingWith("Service")

  @ArchTest
  val `repository should be prefixed` = classes()
      .that().resideInAPackage("..repository..").and().areAnnotatedWith(Repository::class.java)
      .should().haveSimpleNameEndingWith("Repository")

  @ArchTest
  val `controller should be prefixed` = classes()
      .that().resideInAPackage("..controller..").and().areAnnotatedWith(RestController::class.java)
      .should().haveSimpleNameEndingWith("Controller")
}
