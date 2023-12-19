package com.danilovfa.deskmotion.receiver.features.history.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.danilovfa.deskmotion.receiver.features.history.detail.HistoryDetailComponent
import com.danilovfa.deskmotion.receiver.features.history.main.HistoryMainComponent
import com.danilovfa.deskmotion.receiver.features.history.path_details.HistoryPathDetailsComponent

interface HistoryRootComponent {
    val childrenStack: Value<ChildStack<*, Child>>

    sealed class Child {
        data class Main(val component: HistoryMainComponent) : Child()
        data class Detail(val component: HistoryDetailComponent) : Child()
        data class PathDetails(val component: HistoryPathDetailsComponent) : Child()
    }
}