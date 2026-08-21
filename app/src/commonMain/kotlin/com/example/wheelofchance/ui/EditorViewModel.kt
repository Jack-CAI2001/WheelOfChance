package com.example.wheelofchance.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wheelofchance.data.WheelRepository
import com.example.wheelofchance.data.local.Entry
import com.example.wheelofchance.data.local.Wheel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditorViewModel(
    private val repository: WheelRepository,
    private val wheelId: Long
) : ViewModel() {

    private val _wheel = MutableStateFlow<Wheel?>(null)
    val wheel: StateFlow<Wheel?> = _wheel.asStateFlow()

    val entries: StateFlow<List<Entry>> = if (wheelId != -1L) {
        repository.getEntriesForWheel(wheelId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    } else {
        MutableStateFlow(emptyList())
    }

    init {
        if (wheelId != -1L) {
            viewModelScope.launch {
                repository.getWheelById(wheelId).collect {
                    _wheel.value = it
                }
            }
        }
    }

    fun updateWheelName(newName: String) {
        val currentWheel = _wheel.value ?: return
        viewModelScope.launch {
            repository.updateWheel(currentWheel.copy(name = newName))
        }
    }

    fun addEntry(text: String, color: String) {
        if (wheelId == -1L) return
        viewModelScope.launch {
            repository.insertEntry(Entry(wheelId = wheelId, text = text, color = color))
        }
    }

    fun updateEntry(entry: Entry) {
        viewModelScope.launch {
            repository.updateEntry(entry)
        }
    }

    fun deleteEntry(entry: Entry) {
        viewModelScope.launch {
            repository.deleteEntry(entry)
        }
    }
}
