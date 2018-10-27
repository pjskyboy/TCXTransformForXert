package com.freesundance

import mu.KotlinLogging
import org.apache.commons.io.FileUtils
import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.File
import java.io.StringReader
import java.time.DayOfWeek
import java.time.LocalDate
import javax.xml.parsers.DocumentBuilderFactory

private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {
        val tcxTransform = TcxTransform()

        var currentDate = LocalDate.parse("2018-09-03")
        var endDate = LocalDate.parse("2018-10-26")
        while (currentDate < endDate.plusDays(1)) {
            logger.info("currentDate=$currentDate ${currentDate.dayOfWeek}")
            if (isWeekday(currentDate.dayOfWeek)) {
                tcxTransform.transform(currentDate.toString(), "TO")
                tcxTransform.transform(currentDate.toString(), "FROM")
            }
            currentDate = currentDate.plusDays(1)
    }
}

fun isWeekday(dayOfWeek : DayOfWeek): Boolean {
    return ((dayOfWeek != (DayOfWeek.SATURDAY)) && (dayOfWeek != (DayOfWeek.SUNDAY)))
}