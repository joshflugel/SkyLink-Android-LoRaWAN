/*
 * Copyright 2019 Punch Through Design LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lora.skylink.presentation.scan

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lora.skylink.databinding.RowScanResultBinding




class ScanResultAdapter(
    private var items: List<ScanResult>,
    private val onItemClick: (ScanResult) -> Unit
) : RecyclerView.Adapter<ScanResultAdapter.ScanResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanResultViewHolder {
        val binding = RowScanResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScanResultViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ScanResultViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<ScanResult>) {
        items = newItems
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

    class ScanResultViewHolder(
        private val binding: RowScanResultBinding,
        private val onItemClick: (ScanResult) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("MissingPermission")
        fun bind(scanResult: ScanResult) {
            binding.deviceName.text = scanResult.device.name ?: "Unnamed"
            binding.macAddress.text = scanResult.device.address
            binding.root.setOnClickListener { onItemClick(scanResult) }
        }
    }
}


/*
class ScanResultAdapterORIGINAL(
    private val items: List<ScanResult>,
    private val onClickListener: ((device: ScanResult) -> Unit)
) : RecyclerView.Adapter<ScanResultAdapterORIGINAL.ViewHolder>() {

    lateinit var view: View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        /*
        val view = parent.context.layoutInflater.inflate(
            R.layout.row_scan_result,
            parent,
            false
        )
         */
        view = parent.inflate(R.layout.row_scan_result, false)
        return ViewHolder(view, onClickListener)
    }

    override fun getItemCount() = items.count()

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        // FLUGEL BLE Device: F4:12:FA:66:37:CD
        // Log.e("joshtag","FLUGEL BLE Device: " + item.device)
        holder.bind(item)
    }

    class ViewHolder(
        private val view: View,
        private val onClickListener: ((device: ScanResult) -> Unit)
    ) : RecyclerView.ViewHolder(view) {
        private var binding = RowScanResultBinding.bind(view)
        @SuppressLint("MissingPermission")
        fun bind(result: ScanResult) {
            binding.deviceName.text = result.device.name ?: "Unnamed"
            binding.macAddress.text = result.device.address
            binding.signalStrength.text = "${result.rssi} dBm"
            view.setOnClickListener { onClickListener.invoke(result)}
        }
    }
}


 */
