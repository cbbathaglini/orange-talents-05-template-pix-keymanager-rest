package br.com.pix.rest

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("br.com.pix.rest")
		.start()
}

