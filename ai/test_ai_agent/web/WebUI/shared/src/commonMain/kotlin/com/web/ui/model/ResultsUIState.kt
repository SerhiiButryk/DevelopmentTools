package com.web.ui.model

import com.ai.proto.Company

data class ResultsUIState(
    val companies: List<Company> = emptyList(), val hasError: Boolean = false
)