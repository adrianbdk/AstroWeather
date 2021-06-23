package com.example.astroweather

import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class SettingsFragment : PreferenceFragmentCompat() {

    private var latitude: EditTextPreference? = null
    private var longitude: EditTextPreference? = null
    private var cityName: Preference? = null
    private var listener: SharedPreferences.OnSharedPreferenceChangeListener? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        latitude = preferenceManager.findPreference(getString(R.string.lat_key))
        latitude?.text = SharedPreferencesData.getString(context!!, getString(R.string.lat_key)).toString()
        longitude = preferenceManager.findPreference(getString(R.string.long_key))
        longitude?.text = SharedPreferencesData.getString(context!!, getString(R.string.long_key)).toString()
        createListener()
    }

    private fun createListener() {
        listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, _ ->
            val latitudePref =
                sharedPreferences.getString(
                    context?.getString(R.string.lat_key),
                    context?.getString(R.string.latitude))
            val longitudePref =
                sharedPreferences.getString(
                    context?.getString(R.string.long_key),
                    context?.getString(R.string.latitude)
                )

            latitude?.setOnBindEditTextListener { editText ->
                editText.inputType =
                    InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED or InputType.TYPE_NUMBER_FLAG_DECIMAL
            }

            longitude?.setOnBindEditTextListener { editText ->
                editText.inputType =
                    InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED or InputType.TYPE_NUMBER_FLAG_DECIMAL
            }

            Log.i("setOnBindEditTextListener values", "$latitude, $longitude, $latitudePref, $latitudePref")

            if (latitudePref != null) {
                if (latitudePref == "" || latitudePref.takeLast(1) == "." ||
                    !(latitudePref.toDouble() >= -90 && latitudePref.toDouble() <= 90)
                ) {
                    latitude?.text = getString(R.string.default_value)
                    Toast.makeText(activity, "You've entered wrong number ", Toast.LENGTH_LONG).show()
                }
            }

            if (longitudePref != null) {
                if (longitudePref == "" || longitudePref.takeLast(1) == "." ||
                    !(longitudePref.toDouble() >= -180 && longitudePref.toDouble() <= 180)
                ) {
                    longitude?.text = getString(R.string.default_value)
                    Toast.makeText(activity, "You've entered wrong number ", Toast.LENGTH_LONG).show()
                }
            }

        }

        PreferenceManager.getDefaultSharedPreferences(context)
            .registerOnSharedPreferenceChangeListener(listener)
    }
}