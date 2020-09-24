package tk.zwander.galaxyunlock.ui

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tk.zwander.galaxyunlock.R
import tk.zwander.galaxyunlock.sendRequest

@Composable
fun VenmoDialog(showVenmoEmailDialog: MutableState<Boolean>) {
    val context = ContextAmbient.current
    val enteredEmail = remember {
        mutableStateOf("")
    }

    if (showVenmoEmailDialog.value) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    stringResource(R.string.enter_venmo_email),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                )
            },
            text = {
                Column {
                    Text(text = stringResource(id = R.string.enter_venmo_email_desc))
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = enteredEmail.component1(),
                        keyboardType = KeyboardType.Email,
                        onValueChange = {
                            enteredEmail.value = it
                        }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (Patterns.EMAIL_ADDRESS.matcher(enteredEmail.value).matches()) {
                            context.sendRequest(enteredEmail.value)
                            enteredEmail.value = ""
                            showVenmoEmailDialog.value = false
                        } else {
                            Toast.makeText(
                                context,
                                R.string.enter_valid_email_msg,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp,
                    border = BorderStroke(1.dp, MaterialTheme.colors.secondary),
                ) {
                    Text(text = stringResource(android.R.string.ok))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showVenmoEmailDialog.value = false
                    },
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp,
                    border = BorderStroke(1.dp, MaterialTheme.colors.secondary)
                ) {
                    Text(text = stringResource(id = android.R.string.cancel))
                }
            },
        )
    }
}