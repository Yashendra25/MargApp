package com.yashendra.margapp_2.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yashendra.margapp_2.Model.Question
import com.yashendra.margapp_2.R

class QuestionAdapter(val context: Context, private val questions: MutableMap<String, Question>?) :
    RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define your view components (TextViews, CheckBoxes) here
        // Initialize them using itemView.findViewById()
        var questionNumber = itemView.findViewById<TextView>(R.id.questionNumberTextView)
        var question = itemView.findViewById<TextView>(R.id.questionDescriptionTextView)
        var option1=itemView.findViewById<CheckBox>(R.id.option1CheckBox)
        var option2=itemView.findViewById<CheckBox>(R.id.option2CheckBox)
        var option3=itemView.findViewById<CheckBox>(R.id.option3CheckBox)
        var option4=itemView.findViewById<CheckBox>(R.id.option4CheckBox)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.question_card, parent, false
        )
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        // Bind data from the questions list to the views in the question_card.xml layout
        val question = questions?.get("question${position + 1}")

        holder.questionNumber.text = "Question ${position + 1}"
        holder.question.text = question?.description
        holder.option1.text = question?.option1
        holder.option2.text = question?.option2
        holder.option3.text = question?.option3
        holder.option4.text = question?.option4

        // Handle checkbox clicks
        holder.option1.setOnClickListener {
            handleCheckboxClick(holder.option1 , question, 1)
        }
        holder.option2.setOnClickListener {
            handleCheckboxClick(holder.option2, question, 2)
        }
        holder.option3.setOnClickListener {
            handleCheckboxClick(holder.option3, question, 3)
        }
        holder.option4.setOnClickListener {
            handleCheckboxClick(holder.option4, question, 4)
        }
    }

    private fun handleCheckboxClick(
        checkbox: CheckBox,
        question: Question?,
        optionNumber: Int
    ) {
        if (checkbox.isChecked) {
            // Update user's answer based on the clicked checkbox
            if (question != null) {
                question?.userAnswer = when (optionNumber) {
                    1 -> question.option1
                    2 -> question.option2
                    3 -> question.option3
                    4 -> question.option4
                    else -> ""
                }
            }
        } else {
            // Checkbox is unchecked, clear user's answer
            question?.userAnswer = ""
        }
    }

    override fun getItemCount(): Int {
        return questions?.size ?: 0
    }
}
