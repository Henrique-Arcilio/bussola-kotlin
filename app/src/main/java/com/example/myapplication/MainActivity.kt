package com.example.myapplication

import android.content.ContentValues.TAG
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(), SensorEventListener {

    var sensorManager: SensorManager ?= null
    var sensor : Sensor ?= null

    lateinit var bussolaImage : ImageView
    lateinit var texto : TextView

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        print(p1)
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        Log.d(TAG, "onSensorChanged: " + p0?.values[0])
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause();
        sensorManager?.unregisterListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        bussolaImage = findViewById<ImageView>(R.id.bussulaImagem)
        texto = findViewById<TextView>(R.id.texto)
    }
}