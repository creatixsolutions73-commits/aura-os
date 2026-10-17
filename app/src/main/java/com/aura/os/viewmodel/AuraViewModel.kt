package com.aura.os.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aura.os.commands.CommandInterpreter
import com.aura.os.device.DeviceControlManager
import com.aura.os.voice.TextToSpeechManager
import com.aura.os.voice.VoiceRecognitionManager
import com.aura.os.voice.VoiceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuraUiState(
    val listening: Boolean = false,
    val heard: String = "",
    val status: String = "Tap the orb to speak",
    val log: List<String> = emptyList()
)

class AuraViewModel(app: Application) : AndroidViewModel(app) {
    private val voice = VoiceRecognitionManager(app)
    private val tts = TextToSpeechManager(app)
    private val device = DeviceControlManager(app)
    private val interpreter = CommandInterpreter()

    private val _ui = MutableStateFlow(AuraUiState())
    val ui: StateFlow<AuraUiState> = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            voice.state.collect { s ->
                when (s) {
                    VoiceState.Idle -> _ui.update { it.copy(listening = false) }
                    VoiceState.Listening -> _ui.update { it.copy(listening = true, status = "I'm listening", heard = "") }
                    is VoiceState.Partial -> _ui.update { it.copy(heard = s.text) }
                    is VoiceState.Result -> handle(s.text)
                    is VoiceState.Error -> {
                        _ui.update { it.copy(listening = false, status = s.message) }
                        tts.speak(s.message)
                    }
                }
            }
        }
    }

    fun onMicPermissionDenied() {
        _ui.update { it.copy(status = "Microphone permission is required to listen.") }
    }

    fun toggleListening() {
        if (_ui.value.listening) { voice.stop(); _ui.update { it.copy(status = "Stopped listening") } }
        else { tts.stop(); voice.start() }
    }

    private fun handle(text: String) {
        val result = device.execute(interpreter.interpret(text))
        _ui.update {
            it.copy(
                listening = false,
                heard = text,
                status = result.message,
                log = (it.log + "You: $text" + "AURA: ${result.message}").takeLast(40)
            )
        }
        tts.speak(result.message)
    }

    override fun onCleared() {
        voice.stop()
        tts.shutdown()
    }
}
