package com.example.proyectocasafantasmas

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.GridLayout
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectocasafantasmas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var mibinding: ActivityMainBinding
    lateinit var gridLayout: GridLayout
    lateinit var cardView: CardView
    lateinit var imageView: ImageView

    // ARRAY PARA CREAR UN 4X4 DE CARDS
    val board = Array(4) { arrayOfNulls<CardView>(4) }

    // VARIABLES PARA SABER LA POSICION DEL USUARIO
    lateinit var cardUser: CardView
    lateinit var imageUser: ImageView

    // GENERAMOS POSICIONES ALEATORIAS
    var randomRowStart = (0..3).random()
    var randomColumnStart=(0..3).random()
    val randomRowFinish=(0..3).random()
    val randomColumnFinish=(0..3).random()

    // VARIABLES PARA LA POSICION INICIAL DEL USUARIO Y DEL FINAL
    var cardStartPosition:Int = 0
    var cardFinishPosition:Int = 0

    // VARIABLE PARA GUARDAR POSICIONES ADYACENTES ANTIGUAS
    lateinit var listaPosiciones:MutableList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        initGridLayout()
    }

    private fun initGridLayout() {
        for (i in 1..16) {
            // CREAMOS UNA cardView
            cardView = LayoutInflater.from(this).inflate(R.layout.card, gridLayout, false) as CardView

            // CREAMOS EL imageView DE DENTRO DE LA TARJETA
            imageView = cardView.findViewById(R.id.imageView)

            // SETTEAMOS EL FONDO PREDETERMINADO DE LAS TARJETAS
            imageView.setImageResource(R.drawable.ic_launcher_background)

            // AÑADIRMOS LA VISTA DE LA TARJETA AL gridLayout
            gridLayout.addView(cardView)
        } // BUCLE PARA RECORRER EL GRID
    } // INICIAR EL GridLayout
}