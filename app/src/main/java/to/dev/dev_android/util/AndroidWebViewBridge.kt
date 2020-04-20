package to.dev.dev_android.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface

/**
 * This class currently is empty because more methods would be added to it
 * when new bridge functionalities are added.
 */
class AndroidWebViewBridge(private val mContext: Context) {

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
    fun copyToClipboard(text: String) {
        val clipboard = mContext.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText("clipboard", text)
        clipboard?.primaryClip = clip
    }
}
