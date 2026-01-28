package com.dev.core

abstract class TextLoaderProvider<V : TextLoader> {

    abstract fun create(): V
}