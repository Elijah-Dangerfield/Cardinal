package com.dangerfield.cardinal.presentation.ui.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(): ViewModel() {
    val settingsOptions = listOf(SettingsItemData("About"), SettingsItemData("Clear Cache"), SettingsItemData("Edit Categories"), SettingsItemData("Version 1.2.3", false))
}