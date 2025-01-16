plugins {
    id("org.jetbrains.kotlin.plugin.jpa") version "1.9.25"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.mysql:mysql-connector-j")

    testImplementation("org.testcontainers:mysql")
    testImplementation("org.testcontainers:elasticsearch")
}