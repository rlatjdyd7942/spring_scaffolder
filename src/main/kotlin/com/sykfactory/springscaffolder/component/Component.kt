package com.sykfactory.springscaffolder.builder

import java.lang.String.join

interface Component {
    fun toText(depth: Int = 0): String
}

