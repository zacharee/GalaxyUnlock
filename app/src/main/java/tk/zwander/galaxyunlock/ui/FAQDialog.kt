package tk.zwander.galaxyunlock.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Text
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tk.zwander.galaxyunlock.data.FAQItem
import tk.zwander.galaxyunlock.launchUrl

@Composable
fun FAQDialog(info: FAQItem, state: MutableState<Boolean>) {
    if (state.value) {
        val context = ContextAmbient.current

        AlertDialog(
            onDismissRequest = {
                state.value = false
            },
            title = {
                Text(
                    stringResource(info.title),
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(AnnotatedString(stringResource(info.desc)))
            },
            confirmButton = {
                Button(
                    onClick = {
                        state.value = false
                    },
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp,
                    border = BorderStroke(1.dp, MaterialTheme.colors.secondary),
                ) {
                    Text(text = stringResource(android.R.string.ok))
                }
            },
            dismissButton = {
                if (info.button != null) {
                    Button(
                        onClick = {
                            context.launchUrl(info.button.buttonLink)
                            state.value = false
                        },
                        backgroundColor = Color.Transparent,
                        elevation = 0.dp,
                        border = BorderStroke(1.dp, MaterialTheme.colors.secondary)
                    ) {
                        Text(text = stringResource(info.button.buttonText))
                    }
                }
            }
        )
    }
}