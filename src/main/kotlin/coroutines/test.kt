package coroutines


import kotlinx.coroutines.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// Co-routines tutorial (async/await, withContext, launch, delay, runBlocking)

// Co-routine == lightweight thread


fun main() {

    //exampleBlocking()

    //exampleBlockingCR()

    //exampleBlockingDispatcher()

    //exampleLaunchGlobal()

    //exampleLaunchCoroutineScopeLocal()

    exampleAsyncAwait()
}


fun printlnDelayed(message: String) {
    // complex calculation
    Thread.sleep(2000)
    println(message)
}


/*
    Note: normal thread style.
 */
fun exampleBlocking() {
    println("one")
    printlnDelayed("two")
    println("three")
}


// ----

suspend fun printlnDelayedCR(message: String) {
    // complex calculation
    delay(2000)
    println(message)
}


fun exampleBlockingCR() {
    println("one")

    // runBlocking == simulates somehow thread block.
    runBlocking {
        printlnDelayedCR("two")
    }

    println("three")
}


fun exampleBlockingDispatcher() {

    runBlocking(Dispatchers.Default) {
        println("one from thread: ${Thread.currentThread().name}")
        printlnDelayedCR("two from thread: ${Thread.currentThread().name}")
    }
    println("three from thread: ${Thread.currentThread().name}")

}

fun exampleLaunchGlobal() {

    println("one from thread: ${Thread.currentThread().name}")

    val job = GlobalScope.launch(Dispatchers.Default) {
        printlnDelayedCR("two from thread: ${Thread.currentThread().name}")

    }

    println("three from thread: ${Thread.currentThread().name}")

    runBlocking {
        job.join()
    }

}

fun exampleLaunchCoroutineScopeLocal() {

    val customDispatcher = Executors.newFixedThreadPool(2).asCoroutineDispatcher()

    runBlocking {

        println("one from thread: ${Thread.currentThread().name}")

        // Note: inside local coroutine scope we do not need explicitly to join the job, it is done by the compiler.
        launch(customDispatcher) {
            printlnDelayedCR("two from thread: ${Thread.currentThread().name}")
        }

        println("three from thread: ${Thread.currentThread().name}")

        (customDispatcher.executor as ExecutorService).shutdownNow()
    }

}


suspend fun heavyCalculate(number: Int): Int {
    delay(1000)
    return number + 1000
}


// Note: async - await runs concurrently.
fun exampleAsyncAwait() = runBlocking {

    val sT = System.currentTimeMillis()

    val n1 = async(Dispatchers.IO) {
        println("1 ${Thread.currentThread().name}")
        heavyCalculate(1)
    }

    val n2 = async(Dispatchers.IO) {
        println("2 ${Thread.currentThread().name}")
        heavyCalculate(2)
    }

    val n3 = async(Dispatchers.IO) {
        println("3 ${Thread.currentThread().name}")
        heavyCalculate(3)
    }

    val n = n1.await() + n2.await() + n3.await()

    val tT = System.currentTimeMillis() - sT
    println("total time: $tT")

    n
}


// Note: withContext does not block main thread, but it is like async { ... } .await()