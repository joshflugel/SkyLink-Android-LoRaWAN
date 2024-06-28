package com.lora.skylink.presentation.chat

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.lora.skylink.R
import com.lora.skylink.bluetoothlegacy.CharacteristicProperty
import com.lora.skylink.bluetoothlegacy.ConnectionEventListener
import com.lora.skylink.bluetoothlegacy.ConnectionManager
import com.lora.skylink.bluetoothlegacy.byteArrayToAsciiString
import com.lora.skylink.bluetoothlegacy.isIndicatable
import com.lora.skylink.bluetoothlegacy.isNotifiable
import com.lora.skylink.bluetoothlegacy.isReadable
import com.lora.skylink.bluetoothlegacy.isWritable
import com.lora.skylink.bluetoothlegacy.isWritableWithoutResponse
import com.lora.skylink.common.DateUtil
import com.lora.skylink.common.logd
import com.lora.skylink.common.loge
import com.lora.skylink.common.logi
import com.lora.skylink.databinding.FragmentChatBinding
import com.lora.skylink.presentation.common.PermissionsRequesterLEGACY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.fragment_chat) {

    private val args: ChatFragmentArgs by navArgs()
    private var permissionsRequester = PermissionsRequesterLEGACY(this)

    private val viewModel: ChatViewModel by viewModels()
    private lateinit var  chatState: ChatState
    private lateinit var bluetoothDeviceName: String

    private lateinit var binding: FragmentChatBinding

    private var bluetoothDevice
    : BluetoothDevice? = null
    @Inject
    lateinit var bluetoothArduinoLoraAdapter: BluetoothAdapter

    private var isKeyboardShowing = false
    private var previousWindowBottomPosition = 0;

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loge("ChatFragment  .onViewCreated")

        bluetoothDevice = args.scanResult
        bluetoothDeviceName = bluetoothDevice!!.name
        loge("ChatFragment, CONNECTED to: $bluetoothDeviceName")
        Toast.makeText(context, "Connected with: $bluetoothDeviceName", Toast.LENGTH_LONG).show()

// ==== END BLUETOOTH
        // chatState = buildChatState()
       // binding = FragmentChatBinding.bind(view)

        //setupRecyclerView()


        setupCharacteristics()
        setupUI(view)
        setupFragmentAdjustmentAccordingToKeyboardVisibility()
        setupOnBackPressedCallback()

    }

    override fun onResume() {
        super.onResume()
        loge("ChatFragment  .onResume")
        scrollToNewestMessage()

        if(!(permissionsRequester.checkAllPermissions() && isBluetoothAdapterON())) {
            loge("ChatFragment - SOME PERMISSIONS WERE MISSING, Returning to PermissionsFragment")
            findNavController().popBackStack()
        }

        ConnectionManager.registerListener(connectionEventListener)
        //bluetoothDevice?.let { ConnectionManager.connect(it,context) }
    }
    override fun onDestroy() {
        ConnectionManager.unregisterListener(connectionEventListener)
        //bluetoothDevice?.let { ConnectionManager.teardownConnection(it) }
        super.onDestroy()
        loge("ChatFragment  .onDestroy")
    }

    private fun setupOnBackPressedCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                bluetoothDevice?.let {
                    //flugel ConnectionManager.teardownConnection(it)
                    ConnectionManager.disconnectFromDevice(it)
                } ?: run {
                    findNavController().popBackStack()
                }
                isEnabled = false
               // navigateToScanFragment()

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    fun setupCharacteristics() {
        characteristics.forEach { characteristic ->
            characteristicProperties[characteristic]?.let { properties ->
                properties.forEachIndexed { _, property ->
                    loge("UUIID" + characteristic.uuid.toString() + ", properties->" + characteristic.properties)
                    when (property) {
                        CharacteristicProperty.Readable -> {
                            loge("READABLE")
                        }
                        CharacteristicProperty.Writable, CharacteristicProperty.WritableWithoutResponse -> {
                            loge("WRITE_NO_RESPONSE")
                        }
                        CharacteristicProperty.Notifiable -> {
                            loge("NOTIFY and INDICATABLE")
                            ConnectionManager.enableNotifications(bluetoothDevice!!, characteristic)
                            bluetoothCharacteristic = characteristic
                        }
                        else -> {}
                    }
                }
            }
        }
    }





    private fun setupUI(view: View) {
        binding = FragmentChatBinding.bind(view)
        setupRecyclerView()

        binding.buttonSend.setOnClickListener {
            loge("SEND Pressed")
            if (binding.inputTextbox.text.toString().isNotEmpty()) {
                // Changes: Check if bluetoothCharacteristic is initialized
                if (::bluetoothCharacteristic.isInitialized) {
                    val bytes = binding.inputTextbox.text.toString().toByteArray()
                    loge("Writing to ${bluetoothCharacteristic.uuid}: ${bytes.byteArrayToAsciiString()}")
                    bluetoothDevice?.let {
                        ConnectionManager.writeCharacteristic(it, bluetoothCharacteristic, bytes)
                    }
                } else {
                    loge("bluetoothCharacteristic is not initialized")
                    Toast.makeText(context, "Bluetooth characteristic not initialized", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun isBluetoothAdapterON():Boolean {
        bluetoothArduinoLoraAdapter.let { adapter ->
            if (adapter.isEnabled) {
                return true
            }
        }
        return false
    }


    private val chatBubblesAdapter: ChatAdapter by lazy { ChatAdapter() }
    private fun setupRecyclerView() {
        binding.conversationRecyclerView.apply {
            adapter = chatBubblesAdapter
            layoutManager = LinearLayoutManager(
                this@ChatFragment.requireContext(),
                RecyclerView.VERTICAL,
                false
            )
            isNestedScrollingEnabled = false
        }

        val animator = binding.conversationRecyclerView.itemAnimator
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }
    }

    private fun setupFragmentAdjustmentAccordingToKeyboardVisibility() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            binding.root.getWindowVisibleDisplayFrame(r)
            val screenHeight: Int = binding.root.rootView.height
            val keypadHeight = screenHeight - r.bottom
            val currentWindowBottomPosition = r.bottom
            if(previousWindowBottomPosition == 0) {previousWindowBottomPosition = r.bottom}
            val screenHeightChange = currentWindowBottomPosition - previousWindowBottomPosition

            /*
            logd("keypadHeight = $keypadHeight")
            logd("r.bottom = ${r.bottom}")
            logd("r.top    = ${r.top}")
            logd("r.height = ${r.height()}")
            loge("* RecyclerHeight: ${binding.conversationRecyclerView.height}")
            loge("* RecyclerY     : ${binding.conversationRecyclerView.y}")
             */

            if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is enough to determine keyboard is opened
                if (!isKeyboardShowing) {
                    isKeyboardShowing = true
                    onKeyboardVisibilityChanged(true, screenHeightChange)
                    previousWindowBottomPosition = r.bottom
                }
            } else {  // keyboard is closed
                if (isKeyboardShowing) {
                    isKeyboardShowing = false
                    onKeyboardVisibilityChanged(false, screenHeightChange)
                    previousWindowBottomPosition = r.bottom
                }
            }
        }
    }

   private fun onKeyboardVisibilityChanged(shown: Boolean, screenHeightChange: Int) {
        if(shown) {
            logi("** KEYBOARD VISIBLE **")
        } else {
            logi("** KEYBOARD HIDDEN **")
        }
       loge("current RecyclerHeight: ${binding.conversationRecyclerView.height}")
       val params = LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,
           binding.conversationRecyclerView.height + screenHeightChange)
       binding.conversationRecyclerView.layoutParams = params
       loge("new RecyclerHeight: ${binding.conversationRecyclerView.height}")
       scrollToNewestMessage()
    }

    private fun scrollToNewestMessage () {
        binding.conversationRecyclerView.scrollToPosition(binding.conversationRecyclerView.adapter?.itemCount?.minus(
            1
        ) ?: 0);
    }

    /////// BLUETOOTH COMMS
    private val characteristics by lazy {
        bluetoothDevice?.let {
            ConnectionManager.servicesOnDevice(it)?.flatMap { service ->
                service.characteristics ?: listOf()
            }
        } ?: listOf()
    }

    //  Map<BluetoothGattCharacteristic, List<BleOperationsActivity.CharacteristicProperty>>
    private val characteristicProperties by lazy {
        characteristics.associateWith { characteristic ->
            mutableListOf<CharacteristicProperty>().apply {
                if (characteristic.isNotifiable()) add(CharacteristicProperty.Notifiable)
                if (characteristic.isIndicatable()) add(CharacteristicProperty.Indicatable)
                if (characteristic.isReadable()) add(CharacteristicProperty.Readable)
                if (characteristic.isWritable()) add(CharacteristicProperty.Writable)
                if (characteristic.isWritableWithoutResponse()) {
                    add(CharacteristicProperty.WritableWithoutResponse)
                }
            }.toList()
        }
    }

    private lateinit var bluetoothCharacteristic: BluetoothGattCharacteristic

    private val connectionEventListener by lazy {
        ConnectionEventListener().apply {
            onDisconnect = {device ->
                GlobalScope.launch(Dispatchers.Main) {
                    loge("...ChatFrag DISCONNECTED from Bluetooth LoRa Arduino")
                    //requireActivity().onBackPressedDispatcher.onBackPressed()
                    //navigateToScanFragment()
                    findNavController().popBackStack()
                    Toast.makeText(context, "Arduino ${device.name} is Disconnected", Toast.LENGTH_LONG).show()
                }
            }
            onConnectionSetupComplete = {
                loge("onConnectionSetupComplete" + it.device.name.toString())
            }
            onMtuChanged = { _, mtu ->
                loge("MTU updated to $mtu")
            }
            onCharacteristicChanged = { _, characteristic ->
                val incomingText = characteristic.value.byteArrayToAsciiString()
                logd("incomingText: $incomingText")
                val usernameSeparator = ",user="
                val messageText: String
                var usernameText = "Remote LoRaWAN device"
                if(incomingText.contains(usernameSeparator)) {
                    val tokens = incomingText.split(usernameSeparator)
                    usernameText = tokens.last()
                    logd("usernameText: $usernameText")
                    messageText = tokens.first()
                } else {
                    messageText = incomingText
                    logd("no separator, messageText: $messageText")
                }

                loge("Value changed on ${characteristic.uuid}: $incomingText")
                lifecycleScope.launch(Dispatchers.Main) {
                    loge("INCOMING message")
                    val newMessage = ChatMessage().apply {
                        username = usernameText
                        text = messageText
                        timestamp = DateUtil.formatTime(System.currentTimeMillis())
                        type = MessageType.RECEIVED
                    }
                    binding.conversationRecyclerView.apply {
                        chatBubblesAdapter.addNewMessage(newMessage)
                        adapter = chatBubblesAdapter
                    }
                    scrollToNewestMessage()
                }
            }
            onCharacteristicRead = { _, characteristic ->
                loge("Read from ${characteristic.uuid}: ${characteristic.value.byteArrayToAsciiString()}")
            }
            onCharacteristicWrite = { _, characteristic ->
                loge("Wrote to ${characteristic.uuid}")
                lifecycleScope.launch(Dispatchers.Main) {
                    loge("OUTGOING message")
                    val newMessage = ChatMessage().apply {
                        username = bluetoothDeviceName
                        text = binding.inputTextbox.text.toString()
                        timestamp = DateUtil.formatTime(System.currentTimeMillis())
                        type = MessageType.SENT
                    }
                    binding.conversationRecyclerView.apply {
                        chatBubblesAdapter.addNewMessage(newMessage)
                        adapter = chatBubblesAdapter
                    }

                    binding.inputTextbox.text.clear()
                    scrollToNewestMessage()
                }
            }
            onNotificationsEnabled = { _, characteristic ->
                loge("Enabled notifications on ${characteristic.uuid}")
            }
            onNotificationsDisabled = { _, characteristic ->
                loge("Disabled notifications on ${characteristic.uuid}")
            }
        }
    }

    fun navigateToScanFragment() {
        GlobalScope.launch(Dispatchers.Main) {
            val action = ChatFragmentDirections.actionChatDestToScanDest()
            findNavController().navigate(action)
        }
    }
}