package com.sykfactory.springscaffolder.command

import com.sykfactory.springscaffolder.Setting
import com.sykfactory.springscaffolder.generator.view.LayoutFileGenerator
import kotlinx.cli.Subcommand

class Initialization: Subcommand("init", "Initialize scaffold settings") {
    override fun execute() {
        Setting.save()

        LayoutFileGenerator(Setting.layoutPath).generateFile()
    }
}