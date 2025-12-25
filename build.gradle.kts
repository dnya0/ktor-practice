val kotlin_version: String by project
val logback_version: String by project
val postgres_version: String by project
val exposed_version: String by project

plugins {
    kotlin("jvm") version "2.2.20"
    id("io.ktor.plugin") version "3.3.1"
    kotlin("plugin.serialization") version "2.2.20"
}

group = "practice.ktor"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

dependencies {
    implementation("io.ktor:ktor-server-call-logging")
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-server-netty")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml")
    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    //ktor exposed
    implementation("org.jetbrains.exposed:exposed-core:${exposed_version}")
    implementation("org.jetbrains.exposed:exposed-jdbc:${exposed_version}")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")

    //postgres
    implementation("org.postgresql:postgresql:${postgres_version}")
    
    //h2 in-memory database
    implementation("com.h2database:h2:2.2.224")
}
