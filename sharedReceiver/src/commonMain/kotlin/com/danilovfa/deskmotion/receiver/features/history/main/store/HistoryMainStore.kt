package com.danilovfa.deskmotion.receiver.features.history.main.store

import com.arkivanov.mvikotlin.core.store.Store
import com.danilovfa.deskmotion.receiver.domain.model.PlayLog
import com.danilovfa.deskmotion.receiver.domain.model.User
import com.danilovfa.deskmotion.receiver.features.history.main.model.HistoryMainPage
import com.danilovfa.deskmotion.receiver.features.history.main.model.HistoryMainUserSort

interface HistoryMainStore :
    Store<HistoryMainStore.Intent, HistoryMainStore.State, HistoryMainStore.Label> {
    sealed class Intent {
        data class OnPlayLogClicked(val log: PlayLog) : Intent()
        data class OnUserClicked(val user: User) : Intent()
        data object DismissErrorDialog : Intent()
        data object OnBackButtonClicked : Intent()
        data class OnUserSearchQueryChanged(val query: String) : Intent()
        data class OnSortChanged(val sort: HistoryMainUserSort) : Intent()
    }

    data class State(
        val logs: List<PlayLog> = emptyList(),
        val transformedLogs: List<PlayLog> = logs,
        val selectedUser: User? = null,
        val users: List<User> = emptyList(),
        val transformedUsers: List<User> = users,
        val currentPage: HistoryMainPage = HistoryMainPage.LOGS,
        val userSort: HistoryMainUserSort = HistoryMainUserSort.LastName(),
        val userSearchQuery: String = "",
        val isLoading: Boolean = true,
        val isError: Boolean = false,
        val errorMessage: String = ""
    ) {
        val isBackButtonVisible get() = currentPage == HistoryMainPage.CHILD_LOGS
        val isSearchQueryVisible get() = currentPage == HistoryMainPage.CHILDREN
        val isEmpty
            get() = (currentPage == HistoryMainPage.CHILDREN && transformedUsers.isEmpty() ||
                    currentPage != HistoryMainPage.CHILDREN && transformedLogs.isEmpty()) &&
                    !isLoading && !isError
    }

    sealed class Label {
        data class OnPlayLogClicked(val log: PlayLog) : Label()
    }
}