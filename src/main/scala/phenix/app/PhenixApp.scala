package phenix.app

import java.time.LocalDate

import phenix.dataFiles.DataFileServiceImpl
import phenix.dataProcessors.productQuantityAggregation.LinearProductQuantityAggregator
import phenix.io.IOServiceImpl
import phenix.utils.ResourceCloseable

object PhenixApp extends App with ResourceCloseable {

    val ioService = new IOServiceImpl
    val dataFileFactory = new DataFileServiceImpl(ioService)
    val productQuantityAggregator = new LinearProductQuantityAggregator(dataFileFactory)

    productQuantityAggregator.aggregate(dataFileFactory.getTransactionReader(LocalDate.of(2017, 5, 14)))

}
