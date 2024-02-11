package com.lora.skylink.ui.chat


import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.lora.skylink.R
import com.lora.skylink.bluetooth.ConnectionEventListener
import com.lora.skylink.bluetooth.ConnectionManager
import com.lora.skylink.common.loge
import com.lora.skylink.databinding.FragmentChatBinding
import com.lora.skylink.ui.common.PermissionsRequester
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.fragment_chat) {

    private val args: ChatFragmentArgs by navArgs()
    var permissionsRequester = PermissionsRequester(this)

    private val viewModel: ChatViewModel by viewModels() //dev ex says
    private lateinit var  chatState: ChatState

    private var bluetoothDevice: BluetoothDevice? = null
    @Inject
    lateinit var bluetoothArduinoLoraAdapter: BluetoothAdapter

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bluetoothDevice = args.scanResult
        val bluetoothDeviceName = bluetoothDevice!!.name
        loge("ChatFragment, CONNECTED to: $bluetoothDeviceName")
        Toast.makeText(context, "Connected with: $bluetoothDeviceName", Toast.LENGTH_LONG).show()
        chatState = buildChatState()
        val binding = FragmentChatBinding.bind(view)

        binding.btnSend.setOnClickListener{
            //TODO
            loge("SEND Pressed")
        }
    }

    private val connectionEventListener by lazy {
        ConnectionEventListener().apply {
            onDisconnect = {
                GlobalScope.launch(Dispatchers.Main) {
                    loge("...DISCONNECTED from Bluetooth LoRa Arduino")
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                    Toast.makeText(context, "The Arduino disconnected", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        loge("ChatFragment  .onResume")
        ConnectionManager.registerListener(connectionEventListener)
        bluetoothDevice?.let { ConnectionManager.connect(it,context) }

        if(!(permissionsRequester.checkAllPermissions() && isBluetoothAdapterON())) {
            loge("ScanFragment - SOME PERMISSIONS WERE MISSING, Returning to PermissionsFragment")
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    fun isBluetoothAdapterON():Boolean {
        bluetoothArduinoLoraAdapter.let { adapter ->
            if (adapter.isEnabled) {
                return true
            }
        }
        return false
    }

    override fun onPause() {
        super.onPause()
        loge("ChatFragment  .onPause")
    }
    override fun onStop() {
        super.onStop()
        loge("ChatFragment  .onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        loge("ChatFragment  .onDestroy")
        ConnectionManager.unregisterListener(connectionEventListener)
    }
}