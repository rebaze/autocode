package org.rebaze.autocode

import org.gradle.internal.exceptions.LocationAwareException
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProgressListener
import java.io.File

class GradleSupport(project: Project) : AutoCloseable, Verifier {
    private val connection = GradleConnector.newConnector()
            .forProjectDirectory(project.location)
            .connect()

    override fun execute(): Boolean {
        try {
            val build = connection.newBuild()
            build.setJavaHome(File("/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home"))
            val listener: ProgressListener? = ProgressListener {
                //println("${project.location} : ${it.description}")
            }
            build.addProgressListener(listener)
            build.setColorOutput(true)
            build.withArguments("--build-cache", "--parallel");

            //build.setStandardOutput(System.out)
            //build.setStandardError(System.err)

            val runable = build.forTasks("test")
            runable.run()
        } catch (e: Exception) {
            if (e.cause is LocationAwareException) {
                val locationException = e.cause as LocationAwareException
                //println("build failed: ${locationException.location}")
            } else {
                val locationException = e.cause
                //println("build failed: ${locationException.location}")

                //println("build failed: NO location: " + e.cause?.javaClass)

            }
            return false
        }

        return true
    }

    override fun close() {
        connection.close()
    }
}

data class Project(
        val location: File
)
