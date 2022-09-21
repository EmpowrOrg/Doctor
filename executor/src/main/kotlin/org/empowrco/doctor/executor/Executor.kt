package org.empowrco.doctor.executor

import org.empowrco.doctor.models.ExecutorResponse


 abstract class Executor {
     abstract val handledLanguages: Set<String>
     abstract suspend fun execute(code: String): ExecutorResponse
     fun canHandle(language: String): Boolean {
         return handledLanguages.contains(language)
     }
 }

