package com.programmers.kmooc.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.programmers.kmooc.models.Lecture
import com.programmers.kmooc.repositories.KmoocRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class KmoocDetailViewModel(private val repository: KmoocRepository) : ViewModel() {

    val progress: LiveData<Boolean> get() = _progress
    private var _progress = MutableLiveData<Boolean>()

    val lecture: LiveData<Lecture> get() = _lecture
    private var _lecture = MutableLiveData<Lecture>()

    fun detail(courseId: String) {
        _progress.value = true
        repository.detail(courseId) { lecture ->
            CoroutineScope(Dispatchers.Main).launch {
                _lecture.value = lecture
                _progress.value = false
            }
        }
    }
}

class KmoocDetailViewModelFactory(private val repository: KmoocRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KmoocDetailViewModel::class.java)) {
            return KmoocDetailViewModel(repository) as T
        }
        throw IllegalAccessException("Unkown Viewmodel Class")
    }
}