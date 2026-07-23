package com.web.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.web.data.SYSTEM_PROMPT_KEY
import com.web.data.Storage
import com.web.data.defaultSystemPrompt
import com.web.data.defaultUserQuery
import com.web.repo.AIPromptManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UIViewModel : ViewModel() {

    companion object {
        val factory = viewModelFactory {
            initializer {
                UIViewModel()
            }
        }
    }

    private val scope: CoroutineScope = viewModelScope

    private val promptManager = AIPromptManager()

    private var sharedMessages = MutableStateFlow(ResultsUIState())
    val messagesState = sharedMessages.asSharedFlow()

    private var sharedPromptState = MutableStateFlow(PromptUIState())
    val promptState = sharedPromptState.asSharedFlow()

    private var sharedSettingsState = MutableStateFlow(
        SettingsUIState(systemPrompt = defaultSystemPrompt)
    )
    val settingsState = sharedSettingsState.asSharedFlow()

    init {
        scope.launch {

            val storage = Storage()
            val systemPrompt = storage.load(SYSTEM_PROMPT_KEY) ?: defaultSystemPrompt
            sharedSettingsState.emit(SettingsUIState(systemPrompt = systemPrompt))

            sharedPromptState.emit(PromptUIState(defaultUserQuery))
        }
    }

    override fun onCleared() {
        scope.launch {
            promptManager.onClose()
        }
    }

    fun onTextEnter(onNavToNextScreen: () -> Unit) {
        scope.launch {
            withContext(Dispatchers.Default) {

                onProgress(true)

                val systemPrompt = sharedSettingsState.value.systemPrompt
                val query = sharedPromptState.value.userQuery
                val companies = promptManager.sendRequest(query, systemPrompt)

                sharedMessages.emit(
                    ResultsUIState(
                        companies = companies,
                        hasError = companies.isEmpty()
                    )
                )

                onProgress(false)

                withContext(Dispatchers.Main) {
                    onNavToNextScreen()
                }

            }
        }
    }

    fun onTextChanged(newQuery: String) {
        scope.launch {
            val newState = sharedPromptState.value.copy(userQuery = newQuery)
            sharedPromptState.emit(newState)
        }
    }

    fun onSystemPromptChanged(newText: String) {
        scope.launch {

            val storage = Storage()
            storage.store(SYSTEM_PROMPT_KEY, newText)

            sharedSettingsState.emit(
                SettingsUIState(systemPrompt = newText)
            )
        }
    }

    private fun onProgress(isLoading: Boolean) {
        scope.launch {
            val newState = sharedPromptState.value.copy(isLoading = isLoading)
            sharedPromptState.emit(newState)
        }
    }

}