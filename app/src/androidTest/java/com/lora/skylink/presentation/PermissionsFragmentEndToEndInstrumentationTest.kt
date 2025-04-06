package com.lora.skylink.presentation



import android.content.Intent
import android.widget.Switch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import com.lora.skylink.R
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class PermissionsFragmentEndToEndInstrumentationTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<NavHostActivity> = ActivityScenarioRule(NavHostActivity::class.java)

    val timeoutMilliseconds = 4000L


    private fun setSwitchTo(switch: UiObject2, state: Boolean) {
        val isChecked = switch.isChecked
        if (isChecked != state) {
            switch.click()
        }
    }

    private fun navigateToBluetoothSettingsSwitch() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        device.pressHome()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = context.packageManager.getLaunchIntentForPackage("com.android.settings")
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) // Clear out any previous instances
        context.startActivity(intent)

        device.wait(Until.hasObject(By.pkg("com.android.settings").depth(0)), 5000)

        val connections = device.wait(Until
            .findObject(By.text(getLocalizedString(R.string.connections_settings_text))),
            timeoutMilliseconds)
        connections?.clickAndWait(Until.newWindow(), timeoutMilliseconds)

        val bluetoothOption = device.wait(Until
            .findObject(By.text(getLocalizedString(R.string.bluetooth_settings_text))),
            timeoutMilliseconds)
        bluetoothOption?.clickAndWait(Until.newWindow(), timeoutMilliseconds)
    }

    private fun setAndroidBluetoothSetting(shouldBeTurnedOn: Boolean): Boolean {
        var couldSetBluetoothSuccessfully = false
        navigateToBluetoothSettingsSwitch()

        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        // Find the Bluetooth switch
        val bluetoothSwitch = device.wait(Until.findObject(By.clazz(Switch::class.java)), timeoutMilliseconds)
        if (bluetoothSwitch != null) {
            couldSetBluetoothSuccessfully = true
        }
        setSwitchTo(bluetoothSwitch, shouldBeTurnedOn)
        return couldSetBluetoothSuccessfully
    }
    private fun turnOnAndroidBluetooth(): Boolean { return setAndroidBluetoothSetting(true) }
    private fun turnOffAndroidBluetooth(): Boolean { return setAndroidBluetoothSetting(false) }

    private fun getLocalizedString(resId: Int): String {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        return context.getString(resId)
    }

    private fun navigateBackToApp(device: UiDevice) {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) // Clear out any previous instances
        }
        context.startActivity(intent)
        device.wait(Until.hasObject(By.pkg(context.packageName).depth(0)), timeoutMilliseconds)
    }


    @Test
    fun testTheScanFragmentIsShownAfterEnablingPermissionsAndBluetooth() {

        turnOnAndroidBluetooth()

        // Navigate back to the App to interact with the system permission dialog
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        navigateBackToApp(device)

        onView(withId(R.id.btn_grant_permissions)).perform(ViewActions.click())

        val allowButton = device.findObject(UiSelector()
            .textMatches("(?i)${getLocalizedString(R.string.while_using_the_app_button_text)}"))

        if (allowButton.exists() && allowButton.isEnabled) {
            allowButton.click()

            val allowButton2 = device.findObject(UiSelector().textMatches("(?i)${getLocalizedString(R.string.allow_button_text)}"))

            if (allowButton2.exists() && allowButton2.isEnabled) {
                allowButton2.click()

                val enableBluetoothButton = device.findObject(UiSelector()
                    .textMatches("(?i)${getLocalizedString(R.string.allow_button_text)}"))

                if (enableBluetoothButton.exists() && enableBluetoothButton.isEnabled) {
                    enableBluetoothButton.click()
                }
            }
        }

        onView(withId(R.id.scan_fragment_rootview)).check(matches(isDisplayed()))
    }
}
