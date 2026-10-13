package com.example.data

import com.example.BuildConfig
import com.example.model.ClueCategory
import com.example.model.ClueItem
import com.example.model.GameTurn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiGameEngine {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    fun isApiKeyAvailable(): Boolean {
        return try {
            val key = BuildConfig.GEMINI_API_KEY
            key.isNotBlank() && key != "MY_GEMINI_API_KEY"
        } catch (_: Exception) {
            false
        }
    }

    suspend fun generateNextTurn(
        currentTurn: Int,
        playerAction: String,
        currentSuspicion: Int,
        history: List<GameTurn>,
        clues: List<ClueItem>
    ): GameTurn = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY

        val historySummary = buildString {
            history.takeLast(4).forEach { turn ->
                append("Turn ${turn.turnNumber}: ")
                if (turn.playerAction != null) append("Player did: '${turn.playerAction}'. ")
                append("Story: ${turn.narrativeText} (Suspicion: ${turn.suspicionLevel})\n")
            }
        }

        val cluesSummary = clues.joinToString("; ") { "${it.title}: ${it.description}" }

        val systemPrompt = """
            You are the Game Engine for an interactive detective game called "Echoes of Mind".
            
            STORY PREMISE:
            Year 1984. Julian Thorne, audio synthesizer engineer, was murdered in Room 204 of The Neon Flamingo Motel. The Project Chimera audio cassette was stolen.
            Suspects:
            1. Vincent Vane: Record producer Julian threatened to expose for embezzlement.
            2. Roxanne Sterling: Velvet Viper singer who had late-night rendezvous with Julian.
            3. Detective Marcus Miller: Corrupt vice detective whose payoffs Julian recorded.
            
            RULES:
            1. Keep the atmosphere heavy 1980s retro synth noir (rain, neon, cassette tapes, smoke).
            2. Current turn is $currentTurn of 5.
            3. Advance the story based on the player's choice or question: "$playerAction".
            4. Keep track of a hidden "Suspicion Level" (1 to 100). Current suspicion is $currentSuspicion.
               - Calculating suspicion: Subtle detective work lowers or maintains it (-5 to +5); aggressive threats, gun-brandishing, or loud accusations raise it (+10 to +25).
            5. Provide 3 distinct clues or choices for the next turn.
            6. On turn 5, make it clear that turn 5 is the final clue-gathering turn before forced accusation.
            7. Response narrative MUST be under 150 words, rich in atmosphere, and end with the EXACT question: "What do you do next?"
            
            Respond ONLY with a JSON object matching this schema:
            {
              "narrative": "atmospheric narrative under 150 words ending with 'What do you do next?'",
              "suspicionDelta": 5,
              "newSuspicion": 35,
              "choices": ["Choice 1", "Choice 2", "Choice 3"],
              "clueTitle": "Optional title if new clue found or empty string",
              "clueDescription": "Optional short clue detail or empty string"
            }
        """.trimIndent()

        val userPrompt = """
            History:
            $historySummary
            Known Clues:
            $cluesSummary
            
            Player Action: "$playerAction"
            Next Turn: ${currentTurn + 1}
        """.trimIndent()

        val jsonPayload = JSONObject().apply {
            val contentsArray = JSONArray()
            val contentObj = JSONObject()
            val partsArray = JSONArray()
            partsArray.put(JSONObject().put("text", userPrompt))
            contentObj.put("parts", partsArray)
            contentsArray.put(contentObj)
            put("contents", contentsArray)

            val sysInstructionObj = JSONObject()
            val sysParts = JSONArray()
            sysParts.put(JSONObject().put("text", systemPrompt))
            sysInstructionObj.put("parts", sysParts)
            put("systemInstruction", sysInstructionObj)

            val genConfig = JSONObject()
            genConfig.put("temperature", 0.7)
            genConfig.put("responseMimeType", "application/json")
            put("generationConfig", genConfig)
        }

        val requestBody = jsonPayload.toString().toRequestBody("application/json".toMediaType())
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val response = okHttpClient.newCall(request).execute()
        val responseBodyString = response.body?.string() ?: throw RuntimeException("Empty response from Gemini")

        val rootJson = JSONObject(responseBodyString)
        val candidates = rootJson.optJSONArray("candidates")
        val firstCandidate = candidates?.optJSONObject(0)
        val content = firstCandidate?.optJSONObject("content")
        val parts = content?.optJSONArray("parts")
        val text = parts?.optJSONObject(0)?.optString("text") ?: throw RuntimeException("No text in candidate")

        val parsedData = JSONObject(text.trim().removePrefix("```json").removeSuffix("```").trim())
        var narrative = parsedData.optString("narrative", "")
        if (!narrative.endsWith("What do you do next?")) {
            narrative = if (narrative.endsWith("?")) {
                narrative.substringBeforeLast(".") + ". What do you do next?"
            } else {
                "$narrative What do you do next?"
            }
        }

        val suspicionDelta = parsedData.optInt("suspicionDelta", 5)
        val newSuspicion = parsedData.optInt("newSuspicion", (currentSuspicion + suspicionDelta).coerceIn(1, 100))

        val choicesJson = parsedData.optJSONArray("choices")
        val choices = mutableListOf<String>()
        if (choicesJson != null) {
            for (i in 0 until choicesJson.length()) {
                choices.add(choicesJson.getString(i))
            }
        }
        if (choices.isEmpty()) {
            choices.addAll(listOf("Inspect the scene", "Question suspects", "Review forensic notes"))
        }

        val clueTitle = parsedData.optString("clueTitle", "")
        val clueDesc = parsedData.optString("clueDescription", "")
        val newClue = if (clueTitle.isNotBlank()) {
            ClueItem(
                id = "ai_clue_${System.currentTimeMillis()}",
                title = clueTitle,
                description = clueDesc.ifBlank { "Discovered during investigation." },
                category = ClueCategory.PHYSICAL,
                turnDiscovered = currentTurn + 1
            )
        } else null

        GameTurn(
            turnNumber = currentTurn + 1,
            narrativeText = narrative,
            playerAction = playerAction,
            suspicionDelta = suspicionDelta,
            suspicionLevel = newSuspicion.coerceIn(1, 100),
            choices = choices.take(3),
            discoveredClue = newClue
        )
    }
}
