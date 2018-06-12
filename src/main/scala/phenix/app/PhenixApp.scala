package phenix.app

import java.time.LocalDate

import phenix.dataFiles.DataFileServiceImpl
import phenix.dataFiles.specifics.TransactionFileReader
import phenix.dataProcessors.MapReduceProductQuantityAggregator

object PhenixApp extends App {

    val dataFileFactory = new DataFileServiceImpl

    new MapReduceProductQuantityAggregator(dataFileFactory).aggregate(new TransactionFileReader(LocalDate.of(2017, 5, 14)))

}
