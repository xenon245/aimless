plugins {
    id("org.jetbrains.dokka") version "1.5.0"
    `maven-publish`
    signing
}

tasks {
    create<Jar>("sourcesJar") {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }

    create<Jar>("dokkaJar") {
        archiveClassifier.set("javadoc")
        dependsOn("dokkaHtml")

        from("$buildDir/dokka/html/") {
            include("**")
        }
    }
}

publishing {
    repositories {
        mavenLocal()

        maven {
            name = "debug"
            url = rootProject.uri(".debug-paper/libraries")
        }

        maven {
            name = "central"

            credentials.runCatching {
                val nexusUsername: String by project
                val nexusPassword: String by project
                username = nexusUsername
                password = nexusPassword
            }.onFailure {
                logger.warn("Failed to load nexus credentials, Check the gradle.properties")
            }

            url = uri(
                if ("SNAPSHOT" in version as String) {
                    "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                } else {
                    "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                }
            )
        }
    }

    publications {
        create<MavenPublication>("api") {
            artifactId = project.name
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["dokkaJar"])

            pom {
                name.set(project.name)
                description.set("Blah blah")
                url.set("https://github.com/xenon245/${rootProject.name}")

                licenses {
                    license {
                        name.set("GNU General Public License version 3")
                        url.set("https://opensource.org/licenses/GPL-3.0")
                    }
                }

                developers {
                    developer {
                        id.set("monun")
                        name.set("Monun")
                        email.set("monun1010@gmail.com")
                        url.set("https://github.com/monun")
                        roles.addAll("developer")
                        timezone.set("Asia/Seoul")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/xenon245/${rootProject.name}.git")
                    developerConnection.set("scm:git:ssh://github.com:xenon245/${rootProject.name}.git")
                    url.set("https://github.com/xenon245/${rootProject.name}")
                }
            }
        }
    }
}

signing {
    isRequired = true
    sign(tasks.jar.get(), tasks["sourcesJar"], tasks["dokkaJar"])
    sign(publishing.publications["api"])
}
