package home.interactor

import base.RxInteractor
import base.RxUseCase
import home.model.HomeCommand
import home.model.HomeMutation
import home.model.HomeState
import io.reactivex.Observable
import org.koin.core.KoinComponent

class HomeInteractor(
    override val initialState: HomeState,
    private val quoteButtonTapUseCase: RxUseCase<String, HomeMutation>
) :
    RxInteractor<HomeCommand, HomeMutation, HomeState>(initialState), KoinComponent {

    override fun mutation(command: HomeCommand, currentState: HomeState): Observable<HomeMutation> {
        return when (command) {
            is HomeCommand.ButtonTap -> quoteButtonTapUseCase.execute(command.searchValue)
        }
    }

    override fun reduce(mutation: HomeMutation, currentState: HomeState): HomeState {
        val newState = currentState.copy()
        newState.isButtonEnable = true
        newState.errorMessage = null

        when (mutation) {
            is HomeMutation.DisableButton -> newState.isButtonEnable = false
            is HomeMutation.UpdateData -> newState.data = mutation.data
            is HomeMutation.Error -> newState.errorMessage = mutation.message
        }
        return newState
    }
}