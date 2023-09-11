import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.3"
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	kotlin("plugin.jpa") version "1.8.22"
	kotlin("plugin.allopen") version "1.8.22"
	jacoco
}

allOpen {
	annotation("javax.persistence.Entity")
	annotation("javax.persistence.Embeddable")
	annotation("javax.persistence.MappedSuperclass")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("com.h2database:h2")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(module = "junit")
		exclude(module = "mockito-core")
	}
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
	testImplementation("com.ninja-squad:springmockk:3.0.1")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {

	dependsOn(tasks.test)
	reports {
		html.required = true
		html.outputLocation = file("$buildDir/reports/myReport.html")
		csv.required = true
		xml.required = true
	}

	var excludes = mutableListOf<String>()
	excludes.add("com/example/kotlin/BlogConfiguration.kt")
	excludes.add("com/example/kotlin/Extensions.kt")
	excludes.add("com/example/kotlin/KotlinApplication.kt")

	classDirectories.setFrom(
		sourceSets.main.get().output.asFileTree.matching {
			exclude(excludes)
		}
	)

	finalizedBy(tasks.jacocoTestCoverageVerification)
}

tasks.jacocoTestCoverageVerification {

	var Qdomains = mutableListOf<String>()

	for (qPattern in 'A' .. 'Z') {
		Qdomains.add("*.Q${qPattern}*")
	}

	violationRules {
		rule {
			enabled = true
			element = "CLASS"

			limit {
				counter = "BRANCH"
				value = "COVEREDRATIO"
				minimum = "0.80".toBigDecimal()
			}

			limit {
				counter = "LINE"
				value = "TOTALCOUNT"
				maximum = "200".toBigDecimal()
			}

			excludes = Qdomains
		}
	}

	var excludes = mutableListOf<String>()
	excludes.add("com/yolo/jean/config")
	excludes.add("com/yolo/jean/global")
	excludes.add("com/yolo/jean/reply/service/ReplyService.class")
	excludes.add("com/yolo/jean/board/service/BoardSearchService.class")

	classDirectories.setFrom(
		sourceSets.main.get().output.asFileTree.matching {
			exclude(excludes)
		}
	)
}
