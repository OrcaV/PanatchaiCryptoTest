package com.v.panatchai.cryptocurrency.presentation.views

import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class SimpleNavigation<T> @Inject constructor(
    private val host: NavigationHost<T>
) {

    /**
     * Navigate to a destination using the [destinationModel].
     */
    fun navigateBy(destinationModel: T) {
        host.navigateBy(destinationModel)
    }

    interface NavigationHost<T> {

        /**
         * Navigate to a destination specified by the [destinationModel].
         *
         * @param destinationModel either data model that describes the navigation destination.
         */
        fun navigateBy(destinationModel: T)
    }
}
