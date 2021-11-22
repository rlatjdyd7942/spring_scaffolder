package com.sykfactory.springscaffolder.command

import com.sykfactory.springscaffolder.Setting
import kotlinx.cli.Subcommand

class Initialization: Subcommand("init", "Initialize scaffold settings") {
    override fun execute() {
        Setting.apply {
            basePackageName = "# path.to.package"
            modelPath = "model"
            repositoryPath = "repository"
            controllerPath = "controller"
        }.save()
    }
}