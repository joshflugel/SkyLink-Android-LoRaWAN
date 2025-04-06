# SkyLink 

![SkyLink Banner](app/src/main/res/drawable/img_header_devices_highway.png)

**SkyLink** is a **mobile Android app** enabling **long-range, peer-to-peer messaging** between smartphones — even in total **network dead zones**.

By pairing with an **Arduino + LoRaWAN transceiver** over **Bluetooth Low Energy (BLE)**, SkyLink allows one Android device to communicate with another over long distances without the need for cellular or Wi-Fi networks.
The Arduino code in C is available in this separate repository: [SkyLink Arduino LoRaWAN](https://github.com/joshflugel/Arduino_LoRa_Bluetooth_Wireless_Bridge_For_Android)

- 📶 **Offline-first communication**: Connect anywhere, anytime.
- 🔗 **BLE + LoRa integration**: Pair with Arduino for long-range reach.
- 💬 **Lightweight text messaging**: Minimal text-only interface.

---

## 🧠 Features

- 📡 **LoRa-powered communication** via BLE-paired Arduino devices
- 🔗 **Device scanning & pairing** using Bluetooth LE
- 💬 **Real-time text message exchange**
- 📥 **Reactive UI** for message logs
- 🔐 **Straightforward permissions flow**

---

## 🧱 Tech Stack

| Layer        | Technology |
|--------------|------------|
| **Language** | Kotlin |
| **UI**       | MVVM XML |
| **Connectivity** | BLE (BluetoothGatt API) |
| **Hardware** | Arduino + LoRaWAN Module |
| **DI**       | Hilt |
| **Async**    | Coroutines + StateFlow |
| **Architecture** | CLEAN (3-layered) |
| **Testing**  | JUnit, MockK, Integration, Espresso |

---

## 🎨 Design Philosophy

### 🛰️ **Offline Connectivity**
SkyLink is built for **zero-network environments**: maybe rural exploration, disaster relief, or digital freedom. It transforms your Android device into a LoRa messenger paired with Arduino hardware.

### 🔗 **BLE First, LoRa Beyond**
BLE is used for secure, short-range pairing between Android and Arduino. The Arduino then handles **long-range** transmission via LoRa.

### ⚡ **Responsiveness**
- Flow = Smooth, reactive UI, with custom aesthetics.
- Real-time message updates
- Background-safe Bluetooth lifecycle handling

### 🧭 **UX Simplicity**
- Message input + Send button
- Full featured Arduino BLE device Scanning

---

## 🧪 Test Strategy 

- ✅ Unit tests for core use cases
- 🔄 Integration tests 
- ⚙️ Manual validation with real Arduino hardware

---

## 📜 Project Context

This app was created as the **final project** for the  
**Architect Coders Android Bootcamp ** by [DevExpert.io](https://devexpert.io).



SkyLink showcases modern Android practices applied to **low-level hardware communication**, combining BLE, CLEAN Architecture, and Kotlin.

---

## 📆 2025  
**Josh Flugel**
