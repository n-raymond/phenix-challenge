package phenix.app

import java.time.LocalDate

import phenix.dataFiles.DataFileServiceImpl
import phenix.dataFiles.specifics.TransactionFileReader
import phenix.dataProcessors.MapReduceProductQuantityAggregator
import phenix.io.IOServiceImpl

object PhenixApp extends App {

    val ioService = new IOServiceImpl
    val dataFileFactory = new DataFileServiceImpl(ioService)

    new MapReduceProductQuantityAggregator(dataFileFactory).aggregate(new TransactionFileReader(LocalDate.of(2017, 5, 14), ioService))

}
