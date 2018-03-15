package phenix.app

import java.time.LocalDate

import phenix.dataFiles.TransactionFileReader
import phenix.dataProcessors.ProductQuantityAggregeable

object PhenixApp extends App {

    ProductQuantityAggregeable.apply.aggregate(new TransactionFileReader(LocalDate.of(2017, 5, 14)))

}
