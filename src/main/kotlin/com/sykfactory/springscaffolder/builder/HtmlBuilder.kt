package com.sykfactory.springscaffolder.builder

import java.lang.String.join

class HtmlBuilder {
    private val components: MutableList<Component> = mutableListOf()

    fun tag(tagName: String, vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): HtmlBuilder {
        components.add(Tag(tagName, mutableMapOf(*attributes), block))
        return this
    }

    fun html(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): HtmlBuilder {
        components.add(Text("<!DOCTYPE HTML>"))
        components.add(Tag("html", mutableMapOf("xmlns:th" to "http://www.thymeleaf.org", *attributes), block))
        return this
    }

    fun build(): String = "${join("\n", components.map { it.toText() })}"
}