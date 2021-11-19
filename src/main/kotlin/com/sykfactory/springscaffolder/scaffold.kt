package com.sykfactory.springscaffolder

import com.squareup.kotlinpoet.*
import com.sykfactory.springscaffolder.command.AllGeneration
import com.sykfactory.springscaffolder.command.Initialization
import com.sykfactory.springscaffolder.command.ModelGeneration
import kotlinx.cli.ArgParser
import java.io.File
import java.lang.String.join
import kotlin.io.path.Path

fun main(args: Array<String>) {
    val parser = ArgParser("scaffold")
    parser.subcommands(
        Initialization(),
        ModelGeneration(),
        AllGeneration()
    )
    parser.parse(args.copyOfRange(1, args.size))
}