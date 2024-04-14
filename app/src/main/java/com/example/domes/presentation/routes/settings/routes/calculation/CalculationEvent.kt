package com.example.domes.presentation.routes.settings.routes.calculation

import com.batoulapps.adhan2.CalculationMethod
import com.batoulapps.adhan2.Madhab

sealed class CalculationEvent {
    data class ChangeCalculationMethod(val calculationMethod: CalculationMethod): CalculationEvent()
    data class ChangeJuristic(val juristic: Madhab): CalculationEvent()
    data object ResetSettings : CalculationEvent()
    data class SetCalculationMethodDialogVisibility(val isVisible: Boolean? = null): CalculationEvent()
    data class SetJuristicDialogVisibility(val isVisible: Boolean? = null): CalculationEvent()
}
