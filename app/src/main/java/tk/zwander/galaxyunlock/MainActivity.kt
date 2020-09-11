package tk.zwander.galaxyunlock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.materialIcon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageAssetConfig
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        FAQItem(R.string.supported_devices_title, R.string.supported_devices_desc),
        FAQItem(R.string.needed_info_title, R.string.needed_info_desc),
        FAQItem(R.string.refunds_title, R.string.refunds_desc),
        FAQItem(R.string.needed_tools_title, R.string.needed_tools_desc),
        FAQItem(R.string.permanence_title, R.string.permanence_desc),
        FAQItem(R.string.wait_time_title, R.string.wait_time_desc),
        FAQItem(R.string.other_questions_title, R.string.other_questions_desc,
            ButtonInfo(R.string.telegram_group, "https://t.me/joinchat/EkE57lDUTDaP4b9jDlE4-Q")),
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
        Column(
            modifier = Modifier.fillMaxHeight()
                .fillMaxWidth(),
        ) {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.app_name))
                },
                backgroundColor = MaterialTheme.colors.primary,
            )
            ConstraintLayout(
                modifier = Modifier.padding(InnerPadding(16.dp))
                    .fillMaxHeight(),
                constraintSet = ConstraintSet {
                    val listRef = createRefFor("faq_list")
                    val requestRef = createRefFor("request_unlock")

                    constrain(listRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(requestRef.top)
                    }

                    constrain(requestRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(listRef.bottom)
                        bottom.linkTo(parent.bottom)
                    }
                }
            ) {
                LazyColumnFor(
                    items = createFAQItems(),
                    itemContent = { item ->
                        FAQDialogCard(info = item)
                    },
                    modifier = Modifier.layoutId("faq_list"),
                )

                val context = ContextAmbient.current

                Button(
                    onClick = {
                        context.sendRequest()
                    },
                    modifier = Modifier.layoutId("request_unlock"),
                    shape = RoundedCornerShape(8.dp),
                    backgroundColor = MaterialTheme.colors.secondary
                ) {
                    Icon(
                        asset = Icons.Filled.LockOpen,
                        tint = MaterialTheme.colors.onSurface,
                        modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 0.dp),
                    )
                    Text(
                        text = getString(id = R.string.request_unlock),
                        fontSize = TextUnit.Companion.Sp(36),
                        modifier = Modifier.padding(8.dp),
                    )
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
                    verticalGravity = Alignment.CenterVertically,
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