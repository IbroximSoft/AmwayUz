package uz.ibrohim.amwayuz.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yariksoffice.lingver.Lingver
import es.dmoral.toasty.Toasty
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

fun Fragment.warningToast(text: String) {
    Toasty.warning(requireContext(), text, Toasty.LENGTH_SHORT, true).show()
}

fun warningToast(text: String, context: Context) {
    Toasty.warning(context, text, Toasty.LENGTH_SHORT, true).show()
}

fun Fragment.errorToast(text: String) {
    Toasty.error(requireContext(), text, Toasty.LENGTH_SHORT, true).show()
}

fun errorToast(text: String, context: Context) {
    Toasty.error(context, text, Toasty.LENGTH_SHORT, true).show()
}

fun Fragment.successToast(text: String) {
    Toasty.success(requireContext(), text, Toasty.LENGTH_SHORT, true).show()
}

//fun progressInterface(circularProgress: CircularProgress) {
//    circularProgress.setColor(Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN)
//    circularProgress.setBodyColor(R.color.primary)
//    circularProgress.setRotationSpeeed(25)
//}

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
