package org.rebaze.autocode

import org.gradle.internal.exceptions.LocationAwareException
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProgressListener
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files

class DependencyVersionRefactoring(val anchor: String, val target: String) : Refactoring {
    override fun execute(project: Project) {
        val path = File(project.location, "gradle/dependencies.gradle").toPath()
        val charset = StandardCharsets.UTF_8
        var content = String(Files.readAllBytes(path), charset)


        content = content.replace("org\\.ow2\\.asm\\:asm\\:5.2".toRegex(), target)
        Files.write(path, content.toByteArray(charset))
    }

    override fun toString() = "Dependency_" + target
}

class GradleVersionRefactoring(val version: String) : Refactoring {
    override fun execute(project: Project) {
        val connection = GradleConnector.newConnector()
                .forProjectDirectory(project.location)
                .connect()

        try {
            connection.newBuild()
                    .setJavaHome(File("/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home"))
                    .forTasks("wrapper","--gradle-version=$version").run()
            connection.newBuild()
                    .setJavaHome(File("/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home"))
                    .forTasks("wrapper","--gradle-version=$version").run()

        } catch (e: Exception) {
            System.err.println("Gradle Wrapper failed!")
            e.printStackTrace()
            //return false
        } finally {
            connection.close()
        }
    }

    override fun toString() = "gradle_upgrade_to_" + version
}
