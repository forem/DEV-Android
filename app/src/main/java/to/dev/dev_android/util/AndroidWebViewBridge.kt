package to.dev.dev_android.util

import android.util.Log
import android.webkit.JavascriptInterface

/**
 * This class currently is empty because more methods would be added to it
 * when new bridge functionalities are added.
 */
class AndroidWebViewBridge {
    /**
     * Every method that has to be accessed from web-view needs to be marked with
     * `@JavascriptInterface`.
     * This is currently just a sample method which logs an error to Logcat.
     */
    @JavascriptInterface
    fun logError(errorTag: String, errorMessage: String) {
        Log.e(errorTag, errorMessage)
    }
}
