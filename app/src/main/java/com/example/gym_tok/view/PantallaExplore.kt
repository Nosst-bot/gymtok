package com.example.gym_tok.view

import android.widget.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaExplorar(){
    Column(
        Modifier.fillMaxSize().padding(16.dp)){
        Text(text = "Explorar gimnasios")
        Spacer(Modifier.height(8.dp))
        Button(onClick = { /*TODO*/ }) { Text("Buscar Gimnasios") }



    }
}
