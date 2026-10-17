package com.aura.os

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aura.os.ui.AuraDashboard
import com.aura.os.ui.AuraTheme
import com.aura.os.viewmodel.AuraViewModel

class MainActivity : ComponentActivity() {
    private val vm: AuraViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AuraTheme {
                val state = vm.ui.collectAsStateWithLifecycle().value
                val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                    if (granted) vm.toggleListening() else vm.onMicPermissionDenied()
                }
                AuraDashboard(state) {
                    val granted = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                    if (granted) vm.toggleListening() else launcher.launch(Manifest.permission.RECORD_AUDIO)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (vm.ui.value.listening) vm.toggleListening()
    }
}
