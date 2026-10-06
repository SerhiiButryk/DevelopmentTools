package com.web.data

import aiagent.shared.generated.resources.Res
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.concurrent.Volatile

object PromptData {

    @Volatile
    var defaultSystemPrompt: String = ""
    @Volatile
    var defaultUserQuery: String = ""

    suspend fun load() {
        val path = "files/"
        defaultSystemPrompt = Res.readBytes("${path}system_prompt.txt").decodeToString()
        defaultUserQuery = Res.readBytes("${path}user_prompt.txt").decodeToString()
    }

}