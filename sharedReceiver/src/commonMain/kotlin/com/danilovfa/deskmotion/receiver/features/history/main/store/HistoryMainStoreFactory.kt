package com.danilovfa.deskmotion.receiver.features.history.main.store

import co.touchlab.kermit.Logger
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.danilovfa.deskmotion.library.lce.lceFlow
import com.danilovfa.deskmotion.library.lce.mapToLce
import com.danilovfa.deskmotion.library.lce.onEachContent
import com.danilovfa.deskmotion.library.lce.onEachError
import com.danilovfa.deskmotion.library.lce.onEachLoading
import com.danilovfa.deskmotion.receiver.domain.model.PlayLog
import com.danilovfa.deskmotion.receiver.domain.model.User
import com.danilovfa.deskmotion.receiver.domain.repository.DeskMotionRepository
import com.danilovfa.deskmotion.receiver.domain.repository.UserRepository
import com.danilovfa.deskmotion.receiver.features.history.main.model.HistoryMainPage
import com.danilovfa.deskmotion.receiver.features.history.main.model.HistoryMainUserSort
import com.danilovfa.deskmotion.receiver.features.history.main.store.HistoryMainStore.Intent
import com.danilovfa.deskmotion.receiver.features.history.main.store.HistoryMainStore.Label
import com.danilovfa.deskmotion.receiver.features.history.main.store.HistoryMainStore.State
import com.danilovfa.deskmotion.utils.time.formatted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HistoryMainStoreFactory(
    private val storeFactory: StoreFactory
) {
    fun create(): HistoryMainStore = object : HistoryMainStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = STORE_NAME,
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Action.LoadLogs),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    sealed class Action {
        data object LoadLogs : Action()
        data object TransformUsers : Action()
    }

    sealed class Msg {
        data class ShowError(val message: String) : Msg()
        data object HideError : Msg()
        data object ShowLoading : Msg()
        data object HideLoading : Msg()
        data class UpdateLogs(val logs: List<PlayLog>) : Msg()
        data class UpdateTransformedLogs(val logs: List<PlayLog>) : Msg()
        data class UpdatePage(val page: HistoryMainPage) : Msg()
        data class UpdateSort(val sort: HistoryMainUserSort) : Msg()
        data class UpdateUsers(val users: List<User>) : Msg()
        data class UpdateTransformedUsers(val transformedUsers: List<User>) : Msg()
        data class UpdateSelectedUser(val user: User?) : Msg()
        data class UpdateUserSearchQuery(val query: String) : Msg()
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<Intent, Action, State, Msg, Label>(
            Dispatchers.Main
        ), KoinComponent {
        val repository: DeskMotionRepository by inject()
        val userRepository: UserRepository by inject()

        private var loadLogsJob: Job? = null
        private var loadChildrenJob: Job? = null

        override fun executeAction(action: Action, getState: () -> State) =
            when (action) {
                Action.LoadLogs -> loadLogs()
                Action.TransformUsers -> searchAndSortUsers(getState())
            }

        override fun executeIntent(
            intent: Intent,
            getState: () -> State
        ) = when (intent) {
            is Intent.OnPlayLogClicked -> publish(
                Label.OnPlayLogClicked(
                    intent.log
                )
            )

            Intent.DismissErrorDialog -> dismissErrorDialog()
            Intent.OnBackButtonClicked -> navigateBackChildLogs()
            is Intent.OnUserClicked -> navigateToChildLogs(intent.user, getState())
            is Intent.OnUserSearchQueryChanged -> {
                dispatch(Msg.UpdateUserSearchQuery(intent.query))
                executeAction(Action.TransformUsers)
            }

            is Intent.OnSortChanged -> {
                dispatch(Msg.UpdateSort(intent.sort))
                executeAction(Action.TransformUsers)
            }
        }

        private fun loadLogs() {
            loadLogsJob?.cancel()
            loadLogsJob = repository
                .getLogs()
                .mapToLce()
                .onEachLoading {
                    dispatch(Msg.ShowLoading)
                }
                .onEachContent { playLogs ->
                    checkIfTheOnlyChild(playLogs)
                }
                .onEachError { dispatch(Msg.ShowError(it.message ?: "")) }
                .launchIn(scope)
        }

        private fun checkIfTheOnlyChild(playLogs: List<PlayLog>) {
            if (playLogs.isEmpty()) return
            var isTheOnlyChild = true

            val iterator = playLogs.iterator()
            var prevItem = iterator.next()
            while (iterator.hasNext() && isTheOnlyChild) {
                val currItem = iterator.next()
                if (prevItem.userId != currItem.userId) {
                    isTheOnlyChild = false
                }

                prevItem = currItem
            }

            if (isTheOnlyChild.not()) {
                loadChildren()
            } else {
                dispatch(Msg.HideLoading)
            }
            dispatch(Msg.UpdateLogs(playLogs))
            dispatch(Msg.UpdateTransformedLogs(playLogs))
        }

        private fun loadChildren() {
            loadChildrenJob?.cancel()
            loadChildrenJob = lceFlow {
                emit(userRepository.getAllUsers())
            }
                .onEachLoading { dispatch(Msg.ShowLoading) }
                .onEachError { dispatch(Msg.ShowError(it.message ?: "")) }
                .onEachContent { users ->
                    dispatch(Msg.HideLoading)
                    dispatch(Msg.UpdateUsers(users))
                    executeAction(Action.TransformUsers)
                }
                .launchIn(scope)
        }

        private fun searchAndSortUsers(state: State) {
            val filteredUsers = state.users
                .filter { user ->
                    val query = state.userSearchQuery
                    user.lastName.contains(query) || user.dateOfBirth.formatted().contains(query) ||
                            user.firstName.contains(query) || user.middleName.contains(query)
                }
                .filter { user ->
                    state.logs.map { it.userId }.contains(user.id)
                }

            Logger.d { "UserIds: ${state.logs.map { it.userId }.joinToString()}" }

            val sortedByDescendingUsers = when (state.userSort) {
                is HistoryMainUserSort.DateOfBirth -> filteredUsers.sortedByDescending { it.dateOfBirth }
                is HistoryMainUserSort.FirstName -> filteredUsers.sortedByDescending { it.firstName }
                is HistoryMainUserSort.LastName -> filteredUsers.sortedByDescending { it.lastName }
                is HistoryMainUserSort.MiddleName -> filteredUsers.sortedByDescending { it.middleName }
            }

            val searchAndSortedUsers = if (state.userSort.isDescending) {
                sortedByDescendingUsers
            } else {
                sortedByDescendingUsers.reversed()
            }

            dispatch(Msg.UpdateTransformedUsers(searchAndSortedUsers))
            if (state.selectedUser == null) {
                dispatch(Msg.UpdatePage(HistoryMainPage.CHILDREN))
            }
        }

        private fun navigateToChildLogs(user: User, state: State) {
            dispatch(Msg.UpdateSelectedUser(user))
            dispatch(Msg.UpdateTransformedLogs(state.logs.filter { it.userId == user.id }))
            dispatch(Msg.UpdatePage(HistoryMainPage.CHILD_LOGS))
        }

        private fun navigateBackChildLogs() {
            dispatch(Msg.UpdatePage(HistoryMainPage.CHILDREN))
            dispatch(Msg.UpdateSelectedUser(null))
        }

        private fun dismissErrorDialog() {
            dispatch(Msg.HideError)
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.UpdateLogs -> copy(logs = msg.logs, isLoading = false)
            is Msg.ShowError -> copy(isError = true, errorMessage = msg.message)
            Msg.HideError -> copy(isError = false, errorMessage = "")
            Msg.HideLoading -> copy(isLoading = false)
            Msg.ShowLoading -> copy(isLoading = true)
            is Msg.UpdatePage -> copy(currentPage = msg.page)
            is Msg.UpdateTransformedLogs -> copy(transformedLogs = msg.logs)
            is Msg.UpdateUsers -> copy(users = msg.users)
            is Msg.UpdateSelectedUser -> copy(selectedUser = msg.user)
            is Msg.UpdateTransformedUsers -> copy(transformedUsers = msg.transformedUsers)
            is Msg.UpdateUserSearchQuery -> copy(userSearchQuery = msg.query)
            is Msg.UpdateSort -> copy(userSort = msg.sort)
        }
    }

    companion object {
        const val STORE_NAME = "HistoryMainStore"
    }
}