package tk.zwander.galaxyunlock.ui

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.material.composethemeadapter.MdcTheme
import tk.zwander.galaxyunlock.*

@Composable
fun MainContent() {
    MdcTheme(setTextColors = true) {
        val context = ContextAmbient.current

        Column {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.app_name))
                },
                backgroundColor = MaterialTheme.colors.primary,
            )

            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                )
                    .weight(1f),
            ) {
                LazyColumnFor(
                    items = context.createFAQItems(),
                    itemContent = { item ->
                        FAQDialogCard(info = item)
                    },
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(
                        color = colorResource(id = R.color.colorBottomBar),
                        shape = RoundedCornerShape(
                            topLeft = 12.dp, topRight = 12.dp,
                        )
                    )
                    .padding(bottom = 16.dp),
            ) {
                Button(
                    onClick = {
                        context.launchUrl(context.resources.getString(R.string.discord_group_link))
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
                        text = stringResource(id = R.string.request_unlock),
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