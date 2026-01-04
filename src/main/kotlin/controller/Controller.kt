package controller

import repository.CrewRepository
import util.retry
import view.InputView
import view.OutputView

class Controller {
    private val crewRepository = CrewRepository("/attendances.csv")
    private val outputView = OutputView()
    private val inputView = InputView(crewRepository)

    fun run() {
        while(true) {
            outputView.printFeatureSelect()
            val menuNumber = retry{ inputView.readMenuNumber() }
            when(menuNumber) {
                "1" -> {
                    retry{ inputView.readCheckNickname() }
                    retry{ inputView.readCheckTime() }
                }
                "Q" -> break
            }
        }
    }

}