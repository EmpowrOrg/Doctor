package org.empowrco.doctor.tester.presenters

import org.empowrco.doctor.models.Error
import org.empowrco.doctor.models.NoValidExecutor
import org.empowrco.doctor.models.TestError
import org.empowrco.doctor.models.TestSuccess
import org.empowrco.doctor.tester.backend.TesterRepository
import org.empowrco.doctor.utils.UnknownException
import org.empowrco.doctor.utils.UnsupportedLanguage

interface TesterPresenter {
    suspend fun test(request: ExecuteTestRequest): ExecuteTestsResponse
}

internal class RealTesterPresenter(
    private val repo: TesterRepository
) : TesterPresenter {
    override suspend fun test(request: ExecuteTestRequest): ExecuteTestsResponse {
        return when (val result = repo.test(request.language, request.code, request.tests)) {
            is NoValidExecutor -> throw UnsupportedLanguage(result.message)
            is Error -> throw result
            TestSuccess -> {
                ExecuteTestsResponse(
                    output = "All tests passed",
                    success = true
                )
            }

            is TestError -> {
                ExecuteTestsResponse(result.failures.first(), false)
            }

            else -> throw UnknownException
        }
    }
}