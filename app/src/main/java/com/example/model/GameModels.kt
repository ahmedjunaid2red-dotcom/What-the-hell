package com.example.model

enum class ClueCategory {
    PHYSICAL,
    AUDIO_TAPE,
    TESTIMONY,
    FORENSIC
}

data class ClueItem(
    val id: String,
    val title: String,
    val description: String,
    val category: ClueCategory,
    val detailNotes: String = "",
    val turnDiscovered: Int = 1
)

data class Suspect(
    val id: String,
    val name: String,
    val alias: String,
    val role: String,
    val motive: String,
    val alibi: String,
    val quote: String,
    val isGuilty: Boolean = false,
    val portraitResName: String = ""
)

data class GameTurn(
    val turnNumber: Int,
    val narrativeText: String,
    val playerAction: String? = null,
    val suspicionDelta: Int = 0,
    val suspicionLevel: Int = 15,
    val choices: List<String> = emptyList(),
    val discoveredClue: ClueItem? = null,
    val timestamp: Long = System.currentTimeMillis()
)

enum class GamePhase {
    INTRO,
    INVESTIGATING,
    ACCUSATION_TIME,
    CASE_RESOLVED
}

data class CaseVerdict(
    val accusedSuspectId: String,
    val wasCorrect: Boolean,
    val motiveRevealed: String,
    val epilogueNarrative: String,
    val finalSuspicion: Int,
    val detectiveRank: String,
    val cluesFoundCount: Int
)

data class GameUiState(
    val phase: GamePhase = GamePhase.INTRO,
    val currentTurn: Int = 1,
    val maxTurns: Int = 5,
    val suspicionLevel: Int = 20, // Hidden tension 1-100
    val turnsHistory: List<GameTurn> = emptyList(),
    val currentNarrative: String = "",
    val currentChoices: List<String> = emptyList(),
    val cluesDiscovered: List<ClueItem> = emptyList(),
    val suspects: List<Suspect> = emptyList(),
    val selectedSuspectForAccusation: Suspect? = null,
    val selectedEvidenceForAccusation: ClueItem? = null,
    val verdict: CaseVerdict? = null,
    val isLoadingAi: Boolean = false,
    val isAiPowered: Boolean = false,
    val statusMessage: String? = null,
    val soundEffectsEnabled: Boolean = true
)
