package com.example.tep_timeshareexchangeplatform.AppConfig.CustomView.CustomBottomNav

import android.content.Context
import android.content.res.XmlResourceParser
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.tep_timeshareexchangeplatform.Common.Constant

class BottomBarParser(private val context: Context, res: Int) {

    private val parser: XmlResourceParser = context.resources.getXml(res)

    fun parse(): List<BottomBarItem> {
        val items: MutableList<BottomBarItem> = mutableListOf()
        var eventType: Int?

        do {
            eventType = parser.next()

            if (eventType == XmlResourceParser.START_TAG && parser.name == Constant.ITEM_TAG)
                items.add(getTabConfig(parser))

        } while (eventType != XmlResourceParser.END_DOCUMENT)

        return items.toList()
    }

    private fun getTabConfig(parser: XmlResourceParser): BottomBarItem {
        val attributeCount = parser.attributeCount
        var itemText: String? = null
        var itemDrawable: Drawable? = null

        for (i in 0 until attributeCount)
            when (parser.getAttributeName(i)) {
                Constant.ICON_ATTRIBUTE -> itemDrawable =
                    ContextCompat.getDrawable(context, parser.getAttributeResourceValue(i, 0))
                Constant.TITLE_ATTRIBUTE -> {
                    itemText = try {
                        context.getString(parser.getAttributeResourceValue(i, 0))
                    } catch (e: Exception) {
                        parser.getAttributeValue(i)
                    }
                }
            }
        return BottomBarItem(itemText!!, itemDrawable!!)
    }
}