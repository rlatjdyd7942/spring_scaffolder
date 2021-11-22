package com.sykfactory.springscaffolder.generator.view

import com.sykfactory.springscaffolder.builder.HtmlBuilder
import com.sykfactory.springscaffolder.generator.FileGenerator
import com.sykfactory.springscaffolder.util.createFileOnce
import java.lang.String.join

class LayoutFileGenerator(
    private val layoutPath: String
) : FileGenerator {
    override fun generateFile() {
        val html = HtmlBuilder().html {
            head {
                meta {
                    attribute("charset", "UTF-8")
                    title {
                        text("Spring Application")
                    }
                }
            }
            body {
                tag("th:block") {
                    multiLine = false
                    attribute("layout:fragment", "_page_content")
                }
            }
        }.build()
        createFileOnce(fileFullPath("$layoutPath.html")).writeText(html)
    }

    private fun fileFullPath(filePath: String): String
        = join("/", FileGenerator.baseTemplatePath, filePath)
            .replace("//", "/")
}