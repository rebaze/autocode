package org.rebaze.autocode

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import picocli.CommandLine
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Files.readAllBytes
import java.nio.file.Paths


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
        //val versions = arrayOf("1.12.2","0.12.0","1.16.10","1.12.4","1.16.10")
        val versions = arrayOf("org.ow2.asm:asm:4.0","org.ow2.asm:asm:5.0","org.ow2.asm:asm:6.0")
        val project = Project(this.inputProject!!)
        if (GitSupport(project).isClean()) {
            println("Git is clean: OK")
            if (GradleSupport().execute(project)) {
                // Modify: clone elsewhere, modify and build that:
                println("Given Project build is: OK")
                for (v in versions) {
                    val path = File(inputProject,"gradle/dependencies.gradle").toPath()
                    val charset = StandardCharsets.UTF_8
                    var content = String(Files.readAllBytes(path), charset)
                    content = content.replace("org\\.ow2\\.asm\\:asm\\:5.2".toRegex(), v)
                    Files.write(path, content.toByteArray(charset))
                    if (!GitSupport(project).isClean()) {
                        val result = GradleSupport().execute(project)
                        if (result) {
                            println("Version Build: $v : OK")
                            GitSupport(project).save(v)
                        } else {
                            System.err.println("Version Build: $v : NO")

                        }
                        GitSupport(project).reset()
                    }else {
                        println("Version Build: $v : No Change")

                    }
                }
            }else {
                System.err.println("Project builds: NO")
            }
        }else {
            System.err.println("Project is clean: NO")
        }






    }

}
