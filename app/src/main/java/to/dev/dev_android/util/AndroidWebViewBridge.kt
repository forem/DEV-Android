package to.dev.dev_android.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.Toast
import to.dev.dev_android.BuildConfig

/**
 * This class currently is empty because more methods would be added to it
 * when new bridge functionalities are added.
 */
class AndroidWebViewBridge(private val context: Context) {
    /**
     * Every method that has to be accessed from web-view needs to be marked with
     * `@JavascriptInterface`.
     * This is currently just a sample method which logs an error to Logcat.
     */
    @JavascriptInterface
    fun logError(errorTag: String, errorMessage: String) {
        Log.e(errorTag, errorMessage)
    }

    @JavascriptInterface
    fun copyToClipboard(copyText: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clipData = ClipData.newPlainText("DEV Community", copyText)
        clipboard?.primaryClip = clipData
    }

    @JavascriptInterface
    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
