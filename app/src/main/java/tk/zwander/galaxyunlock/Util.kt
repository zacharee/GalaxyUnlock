package tk.zwander.galaxyunlock

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.SystemProperties
import android.text.Html
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import tk.zwander.galaxyunlock.data.ButtonInfo
import tk.zwander.galaxyunlock.data.FAQItem

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
        FAQItem(R.string.void_warranty_title, R.string.void_warranty_desc),
        FAQItem(R.string.wait_time_title, R.string.wait_time_desc),
        FAQItem(
            R.string.other_questions_title, R.string.other_questions_desc,
            ButtonInfo(R.string.telegram_group, "https://t.me/joinchat/EkE57lDUTDaP4b9jDlE4-Q")
        ),
    )
}

fun Context.sendRequest(enteredEmail: String) {
    val intent = Intent(Intent.ACTION_SENDTO)
    val text = resources.getString(R.string.unlock_email_content,
        deviceModel, deviceId, enteredEmail
    )

    intent.data = Uri.parse("mailto:")
    intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.unlock_email_subject,
        deviceModel
    ))
    intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(text, 0))
    intent.putExtra(Intent.EXTRA_HTML_TEXT, text)
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(resources.getString(R.string.unlock_email)))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    startActivity(Intent.createChooser(intent, resources.getText(R.string.email_chooser_msg)))
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