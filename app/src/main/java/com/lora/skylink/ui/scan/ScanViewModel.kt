package com.lora.skylink.ui.scan


import android.bluetooth.le.ScanResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.lora.skylink.usecases.GetScannedDevicesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
  //  getScannedDevicesUseCase: GetScannedDevicesUseCase
): ViewModel() {


    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
       //     getScannedDevicesUseCase()
          //      .collect { discoveredDevices -> _state.update { UiState(discoveredDevicesList = discoveredDevices) }}
            /*
            getPopularMoviesUseCase()
                .catch { cause -> _state.update { it.copy(error = cause.toError()) } }
                .collect { movies -> _state.update { UiState(discoveredDevicesList = ) } }
             */
        }
    }

    data class UiState(
        val scanning: Boolean = false,
        val scanResults: List<ScanResult>? = null
    )
}