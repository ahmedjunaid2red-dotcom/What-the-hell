package com.example.viewmodel

import android.app.Application
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GeminiGameEngine
import com.example.data.OfflineGameEngine
import com.example.model.ClueItem
import com.example.model.GamePhase
import com.example.model.GameTurn
import com.example.model.GameUiState
import com.example.model.Suspect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var toneGenerator: ToneGenerator? = null

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 60)
        } catch (_: Exception) {}
        initGame()
    }

    override fun onCleared() {
        super.onCleared()
        toneGenerator?.release()
        toneGenerator = null
    }

    fun initGame() {
        val initialTurn = OfflineGameEngine.getInitialTurn()
        val initialSuspects = OfflineGameEngine.INITIAL_SUSPECTS
        val isAi = GeminiGameEngine.isApiKeyAvailable()

        _uiState.value = GameUiState(
            phase = GamePhase.INVESTIGATING,
            currentTurn = 1,
            maxTurns = 5,
            suspicionLevel = initialTurn.suspicionLevel,
            turnsHistory = listOf(initialTurn),
            currentNarrative = initialTurn.narrativeText,
            currentChoices = initialTurn.choices,
            cluesDiscovered = initialTurn.discoveredClue?.let { listOf(it) } ?: emptyList(),
            suspects = initialSuspects,
            isAiPowered = isAi,
            statusMessage = if (isAi) "AI Engine Online (Gemini 3.5 Flash)" else "Local 1980s Noir Engine Ready"
        )
    }

    private fun playSoundCue(toneType: Int) {
        if (_uiState.value.soundEffectsEnabled) {
            try {
                toneGenerator?.startTone(toneType, 120)
            } catch (_: Exception) {}
        }
    }

    fun toggleSoundEffects() {
        _uiState.update { it.copy(soundEffectsEnabled = !it.soundEffectsEnabled) }
    }

    fun submitPlayerAction(action: String) {
        if (action.isBlank() || _uiState.value.isLoadingAi) return
        val currentState = _uiState.value

        // Check if we are already in accusation time
        if (currentState.currentTurn >= currentState.maxTurns) {
            _uiState.update { it.copy(phase = GamePhase.ACCUSATION_TIME) }
            return
        }

        _uiState.update { it.copy(isLoadingAi = true, statusMessage = "The engine processes your move...") }
        playSoundCue(ToneGenerator.TONE_PROP_BEEP)

        viewModelScope.launch {
            try {
                val nextTurn: GameTurn = if (currentState.isAiPowered) {
                    try {
                        GeminiGameEngine.generateNextTurn(
                            currentTurn = currentState.currentTurn,
                            playerAction = action,
                            currentSuspicion = currentState.suspicionLevel,
                            history = currentState.turnsHistory,
                            clues = currentState.cluesDiscovered
                        )
                    } catch (e: Exception) {
                        // Seamless fallback
                        OfflineGameEngine.processTurn(
                            currentTurn = currentState.currentTurn,
                            playerAction = action,
                            currentSuspicion = currentState.suspicionLevel,
                            existingClues = currentState.cluesDiscovered
                        )
                    }
                } else {
                    OfflineGameEngine.processTurn(
                        currentTurn = currentState.currentTurn,
                        playerAction = action,
                        currentSuspicion = currentState.suspicionLevel,
                        existingClues = currentState.cluesDiscovered
                    )
                }

                val updatedClues = currentState.cluesDiscovered.toMutableList()
                if (nextTurn.discoveredClue != null && updatedClues.none { it.id == nextTurn.discoveredClue.id }) {
                    updatedClues.add(nextTurn.discoveredClue)
                    playSoundCue(ToneGenerator.TONE_PROP_PROMPT)
                }

                val nextTurnNum = currentState.currentTurn + 1
                val isAccusationTime = nextTurnNum > currentState.maxTurns

                _uiState.update { state ->
                    state.copy(
                        currentTurn = nextTurnNum.coerceAtMost(state.maxTurns),
                        suspicionLevel = nextTurn.suspicionLevel,
                        currentNarrative = nextTurn.narrativeText,
                        currentChoices = nextTurn.choices,
                        cluesDiscovered = updatedClues,
                        turnsHistory = state.turnsHistory + nextTurn,
                        phase = if (isAccusationTime) GamePhase.ACCUSATION_TIME else GamePhase.INVESTIGATING,
                        isLoadingAi = false,
                        statusMessage = if (isAccusationTime) "Time is up! You must accuse Julian's killer!" else "Turn $nextTurnNum recorded."
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoadingAi = false,
                        statusMessage = "Investigation glitch: ${e.message}"
                    )
                }
            }
        }
    }

    fun selectSuspectForAccusation(suspect: Suspect) {
        _uiState.update { it.copy(selectedSuspectForAccusation = suspect) }
        playSoundCue(ToneGenerator.TONE_PROP_ACK)
    }

    fun selectEvidenceForAccusation(clue: ClueItem) {
        _uiState.update { it.copy(selectedEvidenceForAccusation = clue) }
        playSoundCue(ToneGenerator.TONE_PROP_BEEP2)
    }

    fun confirmAccusation() {
        val state = _uiState.value
        val accused = state.selectedSuspectForAccusation ?: return

        playSoundCue(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE)

        val verdict = OfflineGameEngine.evaluateAccusation(
            accusedSuspectId = accused.id,
            presentedEvidence = state.selectedEvidenceForAccusation,
            finalSuspicion = state.suspicionLevel,
            cluesCount = state.cluesDiscovered.size
        )

        _uiState.update {
            it.copy(
                phase = GamePhase.CASE_RESOLVED,
                verdict = verdict,
                statusMessage = if (verdict.wasCorrect) "CASE CLOSED: KILLER APPREHENDED!" else "CASE COLD: REAL KILLER ESCAPED!"
            )
        }
    }

    fun restartGame() {
        initGame()
    }
}
