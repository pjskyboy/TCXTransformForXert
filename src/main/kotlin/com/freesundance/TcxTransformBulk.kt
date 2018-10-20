package com.freesundance

import mu.KotlinLogging
import org.apache.commons.io.FileUtils
import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.File
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {
        val tcxTransform = TcxTransform()

        var day=19
        while (day < 20) {
            logger.info("day=$day")
            val dayStr = String.format("%02d", day)
            val dateStr = "2018-10-$dayStr"
            tcxTransform.transform(dateStr, "TO")
            tcxTransform.transform(dateStr, "FROM")
            day++
    }
}