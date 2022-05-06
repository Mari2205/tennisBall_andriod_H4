package dk.hovdeforlob4.android.thetennisball

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

/**
 * @see https://www.youtube.com/watch?v=xcsuDDQHrLo
 * @see https://developer.android.com/reference/kotlin/android/hardware/SensorManager
 */
class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpAccelerometerSensor()
    }


    private fun setUpAccelerometerSensor(){
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(
            this,                        // listener
            it,                                 // sensor
            SensorManager.SENSOR_DELAY_FASTEST, // sampling period
            SensorManager.SENSOR_DELAY_FASTEST  // max report latency
            )
        }
    }


    override fun onSensorChanged(event: SensorEvent?) {
        val ball = findViewById<ImageView>(R.id.imageView_move)
        val txtview = findViewById<TextView>(R.id.txtView)

        if(event?.sensor?.type == Sensor.TYPE_ACCELEROMETER){
            val leftRight  = event.values[0]
            val upDown     = event.values[1]

            ball.apply {
                x = leftRight * 50f
                y = upDown    * 50f

                translationX = leftRight * -50
                translationY = upDown    * 150
            }

            val colour = if (upDown.toInt() == 0 && leftRight.toInt() == 0)
                Color.GREEN else Color.RED

            txtview.setBackgroundColor(colour)
            txtview.text = "up/down ${upDown.toInt()}\nleft/right ${leftRight.toInt()}"
        }
    }


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }


    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }

}