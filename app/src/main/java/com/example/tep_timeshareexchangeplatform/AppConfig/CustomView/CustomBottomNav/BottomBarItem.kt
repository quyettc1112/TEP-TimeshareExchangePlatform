package com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomBottomNav

import android.graphics.RectF
import android.graphics.drawable.Drawable

data class BottomBarItem(var title: String, val icon: Drawable,
                         var rect: RectF = RectF(), var badgeSize: Float = 0f)