package net.insprill.cjm.util

import sun.misc.Unsafe
import java.lang.reflect.Field
import kotlin.reflect.KClass

object TestUtils {

    @Suppress("DEPRECATION") // Works for now, will fix it if it breaks
    fun setFinalField(clazz: KClass<*>, fieldName: String, value: Any) {
        val unsafeField = Unsafe::class.java.getDeclaredField("theUnsafe")
        unsafeField.isAccessible = true
        val unsafe = unsafeField.get(null) as Unsafe

        val ourField = clazz.java.getDeclaredField(fieldName)
        val staticFieldBase = unsafe.staticFieldBase(ourField)
        val staticFieldOffset = unsafe.staticFieldOffset(ourField)
        unsafe.putObject(staticFieldBase, staticFieldOffset, value)
    }

    fun <E : Enum<E>, V> setEnumField(enumValue: E, fieldName: String, newValue: V) {
        val field: Field = enumValue.javaClass.getDeclaredField(fieldName)
        field.isAccessible = true
        field.set(enumValue, newValue)
    }

}
