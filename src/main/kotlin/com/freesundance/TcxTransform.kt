package com.freesundance

import mu.KotlinLogging
import org.apache.commons.io.FileUtils
import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.File
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

private val logger = KotlinLogging.logger {}

val fileMap = hashMapOf("TO" to "/ToTemplate.tcx", "FROM" to "/FromTemplate.tcx")
const val YYYY_MM_DD = "YYYY-MM-DD"

class TcxTransform {

    fun transform(dateStr: String, type: String) {
        val outfileName = "$dateStr-$type.tcx"
        logger.info("dateStr=$dateStr type=$type outfileName=$outfileName")
        FileUtils.writeStringToFile(File(outfileName), TcxTransform::class.java.getResource(fileMap[type]).readText().replace(YYYY_MM_DD, dateStr), "UTF-8")
        logger.info("completed")
    }
}


fun main(args: Array<String>) {
    if (args.size < 2) {
        logger.error("usage: tcxTransform.kt YYYY-MM-DD TO|FROM")
        logger.warn("e.g. tcxTransform.kt 2018-09-01 TO")
        logger.warn("will output a file for a ride to work for the given date")
    } else {
        val tcxTransform = TcxTransform()
        tcxTransform.transform(args[0], args[1])
    }
}