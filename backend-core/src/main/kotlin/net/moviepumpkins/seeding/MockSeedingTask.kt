package net.moviepumpkins.seeding

import org.springframework.boot.CommandLineRunner

abstract class MockSeedingTask(val taskName: String) : CommandLineRunner {

    private fun doesntContainsTask(args: Args): Boolean {

        val onlyTask = args.find { it?.startsWith("Only") ?: false }
        val isThisTaskForbidden = args.any { it == "Not${taskName.uppercase()}" }

        return isThisTaskForbidden || (onlyTask != null && onlyTask != "Only${taskName.uppercase()}")
    }

    protected abstract fun run()

    override fun run(vararg args: String?) {
        if (doesntContainsTask(args)) {
            return
        }

        run()
    }
}

typealias Args = Array<out String?>