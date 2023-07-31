object Deps {

    object Command {
        val main by lazy { ":command" }
        val fakes by lazy { ":command:fakes" }
    }

    object Executor {
        val main by lazy { ":executor" }
        val fakes by lazy { ":executor:fakes" }
    }

    object Download {
        val main by lazy { ":download" }
        val presenters by lazy { ":download:presenters" }
        val backend by lazy { ":download:backend" }
        val api by lazy { ":download:api" }
    }

    object Run {
        val api by lazy { ":run:api" }
        val presenters by lazy { ":run:presenters" }

        object Backend {
            val main by lazy { ":run:backend" }
            val fakes by lazy { ":run:backend:fakes" }
        }

        val main by lazy { ":run" }
    }

    object Tester {
        val api by lazy { ":tester:api" }
        val presenters by lazy { ":tester:presenters" }

        object Backend {
            val main by lazy { ":tester:backend" }
            val fakes by lazy { ":tester:backend:fakes" }
        }

        val main by lazy { ":tester" }
    }

    object Utils {
        val routing by lazy { ":utils:routing" }
        val main by lazy { ":utils" }
        val files by lazy { ":utils:files" }
    }

    object Hikari {
        val main by lazy { "com.zaxxer:HikariCP:5.0.1" }
    }

    object Postgresql {
        val main by lazy { "org.postgresql:postgresql:42.6.0" }
    }

    object Models {
        val main by lazy { ":models" }
    }

    object Koin {
        val main by lazy { "io.insert-koin:koin-ktor:${Versions.koin}" }
        val logger by lazy { "io.insert-koin:koin-logger-slf4j:${Versions.koin}" }
    }

    object Kotlin {
        val coroutines by lazy { "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3" }
        val dateTime by lazy { "org.jetbrains.kotlinx:kotlinx-datetime:0.4.0" }
    }

    object Ktor {
        val core by lazy { "io.ktor:ktor-server-core-jvm:${Versions.ktor}" }
        val auth by lazy { "io.ktor:ktor-server-auth-jvm:${Versions.ktor}" }
        val headers by lazy { "io.ktor:ktor-server-caching-headers-jvm:${Versions.ktor}" }
        val contentNegotiation by lazy { "io.ktor:ktor-server-content-negotiation-jvm:${Versions.ktor}" }
        val json by lazy { "io.ktor:ktor-serialization-kotlinx-json-jvm:${Versions.ktor}" }
        val gson by lazy { "io.ktor:ktor-client-gson:${Versions.ktor}" }
        val netty by lazy { "io.ktor:ktor-server-netty-jvm:${Versions.ktor}" }
        val statusPages by lazy { "io.ktor:ktor-server-status-pages:${Versions.ktor}" }
        val test by lazy { "io.ktor:ktor-server-test-host:${Versions.ktor}" }
        val callLogging by lazy { "io.ktor:ktor-server-call-logging:${Versions.ktor}" }
    }

    object Sources {
        val main by lazy { ":sources" }
        val fakes by lazy { ":sources:fakes" }
    }

    object Logback {
        val main by lazy { "ch.qos.logback:logback-classic:${Versions.logback}" }
    }

    object Archiver {
        val main by lazy { "org.rauschig:jarchivelib:1.2.0" }
    }

}
