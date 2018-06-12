package phenix.dataProcessors.topSalesAggregation

import java.util.UUID

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import phenix.dataFiles.DataFileService
import phenix.models.{ProductQuantity, ProductValue}

class LinearTopSalesAggregatorSpec extends FlatSpec with Matchers with MockFactory {

    private val dataFileService = mock[DataFileService]

    private val topSalesAggregator = new LinearTopSalesAggregator(dataFileService)


    "intermediateComputation" should "create the good number of files" in {

    }

    "aggregateProductQuantitiesByShop" should "return a map with the good new entries" in {


        val qties = Iterable(
            new ProductQuantity(UUID.fromString("bdc2a431-797d-4b07-9567-67c565a67b84"), 4),
            new ProductQuantity(UUID.fromString("af068240-8198-4b79-9cf9-c28c0db65f63"), 8),
        )

        val productId = 6

        val initialMap = Map(
            UUID.fromString("bdc2a431-797d-4b07-9567-67c565a67b84") -> List(new ProductValue(2, 14), new ProductValue(3, 5)),
            UUID.fromString("d4bfbabf-0160-4e87-8688-78e0943a396a") -> List(new ProductValue(3, 7))
        )

        val expected = Map(
            UUID.fromString("bdc2a431-797d-4b07-9567-67c565a67b84") ->
                List(
                    new ProductValue(6, 4),
                    new ProductValue(2, 14),
                    new ProductValue(3, 5)
                ),
            UUID.fromString("d4bfbabf-0160-4e87-8688-78e0943a396a") -> List(new ProductValue(3, 7)),
            UUID.fromString("af068240-8198-4b79-9cf9-c28c0db65f63") -> List(new ProductValue(6, 8))
        )

        val result = topSalesAggregator.aggregateProductQuantitiesByShop(qties, productId, initialMap)

        result should equal (expected)
    }



}
