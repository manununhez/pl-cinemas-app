package today.kinema.util

import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing

/**
 * Helps to avoid writing T::class.java in every mock
 */
inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

fun <T> whenever(methodCall: T): OngoingStubbing<T> = Mockito.`when`(methodCall)!!
