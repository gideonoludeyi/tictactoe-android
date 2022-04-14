package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tictactoe.ui.theme.Shapes


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val initialPlayer = PlayerState.X

            var player by remember {
                mutableStateOf(initialPlayer)
            }

            val state = remember {
                List(size = 9, init = { PieceState.EMPTY }).toMutableStateList()
            }

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Player $player", style = MaterialTheme.typography.h2)
                Board(data = state, onSelect = { selectedIndex ->
                    if (state[selectedIndex] == PieceState.EMPTY) {
                        state[selectedIndex] = player.toPieceState()
                        player = player.next()
                    }
                })
                Button(onClick = {
                    state.fill(PieceState.EMPTY)
                    player = initialPlayer
                }) {
                    Text("New Game", style = MaterialTheme.typography.button)
                }
            }
        }
    }
}

@Composable
fun Piece(
    modifier: Modifier = Modifier,
    onSelect: (() -> Unit)? = null,
    color: Color = Color.LightGray
) {
    Card(
        modifier = modifier.clickable { onSelect?.invoke() },
        backgroundColor = color,
        contentColor = Color.Black,
        shape = Shapes.large
    ) {

    }
}

@Composable
fun <T> Grid(
    modifier: Modifier = Modifier,
    data: List<List<T>>,
    content: @Composable (T) -> Unit
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(data) { _, row ->
            LazyRow {
                itemsIndexed(row) { _, value ->
                    content(value)
                }
            }
        }
    }
}

enum class PieceState {
    X, O, EMPTY
}

@Composable
fun Board(
    modifier: Modifier = Modifier,
    data: List<PieceState>,
    onSelect: ((index: Int) -> Unit)? = null
) {
    Grid(
        modifier = modifier,
        data = data.withIndex().chunked(3)
    ) {
        Piece(
            modifier = Modifier.size(128.dp),
            onSelect = { onSelect?.invoke(it.index) },
            color = when (it.value) {
                PieceState.X -> MaterialTheme.colors.primary
                PieceState.O -> MaterialTheme.colors.secondary
                PieceState.EMPTY -> Color.LightGray
            }
        )
    }
}

enum class PlayerState {
    X, O;

    fun toPieceState() =
        when (this) {
            X -> PieceState.X
            O -> PieceState.O
        }

    fun next() =
        when (this) {
            X -> O
            O -> X
        }
}

@Preview(showBackground = true)
@Composable
fun PreviewBoard() {
    Board(data = List(size = 9, init = { PieceState.EMPTY }))
}