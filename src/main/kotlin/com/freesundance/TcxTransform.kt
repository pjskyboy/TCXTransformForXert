package com.freesundance

import mu.KotlinLogging
import org.apache.commons.io.FileUtils
import java.io.File

private val logger = KotlinLogging.logger {}

val fileMap = hashMapOf("SHORT-TO" to "/Short-ToTemplate.tcx",
        "SHORT-FROM" to "/Short-FromTemplate.tcx",
        "LONG-TO" to "/Long-ToTemplate.tcx",
        "LONG-FROM" to "/Long-FromTemplate.tcx")
const val YYYY_MM_DD = "YYYY-MM-DD"

class TcxTransform {

    fun transform(dateStr: String, routeSelector: String) {
        val outfileName = "$dateStr-$routeSelector.tcx"
        logger.info("dateStr=$dateStr routeSelector=$routeSelector outfileName=$outfileName")
        FileUtils.writeStringToFile(File(outfileName), TcxTransform::class.java.getResource(fileMap[routeSelector]).readText().replace(YYYY_MM_DD, dateStr), "UTF-8")
        logger.info("completed")
    }
}


fun main(args: Array<String>) {
    if (args.size < 2) {
        logger.error("usage: tcxTransform.kt YYYY-MM-DD SHORT|LONG")
        logger.warn("e.g. tcxTransform.kt 2018-09-01 SHORT")
        logger.warn("will output files for a ride to and from work for the given date on the route type")
    } else {
        val tcxTransform = TcxTransform()
        tcxTransform.transform(args[0], "$args[1]-TO")
        tcxTransform.transform(args[0], "$args[1]-FROM")
    }
}