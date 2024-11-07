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

    implementation(platform("dev.forkhandles:forkhandles-bom:_"))
    implementation("dev.forkhandles:result4k")
    implementation("dev.forkhandles:values4k")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}