package phenix.utils

import org.scalatest.{FlatSpec, Matchers}

class CamelCaseToUnderscoreSpec extends FlatSpec with Matchers with CamelCaseToUnderscore {


    "camelCase2Underscore" should "well convert a word from camelcase to underscores" in {
        camel2Underscore("HelloIAmARobot") should equal ("hello_i_am_a_robot")
    }

}