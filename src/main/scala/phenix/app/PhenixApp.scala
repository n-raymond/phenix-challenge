package phenix.app

import java.time.LocalDate

import phenix.dataFiles.impl.DataFileFactoryImpl
import phenix.dataFiles.impl.specifics.TransactionFileReader
import phenix.dataProcessors.MapReduceProductQuantityAggregator

object PhenixApp extends App {

    val dataFileFactory = new DataFileFactoryImpl

    new MapReduceProductQuantityAggregator(dataFileFactory).aggregate(new TransactionFileReader(LocalDate.of(2017, 5, 14)))

}
