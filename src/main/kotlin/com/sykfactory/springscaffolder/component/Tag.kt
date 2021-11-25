package com.sykfactory.springscaffolder.component

import com.sykfactory.springscaffolder.builder.Component
import java.lang.String.join

class Tag (
    val tagName: String,
    val attributes: MutableMap<String, String> = mutableMapOf(),
    block: Tag.() -> Unit
) : Component {
    private val states: MutableList<String> = mutableListOf()
    private val children: MutableList<Component> = mutableListOf()
    var tagBlock = true
    var multiLine = true

    init {
        block()
    }
    
    fun add(child: Component) {
        children.add(child)
    }

    fun attribute(attributeName: String, value: String) {
        attributes[attributeName] = value
    }

    fun state(stateName: String) {
        states.add(stateName)
    }

    fun classes(value: String) {
        attributes["class"] = value
    }

    fun addClass(value: String) {
        if (attributes["class"].isNullOrBlank()) {
            attributes["class"] = value
        } else {
            attributes["class"] = "${attributes["class"]} value"
        }
    }

    fun id(value: String) {
        attributes["id"] = value
    }

    fun text(text: String) {
        add(Text(text))
    }

    fun tag(tagName: String, attributes: MutableMap<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag(tagName, attributes, block).also { add(it) }
    }

    fun tag(tagName: String, vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag(tagName, mutableMapOf(*attributes), block).also { add(it) }
    }

    fun head(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("head", mutableMapOf(*attributes), block).also { add(it) }
    }

    fun body(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("body", mutableMapOf(*attributes), block).also { add(it) }
    }

    fun title(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("title", mutableMapOf(*attributes), block).also { add(it) }
    }

    fun style(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("style", mutableMapOf(*attributes), block).also { add(it) }
    }

    fun script(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("script", mutableMapOf(*attributes), block).also { add(it) }
    }

    fun link(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("link", mutableMapOf(*attributes), block).also { add(it) }
    }

    fun meta(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("meta", mutableMapOf(*attributes), block).also { add(it) }
    }

    fun div(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("div", mutableMapOf(*attributes), block).also { add(it) }
    }

    fun p(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("p", mutableMapOf(*attributes), block).also { add(it) }
    }

    fun a(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("a", mutableMapOf(*attributes), block).also { add(it) }
    }

    fun input(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("input", mutableMapOf(*attributes), block).also {
            it.tagBlock = false
            add(it)
        }
    }

    fun form(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("form", mutableMapOf(*attributes), block).also { add(it) }
    }

    fun label(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("label", mutableMapOf(*attributes), block).also {
            it.multiLine = false
            add(it)
        }
    }

    fun h1(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("h1", mutableMapOf(*attributes), block).also { add(it) }
    }

    fun h2(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("h2", mutableMapOf(*attributes), block).also { add(it) }
    }

    fun h3(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("h3", mutableMapOf(*attributes), block).also { add(it) }
    }

    fun h4(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("h4", mutableMapOf(*attributes), block).also { add(it) }
    }

    fun h5(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("h5", mutableMapOf(*attributes), block).also { add(it) }
    }

    fun h6(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("h6", mutableMapOf(*attributes), block).also { add(it) }
    }

    fun table(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("table", mutableMapOf(*attributes), block).also { add(it) }
    }

    fun thead(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("thead", mutableMapOf(*attributes), block).also { add(it) }
    }

    fun tbody(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("tbody", mutableMapOf(*attributes), block).also { add(it) }
    }

    fun th(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("th", mutableMapOf(*attributes), block).also { add(it) }
    }

    fun tr(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("tr", mutableMapOf(*attributes), block).also { add(it) }
    }

    fun td(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("td", mutableMapOf(*attributes), block).also { add(it) }
    }

    fun br(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("br", mutableMapOf(*attributes), block).also {
            it.tagBlock = false
            add(it)
        }
    }

    fun select(vararg attributes: Pair<String, String>, block: Tag.() -> Unit = {}): Tag {
        return Tag("select", mutableMapOf(*attributes), block).also { add(it) }
    }

    override fun toText(depth: Int): String {
        return if (tagBlock) {
            if (multiLine) {
                "${"    ".repeat(depth)}<$tagName${attributesString()}${statesString()}>${
                    join("", children.map { "\n${it.toText(depth + 1)}" })
                }\n${"    ".repeat(depth)}</$tagName>"
            } else {
                "${"    ".repeat(depth)}<$tagName${attributesString()}${statesString()}>${
                    join(" ", children.map { "${it.toText(depth + 1)}" })
                }</$tagName>"
            }
        } else {
            "${"    ".repeat(depth)}<$tagName${attributesString()}${statesString()} />".also {
                if (children.isNotEmpty()) println("this tag is not tag block!\n$it")
            }
        }
    }

    private fun attributesString(): String {
        return join("", attributes.map {
            " ${it.key}=\"${it.value}\""
        })
    }

    private fun statesString(): String {
        return join("", states.map { " $it" })
    }
}