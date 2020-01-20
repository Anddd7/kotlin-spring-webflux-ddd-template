import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter.ofPattern

/** -------------- project's properties -------------- */

group = "com.github.anddd7"
version = "0.0.1-SNAPSHOT"

repositories {
  mavenCentral()
  maven { url = uri("https://repo.spring.io/milestone") }
  jcenter()
}

buildscript {
  repositories {
    jcenter()
  }
}

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

/** -------------- import & apply plugins -------------- */

// import plugins into this project
plugins {
  val kotlinVersion = "1.3.61"

  // core plugins, which is already include in plugin dependencies spec
  idea
  java
  jacoco

  kotlin("jvm") version kotlinVersion
  kotlin("plugin.spring") version kotlinVersion

  /**
   * binary(external) plugins, provide id and version to resolve it
   * base plugin for spring-boot, provide plugins and tasks
   */
  id("org.springframework.boot") version "2.2.2.RELEASE"
  id("io.spring.dependency-management") version "1.0.8.RELEASE"

  id("org.flywaydb.flyway") version "6.1.4"

  id("io.gitlab.arturbosch.detekt") version "1.3.0"
}

/** -------------- configure imported plugin -------------- */

val sourceSets = the<SourceSetContainer>()

sourceSets {
  create("apiTest") {
    java.srcDir("src/apiTest/kotlin")
    resources.srcDir("src/apiTest/resources")
    compileClasspath += sourceSets["test"].compileClasspath
    runtimeClasspath += sourceSets["test"].runtimeClasspath
  }
}

idea {
  project {
    jdkName = "11"
  }
  module {
    outputDir = file("$buildDir/idea-compiler/main")
    testOutputDir = file("$buildDir/idea-compiler/test")
  }
}

flyway {
  url = "jdbc:postgresql://localhost:5432/local?user=test&password=test"
}

detekt {
  toolVersion = "1.1.1"
  input = files("src/main/kotlin")
}

jacoco {
  toolVersion = "0.8.3"
}

/** -------------- dependencies management -------------- */

dependencies {
  /* kotlin */
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  testImplementation("io.mockk:mockk:1.9.3")
  testImplementation("org.assertj:assertj-core:3.14.0")

  /* junit5 */
  testImplementation("org.junit.jupiter:junit-jupiter-engine")

  /* webflux x reactor*/
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude(module = "junit")
    exclude(group = "org.mockito")
    exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
  }
  testImplementation("com.ninja-squad:springmockk:1.1.3")
  testImplementation("io.projectreactor:reactor-test")

  /* kotlin coroutines x reactor */
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

  /* security */
  //  implementation("org.springframework.boot:spring-boot-starter-security")
  //  testImplementation("org.springframework.security:spring-security-test")

  /* monitoring x logging */
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  runtimeOnly("net.logstash.logback:logstash-logback-encoder:5.2")

  /* r2bdc: reactive relational database connector */
  implementation("org.springframework.boot.experimental:spring-boot-starter-data-r2dbc:0.1.0.M3")
  // you can import and configure r2dbc manually in spring if you don't want to use experimental
  //  implementation("org.springframework.data:spring-data-r2dbc:1.0.0.RELEASE")
  //  implementation("io.r2dbc:r2dbc-spi:0.8.0.RELEASE")
  runtimeOnly("io.r2dbc:r2dbc-postgresql:0.8.0.RELEASE")
  // jdbc x flyway
  // ps: jdbc is only used by flyway and embedded db to do migration and test preparation
  implementation("org.springframework.boot:spring-boot-starter-jdbc")
  implementation("org.flywaydb:flyway-core:6.1.4")
  runtimeOnly("org.postgresql:postgresql")

  /* mock db x server */
  testImplementation("io.zonky.test:embedded-postgres:1.2.6")
  testImplementation("com.github.tomakehurst:wiremock:2.25.1")

  /* architecture verification */
  testImplementation("com.tngtech.archunit:archunit-junit5-api:0.12.0")
  testRuntimeOnly("com.tngtech.archunit:archunit-junit5-engine:0.12.0")
}

/** -------------- configure tasks -------------- */

tasks.register<Test>("apiTest") {
  description = "Runs the api tests."
  group = "verification"
  testClassesDirs = sourceSets["apiTest"].output.classesDirs
  classpath = sourceSets["apiTest"].runtimeClasspath
  mustRunAfter(tasks["test"])
}

tasks.withType<KotlinCompile>().all {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "11"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt> {
  // include("**/special/package/**") // only analyze a sub package inside src/main/kotlin
  // exclude("**/special/package/internal/**") // but exclude our legacy internal package
}

task("newMigration") {
  group = "flyway"
  description = """
        ./gradlew newMigration -Ptype=<ddl,dml> -Poperation=<operation>. Please ensure you already have dir `db/migration`
        """.trim()

  doLast {
    val (operation, type) = properties["operation"] to properties["type"]
    val resourcesPath = sourceSets["main"].resources.sourceDirectories.singleFile.path
    val timestamp = now().format(ofPattern("yyyyMMddHHmm"))
    val filename = "V${timestamp}__${type}_$operation.sql"
    val filepath = "$resourcesPath/db/migration/$filename"
    File(filepath).takeIf { it.createNewFile() }?.appendText("-- script")
  }
}
