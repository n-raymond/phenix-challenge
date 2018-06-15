package phenix.app

import java.time.LocalDate

import com.typesafe.scalalogging.LazyLogging
import phenix.dataFiles.DataFileServiceImpl
import phenix.dataProcessors.shopQuantityAggregation.LinearShopQuantityAggregator
import phenix.dataProcessors.shopRevenueAggregation.LinearShopRevenueAggregator
import phenix.dataProcessors.topQuantityAggregation.LinearTopSalesAggregator
import phenix.dataProcessors.topRevenuesAggregation.LinearTopRevenueAggregator
import phenix.io.IOServiceImpl

object PhenixApp extends App with LazyLogging {

    /* Argument gestion */
    if (args.length == 0) {
        logger.error("I need at least one parameter : Give me the path of the directory containing the data folder")
        logger.error("usage: phenix-challenge <path-to-root-directory>")
        System.exit(0)
    }
    val rootPath = args(0)

    /* Dependency Injection */
    val ioService = new IOServiceImpl(rootPath)
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
