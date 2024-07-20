package com.dedan.mantramsesontengan.ui.navigation

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GlobalViewModel : ViewModel() {
    private val _audioPlayerUiState = MutableStateFlow<AudioPlayerUiState>(AudioPlayerUiState.Blank)
    val audioPlayerUiState = _audioPlayerUiState.asStateFlow()

    private var _audioPlayer: MediaPlayer? = null
    private var previousAudioUrl: String? = null

    private val audioScopeJob = SupervisorJob()
    private val audioScope = CoroutineScope(Dispatchers.Main + audioScopeJob)

    fun prepareAudio(audioUrl: String, force: Boolean = false) {
        viewModelScope.launch {
            if (
                audioUrl == previousAudioUrl
                && !force
                && (_audioPlayerUiState.value is AudioPlayerUiState.Loading
                        || _audioPlayerUiState.value is AudioPlayerUiState.Paused
                        || _audioPlayerUiState.value is AudioPlayerUiState.Playing)
            ) {
                return@launch
            }

            _audioPlayerUiState.value = AudioPlayerUiState.Loading(0f)
            previousAudioUrl = audioUrl
            _audioPlayer?.release()

            MediaPlayer().apply {
                try {
                    setDataSource("https://mantram.suryamahendra.com/storage/" + audioUrl)
                    setOnBufferingUpdateListener { _, percent ->
                        _audioPlayerUiState.value =
                            if (percent == 100)
                                AudioPlayerUiState.Paused
                            else
                                AudioPlayerUiState.Loading(percent / 100f)
                    }
                    setOnCompletionListener { restartAudio() }
                    setOnPreparedListener {
                        previousAudioUrl = audioUrl
                        _audioPlayer = it
                    }
                    Log.d("GlobalViewModel", "Preparing audio")
                    prepare()
                    Log.d("GlobalViewModel", "Prepared audio")
                } catch (e: Exception) {
                    _audioPlayerUiState.value = AudioPlayerUiState.Error
                }
            }
        }
    }

    fun playAudio() {
        viewModelScope.launch {
            _audioPlayer?.let {
                it.start()

                _audioPlayerUiState.value = AudioPlayerUiState.Playing(0f)

                audioScope.launch {
                    while(_audioPlayer != null) {
                        if (_audioPlayerUiState.value is AudioPlayerUiState.Playing) {
                            _audioPlayerUiState.value = AudioPlayerUiState.Playing(
                                _audioPlayer!!.currentPosition.toFloat() / _audioPlayer!!.duration.toFloat()
                            )
                        }

                        delay(1000)
                    }
                }
            }
        }
    }

    fun stopAudio() {
        viewModelScope.launch {
            _audioPlayer?.let {
                it.pause()
                _audioPlayerUiState.value = AudioPlayerUiState.Paused
            }
        }
    }

    fun restartAudio() {
        viewModelScope.launch {
            _audioPlayer?.let {
                it.pause()
                it.seekTo(0)
                _audioPlayerUiState.value = AudioPlayerUiState.Paused
            }
        }
    }

    fun clearAudio() {
        viewModelScope.launch {
            _audioPlayer?.let {
                it.release()
                _audioPlayerUiState.value = AudioPlayerUiState.Blank
            }

            _audioPlayer = null
        }
    }
}

sealed interface AudioPlayerUiState {
    data class Loading(val progress: Float) : AudioPlayerUiState
    data class Playing(val progress: Float) : AudioPlayerUiState
    object Paused : AudioPlayerUiState
    object Blank : AudioPlayerUiState
    object Error : AudioPlayerUiState
}