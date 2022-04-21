object Counter {
    const val PACKAGE_BRANCH_COVERAGE = "PACKAGE_BRANCH_COVERAGE"
    const val PACKAGE_LINE_COVERAGE = "PACKAGE_LINE_COVERAGE"
    const val MISSEDCOUNT = "MISSEDCOUNT"
    const val METHOD_LINE_COVERAGE = "METHOD_LINE_COVERAGE"
    const val METHOD_COMPLEXITY = "METHOD_COMPLEXITY"
    const val METHOD_BRANCH_COVERAGE = "METHOD_BRANCH_COVERAGE"
}

val mustBeTested = mutableListOf(
    "**/viewmodels/**/*.*",
    "**/*ViewModel.*",
    "**/*Viewmodel.*",
    "**/usecases/**/*.*",
    "**/*UseCase.*",
    "**/*Usecase.*"
)

val jaCocoConfig = mapOf<String, String>(
    // the build will fail if we miss >= 10% of the if-else and switches package-wide.
    Counter.PACKAGE_BRANCH_COVERAGE to "0.9",
    // the build will fail if we miss >= 10% of the lines package-wide.
    Counter.PACKAGE_LINE_COVERAGE to "0.9",
    // Every ViewModel and UseCase class
    // MUST have at least 1 unit test otherwise the build will fail.
    Counter.MISSEDCOUNT to "0",
    // the build will fail if we miss >= 10% of the lines per method/function.
    Counter.METHOD_LINE_COVERAGE to "0.9",
    // the build will fail if the code (per method/function) is way too complex.
    // https://en.wikipedia.org/wiki/Cyclomatic_complexity
    Counter.METHOD_COMPLEXITY to "26",
    // the build will fail if we miss >= 10% of the lines per method/function.
    Counter.METHOD_BRANCH_COVERAGE to "0.9"
)

val excludeList = listOf<String>(
    "**/R.class", // ignore the auto-generated
    "**/R\$*.class", // ignore the auto-generated
    "**/BuildConfig.*", // ignore the auto-generated
    "**/Manifest*.*", // ignore the auto-generated
    "**/*\$ViewInjector*.*", // ignore the auto-generated
    "**/*\$ViewBinder*.*", // ignore the auto-generated
    "**/*\$Lambda$*.*", // Jacoco can not handle several "$" in class name.
    "**/*Module.*", // Modules for Dagger.
    "**/*Dagger*.*", // Dagger auto-generated code.
    "**/*MembersInjector*.*", // Dagger auto-generated code.
    "**/*_Provide*Factory*.*", // ignore the auto-generated
    "**/*_Factory.*", //Dagger auto-generated code
    "**/*\$*", // Anonymous classes generated by kotlin
    "android/**/*.*", // ignore the auto-generated
    "**/databinding/**/*.*", // ignore the auto-generated
    "**/DataBinderMapperImpl*.*", // ignore the auto-generated
    "**/DataBindingInfo.*", // ignore the auto-generated
    "**/BR.*", // ignore the auto-generated
    "**/*Args*.*", // ignore auto-generated classes for Navigation
    "**/**_ViewBinding**", // ignore the auto-generated
    "**/injector/**/*.*", // ignore the auto-generated
    "**/di/**/*.*", // ignore the auto-generated
    "**/*Directions*.*", // ignore the auto-generated
    // remove what we don"t test
    "androidTest/**/*.*", // ignore any test classes
    "test/**/*.*", // ignore any test classes
    "**/test/**/*.*", // ignore any test classes
    "**/models/**/*.*", // ignore the model class
    "**/coroutines/*.*", // ignore the wrapper class, nothing to test
    "**/database/*.*", // ignore the Room database, there is nothing to test
    "**/KeystoreProvider*", // Framework component
    "**/extensions*Reflector*" // Java Reflector to access internal method, no need to test
)

val classDirectoriesTree = fileTree("${project.buildDir}") {
    include(
        "**/classes/**/main/**/*.*",
        "**/intermediates/classes/debug/**/*.*",
        "**/intermediates/javac/debug/classes/**/*.*",
        "**/tmp/kotlin-classes/debug/**/*.*"
    )

    exclude(excludeList)
}

val sourceDirectoriesTree = fileTree("${project.buildDir}") {
    include(
        "src/main/java/**",
        "src/main/kotlin/**",
        "src/main/**/java/**",
        "src/main/**/kotlin/**"
    )
}

val executionDataTree = fileTree("${project.buildDir}") {
    include(
        "outputs/code_coverage/**/*.ec",
        "jacoco/jacocoTestReportDebug.exec",
        "jacoco/testDebugUnitTest.exec",
        "jacoco/test.exec"
    )
}

fun JacocoReportsContainer.reports() {
    csv.isEnabled = false
    xml.apply {
        isEnabled = true
        destination = file("$buildDir/reports/jacoco/coverage.xml")
    }
    html.apply {
        isEnabled = true
        destination = file("$buildDir/reports/jacoco/html")
    }
}

fun JacocoReport.setDirectories() {
    sourceDirectories.setFrom(sourceDirectoriesTree)
    classDirectories.setFrom(classDirectoriesTree)
    executionData.setFrom(executionDataTree)
}

fun JacocoCoverageVerification.setDirectories() {
    sourceDirectories.setFrom(sourceDirectoriesTree)
    classDirectories.setFrom(classDirectoriesTree)
    executionData.setFrom(executionDataTree)
}

tasks.withType<Test> {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
    minHeapSize = "512m"
    maxHeapSize = "1024m"
    maxParallelForks = Runtime.getRuntime().availableProcessors().div(2) ?: 1
}

tasks.register<JacocoReport>("generateJacocoTestReport") {
    group = BuildTaskGroups.verification
    description = "Code coverage reports."
    dependsOn("testDebugUnitTest")
    reports {
        reports()
    }
    setDirectories()
}

val minimumCoverage = "0.8".toBigDecimal()
tasks.register<JacocoCoverageVerification>("jacocoCoverageVerification") {
    group = BuildTaskGroups.verification
    description = "Code coverage verification."
    dependsOn("generateJacocoTestReport")
    violationRules {
        setFailOnViolation(true)

        rule {
            element = "PACKAGE"

            limit {
                value = "COVEREDRATIO"
                counter = "BRANCH"
                minimum =
                    jaCocoConfig.get(Counter.PACKAGE_BRANCH_COVERAGE)!!.toBigDecimal()
            }
        }

        rule {
            element = "PACKAGE"

            limit {
                value = "COVEREDRATIO"
                counter = "LINE"
                minimum = jaCocoConfig.get(Counter.PACKAGE_LINE_COVERAGE)!!.toBigDecimal()
            }
        }

        rule {
            element = "PACKAGE"
            includes = mustBeTested
            enabled = true

            limit {
                value = "MISSEDCOUNT"
                counter = "CLASS"
                maximum = jaCocoConfig.get(Counter.MISSEDCOUNT)!!.toBigDecimal()
            }
        }

        rule {
            element = "METHOD"

            limit {
                value = "COVEREDRATIO"
                counter = "LINE"
                minimum = jaCocoConfig.get(Counter.METHOD_LINE_COVERAGE)!!.toBigDecimal()
            }
        }

        rule {
            element = "METHOD"
            enabled = true

            limit {
                value = "TOTALCOUNT"
                counter = "COMPLEXITY"
                maximum = jaCocoConfig.get(Counter.METHOD_COMPLEXITY)!!.toBigDecimal()
            }
        }

        rule {
            element = "METHOD"
            enabled = true

            limit {
                value = "COVEREDRATIO"
                counter = "BRANCH"
                minimum = jaCocoConfig.get(Counter.METHOD_BRANCH_COVERAGE)!!.toBigDecimal()
            }
        }
    }
    setDirectories()
}

tasks.withType(JacocoCoverageVerification::class) {
    println("Coverage Report -> ${buildDir}/reports/jacoco/html/index.html")
}