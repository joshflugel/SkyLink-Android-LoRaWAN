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
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lora.skylink.data.model.WirelessDevice
import com.lora.skylink.databinding.RowScanResultBinding


class ScanResultAdapter(
    private var items: List<WirelessDevice>,
    private val onItemClick: (WirelessDevice) -> Unit
) : RecyclerView.Adapter<ScanResultAdapter.ScanResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanResultViewHolder {
        val binding = RowScanResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScanResultViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ScanResultViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<WirelessDevice>) {
        items = newItems
        notifyDataSetChanged()
    }

    class ScanResultViewHolder(
        private val binding: RowScanResultBinding,
        private val onItemClick: (WirelessDevice) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("MissingPermission", "SetTextI18n")
        fun bind(wirelessDevice: WirelessDevice) {
            binding.deviceName.text = wirelessDevice.name ?: "Unnamed"
            binding.macAddress.text = wirelessDevice.macAddress
            binding.signalStrength.text = "${wirelessDevice.signalStrength_dBm} dBm"
            binding.rowRoot.setOnClickListener { onItemClick(wirelessDevice) }
        }
    }
}