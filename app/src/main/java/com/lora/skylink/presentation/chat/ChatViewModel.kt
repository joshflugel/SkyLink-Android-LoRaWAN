package com.lora.skylink.presentation.chat

import androidx.lifecycle.ViewModel
import com.lora.skylink.domain.usecases.ManageBluetoothDeviceConnectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val manageBluetoothDeviceConnectionUseCase: ManageBluetoothDeviceConnectionUseCase
): ViewModel() {

    // TODO: Implement the ViewModel

}