package phenix.dataFiles.specifics

import org.scalatest.FlatSpec

class ReferenceFileSpec extends FlatSpec {

    "ReferenceFile.fileNameRegex" should "well rekognize a reference file name" in {
        ReferenceFile.fileNameRegex.findFirstMatchIn("reference_prod-72a2876c-bc8b-4f35-8882-8d661fac2606_20170514.data") match {
            case Some(_) => succeed
            case None => assert(false)
        }
    }

}
