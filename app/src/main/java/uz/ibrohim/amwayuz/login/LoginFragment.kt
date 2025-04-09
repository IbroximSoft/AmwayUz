package uz.ibrohim.amwayuz.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.ibrohim.amwayuz.R
import uz.ibrohim.amwayuz.admin.HomeActivity
import uz.ibrohim.amwayuz.databinding.FragmentLoginBinding
import uz.ibrohim.amwayuz.utils.Preferences
import uz.ibrohim.amwayuz.utils.errorToast
import uz.ibrohim.amwayuz.utils.languageText
import uz.ibrohim.amwayuz.utils.progressInterface
import uz.ibrohim.amwayuz.utils.warningToast
import uz.ibrohim.amwayuz.employee.EmployeeActivity


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        //@amway.uz
//        networkChecking()
        auth = FirebaseAuth.getInstance()
        reference = FirebaseDatabase.getInstance().reference

        Preferences.init(requireContext())
        val languageCache: String = Preferences.language
        if (languageCache.isEmpty()) {
            Preferences.language = "uz"
        }

        binding.apply {
            progressInterface(circularProgress)

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

                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val language = loginLanguage.text.toString()
                    languageText(language, requireContext())
                }
            })

            loginBtn.setOnClickListener {
                userLogin()
            }
        }

        return binding.root
    }

//    fun networkChecking() {
//        val networkConnection = NetworkConnection(activity?.applicationContext!!)
//        networkConnection.observe(requireActivity()) {
//            if (!it) {
//                CustomizedAlertDialog.callAlertDialog(requireContext(), "Diqqat!",
//                    "Sizda internet bilan aloqa yo'q!",
//                    "Tekshirish", "", false,
//                    object : CustomizedAlertDialogCallback<String> {
//                        override fun alertDialogCallback(callback: String) {
//                            if (callback == "1") {
//                                networkChecking()
//                            }
//                        }
//                    })
//            }
//        }
//    }

    private fun userLogin() {
        binding.apply {
            val login = loginName.text.toString()
            val password = loginPassword.text.toString()

            if (login.isEmpty() || password.isEmpty()) {
                warningToast(getString(R.string.fill_them_all))
            } else {
                loginBtn.isVisible = false
                loginProgress.isVisible = true
                auth.signInWithEmailAndPassword("$login@amway.uz", password)
                    .addOnCompleteListener(requireActivity()) {
                        loginBtn.isVisible = false
                        loginProgress.isVisible = true
                        if (it.isSuccessful) {
                            statusSuccess()
                        } else {
                            errorToast(getString(R.string.login_error))
                        }
                    }
            }
        }

    }

    private fun statusSuccess() {
        val currentUserId = auth.currentUser!!.uid
        reference.child("users").child(currentUserId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.child("name").value.toString()
                    val isAdmin = snapshot.child("status").value.toString()

                    Preferences.uid = currentUserId
                    Preferences.name = name
                    Preferences.isAdmin = isAdmin

                    if (isAdmin.toBoolean()) {
                        val intent = Intent(requireContext(), HomeActivity::class.java)
                        getIntents(intent)
                        return
                    } else if (!isAdmin.toBoolean()) {
                        val intent = Intent(requireContext(), EmployeeActivity::class.java)
                        getIntents(intent)
                        return
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun getIntents(intent: Intent) {
        startActivity(intent)
        activity?.finish()
    }

    override fun onResume() {
        super.onResume()
        val language = resources.getStringArray(R.array.language)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.language_item, language)
        binding.loginLanguage.setAdapter(arrayAdapter)
    }
}