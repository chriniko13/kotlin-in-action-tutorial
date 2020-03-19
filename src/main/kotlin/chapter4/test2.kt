package chapter4

import java.io.File

fun main() {


    // ---
    Payroll.allEmployees.add(Payroll.Employee(1, "name", 1234.00))
    println(Payroll.calculateSalary())

    println(CaseInsensitiveFileComparator.compare(File("/User"), File("/user")))
    println(CaseInsensitiveFileComparator.compare(File("/aUser12"), File("/user")))


    val persons = listOf(Person("Bob"), Person("Alice"))
    println(persons.sortedWith(Person.NameComparator))

    A.bar()

    println()

    val subscribingUser = SiteUser.newSubscribingUser("foo@bar.gr")
    println(subscribingUser)

    val facebookUser = SiteUser.newFacebookUser(1)
    println(facebookUser)


    println(Person.Loader.fromJson("afeafe"))
    println(Person.fromJson("adaede3a"))
    val p = Person.fromJson2("adeade")
    println(p)
}


// ---
object Payroll {
    // ...

    data class Employee(val id: Int, val name: String, val salary: Double)

    val allEmployees = arrayListOf<Employee>()

    fun calculateSalary(): Double {
        var total = 0.0
        for (employee in allEmployees) {
            total += employee.salary
        }
        return total
    }
}


object CaseInsensitiveFileComparator : Comparator<File> {
    override fun compare(p0: File?, p1: File?): Int {
        return p0?.path?.compareTo(p1?.path ?: "", ignoreCase = true) ?: -1
    }

}


// business logic module.
data class Person(val name: String) {
    object NameComparator : Comparator<Person> {
        override fun compare(o1: Person?, o2: Person?): Int {
            return o1!!.name.compareTo(o2!!.name)
        }

    }

    companion object Loader : JSONFactory<Person> {
        override fun fromJson(jsonText: String): Person = Person("TODO...")
    }
}

fun Person.Loader.fromJson2(json: String): Person = Person("TODO...")


class A {
    companion object {
        fun bar() {
            println("companion object called")
        }
    }
}


class SiteUser private constructor(val nickname: String) {

    companion object {
        fun newSubscribingUser(email: String) = SiteUser(email.substringBefore('a'))

        fun newFacebookUser(accountId: Int) = SiteUser(getFacebookName(accountId))

        private fun getFacebookName(facebookAccountId: Int): String = ""
    }


}


interface JSONFactory<T> {
    fun fromJson(jsonText: String): T
}