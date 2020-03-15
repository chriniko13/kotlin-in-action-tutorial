package chapter2

import java.lang.IllegalArgumentException
import java.util.*
import java.util.function.Consumer


fun main() {
    println(max(3, 5))


    val sentence = "a sentence"

    val answer: Int
    answer = 42

    println(answer)
    println("the number is: $answer")


    // ---
    class Person(val name: String, var isMarried: Boolean = false)

    val person = Person("dimitra")
    println(person.name)
    person.isMarried = true
    println(person.isMarried)


    // ---
    class Rectangle(val height: Int, val width: Int) {
        val isSquare: Boolean get() = height == width
    }

    val rect = Rectangle(3, 3)
    println(rect.isSquare)


    // ---
    val color = Color.YELLOW
    val result: Unit = when (color) {
        Color.RED, Color.INDIGO -> println("red ---> ${color.rgb()}")
        Color.ORANGE -> println("orange ---> ${color.rgb()}")
        Color.GREEN -> println("green ---> ${color.rgb()}")
        else -> println("under construction")
    }

    println(mix(Color.RED, Color.YELLOW))

    try {
        println(mix(Color.RED, Color.RED))
    } catch (error: Exception) {
        println("ooops...error")
    }

    println(mixOptimized(Color.YELLOW, Color.BLUE))


    // ---
    println()


    println(eval(Sum(Sum(Num(1), Num(2)), Num(4))))
    println(eval2(Sum(Sum(Num(1), Num(2)), Num(4))))
    println(eval3(Sum(Sum(Num(1), Num(2)), Num(4))))


    println(evalWithLogging(Sum(Sum(Num(1), Num(2)), Num(4))))


    // ---
    println()

    val oneToTen = 1..10
    oneToTen.forEach(Consumer { print(it) })

    println()

    for (i in 1..100) {
        print(fizzBuzz(i))
    }

    for (i in 100 downTo 1 step 2) {
        print(fizzBuzz(i))
    }

    val binaryReps = TreeMap<Char, String>()
    for (letter in 'A'..'F') {
        binaryReps[letter] = Integer.toBinaryString(letter.toInt())
    }

    println()
    for ((letter, binary) in binaryReps) {
        println("$letter = $binary")
    }

    println()
    val l = arrayListOf("1", "2", "3")
    for ((idx, elem) in l.withIndex()) {
        println("$idx == $elem")
    }

    fun isLetter(c: Char) = c in 'a'..'z' || c in 'A'..'Z'
    println(isLetter('a'))


    // ---
    println()
    fun validTemp(n: Int): Int = if (n in 1..10) n else throw IllegalArgumentException("not valid temp")
    println(validTemp(2))

    val x2 = try {
        println(validTemp(100))
        1
    } catch (error: Exception) {
        println(error)
        -1
    } finally {
        println("i always get executed")
    }
    println(x2)

}

// ---

fun max(a: Int, b: Int): Int = if (a > b) a else b


// ---

fun mix(c1: Color, c2: Color): Color =
        when (setOf(c1, c2)) {
            setOf(Color.RED, Color.YELLOW) -> Color.ORANGE
            setOf(Color.YELLOW, Color.BLUE) -> Color.GREEN
            setOf(Color.BLUE, Color.VIOLET) -> Color.INDIGO
            else -> throw Exception("dirty color")
        }

fun mixOptimized(c1: Color, c2: Color) =
        when {
            (c1 == Color.RED && c2 == Color.YELLOW) || (c1 == Color.YELLOW && c2 == Color.RED) -> Color.ORANGE


            (c1 == Color.YELLOW && c2 == Color.BLUE) ||
                    (c1 == Color.BLUE && c2 == Color.YELLOW) ->
                Color.GREEN
            (c1 == Color.BLUE && c2 == Color.VIOLET) ||
                    (c1 == Color.VIOLET && c2 == Color.BLUE) ->
                Color.INDIGO

            else -> throw Exception("dirty color")
        }

// ---

enum class Color(
        val r: Int, val g: Int, val b: Int
) {
    RED(255, 0, 0), ORANGE(255, 165, 0),
    YELLOW(255, 255, 0), GREEN(0, 255, 0), BLUE(0, 0, 255),
    INDIGO(75, 0, 130), VIOLET(238, 130, 238);

    fun rgb() = (r * 256 + g) * 256 + b
}

// ---

interface Expr
class Num(val value: Int) : Expr
class Sum(val left: Expr, val right: Expr) : Expr

fun eval(expr: Expr): Int {
    if (expr is Num) {
        // val n = expr as Num
        return expr.value
    }
    if (expr is Sum) {
        return eval(expr.left) + eval(expr.right)
    }
    throw IllegalArgumentException("unknown expression")
}

fun eval2(expr: Expr): Int {
    if (expr is Num) {
        return expr.value
    } else if (expr is Sum) {
        return eval2(expr.left) + eval2(expr.right)
    } else {
        throw IllegalArgumentException("unknown expression")
    }
}

fun eval3(expr: Expr): Int {
    return when (expr) {
        is Num -> expr.value
        is Sum -> eval3(expr.left) + eval3(expr.right)
        else -> throw IllegalArgumentException("unknown expression")
    }
}


fun evalWithLogging(expr: Expr): Int =
        when (expr) {
            is Num -> {
                println("value: ${expr.value}")
                expr.value
            }
            is Sum -> {
                val left = evalWithLogging(expr.left)
                val right = evalWithLogging(expr.right)
                println("sum: ${left + right}")
                left + right
            }
            else -> throw IllegalArgumentException("unknown expression")
        }


// ---
fun fizzBuzz(n: Int) = when {
    n % 15 == 0 -> "FizzBuzz "
    n % 3 == 0 -> "Fizz "
    n % 5 == 0 -> "Fuzz "
    else -> "$n "
}