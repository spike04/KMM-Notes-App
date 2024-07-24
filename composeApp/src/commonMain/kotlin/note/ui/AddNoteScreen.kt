package note.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import note.di.NoteModule
import note.domain.NoteModel

object AddNoteScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { AddNoteViewModel(NoteModule.noteRepository) }
        val state by viewModel.state.collectAsState()

        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(state.isSuccess) {
            if(state.isSuccess) navigator.pop()
        }

        Scaffold(topBar = {
            TopAppBar(title = { Text(text = "Add Note") }, navigationIcon = {
                IconButton(onClick = { navigator.pop() }) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Navigate Back")
                }
            })
        }) {
            AddNoteScreenContent(
                state = state,
                onEvent = viewModel::onEvent,
                modifier = Modifier.padding(it)
            )
        }
    }
}

@Composable
private fun AddNoteScreenContent(
    state: AddNoteState, onEvent: (AddNoteEvent) -> Unit, modifier: Modifier
) {

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.fillMaxSize().padding(20.dp)
    ) {
        TextField(value = title, onValueChange = { title = it }, label = {
            Text(text = "Title")
        }, modifier = Modifier.fillMaxWidth()
        )

        TextField(value = description, onValueChange = { description = it }, label = {
            Text(text = "Description")
        }, modifier = Modifier.fillMaxWidth()
        )

        if (state.isLoading) CircularProgressIndicator()
        else Button(onClick = {
            val note = NoteModel(
                id = "", title = title, description = description
            )
            onEvent(
                AddNoteEvent.AddNote(note = note)
            )
        }) {
            Text(text = "Add Note")
        }
    }
}