package io.github.monun.aimless.plugin

import io.github.monun.tap.loader.LibraryLoader

object PlayerList: Runnable {
    private var update = false
    fun update() {
        update = true
    }

    override fun run() {
        if(update) {
            update = false
            val list = requireNotNull(LibraryLoader.loadNMS(List::class.java)) { "Unable to load NMS Class" }
            list.updatePlayerList()
        }
    }
}