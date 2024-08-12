package uz.ibrohim.amwayuz

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import uz.ibrohim.amwayuz.admin.HomeActivity
import uz.ibrohim.amwayuz.databinding.ActivitySplashesBinding
import uz.ibrohim.amwayuz.login.LoginActivity
import uz.ibrohim.amwayuz.utils.Preferences
import uz.ibrohim.amwayuz.employee.EmployeeActivity

class SplashesActivity : AppCompatActivity() {

    // Account ibrokhimsoft@gmail.com
    private lateinit var binding: ActivitySplashesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Preferences.init(this)
        val uid: String = Preferences.uid
        val isAdmin: String = Preferences.isAdmin

        Handler(Looper.getMainLooper()).postDelayed({
            if (uid.isNotEmpty()) {
                if (isAdmin.toBoolean()) {
                    val intent = Intent(this, HomeActivity::class.java)
                    getIntents(intent)
                    return@postDelayed
                } else if (!isAdmin.toBoolean()) {
                    val intent = Intent(this, EmployeeActivity::class.java)
                    getIntents(intent)
                    return@postDelayed
                }
            }else{
                val intent = Intent(this, LoginActivity::class.java)
                getIntents(intent)
            }
        }, 1800)
    }

    private fun getIntents(intent: Intent) {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}