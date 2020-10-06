package today.kinema

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity


class SplashScreenActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.myLooper()!!).postDelayed({ // This method will be executed once the timer is over
            // Start your app main activity
            val i = Intent(this, MainActivity::class.java)

            startActivity(i)

            // close this activity
            finish()
        }, SPLASH_TIME_OUT)
    }
}