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
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import to.dev.dev_android.R

class ForemAppDialog : DialogFragment() {

    companion object {
        const val PACKAGE_NAME = "com.forem.android"
        private const val FOREM_URL = "ForemAppDialog.url"

        fun newInstance(url: String): ForemAppDialog {
            val foremAppDialog = ForemAppDialog()
            val args = Bundle()
            args.putString(FOREM_URL, url)
            foremAppDialog.arguments = args
            return foremAppDialog
        }

        fun isForemAppAlreadyInstalled(activity: Activity?): Boolean {
            return try {
                activity?.packageManager?.getPackageInfo(PACKAGE_NAME, 0)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
        }

        fun openForemApp(activity: Activity?, url: String?) {
            val packageManager: PackageManager? = activity?.packageManager
            val app = packageManager?.getLaunchIntentForPackage(PACKAGE_NAME)
            if (!url.isNullOrEmpty()) {
                app?.putExtra(Intent.EXTRA_TEXT, url)
            }
            activity?.startActivity(app)
        }
    }

    lateinit var url: String

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
        val args = arguments
        url = args?.getString(FOREM_URL) ?: ""

        val view = inflater.inflate(R.layout.forem_app_dialog, container, false)
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window?.setBackgroundDrawableResource(R.drawable.forem_dialog_fragment_background)
        }
        val downloadInstallForemAppTextView =
            view.findViewById<TextView>(R.id.download_install_forem_app_text_view)
        val downloadOpenForemAppImageView =
            view.findViewById<ImageView>(R.id.download_open_forem_image_view)
        val descriptionTextView =
            view.findViewById<TextView>(R.id.forem_app_dialog_description_text_view)

        if (isForemAppAlreadyInstalled(activity)) {
            downloadInstallForemAppTextView.text = getString(R.string.open_forem_app)
            downloadOpenForemAppImageView.setImageDrawable(
                ContextCompat.getDrawable(
                    this.requireContext(),
                    R.drawable.ic_compass
                )
            )
            descriptionTextView.text = getString(R.string.forem_app_dialog_description_if_installed)
        } else {
            downloadInstallForemAppTextView.text = getString(R.string.download_forem_app)
            downloadOpenForemAppImageView.setImageDrawable(
                ContextCompat.getDrawable(
                    this.requireContext(),
                    R.drawable.ic_baseline_arrow_downward_24
                )
            )
            descriptionTextView.text = getString(R.string.forem_app_dialog_description)
        }

        val downloadLayout = view.findViewById<ConstraintLayout>(R.id.download_forem_app_layout)

        downloadLayout.setOnClickListener {
            openForemAppLink()
        }
        return view
    }

    private fun openForemAppLink() {
        if (isForemAppAlreadyInstalled(activity)) {
            openForemApp(activity, url)
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
