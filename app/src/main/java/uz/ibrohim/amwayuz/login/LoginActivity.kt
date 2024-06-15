package uz.ibrohim.amwayuz.login

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import uz.ibrohim.amwayuz.R
import uz.ibrohim.amwayuz.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    //Account == ibrokhimsoft@gmail.com
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}