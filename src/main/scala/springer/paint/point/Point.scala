package springer.paint.point

/**
  * A 2D point of integer coordinates
  */
final case class Point(x: Int, y: Int) {
    def +(other: Point): Point = Point(x + other.x, y + other.y)
    def unary_-(): Point = Point(-x, -y)
    def -(other: Point): Point = this + (-other)
}
