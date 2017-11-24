package org.rebaze.autocode

import picocli.CommandLine
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Files.readAllBytes

interface Verifier {
    fun execute () : Boolean
}

interface Refactoring {
    fun execute(project: Project)
}

class Autocode {
    @CommandLine.Option(names = ["-v", "--verbose"], description = ["Be verbose."])
    private val verbose = false

    @CommandLine.Option(names = ["-h", "--help"], usageHelp = true, description = ["Displays this help message and quits."])
    private val helpRequested = false

    @CommandLine.Parameters(arity = "1", paramLabel = "PROJECT", description = ["Project to process."])
    private val inputProject: File? = null

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val argMe = arrayOf("/Users/tonit/devel/mockito/")
            try {
                val autocode = CommandLine.populateCommand(Autocode(), *argMe)
                if (autocode.helpRequested) {
                    CommandLine.usage(Autocode(), System.err)
                    System.exit(1)
                }
                autocode.execute()
            }catch (e: CommandLine.MissingParameterException) {
                CommandLine.usage(Autocode(), System.err)
                System.exit(1)
            }

        }
    }

    private fun execute() {
        println("Working on " + this.inputProject)
        val project = Project(this.inputProject!!)

        val refactorings = arrayOf(
                GradleVersionRefactoring("4.0")
                //DependencyVersionRefactoring("","org.ow2.asm:asm:5.0"),
                //DependencyVersionRefactoring("","org.ow2.asm:asm:6.0")
        )

        if (GitSupport(project).isClean()) {
            selectBuilder(project).use { builder ->
                println("Git is clean: OK")
                if (builder.execute()) {
                    // Modify: clone elsewhere, modify and build that:
                    println("Given Project build is: OK")
                    for (refactoring in refactorings) {
                        refactoring.execute(project)
                        saveMutation(project, builder, refactoring)
                    }
                } else {
                    System.err.println("Project builds: NO")
                }
            }
        }else {
            System.err.println("Project is clean: NO")
        }
    }

    private fun selectBuilder(project: Project) = GradleSupport(project)

    private fun saveMutation(project: Project, builder: Verifier, v: Refactoring) {
        if (!GitSupport(project).isClean()) {
            val result = builder.execute()
            if (result) {
                println("Version Build: $v : OK")
                GitSupport(project).save(v)
            } else {
                System.err.println("Version Build: $v : NO")

            }
            GitSupport(project).reset()
        } else {
            println("Version Build: $v : No Change")

        }
    }

}
