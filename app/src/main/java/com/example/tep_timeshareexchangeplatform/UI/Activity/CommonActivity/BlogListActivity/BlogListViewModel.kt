package com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.BlogListActivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tep_timeshareexchangeplatform.API.Repository.PublicPostingAPIRepository
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.PublicPosting.BlogResponse
import com.example.tep_timeshareexchangeplatform.Until.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlogListViewModel @Inject constructor(
    private val publicPostingAPIRepository: PublicPostingAPIRepository
) : ViewModel() {
    // Call Get ALl Exchange Request
    private val _blogList = MutableLiveData<Resource<BlogResponse>>()
    val blogList: MutableLiveData<Resource<BlogResponse>> =
        _blogList

    fun getBlogList( page: Int, size: Int, title: String) {
        viewModelScope.launch {
            _blogList.postValue(Resource.loading(null))
            val response =
                publicPostingAPIRepository.getBlog(page, size, title)
            _blogList.postValue(response)
        }
    }

    // Get Current Page
    private var _currentPage = MutableLiveData<Int>()
    val currentPage: MutableLiveData<Int>
        get() = _currentPage
    // Increment Current Page
    fun incrementCurrentPage() {
        val currentValue = _currentPage.value ?: 0
        _currentPage.value = currentValue + 1
    }

    // Get Current Posting List
    private val _currentBlogList = mutableListOf<BlogResponse.Content>()
    fun loadMoreBlogList(list: List<BlogResponse.Content>) {
        _currentBlogList.addAll(list)
    }
    fun getCurrentBlogList(): List<BlogResponse.Content> {
        return _currentBlogList
    }

    fun clearCurrentBlogList() {
        _currentBlogList.clear()
    }

    init {
        _currentPage.value = 0
    }
}