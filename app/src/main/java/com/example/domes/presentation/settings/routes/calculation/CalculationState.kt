package com.example.domes.presentation.settings.routes.calculation

import com.batoulapps.adhan2.CalculationMethod
import com.batoulapps.adhan2.Madhab

data class CalculationState(
    val calculationMethod: CalculationMethod = CalculationMethod.MUSLIM_WORLD_LEAGUE,
    val juristic: Madhab = Madhab.HANAFI,
    val showCalculationMethodDialog: Boolean = false,
    val showJuristicDialog: Boolean = false,
)
