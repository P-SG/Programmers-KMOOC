package com.programmers.kmooc.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.programmers.kmooc.models.LectureList
import com.programmers.kmooc.repositories.KmoocRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Collections.addAll

class KmoocListViewModel(private val repository: KmoocRepository) : ViewModel() {

    val lectureList: LiveData<LectureList> get() = _lectureList
    private var _lectureList = MutableLiveData<LectureList>()

    val progress: LiveData<Boolean> get() = _progress
    private var _progress = MutableLiveData<Boolean>()

    fun list() {
        _progress.value = true
        repository.list { lectureList ->
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main){
                    _lectureList.value = lectureList
                    _progress.value = false
                }
            }
        }
    }

    fun next() {
        _progress.value = true
        val currentLectureList = lectureList.value ?: return
        repository.next(currentLectureList) { lectureList ->
            val merLecture = currentLectureList.lectures.toMutableList()
                .apply { addAll(lectureList.lectures) }
            lectureList.lectures = merLecture
            CoroutineScope(Dispatchers.Main).launch {
                _lectureList.value = lectureList
                _progress.value = false
            }

        }
    }
}

class KmoocListViewModelFactory(private val repository: KmoocRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KmoocListViewModel::class.java)) {
            return KmoocListViewModel(repository) as T
        }
        throw IllegalAccessException("Unkown Viewmodel Class")
    }
}