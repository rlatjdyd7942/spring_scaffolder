package com.sykfactory.springscaffolder.component

import com.sykfactory.springscaffolder.builder.Component

class Text(private val text: String) : Component {
    override fun toText(depth: Int): String {
        return text
    }
}