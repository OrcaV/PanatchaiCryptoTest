package com.v.panatchai.cryptocurrency.presentation.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.v.panatchai.cryptocurrency.domain.models.BaseModel
import kotlinx.parcelize.Parcelize

/**
 * Root of all Ui Models.
 */
@Parcelize
@Keep
sealed class UiModel : BaseModel(), Parcelable
