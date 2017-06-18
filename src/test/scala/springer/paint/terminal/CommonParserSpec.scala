package springer.paint.terminal

import springer.paint.spec.CommonSpec

trait CommonParserSpec extends CommonSpec {
    def tokenize(input: String): List[String] = input.split(" ").toList
}
