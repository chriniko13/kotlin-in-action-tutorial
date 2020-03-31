package chapter8

import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock


//TODO 239/362 (212)


// Note: higher order functions, function types.

fun main() {


    // ---
    println()
    val sum: (Int, Int) -> Int = { x: Int, y: Int -> x + y }
    println(sum(1, 1))


    val action: () -> Unit = { println(42) }
    println(action())


    performRequest("http://google.com") { code: Int, content: String? ->
        println("code: $code content: $content")
    }


    // ---
    println()
    println("abcdeFGHIJ".filter { it.isLowerCase() })


    fun processAnswer(f: (Int) -> Int) {
        println(f(42))
    }

    processAnswer { it + 10 }

    println(Unit)


    val letters = listOf("Alpha", "Beta")
    println(letters.joinToString())
    println(letters.joinToString(transform = { it.toUpperCase() }))



    fun foo(callback: (() -> Unit)?) {
        val fn = callback ?: { println(42) }
        fn()
    }
    foo(null)


    // ---
    println()

    val calculator = getShippingCostCalculator(Delivery.EXPEDITED)
    println("Shipping costs ${calculator(Order(3))}")



    println(ContactListFilters().getPredicate()(Person("f", "l", null)))


    val log = listOf(
            SiteVisit("/", 34.0, OS.WINDOWS),
            SiteVisit("/", 22.0, OS.MAC),
            SiteVisit("/login", 12.0, OS.WINDOWS),
            SiteVisit("/signup", 8.0, OS.IOS),
            SiteVisit("/", 16.3, OS.ANDROID)
    )


    val averageWindowsDuration = log.filter { it.os == OS.WINDOWS }
            .map { it.duration }
            .average()

    println(averageWindowsDuration)


    fun List<SiteVisit>.averageDurationFor(os: OS) = filter { it.os == os }.map { it.duration }.average()

    println(log.averageDurationFor(OS.ANDROID))


    val averageMobileDuration = log
            .filter { it.os in setOf(OS.IOS, OS.ANDROID) }
            .map(SiteVisit::duration)
            .average()
    println(averageMobileDuration)


    fun List<SiteVisit>.averageDurationFor(predicate: (SiteVisit) -> Boolean) = filter(predicate).map(SiteVisit::duration).average()

    println(log.averageDurationFor { it.path.contains("login") })


    // ---
    println()


    val workerPool = Executors.newFixedThreadPool(10)

    val lock: ReadWriteLock = ReentrantReadWriteLock(true)
    val latch = CountDownLatch(10)

    val workers = (1..10).map {
        {
            mySynchronized(lock) {
                println("${Thread.currentThread().name} acquired lock...will finish soon")
                Thread.sleep(1000)
                latch.countDown()
            }
        }
    }

    workers.forEach { workerPool.submit(it) }

    latch.await()
    workerPool.shutdownNow()

} // main.

// ---


fun performRequest(
        url: String,
        callback: (code: Int, content: String?) -> Unit) {
    println("hitting the following url with post: $url")
    callback(200, "Hello world")
}


fun String.filter(predicate: (Char) -> Boolean): String {
    val sb = StringBuilder()
    for (index in this.indices) {
        val elem = get(index)
        if (predicate(elem)) sb.append(elem)
    }
    return sb.toString()
}


fun <T> Collection<T>.joinToString(
        separator: String = ", ",
        prefix: String = "",
        postfix: String = "",
        transform: (T) -> String = { it.toString() }
): String {
    val result = StringBuilder(prefix)

    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(transform(element))
    }

    result.append(postfix)
    return result.toString()
}


// ----

enum class Delivery { STANDARD, EXPEDITED }

class Order(val itemCount: Int)

fun getShippingCostCalculator(delivery: Delivery): (Order) -> Double {

    if (delivery == Delivery.EXPEDITED) {
        return { order -> 6 + 2.1 * order.itemCount }
    }
    return { order -> 1.2 * order.itemCount }
}


// ----

data class Person(
        val firstName: String,
        val lastName: String,
        val phoneNumber: String?
)

class ContactListFilters {

    var prefix: String = ""
    var onlyWithPhoneNumber: Boolean = true

    fun getPredicate(): (Person) -> Boolean {
        val startsWithPrefix = { p: Person ->
            p.firstName.startsWith(prefix) || p.lastName.startsWith(prefix)
        }
        if (!onlyWithPhoneNumber) {
            return startsWithPrefix
        }
        return {
            startsWithPrefix(it) && it.phoneNumber != null
        }
    }
}


// ----


data class SiteVisit(
        val path: String,
        val duration: Double,
        val os: OS
)

enum class OS { WINDOWS, LINUX, MAC, IOS, ANDROID }


// ----


inline fun <T> mySynchronized(lock: ReadWriteLock, fn: () -> T): T {
    lock.writeLock().lock()
    try {
        return fn()
    } finally {
        lock.writeLock().unlock()
    }
}


