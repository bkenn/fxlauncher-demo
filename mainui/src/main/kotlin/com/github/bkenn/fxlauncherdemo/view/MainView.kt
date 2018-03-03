package com.github.bkenn.fxlauncherdemo.view

import com.github.bkenn.fxlauncherdemo.app.Styles
import tornadofx.*

class MainView : View("Hello TornadoFX") {
    override val root = hbox {
        label(title) {
            addClass(Styles.heading)
        }
    }
}