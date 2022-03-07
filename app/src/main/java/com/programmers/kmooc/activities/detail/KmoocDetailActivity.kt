package com.programmers.kmooc.activities.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.programmers.kmooc.KmoocApplication
import com.programmers.kmooc.databinding.ActivityKmookDetailBinding
import com.programmers.kmooc.models.Lecture
import com.programmers.kmooc.network.ImageLoader
import com.programmers.kmooc.utils.DateUtil
import com.programmers.kmooc.utils.toVisibility
import com.programmers.kmooc.viewmodels.KmoocDetailViewModel
import com.programmers.kmooc.viewmodels.KmoocDetailViewModelFactory

class KmoocDetailActivity : AppCompatActivity() {

    companion object {
        const val INTENT_PARAM_COURSE_ID = "param_course_id"
    }

    private lateinit var binding: ActivityKmookDetailBinding
    private lateinit var viewModel: KmoocDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val kmoocRepository = KmoocApplication.kmoocRepository

        viewModel = ViewModelProvider(this, KmoocDetailViewModelFactory(kmoocRepository)).get(
            KmoocDetailViewModel::class.java
        )

        binding = ActivityKmookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val courseId = intent.getStringExtra(INTENT_PARAM_COURSE_ID)
        if (courseId == null || courseId.isEmpty()) {
            finish()
            return
        }

        viewModel.lecture.observe(this, this::setDetailInfo)
        viewModel.progress.observe(this) { visible ->
            binding.progressBar.visibility = visible.toVisibility()
        }
        binding.toolbar.setNavigationOnClickListener { finish() }

        viewModel.detail(courseId)
    }


    private fun setDetailInfo(lecture: Lecture) {
        binding.toolbar.title = lecture.name
        ImageLoader.loadImage(lecture.courseImageLarge) { binding.lectureImage.setImageBitmap(it) }
        binding.lectureNumber.setDescription("- 강좌번호 :", lecture.number)
        binding.lectureType.setDescription(
            "- 강좌분류 :",
            "${lecture.classfyName} (${lecture.middleClassfyName})"
        )
        binding.lectureOrg.setDescription("- 운영기관 :", lecture.orgName)

        binding.lectureTeachers.setDescription("- 교수정보 :", lecture.teachers ?: "")
        binding.lectureTeachers.visibility = (lecture.teachers?.isEmpty() == false).toVisibility()

        binding.lectureDue.setDescription(
            "- 운영기간 :",
            DateUtil.parseString(lecture.start, lecture.end)
        )

        binding.webView.loadData(lecture.overview ?: "", "text/html", "UTF-8")
        binding.webView.visibility = (lecture.overview?.isEmpty() == false).toVisibility()
    }
}