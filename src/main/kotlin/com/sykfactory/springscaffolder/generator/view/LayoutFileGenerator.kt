package com.sykfactory.springscaffolder.generator.view

import com.sykfactory.springscaffolder.builder.PlainHtmlBuilder
import com.sykfactory.springscaffolder.generator.FileGenerator
import com.sykfactory.springscaffolder.util.createFileOnce
import java.lang.String.join

class LayoutFileGenerator(
    private val layoutPath: String
) : FileGenerator {
    override fun generateFile() {
        val html = PlainHtmlBuilder().html {
            head {
                meta {
                    attribute("charset", "UTF-8")
                }
                meta {
                    attribute("name", "viewport")
                    attribute("content", "width=device-width, initial-scale=1")
                }
                link {
                    attribute("href", "https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css")
                    attribute("rel", "stylesheet")
                    attribute("integrity", "sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC")
                    attribute("crossorigin", "anonymous")
                }
                script {
                    attribute("src", "https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js")
                    attribute("integrity", "sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM")
                    attribute("crossorigin", "anonymous")
                }
                title {
                    text("Spring Application")
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