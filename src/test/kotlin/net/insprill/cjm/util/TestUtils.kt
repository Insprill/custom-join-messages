package net.insprill.cjm.util

import sun.misc.Unsafe
import kotlin.reflect.KClass

object TestUtils {

    fun setFinalField(clazz: KClass<*>, fieldName: String, value: Any) {
        val unsafeField = Unsafe::class.java.getDeclaredField("theUnsafe")
        unsafeField.isAccessible = true
        val unsafe = unsafeField.get(null) as Unsafe

        val ourField = clazz.java.getDeclaredField(fieldName)
        val staticFieldBase = unsafe.staticFieldBase(ourField)
        val staticFieldOffset = unsafe.staticFieldOffset(ourField)
        unsafe.putObject(staticFieldBase, staticFieldOffset, value)
    }

}
