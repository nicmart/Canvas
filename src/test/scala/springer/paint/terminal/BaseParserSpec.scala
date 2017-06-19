package springer.paint.terminal

import springer.paint.spec.CommonSpec

trait BaseParserSpec extends CommonSpec {
    def tokenize(input: String): List[String] = input.split(" ").toList
}
