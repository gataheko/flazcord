package com.jakarispann.flashcard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FlashcardAdapter(
    private val cards: List<Pair<String, String>>,
    private val onItemClick: (Pair<String, String>) -> Unit
) : RecyclerView.Adapter<FlashcardAdapter.FlashcardViewHolder>() {

    // ViewHolder: holds references to the views for each list item
    class FlashcardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvQuestion: TextView = itemView.findViewById(R.id.tvQuestion)
        val tvAnswer: TextView = itemView.findViewById(R.id.tvAnswer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashcardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_flashcard, parent, false)
        return FlashcardViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlashcardViewHolder, position: Int) {
        val card = cards[position]
        holder.tvQuestion.text = "Q: ${card.first}"
        holder.tvAnswer.text = "A: ${card.second}"

        // User interaction: clicking an item shows a Toast with the question
        holder.itemView.setOnClickListener {
            onItemClick(card)
        }
    }

    override fun getItemCount(): Int = cards.size
}
