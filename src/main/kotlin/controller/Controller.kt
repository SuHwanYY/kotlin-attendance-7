package controller

import model.AttendanceManager
import repository.AttendanceRepository
import repository.CrewRepository
import util.retry
import view.InputView
import view.OutputView

class Controller {
    private val crewRepository = CrewRepository("/attendances.csv")
    private val attendanceRepository = AttendanceRepository("/attendances.csv")
    private val attendanceManager = AttendanceManager(attendanceRepository, crewRepository)

    private val inputView = InputView(crewRepository)
    private val outputView = OutputView()

    fun run() {
        while (true) {
            outputView.printFeatureSelect()
            val menu = inputView.readMenuNumber()

            when (menu) {
                "1" -> {
                    attendanceManager.validateTodayCanCheck()

                    val nickname = inputView.readCheckNickname()
                    val time = inputView.readCheckTime()

                    val result = attendanceManager.checkAttendance(nickname, time)
                    outputView.printCheckedResult(result)
                }

                "2" -> {
                    val nickname = inputView.readModifyNickname()
                    val day = inputView.readModifyDayOfMonth()
                    val newTime = inputView.readModifyTime()

                    val (before, after) = attendanceManager.modifyAttendance(nickname, day, newTime)
                    outputView.printModifyResult(before, after)
                }

                "3" -> {
                    val nickname = inputView.readCrewNickname()
                    val (days, summary) = attendanceManager.buildCrewReport(nickname)
                    outputView.printCrewReport(days, summary)
                }

                "4" -> {
                    val riskList = attendanceManager.buildRiskReport()
                    outputView.printRiskReport(riskList)
                }

                "Q" -> return
            }
        }
    }
}