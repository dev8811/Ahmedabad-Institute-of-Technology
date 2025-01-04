package com.example.ahmedabadinstituteoftechnology.ui.dashboard

import android.content.Context
import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ahmedabadinstituteoftechnology.ui.dashboard.DashboardFragment.Notice
import java.text.SimpleDateFormat
import java.util.*


class NoticeAdapter(private var noticeList: MutableList<Notice>) :
    RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val context = parent.context

        // Create CardView dynamically
        val cardView = createCardView(context)

        // Create LinearLayout
        val linearLayout = createLinearLayout(context)

        // Create TextViews
        val titleTextView = createTextView(context, 16f, Typeface.BOLD, 0, 0, 0, 8)
        val authorTextView = createTextView(context, 14f, Typeface.NORMAL)
        val semesterTextView = createTextView(context, 14f, Typeface.NORMAL)
        val classTextView = createTextView(context, 14f, Typeface.NORMAL)
        val dateTextView = createTextView(context, 14f, Typeface.NORMAL)

        // Add views to layout
        linearLayout.addView(titleTextView)
        linearLayout.addView(classTextView)
        linearLayout.addView(semesterTextView)
        linearLayout.addView(authorTextView)
        linearLayout.addView(dateTextView)
        cardView.addView(linearLayout)

        return NoticeViewHolder(
            cardView,
            titleTextView,
            authorTextView,
            semesterTextView,
            classTextView,
            dateTextView
        )
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        val notice = noticeList[position]
        holder.titleTextView.text = notice.Title
        holder.classTextView.text = "Class: ${notice.Classname ?: "N/A"}"
        holder.semesterTextView.text = "Semester: ${notice.Semester ?: "N/A"}"
        holder.authorTextView.text = "Faculty: ${notice.author ?: "N/A"}"

        // Format date
        val dateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale.getDefault())
        val dateString = notice.date?.toDate()?.let { dateFormat.format(it) } ?: "N/A"
        holder.dateTextView.text = "Date: $dateString"
    }

    override fun getItemCount(): Int = noticeList.size

    // Method to sort notices by date
    fun sortNoticesByDate() {
        noticeList.sortByDescending { it.date?.toDate() }
        notifyDataSetChanged()
    }

    private fun createCardView(context: Context): CardView {
        return CardView(context).apply {
            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(16, 16, 16, 16)
            }
            radius = 16f
            cardElevation = 8f
        }
    }

    private fun createLinearLayout(context: Context): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
        }
    }

    private fun createTextView(
        context: Context,
        textSize: Float,
        typeface: Int,
        paddingLeft: Int = 0,
        paddingTop: Int = 0,
        paddingRight: Int = 0,
        paddingBottom: Int = 0
    ): TextView {
        return TextView(context).apply {
            this.textSize = textSize
            setTypeface(null, typeface)
            setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        }
    }

    class NoticeViewHolder(
        itemView: CardView,
        val titleTextView: TextView,
        val authorTextView: TextView,
        val semesterTextView: TextView,
        val classTextView: TextView,
        val dateTextView: TextView
    ) : RecyclerView.ViewHolder(itemView)
}
