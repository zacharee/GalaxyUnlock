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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.material.composethemeadapter.MdcTheme
import tk.zwander.galaxyunlock.R
import tk.zwander.galaxyunlock.coloredDeviceModel
import tk.zwander.galaxyunlock.createFAQItems
import tk.zwander.galaxyunlock.deviceId

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
                modifier = Modifier
                    .background(MaterialTheme.colors.surface)
                    .padding(bottom = 16.dp),
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