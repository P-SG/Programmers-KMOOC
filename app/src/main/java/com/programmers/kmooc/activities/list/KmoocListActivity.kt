package com.programmers.kmooc.activities.list

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.programmers.kmooc.KmoocApplication
import com.programmers.kmooc.activities.detail.KmoocDetailActivity
import com.programmers.kmooc.databinding.ActivityKmookListBinding
import com.programmers.kmooc.models.Lecture
import com.programmers.kmooc.utils.toVisibility
import com.programmers.kmooc.viewmodels.KmoocListViewModel
import com.programmers.kmooc.viewmodels.KmoocListViewModelFactory

class KmoocListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKmookListBinding
    private lateinit var viewModel: KmoocListViewModel
    private lateinit var adapter: LecturesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val kmoocRepository = KmoocApplication.kmoocRepository
        viewModel = ViewModelProvider(this, KmoocListViewModelFactory(kmoocRepository)).get(
            KmoocListViewModel::class.java
        )

        binding = ActivityKmookListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = LecturesAdapter().apply { onClick = this@KmoocListActivity::startDetailActivity }
        binding.lectureList.adapter = adapter

        initView()
        setObserve()

        viewModel.list()
    }

    private fun initView(){

        binding.pullToRefresh.setOnRefreshListener {
            viewModel.list()
        }

        binding.lectureList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = binding.lectureList.layoutManager

                if (viewModel.progress.value != true) {
                    val lastItem = (layoutManager as LinearLayoutManager)
                        .findLastCompletelyVisibleItemPosition()

                    if (layoutManager.itemCount <= lastItem + 5) {
                        viewModel.next()
                    }
                }
            }
        })
    }

    private fun setObserve(){
        viewModel.lectureList.observe(this, {
            adapter.updateLectures(it.lectures)
            binding.pullToRefresh.isRefreshing = false
        })

        viewModel.progress.observe(this) { visible ->
            binding.progressBar.visibility = visible.toVisibility()
        }
    }

    private fun startDetailActivity(lecture: Lecture) {
        startActivity(
            Intent(this, KmoocDetailActivity::class.java)
                .apply { putExtra(KmoocDetailActivity.INTENT_PARAM_COURSE_ID, lecture.id) }
        )
    }
}
