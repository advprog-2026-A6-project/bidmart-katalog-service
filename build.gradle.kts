buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("net.serenity-bdd:serenity-gradle-plugin:5.3.7")
    }
}

plugins {
    java
    jacoco
    id("org.springframework.boot") version "3.5.10"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.sonarqube") version "5.0.0.4638"
}

apply(plugin = "net.serenity-bdd.serenity-gradle-plugin")

val serenityVersion = "5.3.7"

group = "id.ac.ui.cs.advprog"
version = "0.0.1-SNAPSHOT"
description = "bidmart-katalog-service"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}


jacoco {
    toolVersion = "0.8.12"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("net.serenity-bdd:serenity-core:$serenityVersion")
    testImplementation("net.serenity-bdd:serenity-junit5:$serenityVersion")
    testImplementation("net.serenity-bdd:serenity-rest-assured:$serenityVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("dev.samstevens.totp:totp:1.7.1")
    testImplementation("com.h2database:h2")
    runtimeOnly("com.h2database:h2")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.rabbitmq:amqp-client:5.24.0")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    useJUnitPlatform {
        excludeTags("functional")
    }
}

tasks.register<Test>("functionalTest") {
    description = "Runs Serenity functional tests"
    group = "verification"
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
    useJUnitPlatform {
        includeTags("functional")
    }
    systemProperty("serenity.project.name", "BidMart Catalog Service Functional Tests")
    systemProperty(
        "serenity.outputDirectory",
        layout.buildDirectory.dir("site/serenity").get().asFile.absolutePath
    )
    shouldRunAfter(tasks.test)
}

tasks.named<net.serenitybdd.plugins.gradle.AggregateTask>("aggregate") {
    dependsOn("functionalTest")
    mustRunAfter("functionalTest")
    getTestRoot().set(layout.buildDirectory.dir("site/serenity").get().asFile.absolutePath)
    setReportDirectory(layout.buildDirectory.dir("site/serenity").get().asFile.toPath())
}

tasks.named("check") {
    dependsOn("functionalTest")
}

tasks.jacocoTestReport {
    dependsOn(tasks.test, tasks.named("functionalTest"))
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(true)
    }
}

sonar {
    properties {
        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/test/jacocoTestReport.xml")
        property("sonar.coverage.exclusions", "**/config/**, **/model/**, **/*Application.java")
    }
}
