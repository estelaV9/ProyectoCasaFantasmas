package com.example.proyectocasafantasmas

import Question
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.casafantasmas.CustomDialogFragment
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
    var randomColumnStart = (0..3).random()
    var randomRowFinish = (0..3).random()
    var randomColumnFinish = (0..3).random()

    // VARIABLES PARA LA POSICION INICIAL DEL USUARIO Y DEL FINAL
    var cardStartPosition: Int = 0
    var cardFinishPosition: Int = 0

    // VARIABLES PARA POSICIONES DE FANTASMAS
    val ghostPositions = mutableListOf<Int>() // LISTA PARA GUARDAR LAS POSICIONES DE LOS FANTASMAS

    // VARIABLE PARA GUARDAR POSICIONES ADYACENTES ANTIGUAS
    lateinit var listaPosiciones: MutableList<Int>

    // VARIABLE PARA CONTROLAR SI EL JUEGO HA TERMINADO
    var gameFinished: Boolean = false // VARIABLE PARA EVITAR MAS MOVIMIENTOS AL GANAR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        initComponents()
        initGridLayout()
        assingCardsToArray()
        placeGhosts() // COLOCAR FANTASMAS
        displayMoves(randomRowStart, randomColumnStart)
    }

    private fun initComponents() {
        mibinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mibinding.root)
        gridLayout = mibinding.gridLayout

        // CONFIGURAR BOTON DE REINICIAR
        mibinding.restartButton.setOnClickListener {
            restartGame()
        }
    } // METODO PARA INICIALIZAR COMPONENTES

    private fun assingCardsToArray() {
        for (row in 0 until 4) {
            for (column in 0 until 4) {
                val index = row * 4 + column // CALCULA EL INDICE EN EL GRID
                val cardView =
                    gridLayout.getChildAt(index) as CardView // OBTIENE LA TARJETA EN ESA POSICION
                board[row][column] = cardView // GUARDA LA TARJETA EN EL ARRAY
                Log.d("CardAssignment", "board[$row][$column] = $cardView")
                // Log para comprobacion
            }
        }
    } // METODO PARA ASIGNAR LAS CARDS AL ARRAY

    private fun initGridLayout() {
        for (i in 1..16) {
            // CREAMOS UNA CARDVIEW
            cardView =
                LayoutInflater.from(this).inflate(R.layout.card, gridLayout,
                    false) as CardView

            // CREAMOS EL IMAGEVIEW DENTRO DE LA TARJETA
            imageView = cardView.findViewById(R.id.imageView)

            // ESTABLECEMOS EL FONDO PREDETERMINADO DE LAS TARJETAS
            imageView.setImageResource(R.drawable.ic_launcher_background)

            // AÑADIMOS LA TARJETA AL GRIDLAYOUT
            gridLayout.addView(cardView)
        }
        initStartFinish()
    } // METODO PARA INICIAR EL GRIDLAYOUT

    private fun initStartFinish() {
        // ASEGURAMOS QUE LAS POSICIONES INICIAL Y FINAL NO SEAN ADYACENTES O IGUALES
        while (randomRowStart == randomRowFinish + 1 || randomRowStart == randomRowFinish - 1 ||
            randomColumnStart == randomColumnFinish + 1 ||
            randomColumnStart == randomColumnFinish - 1 ||
            (randomRowStart == randomRowFinish && randomColumnStart == randomColumnFinish)
        ) {
            randomRowStart = (0..3).random()
            randomColumnStart = (0..3).random()
        }
        // CONVERTIMOS LAS POSICIONES EN INDICES DEL GRIDLAYOUT
        cardStartPosition = randomRowStart * 4 + randomColumnStart
        cardFinishPosition = randomRowFinish * 4 + randomColumnFinish
        println("cardStartPosition $cardStartPosition")
        updateUserPosition(cardStartPosition)
    }

    private fun placeGhosts() {
        // COLOCAR FANTASMAS EN POSICIONES ALEATORIAS
        while (ghostPositions.size < 3) { // GENERAR 3 FANTASMAS
            val randomPosition = (0 until 16).random()
            if (randomPosition != cardStartPosition && randomPosition != cardFinishPosition &&
                !ghostPositions.contains(
                    randomPosition
                )
            ) {
                ghostPositions.add(randomPosition)
            }
        }
    } // METODO PARA COLOCAR LOS FANTASMAS

    fun updateUserPosition(position: Int) {
        // ACCEDEMOS A LA TARJETA EN LA POSICION ACTUAL
        cardUser = gridLayout.getChildAt(position) as CardView

        // INICIALIZAMOS LA IMAGEN DEL USUARIO
        imageUser = cardUser.findViewById(R.id.imageView)
        imageUser.setImageResource(R.drawable.user_img)

        // CONFIGURAMOS LA TARJETA FINAL
        val cardFinish = gridLayout.getChildAt(cardFinishPosition) as CardView
        val imageFinish = cardFinish.findViewById<ImageView>(R.id.imageView)
        imageFinish.setImageResource(R.drawable.candy)
    }

    fun displayMoves(row: Int, column: Int) {
        // SI EL JUEGO YA TERMINO, EVITAR NUEVOS MOVIMIENTOS
        if (gameFinished) return // EVITA QUE SE EJECUTE EL METODO SI EL JUEGO HA TERMINADO

        // CALCULAMOS LAS POSICIONES ADYACENTES
        val bottomPosition: Int = (row + 1) * 4 + column
        val topPosition: Int = (row - 1) * 4 + column
        val rightPosition: Int = row * 4 + (column + 1)
        val leftPosition: Int = row * 4 + (column - 1)

        // FILTRAR POSICIONES INVALIDAS
        listaPosiciones = mutableListOf(bottomPosition, topPosition, rightPosition, leftPosition)
            .filter { it in 0..15 } // Solo posiciones dentro del tablero
            .filterIndexed { index, _ ->
                !(column == 0 && index == 3) && !(column == 3 && index == 2) // Ignorar bordes
            }.toMutableList()

        // RECORRER POSICIONES VALIDAS
        listaPosiciones.forEach { move ->
            val card = gridLayout.getChildAt(move) as CardView
            val image = card.findViewById<ImageView>(R.id.imageView)

            // Mostrar signo de pregunta
            image.setImageResource(R.drawable.question)

            // Configurar clic para esta posicion
            image.setOnClickListener {
                if (ghostPositions.contains(move)) {
                    // SI ENCONTRAMOS UN FANTASMA
                    Toast.makeText(
                        this,
                        "¡Has encontrado un fantasma! Responde dos preguntas para escapar.",
                        Toast.LENGTH_LONG
                    ).show()

                    var correctAnswers = 0
                    val totalQuestions = 2

                    fun showQuestionDialog() {
                        val dialog = CustomDialogFragment()
                        val args = Bundle()

                        val question: Question<*> = Question.randomQuestion()
                        args.putString("question_text", question.questionText)
                        dialog.arguments = args

                        supportFragmentManager.setFragmentResultListener(
                            "dialog_result",
                            this
                        ) { _, bundle ->
                            val answer = bundle.getString("answer", "")
                            if (question.checkAnswer(answer)) {
                                correctAnswers++
                                if (correctAnswers < totalQuestions) {
                                    // Mostrar otra pregunta si no se han respondido todas
                                    Toast.makeText(
                                        this,
                                        "¡Respuesta correcta! Responde otra pregunta.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    showQuestionDialog()
                                } else {
                                    // Respuestas correctas, mover al jugador
                                    Toast.makeText(
                                        this,
                                        "¡Has escapado del fantasma!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    imageUser.setImageResource(R.drawable.user_img)
                                    imageUser = image
                                    cardUser = card
                                    resetOldPositions()
                                    image.setImageResource(R.drawable.user_img)

                                    // Actualizar posicion y llamar de nuevo a displayMoves
                                    val newRow = move / 4
                                    val newColumn = move % 4
                                    displayMoves(newRow, newColumn)
                                }
                            } else {
                                // Respuesta incorrecta
                                Toast.makeText(
                                    this,
                                    "Respuesta incorrecta, el fantasma te atrapo.",
                                    Toast.LENGTH_LONG
                                ).show()
                                correctAnswers = 0 // Reinicia el contador
                                dialog.dismiss()
                            }
                        }
                        dialog.show(supportFragmentManager, "CustomDialog")
                    }

                    showQuestionDialog()
                } else {
                    // SI ES UNA POSICION NORMAL O LA FINAL
                    val dialog = CustomDialogFragment()
                    val args = Bundle()

                    val question: Question<*> = Question.randomQuestion()
                    args.putString("question_text", question.questionText)
                    dialog.arguments = args

                    supportFragmentManager.setFragmentResultListener(
                        "dialog_result",
                        this
                    ) { _, bundle ->
                        val answer = bundle.getString("answer", "")
                        if (question.checkAnswer(answer)) {
                            if (move == cardFinishPosition) {
                                // SI ES LA POSICION FINAL
                                Toast.makeText(
                                    this,
                                    "¡Felicidades, encontraste los dulces!",
                                    Toast.LENGTH_LONG
                                ).show()
                                gameFinished = true
                                resetAllCards()
                            } else {
                                // Mover al jugador
                                imageUser.setImageResource(R.drawable.user_img)
                                imageUser = image
                                cardUser = card
                                resetOldPositions()
                                image.setImageResource(R.drawable.user_img)

                                // Actualizar posiciOn y llamar de nuevo a displayMoves
                                val newRow = move / 4
                                val newColumn = move % 4
                                displayMoves(newRow, newColumn)
                            }
                        } else {
                            // Respuesta incorrecta
                            Toast.makeText(
                                this,
                                "Respuesta incorrecta, prueba otra vez.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    dialog.show(supportFragmentManager, "customDialog")
                }
            }
        }
    }



    fun restartGame() {
        // Reiniciar variables del juego
        gameFinished = false
        randomRowStart = (0..3).random()
        randomColumnStart = (0..3).random()
        randomRowFinish = (0..3).random()  // Reiniciar posicion de finalizacion
        randomColumnFinish = (0..3).random()

        listaPosiciones.clear()
        ghostPositions.clear()

        // Limpiar las imagenes de todas las tarjetas
        for (i in 0 until gridLayout.childCount) {
            val card = gridLayout.getChildAt(i) as CardView
            val image = card.findViewById<ImageView>(R.id.imageView)
            image.setImageResource(R.drawable.ic_launcher_background)
            image.setOnClickListener(null) // Eliminar listeners anteriores
        }

        // Generar nuevas posiciones de inicio y final
        initStartFinish()  // Regeneramos las posiciones aleatorias de inicio y final

        // Colocar nuevos fantasmas
        placeGhosts()

        // Mostrar movimientos iniciales
        displayMoves(randomRowStart, randomColumnStart)

        // Mostrar mensaje de reinicio
        Toast.makeText(this, "Juego reiniciado!", Toast.LENGTH_SHORT).show()
    }


    fun resetOldPositions() {
        for (i in listaPosiciones) {
            val card = gridLayout.getChildAt(i) as CardView
            val image = card.findViewById<ImageView>(R.id.imageView)
            image.setImageResource(R.drawable.ic_launcher_background)
            image.setOnClickListener(null)
        }
    }

    fun resetAllCards() {
        // RECORREMOS TODAS LAS TARJETAS Y DESACTIVAMOS LOS LISTENERS
        for (i in 0 until gridLayout.childCount) {
            val card = gridLayout.getChildAt(i) as CardView
            val image = card.findViewById<ImageView>(R.id.imageView)
            image.setOnClickListener(null)
        }
    } // METODO PARA DESACTIVAR TODOS LOS MOVIMIENTOS AL TERMINAR EL JUEGO
}
