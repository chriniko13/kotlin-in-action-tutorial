package chapter7

import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import kotlin.properties.Delegates
import kotlin.reflect.KProperty


fun main() {

    // ---
    println()

    val p = PersonDelegatedProperty("Dmitry", 34, 2000)
    p.addPropertyChangeListener(PropertyChangeListener {
        println("${Thread.currentThread().name} :: Property ${it.propertyName} changed " +
                "from ${it.oldValue} to ${it.newValue}")
    })

    p.age = 35
    p.salary = 2100


    // ---
    println()

    val p2 = PersonDelegatedProperty2("Dmitry", 34, 2000)
    p2.addPropertyChangeListener(PropertyChangeListener {
        println("${Thread.currentThread().name} :: Property ${it.propertyName} changed " +
                "from ${it.oldValue} to ${it.newValue}")
    })
    p2.age = 27
    p2.salary = 2300


    // ---
    println()
    val p3 = PersonDelegatedProperty3("Dmitry", 34, 2000)
    p3.addPropertyChangeListener(PropertyChangeListener {
        println("${Thread.currentThread().name} :: Property ${it.propertyName} changed " +
                "from ${it.oldValue} to ${it.newValue}")
    })
    p3.age = 27
    p3.salary = 2300

    p3.skills = mutableListOf("java", "scala", "kotlin")


    // ---
    println("\n")
    val pExpando = PersonExpando()
    val data = mapOf("name" to "Dmitry", "company" to "JetBrains")

    for ((attrName, value) in data)
        pExpando.setAttribute(attrName, value)

    println(pExpando.name)

    pExpando.setAttribute("address", "some address")
    println(pExpando.address)


} // main.


// ---------------------------------------------------------------------------------------------------------------------

open class PropertyChangeAware {

    protected val changeSupport = PropertyChangeSupport(this)

    fun addPropertyChangeListener(listener: PropertyChangeListener) {
        changeSupport.addPropertyChangeListener(listener)
    }

    fun removePropertyChangeListener(listener: PropertyChangeListener) {
        changeSupport.removePropertyChangeListener(listener)
    }
}

// 1st example
class PersonDelegatedProperty(val name: String, age: Int, salary: Int) : PropertyChangeAware() {

    var age: Int = age
        set(newValue) {
            val oldValue = field
            field = newValue
            changeSupport.firePropertyChange(
                    "age", oldValue, newValue)
        }

    var salary: Int = salary
        set(newValue) {
            val oldValue = field
            field = newValue
            changeSupport.firePropertyChange(
                    "salary", oldValue, newValue)
        }

}


// delegate
class ObservableProperty(val propName: String, var propValue: Any, val changeSupport: PropertyChangeSupport) {

    fun getValue(): Any = propValue

    fun setValue(value: Any): Unit {
        val oldValue = propValue
        propValue = value
        changeSupport.firePropertyChange(propName, oldValue, value)
    }
}


// 2nd example (the following is created automatically from 'by' keyword)
class PersonDelegatedProperty2(val name: String, age: Int, salary: Int) : PropertyChangeAware() {

    // delegate.
    private val _age = ObservableProperty("age", age, changeSupport)

    var age: Int
        get() = _age.getValue() as Int
        set(value) = _age.setValue(value)


    // delegate.
    private val _salary = ObservableProperty("salary", salary, changeSupport)

    var salary: Int
        get() = _salary.getValue() as Int
        set(value) = _salary.setValue(value)

}


// delegate
class ObservableProperty2(var propValue: Int, val changeSupport: PropertyChangeSupport) {

    operator fun getValue(p: PersonDelegatedProperty3/*receiver*/, prop: KProperty<*>): Int = propValue

    operator fun setValue(p: PersonDelegatedProperty3/*receiver*/, prop: KProperty<*>, newValue: Int) {
        val oldValue = propValue
        propValue = newValue
        changeSupport.firePropertyChange(prop.name, oldValue, newValue)
    }

}


// 3rd example
class PersonDelegatedProperty3(val name: String, age: Int, salary: Int) : PropertyChangeAware() {

    var age: Int by ObservableProperty2(age, changeSupport)

    var salary: Int by ObservableProperty2(salary, changeSupport)

    var skills: MutableList<String> by Delegates.observable(mutableListOf(),
            onChange = { property: KProperty<*>, oldValue: MutableList<String>, newValue: MutableList<String> ->
                changeSupport.firePropertyChange(property.name, oldValue, newValue)
            }
    )

}

// ---------------------------------------------------------------------------------------------------------------------

class PersonExpando {
    private val _attributes = hashMapOf<String, String>()

    fun setAttribute(attrName: String, value: String) {
        _attributes[attrName] = value
    }

    val name: String
        get() = _attributes["name"]!!

    val address:String by _attributes

}

