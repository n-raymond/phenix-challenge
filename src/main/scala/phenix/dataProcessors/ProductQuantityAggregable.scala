package phenix.dataProcessors

import phenix.dataFiles.{ProductQuantityFile, TransactionFileReader}


/**
  * A ProductQuantityAggregator offer a way to process transaction
  * and aggregate the sum of each sold product into ProductQuantityFiles
  */
trait ProductQuantityAggregable {

    def aggregate(transactionFileReader: TransactionFileReader) : Iterable[ProductQuantityFile.Reader]

}

object ProductQuantityAggregable {

    /**
      * A factory that returns the default implementation of ProductQuantityAggregator
      * @return A ProductQuantityAggregator
      */
    def apply: ProductQuantityAggregable = MapReduceQuantityAggregator


}

