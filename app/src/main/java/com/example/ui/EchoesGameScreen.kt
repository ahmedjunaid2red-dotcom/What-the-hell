package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.GamePhase
import com.example.ui.theme.NoirBackground
import com.example.viewmodel.GameViewModel

@Composable
fun EchoesGameScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDossier by remember { mutableStateOf(false) }

    // Scroll to top or bottom when turn advances
    LaunchedEffect(uiState.currentTurn, uiState.phase) {
        scrollState.animateScrollTo(0)
    }

    LaunchedEffect(uiState.statusMessage) {
        uiState.statusMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    BackHandler(enabled = showDossier) {
        showDossier = false
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(NoirBackground)
            .windowInsetsPadding(WindowInsets.safeDrawing),
        containerColor = NoirBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 680.dp)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header Bar with Tape Spinner, Turn counter & Suspicion monitor
                TapeReelHeader(
                    currentTurn = uiState.currentTurn,
                    maxTurns = uiState.maxTurns,
                    suspicionLevel = uiState.suspicionLevel,
                    isAiPowered = uiState.isAiPowered,
                    soundEnabled = uiState.soundEffectsEnabled,
                    onToggleSound = { viewModel.toggleSoundEffects() },
                    onOpenDossier = { showDossier = true }
                )

                // Visual Hero Banner: Room 204 Crime Scene at The Neon Flamingo Motel
                CrimeSceneBannerView()

                // Dispatch / Story narrative terminal card
                NarrativeCard(
                    turnNumber = uiState.currentTurn,
                    narrativeText = uiState.currentNarrative,
                    isLoading = uiState.isLoadingAi
                )

                // Main Interaction Content depending on Game Phase
                when (uiState.phase) {
                    GamePhase.INTRO,
                    GamePhase.INVESTIGATING -> {
                        InvestigationChoicesView(
                            choices = uiState.currentChoices,
                            isLoading = uiState.isLoadingAi,
                            onSelectChoice = { choice ->
                                viewModel.submitPlayerAction(choice)
                            },
                            onSubmitCustomAction = { customAction ->
                                viewModel.submitPlayerAction(customAction)
                            }
                        )
                    }

                    GamePhase.ACCUSATION_TIME -> {
                        AccusationDialog(
                            suspects = uiState.suspects,
                            clues = uiState.cluesDiscovered,
                            selectedSuspect = uiState.selectedSuspectForAccusation,
                            selectedEvidence = uiState.selectedEvidenceForAccusation,
                            onSelectSuspect = { suspect ->
                                viewModel.selectSuspectForAccusation(suspect)
                            },
                            onSelectEvidence = { clue ->
                                viewModel.selectEvidenceForAccusation(clue)
                            },
                            onConfirmAccusation = {
                                viewModel.confirmAccusation()
                            }
                        )
                    }

                    GamePhase.CASE_RESOLVED -> {
                        uiState.verdict?.let { verdict ->
                            VerdictView(
                                verdict = verdict,
                                onRestart = { viewModel.restartGame() }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Case Dossier Modal Dialog
            if (showDossier) {
                CaseDossierDialog(
                    clues = uiState.cluesDiscovered,
                    suspects = uiState.suspects,
                    turns = uiState.turnsHistory,
                    onDismiss = { showDossier = false }
                )
            }
        }
    }
}
