package architecture

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.library.Architectures.layeredArchitecture

@AnalyzeClasses(packages = ["com.github.anddd7"])
class LayerDependencyTest {

  @ArchTest
  fun `should pass the layer dependency check for all packages`(classes: JavaClasses) =
      layeredArchitecture()
          .layer("API").definedBy("..adapter.inbound..")
          .layer("Resource").definedBy("..adapter.outbound..")
          .layer("UserCase").definedBy("..application..")
          .layer("Domain").definedBy("..domain..")

          .whereLayer("API").mayNotBeAccessedByAnyLayer()
          .whereLayer("Resource").mayOnlyBeAccessedByLayers("API")
          .whereLayer("UserCase").mayOnlyBeAccessedByLayers("API")
          // Domain can be accessed by any layer
          .check(classes)
}
