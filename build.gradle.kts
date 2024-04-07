plugins {
	java
	id("org.springframework.boot") version "3.2.3"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.wayfare"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.slf4j:slf4j-simple:1.7.25")
	implementation("com.google.maps:google-maps-services:2.2.0")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.0")
	implementation("org.apache.commons:commons-lang3:3.14.0")	
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.16.0")
	implementation("io.jsonwebtoken:jjwt-api:0.12.3")
	implementation("com.mashape.unirest:unirest-java:1.4.9")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	implementation(platform("com.azure:azure-sdk-bom:1.2.22"))
	implementation("com.azure:azure-storage-blob")
	implementation("com.azure:azure-identity")
	implementation("com.azure:azure-storage-queue")
	implementation("com.azure:azure-storage-file-share")
	implementation("com.azure:azure-storage-file-datalake")
	implementation("com.azure:azure-security-keyvault-secrets")

}

tasks.withType<Test> {
	useJUnitPlatform()
}