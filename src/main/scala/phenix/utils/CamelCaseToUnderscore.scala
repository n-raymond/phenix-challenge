package phenix.utils

trait CamelCaseToUnderscore {

    def camel2Underscore(text: String) = text.drop(1).foldLeft(text.headOption.map(_.toLower + "") getOrElse "") {
        case (acc, c) if c.isUpper => acc + "_" + c.toLower
        case (acc, c) => acc + c
    }

}
