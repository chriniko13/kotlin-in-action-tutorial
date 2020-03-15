package chapter4

import java.io.Serializable

//TODO 103/362


fun main() {

    Button().click()
    Button().showOff()
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

open class RichButton: Clickable {

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

interface State: Serializable

interface View {
    fun getCurrentState(): State
    fun restoreState(state: State) {}
}

class Button2: View {
    override fun getCurrentState(): State {
        return ButtonState()
    }

    override fun restoreState(state: State) {
        /* ... */
    }

    class ButtonState: State {
        /* ... */
    }

}