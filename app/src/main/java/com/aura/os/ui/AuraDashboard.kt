package com.aura.os.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aura.os.viewmodel.AuraUiState

@Composable
fun AuraDashboard(state: AuraUiState, onOrbTap: () -> Unit) {
    Column(
        Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("AURA OS", color = Cyan, fontSize = 26.sp, fontWeight = FontWeight.Light, letterSpacing = 6.sp)
        Text("SYSTEM STATUS: ONLINE", color = Violet, fontSize = 12.sp, letterSpacing = 3.sp)
        Spacer(Modifier.height(24.dp))
        AuraOrb(state.listening, onOrbTap)
        Spacer(Modifier.height(16.dp))
        Text(state.status, color = Color.White, fontSize = 16.sp)
        if (state.heard.isNotBlank()) Text("\u201C${state.heard}\u201D", color = Cyan, fontSize = 14.sp)
        Spacer(Modifier.height(12.dp))
        Button(onClick = onOrbTap, colors = ButtonDefaults.buttonColors(containerColor = Violet)) {
            Text(if (state.listening) "STOP LISTENING" else "ACTIVATE MIC")
        }
        Spacer(Modifier.height(16.dp))
        LazyColumn(Modifier.weight(1f).fillMaxWidth()) {
            items(state.log) { Text(it, color = Color.White.copy(alpha = 0.75f), fontSize = 13.sp, modifier = Modifier.padding(vertical = 2.dp)) }
        }
    }
}
