package today.kinema.util

import org.mockito.Mockito

/**
 * Helps to avoid writing T::class.java in every mock
 */
inline fun <reified T> mock(): T = Mockito.mock(T::class.java)
