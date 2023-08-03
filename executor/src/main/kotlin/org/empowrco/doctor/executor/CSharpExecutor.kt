package org.empowrco.doctor.executor

import io.ktor.utils.io.core.use
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.empowrco.doctor.command.CommandResponse
import org.empowrco.doctor.command.Commander
import org.empowrco.doctor.models.Error
import org.empowrco.doctor.models.ExecutorResponse
import org.empowrco.doctor.models.Success
import org.empowrco.doctor.utils.files.FileUtil
import java.io.File
import java.io.FileWriter

class CSharpExecutor(private val commander: Commander, private val fileUtil: FileUtil) : Executor() {
    override val handledLanguages: Set<String> = setOf("text/x-csharp", "csharp", "c#")

    override suspend fun execute(code: String): ExecutorResponse {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val tempFile = fileUtil.writeToFile("c-sharp-exc", ".cs") {
                    it.appendLine(code)
                }
                val compileResult = commander.execute("csc ${tempFile.absolutePath}")
                tempFile.deleteRecursively()
                if (compileResult is CommandResponse.Error || hasOutputError(compileResult.output)) {
                    Success(compileResult.output, true)
                } else {
                    val path = System.getProperty("user.dir").plus("/${tempFile.name.replace(".cs", ".exe")}")
                    val result = commander.execute("mono $path")
                    File(path).deleteRecursively()
                    Success(result.output, result is CommandResponse.Error)
                }

            } catch (ex: Exception) {
                Error(ex.message ?: "")
            }
        }
    }

    private fun hasOutputError(output: String): Boolean {
        return ".cs\\((\\d).*,(\\d.*)\\):\\serror".toRegex().containsMatchIn(output)
    }

    override suspend fun test(code: String, unitTests: String): ExecutorResponse {
        return withContext(Dispatchers.IO) {
            val tempFolder = fileUtil.createTempDirectory()
            return@withContext try {
                val nameRegex = "class\\s(.*)?\\s".toRegex()
                val testName =
                    nameRegex.find(unitTests)?.groupValues?.lastOrNull()?.removeSuffix(":")?.removeSuffix("Tests")
                        ?: return@withContext Error("Unit tests were not wrapped in a valid class")
                val namespaceRegex = "namespace\\s(.*);".toRegex()
                val namespace = namespaceRegex.find(unitTests)?.groupValues?.lastOrNull()
                    ?: namespaceRegex.find(code)?.groupValues?.lastOrNull() ?: "UnitTesting"
                val tester = Tester.values()
                    .find { it.regexMatcher.containsMatchIn(unitTests) || it.regexMatcher.containsMatchIn(code) }?.command
                    ?: return@withContext Error("No matching tester. Please use either Nunit or Xunit for your unit tests")
                val createTestResult = commander.execute("dotnet new $tester -n $namespace", tempFolder)
                if (createTestResult is CommandResponse.Error) {
                    return@withContext Error(createTestResult.output)
                }
                val testFolder = File(tempFolder, "/$namespace")
                val codeFile = testFolder.listFiles()?.firstOrNull {
                    it.name.contains("Usings", ignoreCase = true) && it.name.endsWith(
                        ".cs",
                        ignoreCase = true
                    )
                }
                    ?: return@withContext Error(
                        "Error finding source file for Test $testName"
                    )
                FileWriter(codeFile).use {
                    it.write("")
                    it.appendLine(code)
                }
                val testsFile = testFolder.listFiles()?.firstOrNull {
                    it.name.endsWith(".cs", ignoreCase = true) && it.name.contains("test", ignoreCase = true)
                } ?: return@withContext Error(
                    "Error finding test file for Test $testName"
                )
                FileWriter(testsFile).use {
                    it.write("")
                    it.write(unitTests)
                }
                val executeTestResult = commander.execute("dotnet test", testFolder)
                if (executeTestResult is CommandResponse.Success) {
                    return@withContext Success(executeTestResult.output, hasOutputError(executeTestResult.output))
                } else {
                    return@withContext Error(executeTestResult.output)
                }
            } catch (ex: Exception) {
                Error(ex.message ?: "")
            } finally {
                fileUtil.deleteFiles(tempFolder)
            }
        }
    }
}

private enum class Tester(val regexMatcher: Regex, val command: String) {
    Nunit("(?<=\\[)(Test)(?=])".toRegex(), "nunit"), Xunit("(?<=\\[)(Fact)(?=])".toRegex(), "xunit"),
}
