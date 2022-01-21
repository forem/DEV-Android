package to.dev.dev_android.activities

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import android.content.pm.PackageManager
import to.dev.dev_android.R

class ForemAppDialog : DialogFragment() {

    companion object {
        const val PACKAGE_NAME = "com.forem.android"

        fun isForemAppAlreadyInstalled(activity: Activity?): Boolean {
            return try {
                activity?.packageManager?.getPackageInfo(PACKAGE_NAME, 0)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val width = resources.displayMetrics.widthPixels
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.forem_app_dialog, container, false)
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window?.setBackgroundDrawableResource(R.drawable.forem_dialog_fragment_background)
        }
        val downloadLayout = view.findViewById<ConstraintLayout>(R.id.download_forem_app_layout)
        downloadLayout.setOnClickListener {
            openForemAppLink()
        }
        return view
    }

    private fun openForemAppLink() {
        if (isForemAppAlreadyInstalled(activity)) {
            val packageManager: PackageManager? = activity?.packageManager
            val app = packageManager?.getLaunchIntentForPackage(PACKAGE_NAME)
            activity?.startActivity(app)
        } else {
            try {
                // Opens Forem app in Play Store, if Play Store app is available.
                activity?.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$PACKAGE_NAME")
                    )
                )
            } catch (e: ActivityNotFoundException) {
                // Opens Forem app on Play Store in browser.
                activity?.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$PACKAGE_NAME")
                    )
                )
            }
        }
        this.dismiss()
    }
}
