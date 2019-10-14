import org.gradle.api.Project

fun Project.customTasks() {
    tasks.register("hello") {
        group = "custom"
        description = "Hello World task - useful to solve build problems"

        doLast {
            println("Hello :)")
        }
    }
}