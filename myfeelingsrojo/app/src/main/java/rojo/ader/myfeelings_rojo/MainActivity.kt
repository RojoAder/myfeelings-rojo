package rojo.ader.myfeelings_rojo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import rojo.ader.myfeelings_rojo.utilities.CustomBarDrawable
import rojo.ader.myfeelings_rojo.utilities.CustomCircleDrawable
import rojo.ader.myfeelings_rojo.utilities.Emociones
import rojo.ader.myfeelings_rojo.utilities.JSONFile

class MainActivity : AppCompatActivity() {
    var jsonFile: JSONFile? = null
    var veryHappy = 0.0F
    var happy = 0.0F
    var neutral = 0.0F
    var sad = 0.0F
    var verysad = 0.0F
    var data: Boolean = false
    var lista = ArrayList<Emociones>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        jsonFile = JSONFile()
        fetchingData()

        val graphVeryHappy = findViewById(R.id.graphVeryHappy) as View
        val graphHappy = findViewById(R.id.graphHappy) as View
        val graphNeutral = findViewById(R.id.graphNeutral) as View
        val graphSad = findViewById(R.id.graphSad) as View
        val graphVerySad = findViewById(R.id.graphVerySad) as View
        val graph = findViewById(R.id.graphg) as ConstraintLayout
        val guardarButton = findViewById(R.id.guardarBoton) as Button
        val veryHappyButton = findViewById(R.id.veryHappyButton) as ImageButton
        val happyButton = findViewById(R.id.happyButton) as ImageButton
        val neutralButton = findViewById(R.id.neutralButton) as ImageButton
        val sadButton = findViewById(R.id.sadButton) as ImageButton
        val verySadButton = findViewById(R.id.verySadButton) as ImageButton

        if (!data) {
            var emociones = ArrayList<Emociones>()
            val fondo = CustomCircleDrawable(this, emociones)

            graphVeryHappy.background = CustomBarDrawable(
                this,
                Emociones("Muy Feliz", 0.0F, R.color.mustard, veryHappy)
            )
            graphHappy.background =
                CustomBarDrawable(this, Emociones("Feliz", 0.0F, R.color.orange, happy))
            graphNeutral.background =
                CustomBarDrawable(this, Emociones("Neutral", 0.0F, R.color.greenie, neutral))
            graphSad.background =
                CustomBarDrawable(this, Emociones("Triste", 0.0F, R.color.blue, sad))
            graphVerySad.background =
                CustomBarDrawable(this, Emociones("Muy Triste", 0.0F, R.color.deepBlue, verysad))

            graph.background = fondo
        } else {
            actualizarGrafica()
            iconoMayoria()
        }

        guardarButton.setOnClickListener {
            guardar()
        }

        veryHappyButton.setOnClickListener {
            veryHappy++
            iconoMayoria()
            actualizarGrafica()
        }

        happyButton.setOnClickListener {
            happy++
            iconoMayoria()
            actualizarGrafica()
        }

        neutralButton.setOnClickListener {
            neutral++
            iconoMayoria()
            actualizarGrafica()
        }

        sadButton.setOnClickListener {
            sad++
            iconoMayoria()
            actualizarGrafica()
        }
        verySadButton.setOnClickListener {
            verysad++
            iconoMayoria()
            actualizarGrafica()
        }

    }

    fun fetchingData() {
        try {
            var json: String = jsonFile?.getData(this) ?: ""
            if (json != "") {
                this.data = true
                var jsonArray: JSONArray = JSONArray(json)

                this.lista = parseJson(jsonArray)

                for (i in lista) {
                    when (i.nombre) {
                        "Muy feliz" -> veryHappy = i.total
                        "Feliz" -> happy = i.total
                        "Neutral" -> neutral = i.total
                        "Triste" -> sad = i.total
                        "Muy Triste" -> verysad = i.total
                    }
                }
            } else {
                this.data = false
            }
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }
    }

    fun iconoMayoria() {
        val icon = findViewById(R.id.icon) as ImageView
        if (happy > veryHappy && happy > neutral && happy > sad && happy > verysad) {
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_happy))
        }
        if (veryHappy > happy && veryHappy > neutral && veryHappy > sad && happy > verysad) {
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_veryhappy))
        }
        if (neutral > veryHappy && neutral > happy && neutral > sad && neutral > verysad) {
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_neutral))
        }
        if (sad > veryHappy && sad > neutral && sad > happy && sad > verysad) {
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_sad))
        }
        if (verysad > veryHappy && verysad > neutral && verysad > sad && verysad > happy) {
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_verysad))
        }
    }

    fun actualizarGrafica() {
        val total = veryHappy + happy + neutral + sad + verysad

        var pVH: Float = (veryHappy * 100 / total).toFloat()
        var pH: Float = (happy * 100 / total).toFloat()
        var pN: Float = (neutral * 100 / total).toFloat()
        var pS: Float = (sad * 100 / total).toFloat()
        var pVS: Float = (verysad * 100 / total).toFloat()

        Log.d("porcentajes", "very happy " + pVH)
        Log.d("porcentajes", "happy " + pH)
        Log.d("porcentajes", "neutral " + pN)
        Log.d("porcentajes", "sad " + pS)
        Log.d("porcentajes", "very sad " + pVS)

        lista.clear()
        lista.add(Emociones("Muy Feliz", pVH, R.color.mustard, veryHappy))
        lista.add(Emociones("Feliz", pH, R.color.orange, happy))
        lista.add(Emociones("Neutral", pN, R.color.greenie, neutral))
        lista.add(Emociones("Triste", pS, R.color.blue, sad))
        lista.add(Emociones("Muy Triste", pVS, R.color.deepBlue, verysad))

        val fondo = CustomCircleDrawable(this, lista)

        val graphVeryHappy = findViewById(R.id.graphVeryHappy) as View
        val graphHappy = findViewById(R.id.graphHappy) as View
        val graphNeutral = findViewById(R.id.graphNeutral) as View
        val graphSad = findViewById(R.id.graphSad) as View
        val graphVerySad = findViewById(R.id.graphVerySad) as View
        val graph = findViewById(R.id.graphg) as ConstraintLayout

        graphVeryHappy.background = CustomBarDrawable(
            this,
            Emociones("Muy Feliz", pVH, R.color.mustard, veryHappy)
        )
        graphHappy.background =
            CustomBarDrawable(this, Emociones("Feliz", pH, R.color.orange, happy))
        graphNeutral.background =
            CustomBarDrawable(this, Emociones("Neutral", pN, R.color.greenie, neutral))
        graphSad.background = CustomBarDrawable(this, Emociones("Triste", pS, R.color.blue, sad))
        graphVerySad.background =
            CustomBarDrawable(this, Emociones("Muy Triste", pVS, R.color.deepBlue, verysad))

        graph.background = fondo

    }

    fun parseJson(jsonArray: JSONArray): ArrayList<Emociones> {
        var lista = ArrayList<Emociones>()

        for (i in 0..jsonArray.length()) {
            try {
                val nombre = jsonArray.getJSONObject(i).getString("nombre")
                val porcentaje = jsonArray.getJSONObject(i).getDouble("porcentaje").toFloat()
                val color = jsonArray.getJSONObject(i).getInt("color")
                val total = jsonArray.getJSONObject(i).getDouble("total").toFloat()
                var emocion = Emociones(nombre, porcentaje, color, total)
                lista.add(emocion)
            } catch (exception: JSONException) {
                exception.printStackTrace()
            }
        }
        return lista
    }

    fun guardar() {
        var jsonArray = JSONArray()
        var o: Int = 0

        for (i in lista) {
            Log.d("objetos", i.toString())
            var j = JSONObject()
            j.put("nombre", i.nombre)
            j.put("porcentaje", i.porcentaje)
            j.put("color", i.color)
            j.put("total", i.total)

            jsonArray.put(o, j)
            o++
        }

        jsonFile?.saveData(this, jsonArray.toString())

        Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()
    }
}