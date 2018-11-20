package to.dev.devto.android.ui.main.view

import android.annotation.SuppressLint
import android.os.Bundle
import to.dev.devto.android.R
import to.dev.devto.android.base.activity.BaseActivity
import to.dev.devto.android.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun layout(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWebViewSettings()
        navigateToHome()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebViewSettings() {
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
    }

    private fun navigateToHome() {
        binding.webView.loadUrl(resources.getString(R.string.main_url))
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
