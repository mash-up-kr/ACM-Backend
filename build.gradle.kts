import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	id("org.springframework.boot") version "2.5.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("jacoco")
	id("org.sonarqube") version "3.0"
	kotlin("jvm") version "1.5.21"
	kotlin("plugin.spring") version "1.5.21"
	kotlin("plugin.jpa") version "1.5.21"
}

buildscript {
	repositories {
		mavenCentral()
	}
}

allprojects {
	repositories {
		mavenCentral()
	}
}

subprojects {
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")
	apply(plugin = "java")
	apply(plugin = "kotlin")
	apply(plugin = "kotlin-kapt")
	apply(plugin = "kotlin-jpa")
	apply(plugin = "kotlin-spring")
	apply(plugin = "org.sonarqube")
	apply(plugin = "jacoco")

	the<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension>().apply {
		imports {
			mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
		}
	}

	group = "mashup.backend.spring"
	version = "0.0.1-SNAPSHOT"
	java.sourceCompatibility = JavaVersion.VERSION_11

	dependencies {
		implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
		testImplementation("org.springframework.boot:spring-boot-starter-test") {
			exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
		}
	}

	repositories {
		mavenCentral()
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "11"
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
		finalizedBy("jacocoTestReport")
	}

	tasks.jacocoTestReport {
		dependsOn("test")
		reports {
			xml.required.set(true)
			csv.required.set(false)
			html.required.set(true)
		}
	}

	tasks.withType<org.sonarqube.gradle.SonarQubeTask> {
		dependsOn("jacocoTestResult")
	}
}

tasks.withType<BootJar> {
	enabled = false
}

jacoco {
	toolVersion = "0.8.7"
}

sonarqube {
	properties {
		property("sonar.sourceEncoding", "UTF-8")
		property("sonar.core.codeCoveragePlugin", "jacoco")
	}
}