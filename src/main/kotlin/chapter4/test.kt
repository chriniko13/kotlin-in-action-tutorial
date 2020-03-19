package chapter4

import java.io.Serializable


fun main() {

    Button().click()
    Button().showOff()


    // ---
    println()

    val result = evalExpr(Expr.Sum(Expr.Sum(Expr.Num(1), Expr.Num(2)), Expr.Num(4)))
    println(result)


    val user1 = User("nickname1")
    println(user1.nickname)


    val user2 = User2("nickname2")
    println(user2.nickname)


    println(User3("nickname3"))

    println(TwitterUser("meow").nickname)
    println(Guest())

    // ---
    println()

    val user5 = User5("some-user")
    user5.address = "Some Address"


    val lengthCounter = LengthCounter()
    lengthCounter.addWord("Hi!")
    println(lengthCounter.counter)

    println()
    val record = Record(1)
    println(record)
    println(record.hashCode())
    println(record == Record(1))
    println(record === Record(1))


    println()
    val n = Node(1)
    val n2 = n.copy()
    println(n2.v)
    println(n === n2)


    // ---
    println("\n")

    val countingSet = CountingSet<Int>()
    countingSet.addAll(listOf(1,2,3))
    println(countingSet.objectsAdded)
}

// ---

interface Clickable {
    fun click()
    fun showOff() = println("I'm clickable!")
}

interface Focusable {
    fun showOff() = println("I'm focusable")
}

class Button : Clickable, Focusable {
    override fun click() {
        println("I was clicked")
    }

    override fun showOff() {
        super<Focusable>.showOff()
        super<Clickable>.showOff()
        println("merged show off")
    }
}

open class RichButton : Clickable {

    fun disable() {}

    open fun animate() {}

    final override fun click() {}

}

internal open class TalkativeButton : Focusable {
    private fun yell() = println("Hey!")

    protected fun whisper() = println("Let's talk!")
}

//fun TalkativeButton.giveSpeech() {
//    yell()
//    whisper()
//}


// ---

interface State : Serializable

interface View {
    fun getCurrentState(): State
    fun restoreState(state: State) {}
}

class Button2 : View {
    override fun getCurrentState(): State {
        return ButtonState()
    }

    override fun restoreState(state: State) {
        /* ... */
    }

    class ButtonState : State { // Note: same as static nested class in Java.
        /* ... */
    }

    inner class ButtonState2 : State {
        /* ... */
        fun something() {
            println(this@Button2.getCurrentState()) // Note: reference outer class.
        }
    }

}

// ---

sealed class Expr {

    class Num(val value: Int) : Expr()
    class Sum(val left: Expr, val right: Expr) : Expr()

}

fun evalExpr(expr: Expr): Int {
    return when (expr) {
        is Expr.Num -> expr.value
        is Expr.Sum -> evalExpr(expr.left) + evalExpr(expr.right)
    }
}

// ---

class User constructor(_nickname: String) {
    val nickname: String

    init { // initializer block.
        nickname = _nickname
        println("initializer block called!")
    }
}

class User2(_nickname: String) {
    val nickname = _nickname
}

data class User3(val nickname: String, val isSubscribed: Boolean = true)


open class User4(val nickname: String)
class TwitterUser(nickname: String) : User4(nickname)
open class Guest


class Secretive private constructor() {}
//val secretive = Secretive()


// ---


interface Context

open class BasicView {
    constructor(ctx: Context) {
        //...
    }
}

open class GUIView : BasicView {

    constructor(ctx: Context) : super(ctx) {
        // ...
    }

    constructor(ctx: Context, append: Boolean) : this(ctx) {
        // ...
    }

}

// ---

interface UserInt { // Note: like trait in Scala.
    val nickname: String
    val fastNickname: String get() = nickname.substring(0, 3)
}

class PrivateUser(override val nickname: String) : UserInt

class SubscribingUser(val email: String) : UserInt {
    override val nickname: String get() = email.substringBefore('@')
}

class FacebookUser(val accountId: Int) : UserInt {
    override val nickname: String = getFacebookName(accountId)

    private fun getFacebookName(accountId: Int): String {
        return "dummy"
    }
}

// ---
class User5(val name: String) {
    var address: String? = null
        set(value) {
            println("""Address was changed for $name:"$field" -> "$value".""".trimIndent())
            field = value
        }
}


// ---
class LengthCounter {
    var counter: Int = 0
        private set

    fun addWord(word: String) {
        counter += word.length
    }

}


// ---

data class Record(val v: Int)

class Node(val v: Int) {
    fun copy(v: Int = this.v): Node {
        return Node(v)
    }
}


// ---
// Note: delegates by using keyword by.
class CountingSet<T>(val innerSet: HashSet<T> = HashSet()): MutableCollection<T> by innerSet {

    var objectsAdded = 0

    override fun add(element: T): Boolean {
        objectsAdded++
        return innerSet.add(element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        objectsAdded += elements.size
        return innerSet.addAll(elements)
    }
}


