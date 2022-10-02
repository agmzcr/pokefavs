package dev.agmzcr.pokefavs.ui.settings

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.BuildCompat
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.agmzcr.pokefavs.R

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.preferences, rootKey)
        val preference: Preference? = findPreference(getString(R.string.pref_key_ui))
        preference?.onPreferenceChangeListener = modeChangeListener
    }

    private val modeChangeListener =
        Preference.OnPreferenceChangeListener { _, newValue ->
            Log.i("newValue", newValue.toString())
            newValue as? String
            when (newValue) {
                getString(R.string.pref_ui_night_value) -> {
                    updateTheme(AppCompatDelegate.MODE_NIGHT_YES)
                    preferenceScreen.summary = getString(R.string.pref_ui_night_value)
                    viewModel.setUiMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                getString(R.string.pref_ui_day_value) -> {
                    updateTheme(AppCompatDelegate.MODE_NIGHT_NO)
                    preferenceScreen.summary = getString(R.string.pref_ui_day_value)
                    viewModel.setUiMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                else -> {
                    if (BuildCompat.isAtLeastQ()) {
                        updateTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        viewModel.setUiMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    } else {
                        updateTheme(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                        viewModel.setUiMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                    }
                }
            }
            true
        }

    private fun updateTheme(nightMode: Int) {
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}