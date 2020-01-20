package architecture

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.library.Architectures.layeredArchitecture

@AnalyzeClasses(packages = ["com.github.anddd7"])
class LayerDependencyTest {
  @ArchTest
  fun `should pass the layer dependency check for all packages`(importedClasses: JavaClasses) =
      layeredArchitecture()
          .layer("Controller").definedBy("..controller..")
          .layer("Service").definedBy("..service..")
          .layer("Client").definedBy("..client..")
          .layer("Repository").definedBy("..repository..")
          .layer("Entity").definedBy("..entity..")

          .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
          .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
          .whereLayer("Client").mayOnlyBeAccessedByLayers("Controller", "Service")
          .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")
          // Entity can be accessed by any layer
          .check(importedClasses)
}
