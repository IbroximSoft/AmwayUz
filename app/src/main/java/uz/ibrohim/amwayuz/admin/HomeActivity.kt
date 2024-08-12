package uz.ibrohim.amwayuz.admin

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import uz.ibrohim.amwayuz.R
import uz.ibrohim.amwayuz.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val navController by lazy {
        Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDrawerLayout()

        val close: ImageView = binding.navView.getHeaderView(0).findViewById(R.id.header_img)

        binding.apply {
            close.setOnClickListener {
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.homeFragment -> {
                        binding.adminText.text = getString(R.string.home_page)
                    }

                    R.id.productsFragment -> {
                        binding.adminText.text = getString(R.string.products_page)
                    }

                    R.id.employeeFragment -> {
                        binding.adminText.text = getString(R.string.employee_page)
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, binding.drawerLayout)
    }

    private fun setupDrawerLayout() {
        binding.navView.setupWithNavController(navController)
        binding.homeTool.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
//        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}