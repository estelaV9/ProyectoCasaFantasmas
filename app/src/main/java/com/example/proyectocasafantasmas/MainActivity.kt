package com.example.proyectocasafantasmas

import android.os.Bundle
import android.util.Log
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
        initComponents()
        initGridLayout()
        assingCardsToArray()
    }

    private fun initComponents() {
        mibinding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(mibinding.root)
        gridLayout = mibinding.gridLayout
    } // METODO PAR INICIALIZAR COMPONENTES

    private fun assingCardsToArray() {
        for(row in 0 until 4) {
            for (column in 0 until 4) {
                val index = row * 4 + column // con row * 4 accedemos a la columna y al sumarle la columna, accedemos a la fila (un indice del
                val cardView = gridLayout.getChildAt(index) as CardView // DECLARAR EL CARD ACCEDIENDO AL cardview DE ESA POSICION CON EL INDEX
                board[row][column] = cardView // LA GUARDAMOS EN LA POSICION DEL ARRAY
                Log.d("CardAssignment", "board[$row][$column] = $cardView") // Log para comprobación
            } // BUCLE PARA RECORRER LAS COLUMNAS
        } // BUCLE PARA RECORRER LAS FILAS
    } // METODO PARA ASIGNAR LAS CARDS AL ARRAY

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
        initStartFinish()
    } // INICIAR EL GridLayout

    private fun initStartFinish() {
        // SI COINCIDEN EN EL MISMO SITIO O EN POSICIONES ADYACENTES VERTICAL Y HORIZONTAL, VOLVEMOS A GENERAR LAS POSICIONES
        while (randomRowStart == randomRowFinish+1 || randomRowStart == randomRowFinish-1 ||
            randomColumnStart == randomColumnFinish+1 || randomColumnStart == randomColumnFinish-1 ||
            (randomRowStart == randomRowFinish && randomColumnStart == randomColumnFinish)) {
            randomRowStart = (0..3).random()
            randomColumnStart = (0..3).random()
        }
        // LO PASAMOS A INDICE PARA EL GridLayout
        cardStartPosition = randomRowStart * 4 + randomColumnStart
        cardFinishPosition = randomRowFinish * 4 + randomColumnFinish
        println("cardStartPosition $cardStartPosition")
        updateUserPosition(cardStartPosition)
    }  // METODO PARA DECLARAR LOS INDICES DE LA POSICION


    fun updateUserPosition(position:Int) {
        //Con el indice generado, podemos acceder a la tarjeta de una posicion en el gridlayout
        cardUser = gridLayout.getChildAt(position) as CardView

        //con la tarjeta ya podemos inicializar la imagen del usuario
        imageUser = cardUser.findViewById(R.id.imageView)
        imageUser.setImageResource(R.drawable.img_1)

        //Hacemos lo mismo con otra posicion para el final
        val cardFinish = gridLayout.getChildAt(cardFinishPosition) as CardView
        val imageFinish = cardFinish.findViewById<ImageView>(R.id.imageView)
        imageFinish.setImageResource(R.drawable.img)
    }

}