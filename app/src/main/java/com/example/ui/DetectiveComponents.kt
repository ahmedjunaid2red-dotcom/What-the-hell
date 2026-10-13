package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeMute
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.KeyboardVoice
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.model.CaseVerdict
import com.example.model.ClueCategory
import com.example.model.ClueItem
import com.example.model.GameTurn
import com.example.model.Suspect
import com.example.ui.theme.AlertCrimson
import com.example.ui.theme.CassetteGold
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.NoirBackground
import com.example.ui.theme.NoirSurface
import com.example.ui.theme.NoirSurfaceBorder
import com.example.ui.theme.NoirSurfaceElevated
import com.example.ui.theme.PaperWhite
import com.example.ui.theme.PoliceTapeAmber
import com.example.ui.theme.TerminalGreen
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextMuted

@Composable
fun TapeReelHeader(
    currentTurn: Int,
    maxTurns: Int,
    suspicionLevel: Int,
    isAiPowered: Boolean,
    soundEnabled: Boolean,
    onToggleSound: () -> Unit,
    onOpenDossier: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "tape_spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("tape_header_card"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = NoirSurface),
        border = BorderStroke(1.dp, NoirSurfaceBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Cassette Branding & Tape Spinner
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(NoirSurfaceElevated)
                            .border(1.dp, NeonCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Album,
                            contentDescription = "Cassette reel",
                            tint = CassetteGold,
                            modifier = Modifier
                                .size(24.dp)
                                .rotate(rotation)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "ECHOES OF MIND",
                                color = NeonCyan,
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp,
                                letterSpacing = 1.2.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(NeonMagenta.copy(alpha = 0.2f))
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (isAiPowered) "AI FLASH" else "1984 NOIR",
                                    color = NeonMagenta,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Text(
                            text = "ROOM 204 // SIDE A // 01:15 AM",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // Header actions: Sound & Dossier button
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onToggleSound,
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("sound_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (soundEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeMute,
                            contentDescription = "Toggle sound",
                            tint = if (soundEnabled) NeonCyan else TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Button(
                        onClick = onOpenDossier,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NoirSurfaceElevated,
                            contentColor = PoliceTapeAmber
                        ),
                        border = BorderStroke(1.dp, PoliceTapeAmber.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("open_dossier_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = "Case notes",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "DOSSIER",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Turn Indicator & Suspicion Meter Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Turn Tracker Badge
                val isFinalTurn = currentTurn >= maxTurns
                val turnBadgeColor = if (isFinalTurn) AlertCrimson else NeonCyan
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(turnBadgeColor.copy(alpha = 0.15f))
                        .border(1.dp, turnBadgeColor, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isFinalTurn) "TURN 5/5: FINAL TURN" else "TURN $currentTurn OF $maxTurns",
                        color = turnBadgeColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Suspicion Level Monitor
                val suspicionColor = when {
                    suspicionLevel > 70 -> AlertCrimson
                    suspicionLevel > 40 -> PoliceTapeAmber
                    else -> TerminalGreen
                }
                val suspicionLabel = when {
                    suspicionLevel > 70 -> "DANGER"
                    suspicionLevel > 40 -> "ELEVATED"
                    else -> "QUIET"
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "Suspicion",
                        tint = suspicionColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "SUSPICION: $suspicionLevel% ($suspicionLabel)",
                        color = suspicionColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            // Progress Bar of turns
            LinearProgressIndicator(
                progress = { (currentTurn.toFloat() / maxTurns.toFloat()).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = if (currentTurn >= maxTurns) AlertCrimson else NeonCyan,
                trackColor = NoirSurfaceBorder
            )
        }
    }
}

@Composable
fun CrimeSceneBannerView(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("crime_scene_banner"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = NoirSurfaceElevated),
        border = BorderStroke(1.dp, NoirSurfaceBorder)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.crime_scene_banner),
                contentDescription = "The Neon Flamingo Motel Crime Scene",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )

            // Vignette gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                NoirBackground.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            // Crime scene tape watermark
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(PoliceTapeAmber)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "POLICE CAUTION • ROOM 204",
                    color = TextDark,
                    fontWeight = FontWeight.Black,
                    fontSize = 10.sp,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun NarrativeCard(
    turnNumber: Int,
    narrativeText: String,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("narrative_card"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = NoirSurface),
        border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(AlertCrimson)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "DISPATCH LOG // ENTRY #0$turnNumber",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                }

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = NeonMagenta,
                        strokeWidth = 2.dp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = narrativeText,
                color = PaperWhite,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.testTag("narrative_text")
            )
        }
    }
}

@Composable
fun InvestigationChoicesView(
    choices: List<String>,
    isLoading: Boolean,
    onSelectChoice: (String) -> Unit,
    onSubmitCustomAction: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var customQuery by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "INVESTIGATION OPTIONS (CHOOSE OR ASK):",
            color = PoliceTapeAmber,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
        )

        choices.forEachIndexed { index, choice ->
            Button(
                onClick = { onSelectChoice(choice) },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = NoirSurfaceElevated,
                    contentColor = PaperWhite
                ),
                border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.35f)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .testTag("choice_button_$index")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(NeonCyan.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${index + 1}",
                            color = NeonCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = choice,
                        fontSize = 13.sp,
                        color = PaperWhite,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Choose",
                        tint = NeonCyan,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Custom Detective Action / Freeform Input
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = NoirSurface),
            border = BorderStroke(1.dp, NoirSurfaceBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = customQuery,
                    onValueChange = { customQuery = it },
                    placeholder = {
                        Text(
                            text = "Ask question or interrogate...",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("custom_action_input"),
                    singleLine = true,
                    enabled = !isLoading,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = NoirSurfaceBorder,
                        focusedTextColor = PaperWhite,
                        unfocusedTextColor = PaperWhite
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (customQuery.isNotBlank()) {
                            onSubmitCustomAction(customQuery.trim())
                            customQuery = ""
                        }
                    },
                    enabled = !isLoading && customQuery.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonMagenta,
                        contentColor = TextDark
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("send_action_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send inquiry",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AccusationDialog(
    suspects: List<Suspect>,
    clues: List<ClueItem>,
    selectedSuspect: Suspect?,
    selectedEvidence: ClueItem?,
    onSelectSuspect: (Suspect) -> Unit,
    onSelectEvidence: (ClueItem) -> Unit,
    onConfirmAccusation: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("accusation_card"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NoirSurface),
        border = BorderStroke(2.dp, AlertCrimson)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(AlertCrimson)
                    .padding(vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "FINAL ACCUSATION • 5 TURNS COMPLETE",
                    color = PaperWhite,
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "The coroner has arrived. The precinct captain demands an arrest. Name Julian Thorne's murderer and present your key evidence.",
                color = PaperWhite,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "1. CHOOSE THE SUSPECT:",
                color = PoliceTapeAmber,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            suspects.forEach { suspect ->
                val isSelected = selectedSuspect?.id == suspect.id
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onSelectSuspect(suspect) }
                        .testTag("suspect_${suspect.id}"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) NeonMagenta.copy(alpha = 0.2f) else NoirSurfaceElevated
                    ),
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) NeonMagenta else NoirSurfaceBorder
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = suspect.name,
                            tint = if (isSelected) NeonMagenta else TextMuted,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${suspect.name} (\"${suspect.alias}\")",
                                color = if (isSelected) NeonMagenta else PaperWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = suspect.role,
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Selected",
                                tint = NeonMagenta,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "2. PRESENT INCRIMINATING EVIDENCE:",
                color = PoliceTapeAmber,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            if (clues.isEmpty()) {
                Text(
                    text = "No distinct clues discovered.",
                    color = TextMuted,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            } else {
                clues.forEach { clue ->
                    val isSelected = selectedEvidence?.id == clue.id
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .clickable { onSelectEvidence(clue) }
                            .testTag("evidence_${clue.id}"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) NeonCyan.copy(alpha = 0.2f) else NoirSurfaceElevated
                        ),
                        border = BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) NeonCyan else NoirSurfaceBorder
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fingerprint,
                                contentDescription = clue.title,
                                tint = if (isSelected) NeonCyan else TextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = clue.title,
                                    color = if (isSelected) NeonCyan else PaperWhite,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = clue.description,
                                    color = TextMuted,
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onConfirmAccusation,
                enabled = selectedSuspect != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AlertCrimson,
                    contentColor = PaperWhite
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("accuse_button")
            ) {
                Text(
                    text = if (selectedSuspect != null) "CHARGE ${selectedSuspect.name.uppercase()}" else "SELECT A SUSPECT TO CHARGE",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun VerdictView(
    verdict: CaseVerdict,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("verdict_card"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NoirSurface),
        border = BorderStroke(
            2.dp,
            if (verdict.wasCorrect) TerminalGreen else AlertCrimson
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (verdict.wasCorrect) TerminalGreen else AlertCrimson)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (verdict.wasCorrect) "CASE CLOSED • PERPETRATOR CONVICTED" else "CASE COLD • REAL KILLER AT LARGE",
                    color = TextDark,
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "DETECTIVE RANK: ${verdict.detectiveRank}",
                color = if (verdict.wasCorrect) NeonCyan else AlertCrimson,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NoirSurfaceElevated),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "EPILOGUE:",
                        color = PoliceTapeAmber,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = verdict.epilogueNarrative,
                        color = PaperWhite,
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        fontFamily = FontFamily.Serif
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = NoirSurfaceBorder)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "MOTIVE REVEALED: ${verdict.motiveRevealed}",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "FINAL SUSPICION: ${verdict.finalSuspicion}% • CLUES LOGGED: ${verdict.cluesFoundCount}",
                        color = TerminalGreen,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = onRestart,
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonCyan,
                    contentColor = TextDark
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("restart_button")
            ) {
                Text(
                    text = "BEGIN NEW INVESTIGATION",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun CaseDossierDialog(
    clues: List<ClueItem>,
    suspects: List<Suspect>,
    turns: List<GameTurn>,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Clues, 1: Suspects, 2: Tape Log

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("dossier_dialog"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = NoirSurface),
            border = BorderStroke(1.dp, PoliceTapeAmber)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = "Dossier",
                            tint = PoliceTapeAmber
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "CASE FILE #1984-204",
                            color = PoliceTapeAmber,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            letterSpacing = 1.sp
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Tabs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val tabLabels = listOf("EVIDENCE (${clues.size})", "SUSPECTS (3)", "TAPE LOG")
                    tabLabels.forEachIndexed { index, title ->
                        val isSelected = selectedTab == index
                        Button(
                            onClick = { selectedTab = index },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) NeonCyan else NoirSurfaceElevated,
                                contentColor = if (isSelected) TextDark else TextMuted
                            ),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = title,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Content
                when (selectedTab) {
                    0 -> {
                        // Evidence list
                        if (clues.isEmpty()) {
                            Text("No evidence gathered yet.", color = TextMuted, fontSize = 12.sp)
                        } else {
                            androidx.compose.foundation.lazy.LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                            ) {
                                items(clues.size) { i ->
                                    val clue = clues[i]
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        colors = CardDefaults.cardColors(containerColor = NoirSurfaceElevated)
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.Fingerprint,
                                                    contentDescription = clue.category.name,
                                                    tint = NeonCyan,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = clue.title,
                                                    color = NeonCyan,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 12.sp
                                                )
                                                Spacer(modifier = Modifier.weight(1f))
                                                Text(
                                                    text = "T0${clue.turnDiscovered}",
                                                    color = TextMuted,
                                                    fontSize = 10.sp,
                                                    fontFamily = FontFamily.Monospace
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = clue.description,
                                                color = PaperWhite,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    1 -> {
                        // Suspects list
                        androidx.compose.foundation.lazy.LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) {
                            items(suspects.size) { i ->
                                val s = suspects[i]
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    colors = CardDefaults.cardColors(containerColor = NoirSurfaceElevated)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = "${s.name} (\"${s.alias}\")",
                                            color = NeonMagenta,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                        Text(text = s.role, color = TextMuted, fontSize = 11.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Motive: ${s.motive}",
                                            color = PaperWhite,
                                            fontSize = 11.sp
                                        )
                                        Text(
                                            text = "Alibi: ${s.alibi}",
                                            color = PoliceTapeAmber,
                                            fontSize = 11.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = s.quote,
                                            color = TextMuted,
                                            fontStyle = FontStyle.Italic,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    2 -> {
                        // Tape log / Turns
                        androidx.compose.foundation.lazy.LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) {
                            items(turns.size) { i ->
                                val turn = turns[i]
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    colors = CardDefaults.cardColors(containerColor = NoirSurfaceElevated)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = "TURN ${turn.turnNumber} (Suspicion: ${turn.suspicionLevel}%)",
                                            color = NeonCyan,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                        if (turn.playerAction != null) {
                                            Text(
                                                text = "> Action: ${turn.playerAction}",
                                                color = PoliceTapeAmber,
                                                fontSize = 11.sp
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = turn.narrativeText,
                                            color = PaperWhite,
                                            fontSize = 11.sp,
                                            fontFamily = FontFamily.Serif
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
