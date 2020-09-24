package tk.zwander.galaxyunlock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.ui.tooling.preview.Preview
import com.google.android.material.composethemeadapter.MdcTheme

data class FAQItem(
    val title: Int,
    val desc: Int,
    val button: ButtonInfo? = null
)

data class ButtonInfo(
    val buttonText: Int,
    val buttonLink: String
)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainContent()
        }
    }
}

fun createFAQItems(): List<FAQItem> {
    return listOf(
        FAQItem(R.string.cost_title, R.string.cost_desc),
        FAQItem(
            R.string.how_to_pay_title, R.string.how_to_pay_desc,
            ButtonInfo(R.string.venmo_link, "https://venmo.com/")
        ),
        FAQItem(R.string.when_to_pay_title, R.string.when_to_pay_desc),
        FAQItem(R.string.supported_devices_title, R.string.supported_devices_desc),
        FAQItem(R.string.needed_info_title, R.string.needed_info_desc),
        FAQItem(R.string.refunds_title, R.string.refunds_desc),
        FAQItem(R.string.needed_tools_title, R.string.needed_tools_desc),
        FAQItem(R.string.permanence_title, R.string.permanence_desc),
        FAQItem(R.string.wait_time_title, R.string.wait_time_desc),
        FAQItem(
            R.string.other_questions_title, R.string.other_questions_desc,
            ButtonInfo(R.string.telegram_group, "https://t.me/joinchat/EkE57lDUTDaP4b9jDlE4-Q")
        ),
    )
}

@Composable
fun getString(id: Int): String {
    return ContextAmbient.current.resources.getString(id)
}

@Preview
@Composable
fun Preview() {
    MainContent()
}

@Composable
fun MainContent() {
    MdcTheme(setTextColors = true) {
        val showVenmoEmailDialog = remember {
            mutableStateOf(false)
        }

        VenmoDialog(showVenmoEmailDialog)

        Column {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.app_name))
                },
                backgroundColor = MaterialTheme.colors.primary,
            )

            Column(
                modifier = Modifier.padding(
                    top = 8.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 4.dp
                )
                    .weight(1f),
            ) {
                LazyColumnFor(
                    items = createFAQItems(),
                    itemContent = { item ->
                        FAQDialogCard(info = item)
                    },
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.background(MaterialTheme.colors.surface),
            ) {
                Button(
                    onClick = {
                        showVenmoEmailDialog.value = true
                    },
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    backgroundColor = MaterialTheme.colors.secondary,
                ) {
                    Icon(
                        asset = Icons.Filled.LockOpen,
                        tint = MaterialTheme.colors.onSurface,
                        modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp),
                    )
                    Text(
                        text = getString(id = R.string.request_unlock),
                        fontSize = 36.sp,
                        modifier = Modifier.padding(8.dp),
                    )
                }

                Row {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(id = R.string.device_id),
                            textAlign = TextAlign.Center,
                        )

                        Text(
                            text = deviceId,
                            textAlign = TextAlign.Center,
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(id = R.string.device_model),
                            textAlign = TextAlign.Center,
                        )

                        Text(
                            text = coloredDeviceModel,
                        )
                    }
                }
            }
        }
    }
}

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
                        text = AnnotatedString(getString(info.title)),
                    )
                }
            }
        }
    }

    FAQDialog(info = info, state = showDialog)
}

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
                    getString(info.title),
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(AnnotatedString(getString(info.desc)))
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
                    Text(text = getString(android.R.string.ok))
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
                        Text(text = getString(info.button.buttonText))
                    }
                }
            }
        )
    }
}

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
                    getString(R.string.enter_venmo_email),
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
                    Text(text = getString(android.R.string.ok))
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
                    Text(text = getString(id = android.R.string.cancel))
                }
            },
        )
    }
}
