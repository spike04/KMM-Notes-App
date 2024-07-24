package home.ui

sealed interface HomeEvent {

    data object RefreshNote : HomeEvent
}