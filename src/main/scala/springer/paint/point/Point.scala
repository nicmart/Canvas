package springer.paint.point

/**
  * Created by Nicolò Martini on 28/04/2017.
  */
final case class Point(x: Int, y: Int) {
    def +(other: Point): Point = Point(x + other.x, y + other.y)
    def unary_-(): Point = Point(-x, -y)
    def -(other: Point): Point = this + (-other)
}
