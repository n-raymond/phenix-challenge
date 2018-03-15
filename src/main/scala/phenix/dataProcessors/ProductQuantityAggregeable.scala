package phenix.dataProcessors

import phenix.dataFiles.{ProductQuantityFile, TransactionFileReader}


/**
  * A ProductQuantityAggregator offer a way to process transaction
  * and aggregate the sum of each sold product into ProductQuantityFiles
  */
trait ProductQuantityAggregeable {

    def aggregate(transactionFileReader: TransactionFileReader) : Iterable[ProductQuantityFile.Reader]

}

object ProductQuantityAggregeable {

    /**
      * A factory that returns the default implementation of ProductQuantityAggregator
      * @return A ProductQuantityAggregator
      */
    def apply: ProductQuantityAggregeable = MapReduceProductQuantityAggregator


}

