package phenix.dataProcessors.topValuesAggregation

import java.time.LocalDate
import java.util.UUID

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import phenix.dataFiles.DataFileService
import phenix.dataFiles.general.{ReadableDataFile, WritableDataFile}
import phenix.models.{ShopQuantity, ProductValue}

import scala.util.Success

class LinearTopValuesAggregatorSpec extends FlatSpec with Matchers with MockFactory {



    "intermediateComputation" should "create the good number of files" in {

    }

    "aggregateProductQuantitiesByShop" should "return a map with the good new entries" in {

        val dataFileService = mock[DataFileService]
        val topSalesAggregator = new LinearTopValuesAggregator(dataFileService)

        val qties = Iterable(
            new ShopQuantity(UUID.fromString("bdc2a431-797d-4b07-9567-67c565a67b84"), 4),
            new ShopQuantity(UUID.fromString("af068240-8198-4b79-9cf9-c28c0db65f63"), 8),
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

    "reducer" should "well merge the datafiles" in {
        val dataFileService = mock[DataFileService]
        val topSalesAggregator = new LinearTopValuesAggregator(dataFileService)

        val date = LocalDate.of(2017, 5, 4)

        /* Iterable 1 */

        val uuidA = UUID.fromString("d4bfbabf-0160-4e87-8688-78e0943a396a")
        val fileA1 = mock[ReadableDataFile[ProductValue]]
        val contentA1 = Stream(
            Success(new ProductValue(7, 8)),
            Success(new ProductValue(6, 1)),
            Success(new ProductValue(1,2))
        )

        val uuidB = UUID.fromString("af068240-8198-4b79-9cf9-c28c0db65f63")
        val fileB = mock[ReadableDataFile[ProductValue]]
        val contentB = Stream(Success(new ProductValue(1, 3)))

        val iterable1 = Iterable((uuidA, fileA1), (uuidB, fileB))

        /* Iterable 2 */

        val fileA2 = mock[ReadableDataFile[ProductValue]]
        val contentA2 = Stream(
            Success(new ProductValue(10, 4)),
            Success(new ProductValue(5, 3)),
            Success(new ProductValue(3,5))
        )


        val uuidC = UUID.fromString("bdc2a431-797d-4b07-9567-67c565a67b84")
        val fileC = mock[ReadableDataFile[ProductValue]]
        val contentC = Stream(Success(new ProductValue(7, 3)))

        val iterable2 = Iterable((uuidA, fileA2), (uuidC, fileC))

        /* Output Writers */

        val writerA = mock[WritableDataFile[ProductValue]]
        val contentWriterA = Iterable(
            new ProductValue(7, 8),
            new ProductValue(3, 5),
            new ProductValue(10, 4),
            new ProductValue(5, 3),
            new ProductValue(1,2),
            new ProductValue(6,1)
        )

        val writerB = mock[WritableDataFile[ProductValue]]
        val contentWriterB = Iterable(new ProductValue(1, 3))

        val writerC = mock[WritableDataFile[ProductValue]]
        val contentWriterC = Iterable(new ProductValue(7, 3))

        /* Output Readers */

        val readerA = stub[ReadableDataFile[ProductValue]]
        val readerB = stub[ReadableDataFile[ProductValue]]
        val readerC = stub[ReadableDataFile[ProductValue]]

        /* Expectations */

        inSequence{
            fileA1.date _ expects() returning date
            inAnyOrder{
                fileA1.getContent _ expects() returning contentA1
                fileA1.close _ expects()

                fileA2.getContent _ expects() returning contentA2
                fileA2.close _ expects()
            }
            dataFileService.getShopTopSellsWriter _ expects (uuidA, date) returning writerA
            writerA.writeData _ expects contentWriterA
            writerA.close _ expects()
            dataFileService.getShopTopSellsReader _ expects (uuidA, date) returning readerA

            fileB.getContent _ expects() returning contentB
            fileB.close _ expects()
            dataFileService.getShopTopSellsWriter _ expects (uuidB, date) returning writerB
            writerB.writeData _ expects contentWriterB
            writerB.close _ expects()
            dataFileService.getShopTopSellsReader _ expects (uuidB, date) returning readerB

            fileC.getContent _ expects() returning contentC
            fileC.close _ expects()
            dataFileService.getShopTopSellsWriter _ expects (uuidC, date) returning writerC
            writerC.writeData _ expects contentWriterC
            writerC.close _ expects()
            dataFileService.getShopTopSellsReader _ expects (uuidC, date) returning readerC
        }


        /* Result Matching */

        val result = topSalesAggregator.reducer(iterable1, iterable2).toList.sortBy(_._1)
        val expected = List((uuidA, readerA), (uuidB, readerB), (uuidC, readerC)).sortBy(_._1)
        result should equal (expected)

    }


}