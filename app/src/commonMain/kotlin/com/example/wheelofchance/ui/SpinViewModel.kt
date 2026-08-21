package com.example.wheelofchance.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wheelofchance.data.WheelRepository
import com.example.wheelofchance.data.local.Entry
import com.example.wheelofchance.data.local.Wheel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SpinViewModel(
    private val repository: WheelRepository,
    private val wheelId: Long
) : ViewModel() {

    private val _wheel = MutableStateFlow<Wheel?>(null)
    val wheel: StateFlow<Wheel?> = _wheel.asStateFlow()

    private val _entries = MutableStateFlow<List<Entry>>(emptyList())
    val entries: StateFlow<List<Entry>> = _entries.asStateFlow()

    init {
        loadWheelData()
    }

    private fun loadWheelData() {
        viewModelScope.launch {
            repository.getWheelById(wheelId).collectLatest {
                _wheel.value = it
            }
        }
        viewModelScope.launch {
            repository.getEntriesForWheel(wheelId).collectLatest {
                _entries.value = it
            }
        }
    }
}
