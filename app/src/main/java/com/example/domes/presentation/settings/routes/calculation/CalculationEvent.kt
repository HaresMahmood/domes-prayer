package com.example.domes.presentation.settings.routes.calculation

import com.batoulapps.adhan2.Madhab

sealed class CalculationEvent {
    data class ChangeJuristic(val juristic: Madhab): CalculationEvent()
    data class SetJuristicDialogVisibility(val isVisible: Boolean? = null): CalculationEvent()
}
