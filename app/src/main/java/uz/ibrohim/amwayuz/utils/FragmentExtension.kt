package uz.ibrohim.amwayuz.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.clk.progress.CircularProgress
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yariksoffice.lingver.Lingver
import es.dmoral.toasty.Toasty
import uz.ibrohim.amwayuz.R
import uz.ibrohim.amwayuz.login.LoginActivity
import java.text.SimpleDateFormat
import java.util.Date

fun languageText(text: String, context: Context) {
    Preferences.init(context)
    when (text) {
        "Uzbekcha" -> {
            Lingver.getInstance().setLocale(context, "uz")
            Preferences.language = "uz"
            languageIntents(context)
        }

        "Узбекча" -> {
            Lingver.getInstance().setLocale(context, "ru")
            Preferences.language = "ru"
            languageIntents(context)
        }
    }
}

fun languageIntents(context: Context) {
    val intent = Intent(context, LoginActivity::class.java)
    intent.flags =
        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)
}

fun progressInterface(circularProgress: CircularProgress) {
    circularProgress.setColor(Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN)
    circularProgress.setBodyColor(R.color.primary)
    circularProgress.setRotationSpeeed(25)
}

fun Fragment.warningToast(text: String) {
    Toasty.warning(requireContext(), text, Toasty.LENGTH_SHORT, true).show()
}

fun Fragment.errorToast(text: String) {
    Toasty.error(requireContext(), text, Toasty.LENGTH_SHORT, true).show()
}

fun Fragment.successToast(text: String) {
    Toasty.success(requireContext(), text, Toasty.LENGTH_SHORT, true).show()
}

@SuppressLint("SimpleDateFormat")
fun todayDate(): String {
    val currentDate: String
    val simpleDate = SimpleDateFormat("dd.MM.yyyy")
    currentDate = simpleDate.format(Date())
    return currentDate
}

fun floatingScroll(recycler: RecyclerView, floating: FloatingActionButton) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        recycler.setOnScrollChangeListener { _, _, _, _, oldScrollY ->
            if (oldScrollY < 0 && floating.isShown) {
                floating.hide()
            } else if (oldScrollY > 22) {
                floating.show()
            }
        }
    } else {
        recycler.viewTreeObserver.addOnScrollChangedListener {
            val mScrollY: Int = recycler.scrollY
            if (mScrollY > 0 && floating.isShown) {
                floating.hide()
            } else if (mScrollY < 22) {
                floating.show()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
fun Fragment.requestStoragePermission(
    REQUEST_PERMISSIONS: Int, imageProfile: ActivityResultLauncher<Intent>
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        if (!Environment.isExternalStorageManager()) {

            ImagePicker.with(requireActivity())
                .galleryOnly()
                .createIntent { intent ->
                    imageProfile.launch(intent)
                }

        } else {
            if ((ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                ) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED)
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ), REQUEST_PERMISSIONS
                )
            }
        }
    } else {
        if ((ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            ) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), REQUEST_PERMISSIONS
            )
        } else {
            ImagePicker.with(requireActivity())
                .galleryOnly()
                .createIntent { intent ->
                    imageProfile.launch(intent)
                }
        }
    }
}

fun versionTest(context: Context) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
    builder.setMessage("Eski versiya yangilang")
    builder.setCancelable(false)

    builder.setPositiveButton("Yes") { _, _ ->
        val uri = Uri.parse("https://t.me/+VR4hB01lzp4zZjdi")
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }
    val alert: AlertDialog = builder.create()
    alert.show()
}
