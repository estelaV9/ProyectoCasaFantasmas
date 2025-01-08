class Question<T> {
    val questionText: String
    val answer: T?

    // Constructor vacío
    constructor() {
        this.questionText = ""
        this.answer = null
    }

    // Constructor secundario con los parámetros questionText y answer
    constructor(questionText: String, answer: T?) {
        this.questionText = questionText
        this.answer = answer
    }

    companion object {
        private val questions: List<Question<*>> = listOf(
            Question("¿Cuánto es 2 + 2?", 4),
            Question("¿Capital de Francia?", "Paris"),
            Question("¿Es Kotlin un lenguaje de programación?", "si"),
            Question("¿Cuántos días tiene un año bisiesto?", 366),
            Question("¿Color del cielo?", "Azul"),
            Question("¿Cuántos continentes hay en la Tierra?", 7),
            Question("¿Cuál es el idioma oficial de Brasil?", "Portugues"),
            Question("¿Cuántas patas tiene una araña?", 8),
            Question("¿En qué año llegó el hombre a la Luna?", 1969),
            Question("¿Qué gas respiramos principalmente los seres humanos?", "Oxigeno"),
            Question("¿Cuál es el océano más grande del mundo?", "Pacifico"),
            Question("¿Qué planeta es conocido como el planeta rojo?", "Marte"),
            Question("¿Cuántos minutos tiene una hora?", 60),
            Question("¿Qué instrumento mide la temperatura?", "Termometro"),
            Question("¿Quién escribió *Don Quijote de la Mancha*?", "Miguel de Cervantes")
        )

        fun randomQuestion(): Question<*> {
            return questions.random()
        }
    }

    fun checkAnswer(userAnswer: Any): Boolean {
        return when (answer) {
            is Int -> userAnswer.toString().toIntOrNull() == answer
            is Boolean -> userAnswer.toString().toBooleanStrictOrNull() == answer
            is String -> userAnswer.toString().trim().equals(answer, ignoreCase = true)
            else -> false
        }
    }
}
