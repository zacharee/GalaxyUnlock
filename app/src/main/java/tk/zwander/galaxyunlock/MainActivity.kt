package tk.zwander.galaxyunlock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.runtime.*
import androidx.compose.ui.platform.setContent
import androidx.ui.tooling.preview.Preview
import tk.zwander.galaxyunlock.ui.MainContent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainContent()
        }
    }
}

@Preview
@Composable
fun Preview() {
    MainContent()
}
