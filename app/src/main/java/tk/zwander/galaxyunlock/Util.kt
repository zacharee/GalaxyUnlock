package tk.zwander.galaxyunlock

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.SystemProperties

val deviceId: String
    get() = SystemProperties.get("ro.boot.em.did")

val deviceModel: String
    get() = SystemProperties.get("ro.boot.em.model")

fun Context.sendRequest() {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.unlock_email_subject,
        deviceModel
    ))
    intent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.unlock_email_content,
        deviceModel, deviceId
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