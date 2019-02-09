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
    val usage = "TcxTransformBulk.kt start-date-YYYY-MM-DD end-date-YYYY-MM-DD [SHORT|LONG]"
        if (args.size == 3) {
            val tcxTransform = TcxTransform()
            var currentDate = LocalDate.parse(args[0])
            val endDate = LocalDate.parse(args[1])
            val longOrShort = args[2]
            while (currentDate <= endDate) {
                logger.info("currentDate=$currentDate ${currentDate.dayOfWeek}")
                if (isWeekday(currentDate.dayOfWeek)) {
                    tcxTransform.transform(currentDate.toString(), "$longOrShort-TO")
                    tcxTransform.transform(currentDate.toString(), "$longOrShort-FROM")
                }
                currentDate = currentDate.plusDays(1)
            }
        } else {
            logger.warn(usage)
        }
}

fun isWeekday(dayOfWeek : DayOfWeek): Boolean {
    return ((dayOfWeek != (DayOfWeek.SATURDAY)) && (dayOfWeek != (DayOfWeek.SUNDAY)))
}