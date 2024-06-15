package uz.ibrohim.amwayuz.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import uz.ibrohim.amwayuz.R
import uz.ibrohim.amwayuz.databinding.FragmentLoginBinding
import uz.ibrohim.amwayuz.utils.Preferences
import uz.ibrohim.amwayuz.utils.languageText

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        //@amway.uz
        Handler(Looper.getMainLooper()).postDelayed({
            binding.apply {
                splashLinear.isVisible = false
            }
        }, 1800)

        Preferences.init(requireContext())
        val languageCache: String = Preferences.language
        if (languageCache.isEmpty()) {
            Preferences.language = "uz"
        }

        binding.apply {
            when (languageCache) {
                "uz" -> {
                    loginLanguage.setText("Uzbekcha")
                }

                "ru" -> {
                    loginLanguage.setText("Узбекча")
                }
            }

            loginLanguage.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val language = loginLanguage.text.toString()
                    languageText(language, requireContext())
                }
            })
        }

        return binding.root
    }
}