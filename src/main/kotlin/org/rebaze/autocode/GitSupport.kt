package org.rebaze.autocode

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ResetCommand
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File
import java.io.IOException

class GitSupport(val project : Project) {

    fun isClean() : Boolean {
        val repository: Repository = openRepository()
        Git(repository).use { git: Git ->
            val changes = git.status().call()
            return (!changes.hasUncommittedChanges()) && changes.isClean
        }
        return false
    }

    fun commit(message: String = "No Message"): Boolean {
        val repository: Repository = openRepository()

        Git(repository).use { git: Git ->
            // Stage all files in the repo including new files
            git.add().addFilepattern(".").call()

            // and then commit the changes.
            git.commit()
                    .setCommitter("Autocode","autocode@rebaze.com")
                    .setAuthor("Autocode","autocode@rebaze.com")
                    .setMessage(message)
                    .call()
        }
        return true
    }

    fun reset() {
        val repository: Repository = openRepository()

        Git(repository).use { git: Git ->
            git.checkout().setAllPaths(true).call()
            git.clean()
                    .setCleanDirectories(true)
                    .setForce(true)
                    .call()
            git.reset()
                    .setMode(ResetCommand.ResetType.HARD)
                    .call()
           // git.checkout().addPaths(changes.uncommittedChanges.toList()).call()
        }
    }


    @Throws(IOException::class)
    private fun openRepository(): Repository {
        val builder = FileRepositoryBuilder()
        return builder
                .setGitDir(File(this.project.location,".git"))
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .build()
    }

    @Throws(IOException::class)
    private fun createNewRepository(): Repository {
        // prepare a new folder
        val localPath = File.createTempFile("TestGitRepository", "")
        if (!localPath.delete()) {
            throw IOException("Could not delete temporary file " + localPath)
        }

        // create the directory
        val repository = FileRepositoryBuilder.create(File(localPath, ".git"))
        repository.create()

        return repository
    }

    fun save(v: Refactoring) {
        // create a branch, commit, and switch back to original:
        val repository: Repository = openRepository()
        val baseBranch = repository.branch

        val correct = v.toString().replace("\\:".toRegex(),"_")
        Git(repository).use { git: Git ->
            val name = git.branchCreate().setName("autocode/suggestion-$correct")
                    .call().name

            println("Created branch " + name)
            git.checkout().setName(name).call()
            commit("Upgrade ASM library to version $v")
            git.checkout().setName(baseBranch).call()
        }
    }

}
