plugins {
    java
    id("io.gatling.gradle") version "3.9.2"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("io.gatling.highcharts:gatling-charts-highcharts:3.11.4")
}