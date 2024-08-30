package com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomToolbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.databinding.CustomToolBarBinding

class CustomToolbar (context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private var binding: CustomToolBarBinding

    var onStartIconClick: (() -> Unit)? = null
    var onEndIconClick: (() -> Unit)? = null

    init {
        val attr =  context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar)
        val title = attr.getString(R.styleable.CustomToolbar_android_title) ?: ""
        val titleDetail = attr.getString(R.styleable.CustomToolbar_titleDetail) ?: ""
        val startIcon = attr.getResourceId(R.styleable.CustomToolbar_startIcon, R.drawable.arrow_back_ios_new_24px)
        val endIcon = attr.getResourceId(R.styleable.CustomToolbar_endIcon, R.drawable.more_vert_24px)
        val showStartIcon = attr.getBoolean(R.styleable.CustomToolbar_showStartIcon, true)
        val showEndIcon = attr.getBoolean(R.styleable.CustomToolbar_showEndIcon, false)
        val showTextStart = attr.getBoolean(R.styleable.CustomToolbar_showStartText, true)
        val textStart = attr.getString(R.styleable.CustomToolbar_textStart) ?: "Back"
        val showShadow = attr.getBoolean(R.styleable.CustomToolbar_showShadow, false)
        val showTitleDetail = attr.getBoolean(R.styleable.CustomToolbar_showTitleDetail, false)

        attr.recycle()


        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.custom_tool_bar,
            this,
            true
        )

        setTitle(title)
        setTitleDetail(titleDetail)
        setStartIconResource(startIcon)
        setEndIconResource(endIcon)
        setTextStart(textStart)
        isShowEndIcon(showEndIcon)
        isShowStartIcon(showStartIcon)
        isShowStartText(showTextStart)
        isShowShadow(showShadow)
        isShowTitleDetail(showTitleDetail)



        binding.llStartIcon.setOnClickListener {
            onStartIconClick?.invoke()
        }

        binding.llEndIcon.setOnClickListener {
            onEndIconClick?.invoke()
        }

    }

    fun setTitle(title: String) {
        binding.tvTitle.text = title
    }

    fun setTitleDetail(titleDetail: String) {
        binding.tvTitleDetail.text = titleDetail
    }

    fun setTextStart(text: String) {
        binding.tvStartText.text = text
    }

    fun setStartIconResource(icon: Int){
        binding.ivStartIcon.setImageResource(icon)
    }

    fun setEndIconResource(icon: Int){
        binding.ivEndIcon.setImageResource(icon)
    }

    fun isShowStartIcon(show: Boolean) {
        binding.ivStartIcon.visibility = if(show) View.VISIBLE else View.GONE
    }

    fun isShowEndIcon(show: Boolean) {
        binding.ivEndIcon.visibility = if(show) View.VISIBLE else View.GONE
    }

    fun isShowStartText(show: Boolean) {
        binding.tvStartText.visibility = if(show) View.VISIBLE else View.GONE
    }

    fun isShowShadow(show: Boolean) {
        binding.toolBarShadow.visibility = if(show) View.VISIBLE else View.GONE
    }

    fun isShowTitleDetail(show: Boolean) {
        binding.tvTitleDetail.visibility = if(show) View.VISIBLE else View.GONE
        binding.llTitle.setPadding(
            binding.llTitle.paddingLeft,
            if (show) 10 else 0,
            binding.llTitle.paddingRight,
            binding.llTitle.paddingBottom
        )
    }


}