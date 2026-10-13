package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.OfflineGameEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Echoes of Mind", appName)
    }

    @Test
    fun `game engine initial turn satisfies rules`() {
        val initialTurn = OfflineGameEngine.getInitialTurn()
        // Rule 1: Starts with 1980s mystery scene
        assertTrue(initialTurn.narrativeText.contains("Neon Flamingo Motel") || initialTurn.narrativeText.contains("Julian Thorne"))
        // Rule 2: 3 distinct choices
        assertEquals(3, initialTurn.choices.size)
        // Rule 4: Suspicion level between 1 and 100
        assertTrue(initialTurn.suspicionLevel in 1..100)
        // Rule 6: Narrative ends with "What do you do next?" and under 150 words
        assertTrue(initialTurn.narrativeText.endsWith("What do you do next?"))
        val wordCount = initialTurn.narrativeText.split("\\s+".toRegex()).size
        assertTrue("Word count $wordCount should be under 150", wordCount < 150)
    }

    @Test
    fun `game engine advances 5 turns and evaluates accusation`() {
        var turn = OfflineGameEngine.getInitialTurn()
        assertEquals(1, turn.turnNumber)

        val clues = mutableListOf(turn.discoveredClue!!)

        // Turns 1 to 4 advance to turn 5
        for (turnIdx in 1..4) {
            turn = OfflineGameEngine.processTurn(
                currentTurn = turn.turnNumber,
                playerAction = turn.choices.first(),
                currentSuspicion = turn.suspicionLevel,
                existingClues = clues
            )
            turn.discoveredClue?.let { clues.add(it) }
            assertTrue(turn.narrativeText.endsWith("What do you do next?"))
            assertTrue(turn.suspicionLevel in 1..100)
        }

        assertEquals(5, turn.turnNumber)

        // Rule 5: Accusation verdict
        val verdict = OfflineGameEngine.evaluateAccusation(
            accusedSuspectId = "vincent",
            presentedEvidence = clues.firstOrNull(),
            finalSuspicion = turn.suspicionLevel,
            cluesCount = clues.size
        )
        assertTrue(verdict.wasCorrect)
        assertNotNull(verdict.detectiveRank)
    }
}
