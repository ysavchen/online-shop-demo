plugins {
    java
    id("io.gatling.gradle") version "3.12.0"
}

group = "org.example"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("io.gatling.highcharts:gatling-charts-highcharts:3.12.0")
}