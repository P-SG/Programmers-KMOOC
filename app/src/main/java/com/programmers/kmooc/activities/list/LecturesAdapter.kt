package com.programmers.kmooc.activities.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.programmers.kmooc.R
import com.programmers.kmooc.databinding.ViewKmookListItemBinding
import com.programmers.kmooc.models.Lecture
import com.programmers.kmooc.network.ImageLoader
import com.programmers.kmooc.utils.DateUtil
import java.text.SimpleDateFormat
import java.util.*

class LecturesAdapter : RecyclerView.Adapter<LectureViewHolder>() {

    private val lectures = mutableListOf<Lecture>()
    var onClick: (Lecture) -> Unit = {}

    fun updateLectures(lectures: List<Lecture>) {
        this.lectures.clear()
        this.lectures.addAll(lectures)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return lectures.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_kmook_list_item, parent, false)
        val binding = ViewKmookListItemBinding.bind(view)
        return LectureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LectureViewHolder, position: Int) {
        val lecture = lectures[position]
        holder.bind(lecture)
        holder.itemView.setOnClickListener { onClick(lecture) }
    }
}

class LectureViewHolder(binding: ViewKmookListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    private val thumbnail = binding.lectureImage
    private val title = binding.lectureTitle
    private val orgName = binding.lectureFrom
    private val period = binding.lectureDuration

    fun bind(lecture: Lecture) {
        title.text = lecture.name
        orgName.text = lecture.orgName
        period.text = DateUtil.parseString(lecture.start, lecture.end)

        ImageLoader.loadImage(lecture.courseImage) { bitmap ->
            thumbnail.setImageBitmap(bitmap)
        }
    }


}