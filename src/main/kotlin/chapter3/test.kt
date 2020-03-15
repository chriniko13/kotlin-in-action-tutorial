package chapter3

import java.lang.StringBuilder



const val UNIX_LINE_SEPARATOR = "\n"


fun main() {

    val hashSet = hashSetOf(1, 2, 3)
    println(hashSet + " " + hashSet.javaClass)

    val arrayList = arrayListOf(1, 2, 3)
    println(arrayList + " " + arrayList.javaClass)

    val hashMap = hashMapOf(1 to "1", 2 to "2")
    println(hashMap.toString() + " " + hashMap.javaClass)

    println(hashSet.max())
    println(arrayList.last())
    println(hashMap.maxBy { it.value })


    println(joinToString(arrayListOf(1, 2, 3, 4), separator = ":", prefix = "[", postfix = "]"))
    println(joinToString(arrayListOf(1, 2, 3, 4)))


    println("chriniko".lastChar())

    println(arrayListOf("1", "2").joinToStringExt())


    println()
    val view = View()
    val button = Button()
    view.click()
    button.click()

    val view2: View = Button()
    view2.clickExt()

    // ---
    println()

    println("dimitra".lastChar)

    val sb = StringBuilder("123")
    println(sb.lastChar)
    sb.lastChar = '4'
    println(sb.lastChar)
    println(sb)

    // ---
    println()
    varargsExample(1,2,3,4,"5")

    println((1 to "a").javaClass)

    println(1 goesTo 2) // Note: infix notation.

    val (a, b) = 1 goesTo 2 // Note: destructuring declaration.
    println(a + b)

    // ---
    println()

    println("12.345-6.A".split("\\.".toRegex()))
    println("12.345-6.A".split("."))
    println("12.345-6.A".split(".", "-"))

    parsePath("/Users/yole/kotlin-book/chapter.adoc")
    parsePath2("/Users/yole/kotlin-book/chapter.adoc")


    // ---
    println()

    val kotlinLogo ="""| //
                      .|//
                      .|/ \"""

    println(kotlinLogo.trimMargin("."))



    // ---
    println()

    class User(val id: Int, val name: String, val address: String)

    fun saveUser(user: User) {
        fun User.validate(value: String, fieldName: String) {
            if (value.isEmpty()) {
                throw IllegalArgumentException(
                        "Can't save user ${user.id}: " +
                                "empty $fieldName")
            }
        }

        user.validate(user.name, "Name")
        user.validate(user.address, "Address")

        // Note: time to save the user to the database....
        println("will save user to the database now...")
    }

    val user = User(1, "chriniko", "greece")
    saveUser(user)



}

// ---

@JvmOverloads
fun <T> joinToString(collection: Collection<T>, separator: String = ", ", prefix: String = "", postfix: String = ""): String {

    val result = StringBuilder(prefix)

    for ((index, elem) in collection.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(elem)
    }

    result.append(postfix)
    return result.toString()
}

// ---

fun String.lastChar(): Char {
    return this[this.length - 1]
}

// ---

fun <T> Collection<T>.joinToStringExt(separator: String = ", ", prefix: String = "", postfix: String = ""): String {

    val result = StringBuilder(prefix)

    for ((index, elem) in this.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(elem)
    }

    result.append(postfix)
    return result.toString()
}

// ---

open class View {
    open fun click() = println("View class clicked!")
}

class Button : View() {
    override fun click() {
        println("Button class clicked!")
    }
}

fun View.clickExt() = println("View ext class clicked!")

fun Button.clickExt() = println("Click ext class clicked!")

// ---

val String.lastChar: Char get() = this.get(this.length - 1)

var StringBuilder.lastChar: Char
    get() = this.get(this.length - 1)
    set(value) = this.set(this.length - 1, value)


// ---


fun <T> varargsExample(vararg elems: T) {
    for (elem in elems) {
        println(elem)
    }
}

infix fun Int.goesTo(elem: Int): List<Int> {
    return listOf(this, elem)
}

// ---
fun parsePath(path: String) {
    val directory = path.substringBeforeLast("/")
    val fullName = path.substringAfterLast("/")
    val fileName = fullName.substringBeforeLast(".")
    val extension = fullName.substringAfterLast(".")
    println("Dir: $directory, name: $fileName, ext: $extension")
}

fun parsePath2(path: String) {
    val regex = """(.+)/(.+)\.(.+)""".toRegex()
    val matchResult = regex.matchEntire(path)
    if (matchResult != null) {
        val (directory, filename, extension) = matchResult.destructured
        println("Dir: $directory, name: $filename, ext: $extension")
    }
}

// ---