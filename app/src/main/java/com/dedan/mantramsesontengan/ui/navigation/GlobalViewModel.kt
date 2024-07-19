package com.dedan.mantramsesontengan.ui.navigation

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GlobalViewModel : ViewModel() {
    private val _audioPlayerUiState = MutableStateFlow<AudioPlayerUiState>(AudioPlayerUiState.Blank)
    val audioPlayerUiState = _audioPlayerUiState.asStateFlow()

    private var _audioPlayer: MediaPlayer? = null
    private var previousAudioUrl: String? = null

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

            _audioPlayer?.release()
            _audioPlayerUiState.value = AudioPlayerUiState.Loading(0)

            MediaPlayer().apply {
                try {
                    setDataSource("https://mantram.suryamahendra.com/storage/" + audioUrl)
                    setOnBufferingUpdateListener { _, percent ->
                        _audioPlayerUiState.value =
                            if (percent == 100)
                                AudioPlayerUiState.Paused
                            else
                                AudioPlayerUiState.Loading(percent)
                    }
                    setOnCompletionListener {
                        restartAudio()
                    }
                    setOnPreparedListener {
                        previousAudioUrl = audioUrl
                        _audioPlayer = it

                    }
                    prepare()
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
                _audioPlayerUiState.value = AudioPlayerUiState.Playing
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
    data class Loading(val progress: Int) : AudioPlayerUiState
    object Playing : AudioPlayerUiState
    object Paused : AudioPlayerUiState
    object Blank : AudioPlayerUiState
    object Error : AudioPlayerUiState
}