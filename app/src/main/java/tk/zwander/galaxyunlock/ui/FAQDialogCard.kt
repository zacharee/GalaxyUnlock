package tk.zwander.galaxyunlock.ui

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import tk.zwander.galaxyunlock.data.FAQItem

@Composable
fun FAQDialogCard(info: FAQItem) {
    val showDialog = remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier.padding(0.dp, 8.dp, 0.dp, 8.dp),
    ) {
        Card(
            elevation = 4.dp,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .clickable(onClick = {
                        showDialog.value = true
                    })
                    .padding(8.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .preferredHeight(32.dp),
                ) {
                    Text(
                        text = AnnotatedString(stringResource(info.title)),
                    )
                }
            }
        }
    }

    FAQDialog(info = info, state = showDialog)
}