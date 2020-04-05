import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("jvm") version "1.3.71"
    kotlin("plugin.spring") version "1.3.71"
    id("org.flywaydb.flyway") version "6.0.8"
}

group = "sat.spike"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_11

val developmentOnly by configurations.creating
configurations {
    runtimeClasspath {
        extendsFrom(developmentOnly)
    }
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    // exposed
    jcenter()
}

dependencies {
    // spring boot dependencies
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")


    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation(group = "com.fasterxml.jackson.module", name = "jackson-module-kotlin", version = "2.10.3")

    // DB Stuff
    implementation("com.h2database:h2:1.4.200")
    implementation("org.postgresql:postgresql:42.2.10")
    implementation("org.flywaydb:flyway-core:6.3.0")
    runtimeOnly("org.postgresql:postgresql")

    // exposed
    implementation("org.jetbrains.exposed:exposed-core:0.22.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.22.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.22.1")
    implementation("org.jetbrains.exposed:exposed-jodatime:0.22.1")
    implementation("org.jetbrains.exposed:spring-transaction:0.22.1")

    implementation(kotlin("script-runtime"))

}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

flyway {
    url = "jdbc:postgresql://127.0.0.1:5432/tracking"
    baselineOnMigrate = true
}