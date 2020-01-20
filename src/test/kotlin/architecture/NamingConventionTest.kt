package architecture

import com.github.anddd7.adapter.EndPoint
import com.github.anddd7.adapter.PersistObject
import com.github.anddd7.adapter.RepositoryImpl
import com.github.anddd7.application.DTO
import com.github.anddd7.application.UserCase
import com.github.anddd7.domain.Repository
import com.github.anddd7.domain.Service
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes


@AnalyzeClasses(packages = ["com.github.anddd7"])
class NamingConventionTest {
  @ArchTest
  val `endpoint should be prefixed` = classes()
      .that().resideInAPackage("..inbound..").and().areAssignableTo(EndPoint::class.java)
      .should().haveSimpleNameEndingWith("Controller")

  @ArchTest
  val `repository implementation should be prefixed` = classes()
      .that().resideInAPackage("..outbound..").and().areAssignableTo(RepositoryImpl::class.java)
      .should().haveSimpleNameEndingWith("RepositoryImpl").orShould().haveSimpleNameEndingWith("Client")

  @ArchTest
  val `persist object should be prefixed` = classes()
      .that().resideInAPackage("..outbound..").and().areAssignableTo(PersistObject::class.java)
      .should().haveSimpleNameEndingWith("PO")

  @ArchTest
  val `application service should be prefixed` = classes()
      .that().resideInAPackage("..application..").and().areAssignableTo(UserCase::class.java)
      .should().haveSimpleNameEndingWith("UserCase")

  @ArchTest
  val `dto should be prefixed` = classes()
      .that().resideInAPackage("..application..dto..").and().areAssignableTo(DTO::class.java)
      .should().haveSimpleNameEndingWith("DTO")

  @ArchTest
  val `domain services should be prefixed` = classes()
      .that().resideInAPackage("..domain..").and().areAssignableTo(Service::class.java)
      .should().haveSimpleNameEndingWith("Service")

  @ArchTest
  val `repository should be prefixed` = classes()
      .that().resideInAPackage("..domain..").and().areAssignableTo(Repository::class.java)
      .should().haveSimpleNameEndingWith("Repository")
}
