plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.10"
    id("org.jetbrains.kotlin.kapt") version "1.8.10"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.8.10"
    id("org.jetbrains.kotlin.plugin.noarg") version "1.8.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.micronaut.application") version "3.7.7"
    id("io.micronaut.test-resources") version "3.7.7"
    id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
}

version = "0.1"
group = "com.poisonedyouth"

val kotlinVersion = project.properties["kotlinVersion"]
repositories {
    mavenCentral()
}

dependencies {
    kapt("io.micronaut:micronaut-http-validation")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")
    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("io.micronaut.liquibase:micronaut-liquibase")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("mysql:mysql-connector-java")

    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation("org.testcontainers:junit-jupiter:1.17.2")
    testImplementation("org.testcontainers:testcontainers:1.17.2")
    testImplementation("org.testcontainers:mysql:1.17.2")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("io.micronaut.test:micronaut-test-rest-assured")
}

allOpen {
    // Mark any classes with the following transactions as `open` automatically.
    annotations(
        "io.micronaut.aop.Around",
        "javax.transaction.Transactional",
        "javax.persistence.Entity"
    )
}

noArg {
    annotations("javax.persistence.Entity")
}

application {
    mainClass.set("com.poisonedyouth.ApplicationKt")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
}
graalvmNative.toolchainDetection.set(false)
micronaut {
    runtime("jetty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("com.poisonedyouth.*")
    }
    testResources {
        additionalModules.add("jdbc-mysql")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
