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
            Question("Tengo agujas pero no coso. ¿Qué soy?", "Reloj"),
            Question("Siempre corro pero no tengo pies. ¿Qué soy?", "Agua"),
            Question("Cuanto más me quitas, más grande soy. ¿Qué soy?", "Agujero"),
            Question("Si me nombras, desaparezco. ¿Qué soy?", "Silencio"),
            Question("Tengo ciudades, pero no casas; montañas, pero no árboles. ¿Qué soy?", "Mapa"),
            Question("Si tienes 2 peces y 1 se ahoga, ¿cuántos te quedan?", "Dos"),
            Question("¿Qué animal salta más alto que una casa?", "Todos"),
            Question("¿Qué animal lleva en su nombre una fruta?", "Mono"),
            Question("Siempre subo pero nunca bajo. ¿Qué soy?", "Edad"),
            Question("¿Qué cosa puedes romper sin tocarla?", "Promesa"),
            Question("Si dos son compañía y tres son multitud, ¿qué son cuatro y cinco?", "Nueve"),
            Question("Lana sube y lana baja. ¿De qué se trata?", "Navaja")
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