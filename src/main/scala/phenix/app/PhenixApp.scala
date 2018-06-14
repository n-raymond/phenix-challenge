package phenix.app

import java.time.LocalDate

import phenix.dataFiles.DataFileServiceImpl
import phenix.dataProcessors.shopQuantityAggregation.LinearShopQuantityAggregator
import phenix.dataProcessors.shopRevenueAggregation.LinearShopRevenueAggregator
import phenix.dataProcessors.topRevenuesAggregation.LinearTopRevenueAggregator
import phenix.dataProcessors.topQuantityAggregation.LinearTopSalesAggregator
import phenix.io.IOServiceImpl
import phenix.utils.ResourceCloseable

object PhenixApp extends App with ResourceCloseable {

    /* Dependency Injection */
    val ioService = new IOServiceImpl
    val dataFileFactory = new DataFileServiceImpl(ioService)
    val productQuantityAggregator = new LinearShopQuantityAggregator(dataFileFactory)
    val productRevenueAggregator = new LinearShopRevenueAggregator(dataFileFactory, ioService)
    val topSalesAggregator = new LinearTopSalesAggregator(dataFileFactory)
    val topRevenuesAggregator = new LinearTopRevenueAggregator(dataFileFactory)

    /* Start */
    val date = LocalDate.of(2017, 5, 14)

    val productIds = productQuantityAggregator.aggregate(dataFileFactory.getTransactionReader(date))
    val productRevenues = productRevenueAggregator.aggregate(productIds, date)

    topSalesAggregator.aggregate(productIds, date)
    topRevenuesAggregator.aggregate(productIds, date)

}
