package chapter5

import kotlin.reflect.KProperty1

//TODO 155/362 (128)


fun main() {


    // ---
    val people = listOf(Person("Alice", 29), Person(name = "Bob", age = 31))
    println(findTheOldest(people))

    println(people.maxBy { it.age })
    println(people.maxBy(Person::age))


    // ---
    println()

    val sum: (Int, Int) -> Int = { x: Int, y: Int -> x + y }
    println(sum(1, 1))

    println({ x: Int, y: Int -> x * y }(1, 2))


    val someLambda = { println(42) }
    run(someLambda)


    // ---
    println()

    println(people.maxBy() { it.age })

    println(people.joinToString(separator = ", ", transform = { it.name }))

    println(people.joinToString(", ") { it.name })


    // ---
    println()
    fun printProblemCounts(errors: Collection<String>) {
        var clientErrors = 0
        var serverErrors = 0

        errors.forEach {
            if (it.startsWith("4")) {
                clientErrors++
            }

            if (it.startsWith("5")) {
                serverErrors++
            }
        }

        println("$clientErrors client errors, $serverErrors server errors")
    }

    val responses = listOf("200 OK", "418 I'm a teapot", "500 Internal Server Error")
    printProblemCounts(responses)


    // ---
    println()

    val getAge: KProperty1<Person, Int> = Person::age

    println(getAge(Person("name", 1)))


    fun saySth() {
        println("say something...")
    }

    val saySthFun = ::saySth

    saySthFun()
    run(saySthFun)


    val createPeson = ::Person
    val p = createPeson("n", 1)
    println(p)

    val getAge2 = p::age // bound parameters
    println(getAge.get(p))

    // ---
    println()

    // filter, map
    val people2 = listOf(Person("Alice", 29), Person("Bob", 31))
    println(people2.map { it.name })

    val numbers = mapOf(0 to "zero", 1 to "one")
    println(numbers.mapValues { it.value.toUpperCase() })

    // all, any, count, find
    val canBeInClub27 = { p: Person -> p.age <= 27 }

    println(people2.all(canBeInClub27))
    println(people2.any(canBeInClub27))
    println(people2.count(canBeInClub27))

    println(people2.find(canBeInClub27)) // returns a nullable type (?)

    println(people2.groupBy(getAge))

    // flatMap, flatten

    class Book(val title: String, val authors: List<String>)

    val books = listOf(Book("Thursday Next", listOf("Jasper Fforde")),
            Book("Mort", listOf("Terry Pratchett")),
            Book("Good Omens", listOf("Terry Pratchett", "Neil Gaiman"))
    )
    println(books.map { it.authors }.toSet())
    println(books.map { it.authors }.flatten().toSet())
    println(books.flatMap { it.authors }.toSet())

    val result = people2.asSequence()
            .map(Person::name)
            .filter { it.startsWith("A") }

    println(result.toList())

    println(result.iterator().next())

    // sequence == Java 8 Stream, intermediate operation and terminal operation.

    println(listOf(1, 2, 3, 4).asSequence().map { it * it }.find { it > 3 })


    val naturalNumbers = generateSequence(0) { it + 1 }
    val numbersTo100 = naturalNumbers.takeWhile { it <= 100 }
    println(numbersTo100)
    println(numbersTo100.sum())

    
    // ---
    println()
    
    val postponeUtil = PostponeUtil()
    postponeUtil.postponeComputation(1000) { println(42) }
    postponeUtil.postponeComputation(1000, object: Runnable {
        override fun run() {
            println(42)
        }
    })

    fun createAllDoneRunnable(): Runnable {
        return Runnable { // SAM Constructor.
            println("all done!")
        }
    }

    createAllDoneRunnable().run()


    // --
    println()

    

} // main.

// ---
data class Person(val name: String, val age: Int)

fun findTheOldest(people: List<Person>): Person? {

    var maxAge = 0
    var oldestPerson: Person? = null

    for (person in people) {
        if (person.age >= maxAge) {
            maxAge = person.age
            oldestPerson = person
        }
    }
    return oldestPerson
}