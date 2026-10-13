package com.example.data

import com.example.model.CaseVerdict
import com.example.model.ClueCategory
import com.example.model.ClueItem
import com.example.model.GameTurn
import com.example.model.Suspect

object OfflineGameEngine {

    val INITIAL_SUSPECTS = listOf(
        Suspect(
            id = "vincent",
            name = "Vincent Vane",
            alias = "The Maestro",
            role = "SynthWave Records Executive",
            motive = "Owed Julian $80,000 in royalties; Julian was taking Project Chimera to a rival label tomorrow.",
            alibi = "Claims he was dining at Chez Maxim with his investors until 2 AM.",
            quote = "\"Julian was brilliant, Detective, but unstable. Don't look at me—I made that kid a star.\"",
            isGuilty = true
        ),
        Suspect(
            id = "roxanne",
            name = "Roxanne Sterling",
            alias = "The Siren",
            role = "Velvet Viper Lounge Vocalist",
            motive = "Julian held unreleased master tapes containing her voice that could ruin her solo career.",
            alibi = "Singing midnight jazz set at Velvet Viper; bartender corroborates her till 1:30 AM.",
            quote = "\"Julian used people like cassette tapes—recorded over them when bored. But I wouldn't kill him.\"",
            isGuilty = false
        ),
        Suspect(
            id = "miller",
            name = "Det. Marcus Miller",
            alias = "Mad Dog",
            role = "Disgraced 4th Precinct Detective",
            motive = "Julian had photographic blackmail of Miller taking kickbacks from waterfront loan sharks.",
            alibi = "Claims he responded to an anonymous 911 dispatcher call at 1:10 AM.",
            quote = "\"Back off my crime scene, flatfoot. Julian got what was coming to him in the night city.\"",
            isGuilty = false
        )
    )

    fun getInitialTurn(): GameTurn {
        val narrative = "Rain hammers the cracked neon of Room 204 at The Neon Flamingo Motel. Julian Thorne lies slouched over an analog synthesizer, synthetic blood pooling beneath reel-to-reel tape. The digital clock blinks 1:15 AM. Cold fog drifts through the open fire escape. A vinyl record skips endlessly in the dark. What do you do next?"
        return GameTurn(
            turnNumber = 1,
            narrativeText = narrative,
            playerAction = null,
            suspicionDelta = 0,
            suspicionLevel = 20,
            choices = listOf(
                "Inspect the smashed cassette deck and torn audio tape reel.",
                "Search Julian's trenchcoat for hotel receipts and notes.",
                "Follow the wet muddy boot prints leading down the fire escape."
            ),
            discoveredClue = ClueItem(
                id = "scene_crime",
                title = "Room 204 Crime Scene",
                description = "Julian Thorne found dead at his synthesizer console. Master tape stolen.",
                category = ClueCategory.PHYSICAL,
                turnDiscovered = 1
            )
        )
    }

    fun processTurn(
        currentTurn: Int,
        playerAction: String,
        currentSuspicion: Int,
        existingClues: List<ClueItem>
    ): GameTurn {
        val nextTurnNumber = currentTurn + 1
        val actionLower = playerAction.lowercase()

        val responseNarrative: String
        val delta: Int
        var newClue: ClueItem? = null
        val nextChoices: List<String>

        when (currentTurn) {
            1 -> {
                if (actionLower.contains("cassette") || actionLower.contains("deck") || actionLower.contains("tape")) {
                    delta = 5
                    newClue = ClueItem(
                        id = "chimera_tape",
                        title = "Fragment of Chimera Tape",
                        description = "Magnetic audio strip caught in the capstan wheel with handwritten note: 'Vincent: Royalties or Prison'.",
                        category = ClueCategory.AUDIO_TAPE,
                        turnDiscovered = 2
                    )
                    responseNarrative = "You pry open the cassette player. A sheared ribbon of magnetic tape is tangled in the gears. Written on the plastic casing in red marker: 'Vincent: Royalties or Prison'. An acrid whiff of cheap cigar smoke hangs in the curtains. What do you do next?"
                    nextChoices = listOf(
                        "Confront Vincent Vane about the unpaid royalties note.",
                        "Interrogate the motel manager about who smokes cheap cigars.",
                        "Dust the telephone handset on the nightstand for fingerprints."
                    )
                } else if (actionLower.contains("trenchcoat") || actionLower.contains("receipt") || actionLower.contains("pocket") || actionLower.contains("notes")) {
                    delta = -5
                    newClue = ClueItem(
                        id = "viper_matchbook",
                        title = "Velvet Viper Matchbook",
                        description = "Silver matchbook with crimson lipstick smudge and an inscription: 'Room 204 tonight. Don't be late - R.S.'",
                        category = ClueCategory.PHYSICAL,
                        turnDiscovered = 2
                    )
                    responseNarrative = "Julian's coat pocket yields a soggy matchbook from the Velvet Viper Lounge. Inside, written in scarlet lipstick: 'Room 204 tonight. Don't be late - R.S.' A receipt shows two champagne flutes billed at midnight. What do you do next?"
                    nextChoices = listOf(
                        "Track down singer Roxanne Sterling at the Velvet Viper bar.",
                        "Check the motel register for visitors matching Roxanne's description.",
                        "Inspect Julian's synthesizer memory cartridge for saved recordings."
                    )
                } else {
                    delta = 10
                    newClue = ClueItem(
                        id = "boot_prints",
                        title = "Muddy Size 11 Boot Cast",
                        description = "Heavy tread police-issue combat boots leaving tracks down the metal fire escape toward the alley.",
                        category = ClueCategory.FORENSIC,
                        turnDiscovered = 2
                    )
                    responseNarrative = "You lean out into the rain. Muddy boot treads score the iron fire escape: size 11, heavy-lug police issue. In the alley below, tire marks from an unmarked squad car streak through oil-slicked puddles. What do you do next?"
                    nextChoices = listOf(
                        "Radio the precinct to look up Detective Miller's patrol log.",
                        "Search the dumpster in the alley where the tire tracks sped away.",
                        "Return inside and check Julian's answering machine tape."
                    )
                }
            }

            2 -> {
                if (actionLower.contains("vincent") || actionLower.contains("royalties") || actionLower.contains("producer")) {
                    delta = 15
                    newClue = ClueItem(
                        id = "vane_contract",
                        title = "Altered Recording Contract",
                        description = "Contract signed with Julian granting 100% of Project Chimera audio patents to Vincent Vane upon Julian's demise.",
                        category = ClueCategory.PHYSICAL,
                        turnDiscovered = 3
                    )
                    responseNarrative = "Vincent Vane glares under the lobby neon, smoothing his silk lapels. 'Julian was a blackmailer! He forged that tape!' But his hand trembles as he pockets a bloody handkerchief. His limousine chauffeur looks on with nervous dread. What do you do next?"
                    nextChoices = listOf(
                        "Demand to inspect Vincent's locked briefcase in the limousine.",
                        "Subpoena Julian's bank records to confirm the extortion payoffs.",
                        "Interrogate Detective Miller who suddenly arrives on the scene."
                    )
                } else if (actionLower.contains("roxanne") || actionLower.contains("viper") || actionLower.contains("lipstick") || actionLower.contains("singer")) {
                    delta = -5
                    newClue = ClueItem(
                        id = "roxanne_statement",
                        title = "Roxanne's Tearful Confession",
                        description = "Roxanne saw Julian argue with an executive in a grey trenchcoat at 12:45 AM before gunshots rang out.",
                        category = ClueCategory.TESTIMONY,
                        turnDiscovered = 3
                    )
                    responseNarrative = "Backstage at the Velvet Viper, Roxanne shivers under dressing room bulb light. 'I was there at midnight,' she whispers. 'Julian was frantic. He said Vincent hired a precinct muscle to steal the master reel before morning.' What do you do next?"
                    nextChoices = listOf(
                        "Ask Roxanne if she can identify the precinct muscle.",
                        "Search the motel manager's office for the exterior surveillance tape.",
                        "Examine the crime scene telephone for recent dialed numbers."
                    )
                } else {
                    delta = 12
                    newClue = ClueItem(
                        id = "miller_ledger",
                        title = "Julian's Secret Ledger",
                        description = "Page detailing monthly cash payoffs to Badge #402 (Detective Marcus Miller) for police protection.",
                        category = ClueCategory.PHYSICAL,
                        turnDiscovered = 3
                    )
                    responseNarrative = "A hidden compartment behind the synthesizer yields a ledger. Julian kept meticulous notes: Detective Miller received monthly kickbacks. Just then, Miller steps into the doorway, rain dripping from his fedora, hand resting on his service revolver. What do you do next?"
                    nextChoices = listOf(
                        "Confront Miller directly about his badge number in the ledger.",
                        "Play dumb and ask Miller to bag the audio console for forensics.",
                        "Slip the ledger into your coat and head to the precinct."
                    )
                }
            }

            3 -> {
                if (actionLower.contains("miller") || actionLower.contains("revolver") || actionLower.contains("police") || actionLower.contains("precinct")) {
                    delta = 20
                    newClue = ClueItem(
                        id = "miller_warning",
                        title = "Miller's Threat & Alibi Crack",
                        description = "Miller's boots are still caked with motel alley mud; he admits Vincent paid him $5,000 to make the tape disappear.",
                        category = ClueCategory.TESTIMONY,
                        turnDiscovered = 4
                    )
                    responseNarrative = "Miller's eyes narrow as neon flashes across his damp face. 'Look around, kid. Thorne was a dead man the second he threatened Vincent Vane. Vane called me to clean the room—I didn't pull the trigger.' Miller spits on the linoleum. What do you do next?"
                    nextChoices = listOf(
                        "Corner Vincent Vane with Miller's sworn admission.",
                        "Search Vincent Vane's limousine trunk for the murder weapon.",
                        "Find the missing Project Chimera master tape in the motel boiler room."
                    )
                } else if (actionLower.contains("briefcase") || actionLower.contains("limousine") || actionLower.contains("vane") || actionLower.contains("trunk")) {
                    delta = 10
                    newClue = ClueItem(
                        id = "chimera_master_tape",
                        title = "Project Chimera Master Reel",
                        description = "The stolen gold-cased master synthesizer tape, smeared with gunpowder residue and Vincent Vane's gold signet ring wax.",
                        category = ClueCategory.AUDIO_TAPE,
                        turnDiscovered = 4
                    )
                    responseNarrative = "Inside Vincent's limousine trunk, hidden behind champagne bottles, you discover the gold Project Chimera master cassette. Gunpowder residue clings to the casing. Vane's bodyguard tries to block you, but sirens echo in the wet street. What do you do next?"
                    nextChoices = listOf(
                        "Test the cassette casing for Vincent Vane's fingerprints.",
                        "Interrogate the chauffeur regarding the exact time Vincent left Room 204.",
                        "Examine the silencer-equipped pistol hidden in the tire well."
                    )
                } else {
                    delta = -2
                    newClue = ClueItem(
                        id = "answering_tape",
                        title = "Answering Machine Cassette",
                        description = "Recorded voicemail from 1:05 AM: Vincent's voice shouting 'You won't live to hear this album, Thorne!' followed by struggle.",
                        category = ClueCategory.AUDIO_TAPE,
                        turnDiscovered = 4
                    )
                    responseNarrative = "The microcassette answering machine on Julian's desk clicks and rewinds. At 1:05 AM, Vincent Vane's unmistakable voice roars: 'You won't live to see the dawn, Thorne!' followed by a scuffle and a muffled gunshot. What do you do next?"
                    nextChoices = listOf(
                        "Lock down the hotel exits before Vincent Vane flees the country.",
                        "Arrest Detective Miller as an accomplice at gunpoint.",
                        "Review every piece of evidence before leveling your final accusation."
                    )
                }
            }

            4 -> {
                // Turn 5: Final turn to gather clues before forced accusation!
                delta = 5
                newClue = ClueItem(
                    id = "ballistics_match",
                    title = "Ballistics & Forensic Report",
                    description = "9mm custom brass casing with bespoke engraving matching Vincent Vane's registered collector's firearm.",
                    category = ClueCategory.FORENSIC,
                    turnDiscovered = 5
                )
                responseNarrative = "Turn 5 of 5: The coroner uncovers a custom engraved 9mm bullet casing matching Vincent Vane's private collection. The rain subsides as morning mist rolls over the neon signs. All three suspects are now corralled under precinct searchlights. Time has run out, Detective. What do you do next?"
                nextChoices = listOf(
                    "Review the case ledger and prepare your formal accusation.",
                    "Question each suspect one last time under the spotlight.",
                    "Step forward to formally accuse Julian Thorne's murderer."
                )
            }

            else -> {
                // Accusation stage
                delta = 0
                responseNarrative = "The investigation clock has struck zero. The suspects Vincent Vane, Roxanne Sterling, and Detective Miller stand before you in the precinct courtyard. Who do you accuse of the murder?"
                nextChoices = listOf(
                    "Accuse Vincent Vane (Record Producer)",
                    "Accuse Roxanne Sterling (Lounge Singer)",
                    "Accuse Detective Marcus Miller (Precinct Cop)"
                )
            }
        }

        val updatedSuspicion = (currentSuspicion + delta).coerceIn(1, 100)

        return GameTurn(
            turnNumber = nextTurnNumber,
            narrativeText = responseNarrative,
            playerAction = playerAction,
            suspicionDelta = delta,
            suspicionLevel = updatedSuspicion,
            choices = nextChoices,
            discoveredClue = newClue
        )
    }

    fun evaluateAccusation(
        accusedSuspectId: String,
        presentedEvidence: ClueItem?,
        finalSuspicion: Int,
        cluesCount: Int
    ): CaseVerdict {
        val isCorrect = accusedSuspectId == "vincent"
        val rank = when {
            isCorrect && finalSuspicion < 45 -> "Master Sleuth (S-Rank)"
            isCorrect && finalSuspicion < 75 -> "Hardboiled Gumshoe (A-Rank)"
            isCorrect -> "Reckless Maverick (B-Rank)"
            else -> "Framed & Disgraced (Case Cold)"
        }

        val epilogue = if (isCorrect) {
            "Vincent Vane's composure shatters as you present the incriminating evidence under the precinct lights. Detective Miller surrenders his badge and turns state's witness, confessing that Vane hired him for the cover-up after shooting Thorne to steal the Project Chimera master tape. Roxanne Sterling sheds a quiet tear in the morning fog. Justice echoes through the neon streets of 1984."
        } else if (accusedSuspectId == "roxanne") {
            "You point your finger at Roxanne Sterling. Her attorney instantly produces the Velvet Viper timestamped stage contract proving she was on stage when the fatal shot was fired. While the court erupts into chaos, Vincent Vane quietly slips through the exit into a private jet bound for Zurich with the master tape. The case falls cold."
        } else {
            "You accuse Detective Marcus Miller. Miller laughs harshly as internal affairs clears his service weapon—it was never fired. Vincent Vane walks out a free man, having successfully used Miller as a decoy while retaining the multimillion-dollar Project Chimera audio patents. You are stripped of your detective badge."
        }

        return CaseVerdict(
            accusedSuspectId = accusedSuspectId,
            wasCorrect = isCorrect,
            motiveRevealed = if (isCorrect) "Greed and patent theft: Stealing the revolutionary Project Chimera neural synthesizer tape to avoid paying $80,000 and facing public ruin." else "Mistaken identity; the real killer escaped with the stolen master audio.",
            epilogueNarrative = epilogue,
            finalSuspicion = finalSuspicion,
            detectiveRank = rank,
            cluesFoundCount = cluesCount
        )
    }
}
