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
  val kotlinVersion = "1.3.70"

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
  id("org.springframework.boot") version "2.2.4.RELEASE"
  id("io.spring.dependency-management") version "1.0.9.RELEASE"

  id("org.flywaydb.flyway") version "6.1.4"

  id("io.gitlab.arturbosch.detekt") version "1.3.0"

  id("org.owasp.dependencycheck") version "5.3.2"
}

/** -------------- configure imported plugin -------------- */

val apiSourceSet = sourceSets.create("apiTest") {
  withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
    kotlin.srcDir("src/apiTest/kotlin")
    resources.srcDir("src/apiTest/resources")
  }

  val testSourceSet = sourceSets.test.get()

  compileClasspath += testSourceSet.runtimeClasspath
  runtimeClasspath += testSourceSet.runtimeClasspath
}

idea {
  project {
    jdkName = "11"
  }
  module {
    outputDir = file("$buildDir/idea-compiler/main")
    testOutputDir = file("$buildDir/idea-compiler/test")

    apiSourceSet.withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
      testSourceDirs = testSourceDirs + kotlin.srcDirs
      testResourceDirs = testResourceDirs + resources.srcDirs
    }
  }
}

flyway {
  url = "jdbc:postgresql://localhost:5432/local?user=test&password=test"
}

detekt {
//  failFast = true
  toolVersion = "1.1.1"
  input = files("src/main/kotlin")
}

jacoco {
  toolVersion = "0.8.5"
}

/** -------------- dependencies management -------------- */

dependencies {
  /* kotlin */
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  /* kotlin coroutines*/
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
  implementation("io.projectreactor:reactor-kotlin-extensions:1.0.0.M2")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")
  /* kotlin test */
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
  testImplementation("org.junit.jupiter:junit-jupiter-engine")
  testImplementation("io.mockk:mockk:1.9.3")
  testImplementation("org.assertj:assertj-core:3.15.0")

  /* spring webflux*/
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  /* security */
//  implementation("org.springframework.boot:spring-boot-starter-security")
//  testImplementation("org.springframework.security:spring-security-test")
  /* spring test*/
  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude(module = "junit")
    exclude(group = "org.mockito")
    exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
  }
  testImplementation("com.ninja-squad:springmockk:2.0.0")
  testImplementation("io.projectreactor:reactor-test")

  /* monitoring*/
//  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("net.logstash.logback:logstash-logback-encoder:6.3")

  /* r2bdc */
  // TODO remove this after upgrade spring boot to 2.3.x https://github.com/spring-projects/spring-boot/issues/19988
  implementation("org.springframework.boot.experimental:spring-boot-starter-data-r2dbc:0.1.0.M3")
  runtimeOnly("io.r2dbc:r2dbc-postgresql:0.8.2.RELEASE")
  /* jdbc */
  implementation("org.springframework.boot:spring-boot-starter-jdbc")
  implementation("org.flywaydb:flyway-core:6.2.3")
  runtimeOnly("org.postgresql:postgresql")

  /* mock db x server */
  testImplementation("io.zonky.test:embedded-postgres:1.2.6")
  testImplementation("com.github.tomakehurst:wiremock:2.26.0")

  /* architecture verification */
  testImplementation("com.tngtech.archunit:archunit-junit5-api:0.13.1")
  testRuntimeOnly("com.tngtech.archunit:archunit-junit5-engine:0.13.1")
}

/** -------------- configure tasks -------------- */

tasks.register<Test>("apiTest") {
  description = "Runs the api tests."
  group = "verification"
  testClassesDirs = apiSourceSet.output.classesDirs
  classpath = apiSourceSet.runtimeClasspath
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
