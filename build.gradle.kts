plugins {
    id("java")
    id("idea")
}

group = "net.kamilereon"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    testCompileOnly("org.projectlombok:lombok:1.18.34")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.34")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.register<JavaExec>("runServer") {
    group = "run"
    description = "Runs the server"

    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("net.kamilereon.server.Server")
}

tasks.register<JavaExec>("runClient") {
    group = "run"
    description = "Runs the client"

    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("net.kamilereon.client.Client")
    standardInput = System.`in`
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(
            "Multi-Release" to "true",
            "Main-Class" to "net.kamilereon.server.Server"
        )
    }

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.register<Jar>("serverJar") {
    archiveBaseName.set("quiz")
    archiveVersion.set("")
    archiveClassifier.set("server")
    destinationDirectory.set(file("/"))
    manifest {
        attributes("Main-Class" to "net.kamilereon.server.Server")
    }
    from(sourceSets["main"].output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.register<Jar>("clientJar") {
    archiveBaseName.set("quiz")
    archiveVersion.set("")
    archiveClassifier.set("client")
    destinationDirectory.set(file("/"))
    manifest {
        attributes("Main-Class" to "net.kamilereon.client.Client")
    }
    from(sourceSets["main"].output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}