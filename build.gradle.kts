import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	id("org.springframework.boot") version "2.5.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
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

		// batch
		implementation("org.springframework.boot:spring-boot-starter-batch")
		implementation("org.springframework.boot:spring-boot-starter-data-jpa")
		implementation ("org.jsoup:jsoup:1.14.2")
		implementation ("org.seleniumhq.selenium:selenium-java:3.141.59")
		developmentOnly("org.springframework.boot:spring-boot-devtools")
		runtimeOnly("com.h2database:h2")
		testImplementation("org.springframework.batch:spring-batch-test")
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
	}
}

tasks.withType<BootJar> {
	enabled = false
}