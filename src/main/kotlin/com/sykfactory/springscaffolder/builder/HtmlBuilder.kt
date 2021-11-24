package com.sykfactory.springscaffolder.builder

import com.sykfactory.springscaffolder.component.Tag
import com.sykfactory.springscaffolder.component.Text

class HtmlBuilder {
    private val components: MutableList<Component> = mutableListOf()

    fun html(vararg attributes: Pair<String, String>, block: Tag.() -> Unit): HtmlBuilder {
        components.add(Text("<!DOCTYPE HTML>"))
        components.add(Tag("html", mutableMapOf("xmlns:th" to "http://www.thymeleaf.org", *attributes), block))
        return this
    }

    fun build(): String = java.lang.String.join("\n", components.map { it.toText() })
}