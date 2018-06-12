package phenix.dataProcessors.productQuantityAggregation

import java.util.UUID

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import phenix.dataFiles.DataFileService
import phenix.models.{ProductQuantity, Transaction}

import scala.collection.SortedMap
import scala.util.{Failure, Success}

class LinearProductQuantityAggregatorSpec extends FlatSpec with Matchers with MockFactory {

    private val dataFileService = stub[DataFileService]
    private val productQuantityAggregator = new LinearProductQuantityAggregator(dataFileService)

    "aggregateProductsByShop" should "succeed to aggregate the transactions by shop" in {

        val initialStream = Iterable(
            Transaction(UUID.fromString("29366c83-eae9-42d3-a8af-f15339830dc5"), 513, 8),
            Transaction(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), 684, 12),
            Transaction(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), 513, 2),
            Transaction(UUID.fromString("bdc2a431-797d-4b07-9567-67c565a67b84"), 1, 123),
            Transaction(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), 87, 78),
            Transaction(UUID.fromString("bdc2a431-797d-4b07-9567-67c565a67b84"), 312, 16),
            Transaction(UUID.fromString("29366c83-eae9-42d3-a8af-f15339830dc5"), 78, 34)
        )

        val result = productQuantityAggregator.aggregateProductsByShop(initialStream).toList sortBy (_.shop)

        val expected = Iterable(
            ProductQuantity(UUID.fromString("29366c83-eae9-42d3-a8af-f15339830dc5"), 8 + 34),
            ProductQuantity(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), 12 + 2 + 78),
            ProductQuantity(UUID.fromString("bdc2a431-797d-4b07-9567-67c565a67b84"), 123 + 16)
        ).toList sortBy (_.shop)

        result should equal (expected)

    }

    "aggregateChunk" should "succeed to aggregate the succeeded transaction in the chunk by product and omit the failed one" in {

        val initialStream = Iterable(
            Success(Transaction(UUID.fromString("29366c83-eae9-42d3-a8af-f15339830dc5"), 513, 8)),
            Success(Transaction(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), 513, 12)),
            Success(Transaction(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), 12, 78)),
            Success(Transaction(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), 513, 2)),
            Failure(new Exception("Mouahahah ! You failed !")),
            Success(Transaction(UUID.fromString("bdc2a431-797d-4b07-9567-67c565a67b84"), 12, 123)),
            Success(Transaction(UUID.fromString("bdc2a431-797d-4b07-9567-67c565a67b84"), 12, 16)),
            Success(Transaction(UUID.fromString("29366c83-eae9-42d3-a8af-f15339830dc5"), 78, 34)),
            Success(Transaction(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), 513, 14)),
            Success(Transaction(UUID.fromString("bdc2a431-797d-4b07-9567-67c565a67b84"), 513, 1)),
            Success(Transaction(UUID.fromString("29366c83-eae9-42d3-a8af-f15339830dc5"), 12, 354)),
            Success(Transaction(UUID.fromString("29366c83-eae9-42d3-a8af-f15339830dc5"), 513, 68)),
            Failure(new Exception("Mouahahah ! You failed !")),
            Success(Transaction(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), 12, 45)),
            Success(Transaction(UUID.fromString("29366c83-eae9-42d3-a8af-f15339830dc5"), 12, 6)),
            Success(Transaction(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), 78, 7))
        )

        val result = SortedMap(productQuantityAggregator.aggregateChunk(initialStream).toArray:_*) map {
            case (k, v) => (k, v.toList.sortBy(_.shop))
        }

        val expected = SortedMap(
            513 -> Iterable(
                ProductQuantity(UUID.fromString("29366c83-eae9-42d3-a8af-f15339830dc5"), 8 + 68),
                ProductQuantity(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), 12 + 2 + 14),
                ProductQuantity(UUID.fromString("bdc2a431-797d-4b07-9567-67c565a67b84"), 1)
            ).toList.sortBy(_.shop),
            12 -> Iterable(
                ProductQuantity(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), 78 + 45),
                ProductQuantity(UUID.fromString("bdc2a431-797d-4b07-9567-67c565a67b84"), 123 + 16),
                ProductQuantity(UUID.fromString("29366c83-eae9-42d3-a8af-f15339830dc5"), 354 + 6)
            ).toList.sortBy(_.shop),
            78 -> Iterable(
                ProductQuantity(UUID.fromString("29366c83-eae9-42d3-a8af-f15339830dc5"), 34),
                ProductQuantity(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), 7)
            ).toList.sortBy(_.shop)
        )

        result should equal (expected)
    }

}
