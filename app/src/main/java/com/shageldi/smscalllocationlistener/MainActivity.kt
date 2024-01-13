package com.shageldi.smscalllocationlistener

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.shageldi.smscalllocationlistener.database.AppDatabase
import com.shageldi.smscalllocationlistener.ui.theme.SmsCallLocationListenerTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppDatabase.getDatabase(this)
        setContent {
            SmsCallLocationListenerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val isGranted = rememberMultiplePermissionsState(
                        permissions = listOf(
                            android.Manifest.permission.RECEIVE_SMS,
                            android.Manifest.permission.SEND_SMS,
                            android.Manifest.permission.READ_CONTACTS,
                            android.Manifest.permission.READ_PHONE_STATE,
                            android.Manifest.permission.READ_CALL_LOG,
                            android.Manifest.permission.PROCESS_OUTGOING_CALLS,
                        )
                    )

//                    val pm: PowerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
//                    if(!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && pm.isIgnoringBatteryOptimizations(
//                            context.packageName
//                        ))){
//                        val intent = Intent()
//                        intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
//                        intent.data = Uri.parse("package:${context.packageName}")
//                        context.startActivity(intent)
//                    }





                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (isGranted.allPermissionsGranted) {
                            Text(text = "Ahli rugsatlar berilen")
                        } else {
                            Text(text = "Gerekli rugsatlar berilmedi")
                            Button(onClick = {  isGranted.launchMultiplePermissionRequest()


                            }) {
                                Text(text = "Rugsat ber")
                            }
                        }
                    }
                }
            }
        }
    }
}