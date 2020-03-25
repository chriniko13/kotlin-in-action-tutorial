package chapter6

import com.google.inject.*
import com.google.inject.internal.SingletonScope
import java.lang.IllegalStateException



fun main() {

    // ---
    println()

    fun strLen(s: String?):Int? = s?.length
    fun strLen2(s: String?):Int = s?.length ?: 0


    println(strLen(null))
    println(strLen2(null))


    class Address(val streetAddress: String, val zipCode: Int,
                  val city: String, val country: String)

    class Company(val name: String, val address: Address?)

    class Person(val name: String, val company: Company?)

    fun Person.countryName(): String {
        return this.company?.address?.country ?: "Unknown"
    }

    val person = Person("Dmitry", null)
    println(person.countryName())

    fun printShippingLabel(person: Person): Address {
        val address = person.company?.address ?: throw IllegalArgumentException("No address")
        return with (address) {
            println(streetAddress)
            println("$zipCode $city, $country")
            this
        }
    }

    try {
        printShippingLabel(person)
    } catch (e: Exception) {
        println(e)
    }


    val address = Address("Elsestr. 47", 80687, "Munich", "Germany")
    val jetbrains = Company("JetBrains", address)
    val person2 = Person("Dmitry", jetbrains)
    printShippingLabel(person2)


    var obj: Any? = "123"

    if (obj is String) {
        val s = obj as String
        println(s.length)
    }


    val length = (obj as? String)?.length
    println(length)

    obj = null
    val length2 = (obj as? String)?.length ?: 0
    println(length2)



    fun printFirst(s: String?): Unit = println(s!![0])
    printFirst("first")
    try {
        printFirst(null)
    } catch (e: Exception) {
        println(e)
    }


    fun sum(a: Int, b: Int): Int = a + b

    var a: Int? = null
    var b: Int? = 3

    val result = a?.let {aC ->
        b?.let { bC ->
            sum(aC, bC)
        }
    }
    println(result)


    // ---
    println()


    class MyService {

        init {
            println("MyService initialized from: ${Thread.currentThread().name}")
        }

        fun performAction(): String = "foo"
    }

    class MyResource {

        init {
            println("MyResource initialized from: ${Thread.currentThread().name}")
        }

        private lateinit var myService: MyService

        @Inject
        constructor(myService: MyService) {
            this.myService = myService
        }


        fun consumeResource() = "resource --- ${myService.performAction()}"
    }


    class MyModule : AbstractModule() {
        override fun configure() {123
            bind(MyService::class.java).`in`(SingletonScope())
            bind(MyResource::class.java).`in`(SingletonScope())
        }

    }

    val myModule = MyModule()
    val injector = Guice.createInjector(myModule)

    val myResource = injector.getInstance(MyResource::class.java)
    println(myResource.consumeResource())

    val myResource2 = injector.getInstance(MyResource::class.java)
    println(myResource2.consumeResource())


    // ---
    println("\n")


    fun String?.isNullOrBlank(): Boolean {
        return this == null || this.isEmpty()
    }

    fun verifyUserInput(input: String?) {
        if (input.isNullOrBlank()) {
            println("please fill the required information!")
        }
    }
    verifyUserInput(null)


    // ---
    println()

    fun <T> printHashCode(t: T) {
        println(t?.hashCode())
    }
    printHashCode(null)

    fun <T: Any> printHashCode2(t: T?) {
        println(t?.hashCode())
    }
    printHashCode2(null)



    // ---
    println()

    val personJava = Person() // platform type.
    fun yellAt(person: chapter6.Person) {
        println((person.name ?: "unknown" ).toUpperCase() + "!!!")
    }

    yellAt(personJava)


    // ---
    println()

    fun showProgress(progress: Int) {
        val percent = progress.coerceIn(0, 100)
        println("We're ${percent}% done!")
    }
    showProgress(146)

    val x = 1
    println(x.toLong() in listOf(1L, 2L, 3L))


    // ---
    println()

    val processor = NoResultProcessor()
    println(processor.process(1))



    fun <T: Any>copyElements(source: Collection<T>, target: MutableCollection<T>): Unit {
     for (elem in source) {
         target += elem
     }
    }
    val source: Collection<Int> = arrayListOf(3, 5, 7)
    val target: MutableCollection<Int> = arrayListOf(1)
    copyElements(source, target)
    println(target)


    // ---
    println()

    val arr = arrayOf(1,2,3)
    println(arr)


    val arr2 = Array<String>(26) {i -> ('a' + i).toString()}
    println(arr2)

}


interface Processor<I: Any, O: Any> {
    fun process(i: I): O
}

class NoResultProcessor: Processor<Int, Unit> {
    override fun process(t: Int) {
        //do stuff...
        println(t)
    }
}

class NoResultProcessor2: Processor<Int, Nothing> {
    override fun process(t: Int): Nothing {
        //do stuff...
        throw IllegalStateException()
    }
}

