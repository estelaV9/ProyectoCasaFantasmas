package com.example.casafantasmas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.proyectocasafantasmas.R

class CustomDialogFragment: DialogFragment(){
    private lateinit var questionText: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.dialog_fragment, container, false)

        // OBTENEMOS EL TEXTO DEL BUNDLE
        questionText = arguments?.getString("question_text") ?: "Pregunta no encontrada"
        // COGEMOS EL TEXTVIEW CON EL ROOTVIEW
        val textView = rootView.findViewById<TextView>(R.id.textViewQuestion)
        // SETEAMOS EL TEXTVIEW CON LA PREGUNTA
        textView.text = questionText

        val cancelButton = rootView.findViewById<Button>(R.id.cancel_button)

        cancelButton.setOnClickListener {
            dismiss()
        }

        val submitButton = rootView.findViewById<Button>(R.id.submit_button)
        val textEditAnswer = rootView.findViewById<EditText>(R.id.editTextAnswer)
        submitButton.setOnClickListener {
            val answer = textEditAnswer.text.toString()
            val result = Bundle()
            result.putString("answer", answer)
            parentFragmentManager.setFragmentResult("dialog_result", result)
            dismiss()
        }
        return rootView
    }
}