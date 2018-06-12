package phenix.io.path

import java.io.IOException
import java.nio.file.{Files, Paths}

/**
  * Regroups functionalities to generate or remove paths
  * on file system.
  */
trait PathCreator {

    /**
      * Check if the parent path of the given file exists and if not, create
      * all the corresponding folder structure.
      */
    @throws[IOException]
    def createParentPathIfNotExist(path: String): Unit

}

object PathCreator {

    def apply: PathCreator = (path) => {
        val parent = Paths.get(path).getParent

        if (!Files.exists(parent)) {
            Files.createDirectories(parent)
        }
    }

}
