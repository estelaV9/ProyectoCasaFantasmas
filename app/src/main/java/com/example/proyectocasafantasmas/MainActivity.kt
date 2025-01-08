package com.example.proyectocasafantasmas

import Question
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.casafantasmas.CustomDialogFragment
import com.example.proyectocasafantasmas.databinding.ActivityMainBinding
import kotlin.system.exitProcess

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
        displayMoves(randomRowStart,randomColumnStart)
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
        imageFinish.setImageResource(R.drawable.candy)
    }


    fun displayMoves(row:Int,column:Int) {
        //Con respecto a donde ha empezado el usuario, conseguimos las posiciones adyacentes
        val bottomPosition:Int = (row+1) * 4  + column //Adyacente inferior
        val topPosition:Int = (row-1) * 4 + column //Adyacente superior
        val rightPosition:Int = row * 4  + (column+1) //Adyacente derecha
        val leftPosition:Int = row * 4  + (column-1) //Adyacente izquierda

        println("bottomPosition $bottomPosition")
        println("topPosition $topPosition")
        println("rightPosition $rightPosition")
        println("leftPosition $leftPosition")

        listaPosiciones = mutableListOf(bottomPosition,topPosition,rightPosition,leftPosition)
        //Si la columna es 0, ignorar leftPosition
        if (column == 0) listaPosiciones.removeAt(3)
        //Si la columna es 3, ignorar rightPosition
        if (column == 3) listaPosiciones.removeAt(2)
        // HAGO UN ITERATOR PARA PODER MODIFICAR UNA LISTA MIENTRAS LA RECORRO
        val iterator = listaPosiciones.iterator()
        while (iterator.hasNext()) {
            val move = iterator.next()
            println("move $move")
            var answer = ""
            var question:Question<*>
            if (move in 0..15) {//Si el movimiento es mayor a 0 y menor a 16
                println("move filtrado $move")
                val card = gridLayout.getChildAt(move) as CardView
                val image = card.findViewById<ImageView>(R.id.imageView)
                image.setImageResource(R.drawable.img_2)
                // ESTABLEZCO UN LISTENER PARA CUANDO HAGA CLICK A LAS TARJETAS DISPONIBLES PARA MOVERSE
                image.setOnClickListener {

                    // CONFIGURACION DEL DIALOG FRAGMENT
                    val dialog = CustomDialogFragment()
                    val args = Bundle()
                    question = Question.randomQuestion()
                    args.putString("question_text", question.questionText)
                    dialog.arguments = args

                    // LISTENER PARA EL RESULT DEL DIALOG FRAGMENT PASANDO PARAMETROS POR BUNDLE
                    supportFragmentManager.setFragmentResultListener("dialog_result",this) {_, bundle ->
                        answer = bundle.getString("answer", "")
                        if (question.checkAnswer(answer)) {
                            imageUser.setImageResource(R.drawable.img_3)
                            // GUARDAMOS LA POSICION DEL USUARIO
                            imageUser = image
                            cardUser = card
                            resetOldPositions()
                            image.setImageResource(R.drawable.img_1)
                            // VARIABLES PARA CONSEGUIR LA POSICION DE LA CARD CLICKEADA
                            val newRow = move / 4
                            val newColumn = move % 4
                            println(listaPosiciones)
                            // CON LA NUEVA POSICION, VOLVEMOS A ENTRAR EN ESTE METODO
                            displayMoves(newRow,newColumn)
                            //cambiar antiguas posiciones a fondo predeterminado
                        } else  {
                            Toast.makeText(this, "Respuesta incorrecta, prueba otra vez", Toast.LENGTH_SHORT).show()
                        }
                    }
                    dialog.show(supportFragmentManager,"customDialog")
                }
            } else {
                // ELIMINAMOS EL ELEMENTO, PARA QUE NO DE ERROR AL USAR EL METODO resetOldPositions
                // POR INTENTAR ACCEDER A UN INDEX DEL LAYOUT QUE NO EXISTE
                println("elemento eliminado $move")
                iterator.remove()
            }


            if (move == cardFinishPosition + 1) {
                Toast.makeText(this,
                    "¡Felicidades, encontraste los dulces!", Toast.LENGTH_LONG).show()
                exitProcess(0) // SALIR DEL PROGRAMA
            } // SI LA POSICION COINCIDE CON LA POSICION DE DONDE ESTA EL CARAMELO, SE CIERRA
        }
    }
    fun resetOldPositions() { // PONEMOS LAS ANTIGUAS POSICIONES ADYACENTES A PREDETERMINADAS
        for (i in listaPosiciones) {
            val card = gridLayout.getChildAt(i) as CardView
            val image = card.findViewById<ImageView>(R.id.imageView)
            image.setImageResource(R.drawable.ic_launcher_background)
            // DESACTIVAMOS LOS LISTENERS ANTIGUOS
            image.setOnClickListener(null)
        }
    }
}