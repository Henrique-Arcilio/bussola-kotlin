package com.example.myapplication

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
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager ?= null
    private var sensorAcelerometro : Sensor ?= null
    private var sensorMagnetico : Sensor ?= null;

    private var hasGravidade = false
    private var hasNorteMag = false

    private val vetorGravidade = FloatArray(3)
    private val vetorNorteMagnetico = FloatArray(3)

    private val matrizRotacao = FloatArray(9)

    private val angulosOrientacao = FloatArray(3)

    lateinit var bussolaImage : ImageView
    lateinit var tvRotacao : TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        sensorAcelerometro = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorMagnetico = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        bussolaImage = findViewById<ImageView>(R.id.bussulaImagem)
        tvRotacao = findViewById<TextView>(R.id.texto)

        if (sensorAcelerometro == null) {
            tvRotacao.text = "Sem acelerômetro"
        }

        if (sensorMagnetico == null) {
            tvRotacao.text = "Sem magnetômetro"
        }
    }


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onResume() {
        super.onResume()

        if(sensorMagnetico != null){
            sensorManager?.registerListener(this, sensorMagnetico, SensorManager.SENSOR_DELAY_GAME)

        }
        if(sensorAcelerometro != null){
            sensorManager?.registerListener(this, sensorAcelerometro, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onPause() {
        super.onPause();
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {

        if (event == null) return

        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, vetorGravidade, 0, 3)
            hasGravidade = true
        }

        if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, vetorNorteMagnetico, 0, 3)
            hasNorteMag = true
        }

        if(hasGravidade && hasNorteMag){

            val funcionou =  SensorManager.getRotationMatrix(matrizRotacao, null, vetorGravidade, vetorNorteMagnetico)

            if(funcionou){
                SensorManager.getOrientation(matrizRotacao, angulosOrientacao)

                var azimuth = Math.toDegrees(angulosOrientacao[0].toDouble()).toFloat()

                azimuth = (azimuth + 360) % 360

                bussolaImage.rotation = -azimuth
                tvRotacao.text = "Rotação: %d".format(azimuth.roundToInt())
            }
        }
    }
}