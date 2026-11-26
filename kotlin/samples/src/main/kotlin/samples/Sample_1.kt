package samples

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield

/**
 * An example of observer pattern with some enhancements
 */

typealias Score = Pair<Int, Int>

class ScoreAnnouncer {

    fun announceScore(score: Score) {
        val (first, second) = score
        println("New score is $first : $second")
    }
}

class LeadingTeamAnnouncer {

    fun announceLeadingTeamScore(score: Score) {
        val announce = when {
            score.first > score.second -> "The first team is a leader"
            score.first < score.second -> "The second team is a leader"
            else -> "The game is all tied up"
        }
        println(announce)
    }
}

class Game {

    private val _scoreFlow = MutableStateFlow(0 to 0)
    val scoreFlow = _scoreFlow.asStateFlow()

    suspend fun onFirstTeamScores() {
        // Updates atomically
        _scoreFlow.updateAndGet { it.copy(first = it.first + 1) }
        // Give a chance to another coroutine to do its work
        yield()
    }

    suspend fun onSecondTeamScores() {
        // Updates atomically
        _scoreFlow.updateAndGet { it.copy(second = it.second + 1) }
        // Give a chance to another coroutine to do its work
        yield()
    }
}

fun main() = runBlocking {

    println("START")

    val leadingTeamAnnouncer = LeadingTeamAnnouncer()
    val scoreAnnouncer = ScoreAnnouncer()

    val game = Game()

    val job1 = launch { game.scoreFlow.collect(leadingTeamAnnouncer::announceLeadingTeamScore) }
    val job2 = launch { game.scoreFlow.collect(scoreAnnouncer::announceScore) }

    val job3 = launch {
        game.onFirstTeamScores()
        game.onFirstTeamScores()
        game.onSecondTeamScores()
    }

    job3.join()

    // Stop collecting
    job1.cancel()
    job2.cancel()

    println("END")

}