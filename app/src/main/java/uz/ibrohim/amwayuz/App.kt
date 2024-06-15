package uz.ibrohim.amwayuz

import android.app.Application
import com.yariksoffice.lingver.Lingver

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        Lingver.init(this,"uz")
    }

    companion object {
        lateinit var instance: App
    }
}