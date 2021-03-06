package chapter7

import chapter7.EmailService.loadEmails
import java.lang.IndexOutOfBoundsException
import java.time.LocalDate



// Note: Conventions == operator overloading.


fun main() {


    // ---
    println()

    val p1 = Point(1, 2)

    println(p1 + Point(3, 4))
    println(p1 * Point(3, 4))
    println(Point(1, 1) + 2)
    println(10 * Point(2, 3))

    println()
    println(p1)
    p1 += Point(2, 4)
    println(p1)
    println()


    println('a' * 10)


    println(0x0F and 0xF0)
    println(0x0F or 0xF0)
    println(0x1 shl 4)


    println()


    val numbers = ArrayList<Int>()
    numbers += 42
    println(numbers[0])

    println()

    println(-Point(4,4))

    var p2 = Point(1, 2)
    val p3 = ++p2 // p2++
    println(p3)
    println(p2)


    println()
    val point1 = Point(1, 2)
    val point2 = Point(1, 2)
    val point3 = Point(3, 4)
    println(point1 == point2)
    println(point1 == point1)
    println(point2 == point3)



    // ---
    println()

    println(Point(5, 4) < Point(3, 2))
    println(Point(5, 4) > Point(3, 2))
    println(Point(5, 4) <= Point(3, 2))


    val p4 = Point(10, 20)
    println(p4[0])
    println(p4[1])
    try {
        println(p4[10])
    }catch (ex: Exception) {
        println(ex)
    }


    // ---
    println()

    val mPoint = MutablePoint(1, 2)
    mPoint[0] = 1000
    mPoint[1] = 2000
    println(mPoint)


    // ---
    println()

    val rect = Rectangle(Point(10, 20), Point(50, 50))
    println(Point(20, 30) in rect)
    println(Point(5, 5) in rect)


    // ---
    println()

    val now = LocalDate.now()
    val vacation: ClosedRange<LocalDate> = now..now.plusDays(10)


    println(vacation)
    println(now.plusWeeks(1) in vacation)
    println(0..10)

    operator fun ClosedRange<LocalDate>.iterator(): Iterator<LocalDate> =
        object: Iterator<LocalDate> {

            var current = start

            override fun hasNext(): Boolean = current <= endInclusive

            override fun next(): LocalDate = current.apply {
                current = plusDays(1)
            }

        }

    for (d in vacation) {
        println(d)
    }


    // ---
    println()

    val (x, y) = Point(10, 20)
    println("($x, $y)")



    val map = mapOf("1" to "one", "2" to "two")
    for ((key, value) in map) { // uses extension function for iterator convention, and component1, component2 (destructuring)
        println("$key -> $value")
    }

    for (elem in map) {
        val key = elem.component1() // destructuring.
        val value = elem.component2()
        println("$key -> $value")

    }



    // ---
    println()

    // Example: lazy initialization (by lazy())

    val p = Person("Alice")
    p.emails
    p.emails

    p.emails2
    p.emails2


} // main.




class Point(var x: Int, var y: Int): Comparable<Point> {

    operator fun plus(p: Point): Point {
        return Point(x + p.x, y + p.y)
    }

    operator fun plus(n: Int) : Point {
        return Point(x + n, y + n)
    }

    operator fun plusAssign(p: Point) {
        this.x += p.x
        this.y += p.y
    }

    operator fun unaryMinus(): Point {
        return Point(-x, -y)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Point) return false
        return this.x == other.x && this.y == other.y
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    override fun compareTo(other: Point): Int {
        if (x < other.x) {
            return -1
        } else if (x == other.x) {
            return 0
        } else {
            return 1
        }
    }

    override fun toString(): String {
        return "Point(x=$x, y=$y)"
    }

    operator fun get(idx: Int): Int {
        return when(idx) {
            0 -> x
            1 -> y
            else -> throw IndexOutOfBoundsException()
        }
    }

    // Note: destructuring declarations.
    operator fun component1() = x
    operator fun component2() = y
}

operator fun Point.times(p: Point): Point = Point(x * p.x, y * p.y)

operator fun Int.times(p: Point): Point = Point(this * p.x, this * p.y)

operator fun Char.times(count: Int): String {
    return toString().repeat(count)
}

operator fun Point.inc(): Point = Point(x + 1, y + 1)


// ---

data class MutablePoint(var x: Int, var y: Int) {
    operator fun set(idx: Int, value: Int) {
        when (idx) {
            0 -> x = value
            1 -> y = value
            else -> throw IndexOutOfBoundsException()
        }
    }
}

// ---


data class Rectangle(val upperLeft: Point, val lowerRight: Point) {

    operator fun contains(other: Point): Boolean {
        val xRange = upperLeft.x until lowerRight.x
        val yRange = upperLeft.y until lowerRight.y

        return other.x in xRange && other.y in yRange
    }

}

// ---


object EmailService {
    fun loadEmails(person: Person): List<Email> {
        println("Load emails for ${person.name}")
        return listOf()
    }
}


class Email {
    // ...
}


class Person(val name: String) {
    // ...

    private var _emails: List<Email>? = null

    val emails: List<Email> get() {
        if (_emails == null) {
            _emails = loadEmails(this)
        }
        return _emails!!
    }

    val emails2: List<Email> by lazy { loadEmails(this) }
}
