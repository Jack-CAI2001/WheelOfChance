package com.example.wheelofchance.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wheelofchance.data.WheelRepository
import com.example.wheelofchance.data.local.Wheel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WheelViewModel(private val repository: WheelRepository) : ViewModel() {

    val allWheels: StateFlow<List<Wheel>> = repository.getAllWheels()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addWheel(name: String) {
        viewModelScope.launch {
            repository.insertWheel(Wheel(name = name))
        }
    }

    fun deleteWheel(wheel: Wheel) {
        viewModelScope.launch {
            repository.deleteWheel(wheel)
        }
    }
}
