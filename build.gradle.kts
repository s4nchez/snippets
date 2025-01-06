plugins {
    kotlin("jvm")
}

group = "com.olx"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.http4k:http4k-bom:_"))
    implementation("org.http4k:http4k-contract")
    implementation("org.http4k:http4k-format-jackson")
    implementation("org.http4k:http4k-format-moshi")
    implementation("org.http4k:http4k-client-okhttp")
    implementation("org.http4k:http4k-server-undertow")
    implementation("org.http4k:http4k-server-jetty")
    implementation("org.http4k:http4k-failsafe")

    implementation(platform("dev.forkhandles:forkhandles-bom:_"))
    implementation("dev.forkhandles:result4k")
    implementation("dev.forkhandles:values4k")
    implementation("org.junit.jupiter:junit-jupiter-api:_")
    implementation("org.junit.jupiter:junit-jupiter-engine:_")
    implementation("com.natpryce:hamkrest:_")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}