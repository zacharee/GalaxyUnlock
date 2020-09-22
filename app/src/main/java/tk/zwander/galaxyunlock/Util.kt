package tk.zwander.galaxyunlock

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.SystemProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign

val supportedModels = arrayOf(
    "SM-G970",
    "SM-G973",
    "SM-G975",
    "SM-G977",
    "SM-N970",
    "SM-N971",
    "SM-N973",
    "SM-N975",
    "SM-N976",
    "SM-G981",
    "SM-G986",
    "SM-G988",
    "SM-N981",
    "SM-N986",
    "SM-F916",
)

val supportedRegions = arrayOf(
    "U",
    "W",
    "V",
    "P",
    "T",
    "A",
    "U1",
)

val deviceId: String
    get() = SystemProperties.get("ro.boot.em.did")

val deviceModel: String
    get() = SystemProperties.get("ro.boot.em.model").run {
        if (isBlank()) Build.MODEL else this
    }

val coloredDeviceModel: AnnotatedString
    get() {
        val model = deviceModel

        val color = if (model.startsWith("SM")) {
            val (baseModel, region) = stripRegion(model)

            val isConfirmed = supportedModels.contains(baseModel)
            val isSupportedRegion = supportedRegions.contains(region)
            val isTooOld = checkIfModelTooOld(baseModel)

            when {
                isTooOld || !isSupportedRegion -> {
                    Color.Red
                }

                isConfirmed -> {
                    Color.Green
                }

                else -> {
                    Color.Yellow
                }
            }
        } else {
            Color.Red
        }

        return AnnotatedString.Builder(model).apply {
            addStyle(SpanStyle(color = color), 0, model.length)
            addStyle(ParagraphStyle(textAlign = TextAlign.Center), 0, model.length)
        }.toAnnotatedString()
    }

fun Context.sendRequest(enteredEmail: String) {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.unlock_email_subject,
        deviceModel
    ))
    intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.unlock_email_content,
        deviceModel, deviceId, enteredEmail
    ))
    intent.data = Uri.parse("mailto:${resources.getString(R.string.unlock_email)}")
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    startActivity(intent)
}

//Safely launch a URL.
//If no matching Activity is found, silently fail.
fun Context.launchUrl(url: String) {
    try {
        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    } catch (e: Exception) {}
}

fun checkIfModelTooOld(model: String): Boolean {
    return model.substring(5).toInt() < 70
}

fun stripRegion(model: String): Pair<String, String> {
    return if (model.length > 8) model.substring(0, model.length - 2) to model.substring(model.length - 2)
            else model.substring(0, model.length - 1) to model.substring(model.length - 1)
}