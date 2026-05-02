package com.example.myapplication

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(), SensorEventListener {

    var sensorManager: SensorManager ?= null
    var accelerometerSensor : Sensor ?= null
    var magneticSensor : Sensor ?= null;
    var hasGravidade = false
    var hasNorteMag = false
    var vetorGravidade = FloatArray(3)
    var vetorNorteMagnetico = FloatArray(3)


    lateinit var bussolaImage : ImageView
    lateinit var texto : TextView

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {

        if (event == null) return

        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            vetorGravidade = event.values.clone()
            hasGravidade = true
        }

        if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            vetorNorteMagnetico = event.values.clone()
            hasNorteMag = true
        }

        if(!hasGravidade || !hasNorteMag){
            return
        }

        var rotacao = FloatArray(9);
        var orientacao = FloatArray(3)

        val funcionou =  SensorManager.getRotationMatrix(rotacao, null, vetorGravidade, vetorNorteMagnetico)

        if(!funcionou){
            return
        }

        SensorManager.getOrientation(rotacao, orientacao)

        var azimuth = Math.toDegrees(orientacao[0].toDouble()).toFloat()

        if (azimuth < 0) {
            azimuth += 360
        }

        bussolaImage.rotation = -azimuth
        texto.text = "Rotação: %d".format(azimuth.toInt())

    }

    override fun onResume() {
        super.onResume()

        if(magneticSensor != null){
            sensorManager?.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL)

        }
        if(accelerometerSensor != null){
            sensorManager?.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause();
        sensorManager?.unregisterListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometerSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magneticSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        bussolaImage = findViewById<ImageView>(R.id.bussulaImagem)
        texto = findViewById<TextView>(R.id.texto)
    }
}