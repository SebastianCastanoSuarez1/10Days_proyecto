package com.example.a10days


import android.content.res.Configuration
import android.media.MediaPlayer
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import com.example.a10days.Model.Game
import com.example.a10days.Model.GameRepository
import com.example.a10days.data.Datasource
import com.example.a10days.ui.theme._10DaysTheme

@Preview("Light Theme")
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HeroPreview() {
    val game = Game(
        titulo = R.string.titulo1,
        logoJuego = R.drawable.logo_01,
        Protagonista = R.drawable.altair,
        name = R.string.protagonista1,
        sinopsis = R.string.sinopsis1,
        nameAnatgonist = R.string.antagonista1,
        antagonist = R.drawable.templario1,
        audio = R.raw.ac_i,
        trailer = R.string.trailer1,
        animusPedia = R.string.animusPedia1
    )
    _10DaysTheme {
        GameApp(game = game)
    }
}


@Composable
fun GameApp(
    game: Game, modifier: Modifier = Modifier
) {
    var rotate by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var expandedBack by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sound = remember { mutableStateOf(MediaPlayer.create(context, game.audio)) }
    var reproduccion by remember { mutableIntStateOf(0) }
    val clicks = remember { mutableStateOf(false) }
    val contexto = LocalContext.current
    val trailer = stringResource(id = game.trailer)
    val link = stringResource(id = game.animusPedia)
    val rotar by animateFloatAsState(
        targetValue = if (rotate) 360F else 0F, animationSpec = tween(650)
    )
    if (!rotate) {
        Card(modifier = modifier
            .clickable { rotate = !rotate }
            .graphicsLayer {
                rotationY = rotar
                cameraDistance = 8 * density
            },
            elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.padding_min)),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiary)
        ) {
            Column(
                modifier = modifier.animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioHighBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_medium))
                        .sizeIn(minHeight = dimensionResource(R.dimen.padding_big))
                ) {
                    IconButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.padding_big))
                            .clip(RoundedCornerShape(dimensionResource(R.dimen.padding_small)))
                    ) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                            contentDescription = stringResource(R.string.expandir),
                            tint = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(game.titulo),
                            style = MaterialTheme.typography.displaySmall
                        )
                    }
                    Spacer(Modifier.width(dimensionResource(R.dimen.padding_medium)))
                    Box(
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.padding_big))
                            .clip(RoundedCornerShape(dimensionResource(R.dimen.padding_small)))
                    ) {
                        Image(painter = painterResource(game.logoJuego),
                            contentDescription = null,
                            alignment = Alignment.TopCenter,
                            contentScale = ContentScale.FillHeight,
                            modifier = modifier.clickable {
                                when (reproduccion) {
                                    0 -> {
                                        sound.value.start()
                                        reproduccion = 1
                                    }

                                    1 -> {
                                        sound.value.pause()
                                        sound.value.seekTo(0)
                                        reproduccion = 0
                                    }
                                }
                            })
                    }
                }
                if (expanded) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        GameProtagonist(
                            game = game,
                            modifier = Modifier.padding(bottom = (dimensionResource(R.dimen.padding_small)))
                        )
                    }
                }
            }
        }
    } else {
        Card(modifier = modifier
            .clickable { rotate = !rotate }
            .graphicsLayer {
                rotationY = rotar
                cameraDistance = 8 * density
            },
            elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.padding_min)),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiary)
        ) {
            Column {
                var showDialog by remember { mutableStateOf(false) }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row {
                        Image(painter = painterResource(id = game.antagonist),
                            contentDescription = stringResource(id = game.nameAnatgonist),
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.protagonist_photo))
                                .clickable { showDialog = !showDialog })
                        Text(
                            text = stringResource(id = game.nameAnatgonist),
                            style = MaterialTheme.typography.displayMedium,
                            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_midEnormous))
                        )
                    }
                    Text(
                        text = stringResource(game.sinopsis),
                        modifier = Modifier.padding((dimensionResource(R.dimen.padding_medium)))
                    )
                }
                if (showDialog) {
                    Dialog(onDismissRequest = { showDialog = false }) {
                        Image(painter = painterResource(id = game.antagonist),
                            contentDescription = stringResource(id = game.nameAnatgonist),
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { showDialog = !showDialog })

                    }
                }
            }
            Row {
                Row {
                    IconButton(
                        onClick = {
                            clicks.value = !clicks.value
                            if (clicks.value) {
                                val url = trailer
                                val builder = CustomTabsIntent.Builder()
                                val customTabsIntent = builder.build()
                                customTabsIntent.launchUrl(contexto, url.toUri())
                            }
                        },
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.padding_big))
                            .clip(RoundedCornerShape(dimensionResource(R.dimen.padding_small)))
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = stringResource(R.string.expandir),
                            tint = Color.Red
                        )
                    }
                    Text(
                        text = stringResource(R.string.trailer),
                        style = MaterialTheme.typography.displayLarge
                    )
                }
                Row {
                    Row {
                        IconButton(
                            onClick = {
                                clicks.value = !clicks.value
                                if (clicks.value) {
                                    val url = link
                                    val builder = CustomTabsIntent.Builder()
                                    val customTabsIntent = builder.build()
                                    customTabsIntent.launchUrl(contexto, url.toUri())
                                }
                            },
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.padding_big))
                                .clip(RoundedCornerShape(dimensionResource(R.dimen.padding_small)))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = stringResource(R.string.expandir),
                                tint = Color.Red
                            )
                        }
                    }
                    Text(
                        text = stringResource(R.string.animusPedia),
                        style = MaterialTheme.typography.displayLarge
                    )
                }
            }
        }
    }
}

@Composable
fun GameProtagonist(
    game: Game, modifier: Modifier = Modifier
) {
    val openDialog = remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(painter = painterResource(game.Protagonista),
            contentDescription = stringResource(id = game.name),
            modifier = Modifier
                .size(dimensionResource(R.dimen.protagonist_photo))
                .clickable { openDialog.value = true })
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
        Text(
            text = stringResource(id = game.name), style = MaterialTheme.typography.displayMedium
        )
    }

    if (openDialog.value) {
        AlertDialog(onDismissRequest = { openDialog.value = false },
            title = { Text(text = stringResource(game.titulo)) },
            text = { Text(stringResource(id = game.name)) },
            confirmButton = {
                Button(onClick = { openDialog.value = false }) {
                    Text("Cerrar")
                }
            })
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTopAppBar(modifier: Modifier = Modifier) {
    var posicion by remember { mutableStateOf(1) }
    val imageResource = when (posicion) {
        1 -> R.drawable.main
        2 -> R.drawable.templarios
        else -> {
            posicion = 1
            R.drawable.main
        }
    }
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.padding_big))
                            .padding(dimensionResource(id = R.dimen.padding_small))
                            .clickable { posicion++ },
                        painter = painterResource(imageResource),
                        contentDescription = stringResource(R.string.app_name)
                    )
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.displayMedium
                    )
                }
            }
        }, modifier = modifier
    )
}



@Composable
fun GameList(gameList: List<Game>, modifier: Modifier = Modifier) {
    Scaffold(topBar = { GameTopAppBar() }) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            items(gameList) { game ->
                GameApp(
                    game = game,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
@Composable
fun GameApp() {
    GameList(
        gameList = Datasource().loadgames(),
    )
}