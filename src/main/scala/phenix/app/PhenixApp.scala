package phenix.app

import java.time.LocalDate

import phenix.dataFiles.DataFileServiceImpl
import phenix.dataProcessors.productQuantityAggregation.LinearProductQuantityAggregator
import phenix.dataProcessors.topValuesAggregation.LinearTopValuesAggregator
import phenix.io.IOServiceImpl
import phenix.utils.ResourceCloseable

object PhenixApp extends App with ResourceCloseable {

    /* Dependency Injection */
    val ioService = new IOServiceImpl
    val dataFileFactory = new DataFileServiceImpl(ioService)
    val productQuantityAggregator = new LinearProductQuantityAggregator(dataFileFactory)
    val topSalesAggregator = new LinearTopValuesAggregator(dataFileFactory)

    /* Start */
    val productQuantities = productQuantityAggregator.aggregate(dataFileFactory.getTransactionReader(LocalDate.of(2017, 5, 14)))
    topSalesAggregator.aggregate(productQuantities)

}
