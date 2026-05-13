package com.example.digidex

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.digidex.ui.theme.DigiAccent
import com.example.digidex.ui.theme.DigiBackground
import com.example.digidex.ui.theme.DigiBlue
import com.example.digidex.ui.theme.DigiNavy
import com.example.digidex.ui.theme.LevelChampion
import com.example.digidex.ui.theme.LevelFresh
import com.example.digidex.ui.theme.LevelInTraining
import com.example.digidex.ui.theme.LevelMega
import com.example.digidex.ui.theme.LevelRookie
import com.example.digidex.ui.theme.LevelUltimate
import com.example.digidex.ui.theme.LevelUltra
import com.example.digidex.ui.theme.LevelUnknown

// ─────────────────────────────────────────────────────────────
//  Utilidad: color según nivel
// ─────────────────────────────────────────────────────────────
fun levelColor(level: String): Color = when (level.lowercase()) {
    "fresh"       -> LevelFresh
    "in-training" -> LevelInTraining
    "rookie"      -> LevelRookie
    "champion"    -> LevelChampion
    "ultimate"    -> LevelUltimate
    "mega"        -> LevelMega
    "ultra"       -> LevelUltra
    else          -> LevelUnknown
}

// ─────────────────────────────────────────────────────────────
//  Pantalla principal: Lista de Digimons
// ─────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenDigimonList(
    navController: NavHostController,
    servicio: DigiApiService
) {
    val todosLosDigimons: SnapshotStateList<DigimonBasic> = remember { mutableStateListOf() }
    var isLoading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }

    // Carga inicial de datos
    LaunchedEffect(Unit) {
        try {
            val response = servicio.getDigimons(pageSize = 50, page = 0)
            todosLosDigimons.addAll(response.content)
        } catch (e: Exception) {
            Log.e("DIGI_LIST", "Error cargando lista: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    // Filtro de búsqueda en memoria
    val listaFiltrada = remember(searchQuery, todosLosDigimons.size) {
        if (searchQuery.isBlank()) todosLosDigimons.toList()
        else todosLosDigimons.filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DigiBackground)
    ) {
        // ── Barra de búsqueda ──────────────────────────
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            placeholder = { Text("Buscar Digimon...", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Buscar",
                    tint = DigiBlue
                )
            },
            shape = RoundedCornerShape(50),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = DigiBlue,
                unfocusedBorderColor = Color.LightGray,
                focusedContainerColor   = Color.White,
                unfocusedContainerColor = Color.White
            ),
            singleLine = true
        )

        // ── Contenido principal ────────────────────────
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = DigiBlue)
                    Spacer(Modifier.height(12.dp))
                    Text("Cargando Digimons...", color = DigiNavy, fontWeight = FontWeight.Medium)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "${listaFiltrada.size} Digimons",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                    )
                }
                items(listaFiltrada) { digimon ->
                    DigimonCard(
                        digimon = digimon,
                        onClick = { navController.navigate("detalle/${digimon.id}") }
                    )
                }
                item { Spacer(Modifier.height(12.dp)) }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
//  Card de Digimon en la lista
// ─────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DigimonCard(digimon: DigimonBasic, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ── Imagen del Digimon ──────────────────────
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model          = digimon.image,
                    contentDescription = digimon.name,
                    contentScale   = ContentScale.Fit,
                    modifier       = Modifier.size(70.dp)
                )
            }

            Spacer(Modifier.width(14.dp))

            // ── Info ────────────────────────────────────
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = digimon.name,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 17.sp,
                    color      = DigiNavy,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text     = "ID #${digimon.id}",
                    color    = Color.Gray,
                    fontSize = 12.sp
                )
            }

            // ── Flecha indicadora ───────────────────────
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(DigiAccent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("▶", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}