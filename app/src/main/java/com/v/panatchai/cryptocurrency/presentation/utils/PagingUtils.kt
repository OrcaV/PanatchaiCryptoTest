package com.v.panatchai.cryptocurrency.presentation.utils

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.LoadType
import androidx.paging.PagingSource
import com.v.panatchai.cryptocurrency.presentation.utils.MergedState.NOT_LOADING
import com.v.panatchai.cryptocurrency.presentation.utils.MergedState.REMOTE_ERROR
import com.v.panatchai.cryptocurrency.presentation.utils.MergedState.REMOTE_STARTED
import com.v.panatchai.cryptocurrency.presentation.utils.MergedState.SOURCE_ERROR
import com.v.panatchai.cryptocurrency.presentation.utils.MergedState.SOURCE_LOADING
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.scan

@OptIn(ExperimentalCoroutinesApi::class)
fun Flow<CombinedLoadStates>.asMergedLoadStates(): Flow<LoadStates> {
    val syncRemoteState = LoadStatesMerger()
    return scan(syncRemoteState.toLoadStates()) { _, combinedLoadStates ->
        syncRemoteState.updateFromCombinedLoadStates(combinedLoadStates)
        syncRemoteState.toLoadStates()
    }
}

private enum class MergedState {
    /**
     * Idle state; defer to remote state for endOfPaginationReached.
     */
    NOT_LOADING,

    /**
     * Remote load triggered; start listening for source refresh.
     */
    REMOTE_STARTED,

    /**
     * Waiting for remote in error state to get retried
     */
    REMOTE_ERROR,

    /**
     * Source refresh triggered by remote invalidation, once this completes we can be sure
     * the next generation was loaded.
     */
    SOURCE_LOADING,

    /**
     *  Remote load completed, but waiting for source refresh in error state to get retried.
     */
    SOURCE_ERROR,
}

private class LoadStatesMerger {
    var refresh: LoadState = LoadState.NotLoading(endOfPaginationReached = false)
        private set
    var prepend: LoadState = LoadState.NotLoading(endOfPaginationReached = false)
        private set
    var append: LoadState = LoadState.NotLoading(endOfPaginationReached = false)
        private set
    var refreshState: MergedState = NOT_LOADING
        private set
    var prependState: MergedState = NOT_LOADING
        private set
    var appendState: MergedState = NOT_LOADING
        private set

    fun toLoadStates() = LoadStates(
        refresh = refresh,
        prepend = prepend,
        append = append
    )

    /**
     * For every new emission of [CombinedLoadStates] from the original [Flow], update the
     * [MergedState] of each [LoadType] and compute the new [LoadState].
     */
    fun updateFromCombinedLoadStates(combinedLoadStates: CombinedLoadStates) {
        computeNextLoadStateAndMergedState(
            sourceRefreshState = combinedLoadStates.source.refresh,
            sourceState = combinedLoadStates.source.refresh,
            remoteState = combinedLoadStates.mediator?.refresh,
            currentMergedState = refreshState,
        ).also {
            refresh = it.first
            refreshState = it.second
        }
        computeNextLoadStateAndMergedState(
            sourceRefreshState = combinedLoadStates.source.refresh,
            sourceState = combinedLoadStates.source.prepend,
            remoteState = combinedLoadStates.mediator?.prepend,
            currentMergedState = prependState,
        ).also {
            prepend = it.first
            prependState = it.second
        }
        computeNextLoadStateAndMergedState(
            sourceRefreshState = combinedLoadStates.source.refresh,
            sourceState = combinedLoadStates.source.append,
            remoteState = combinedLoadStates.mediator?.append,
            currentMergedState = appendState,
        ).also {
            append = it.first
            appendState = it.second
        }
    }

    /**
     * Compute which [LoadState] and [MergedState] to transition, given the previous and current
     * state for a particular [LoadType].
     */
    @Suppress("USELESS_IS_CHECK")
    private fun computeNextLoadStateAndMergedState(
        sourceRefreshState: LoadState,
        sourceState: LoadState,
        remoteState: LoadState?,
        currentMergedState: MergedState,
    ): Pair<LoadState, MergedState> {
        if (remoteState == null) return sourceState to NOT_LOADING

        return when (currentMergedState) {
            NOT_LOADING -> when (remoteState) {
                is LoadState.Loading -> LoadState.Loading to REMOTE_STARTED
                is PagingSource.LoadResult.Error<*, *> -> remoteState to REMOTE_ERROR
                else -> LoadState.NotLoading(remoteState.endOfPaginationReached) to NOT_LOADING
            }
            REMOTE_STARTED -> when {
                remoteState is PagingSource.LoadResult.Error<*, *> -> remoteState to REMOTE_ERROR
                sourceRefreshState is LoadState.Loading -> LoadState.Loading to SOURCE_LOADING
                else -> LoadState.Loading to REMOTE_STARTED
            }
            REMOTE_ERROR -> when (remoteState) {
                is PagingSource.LoadResult.Error<*, *> -> remoteState to REMOTE_ERROR
                else -> LoadState.Loading to REMOTE_STARTED
            }
            SOURCE_LOADING -> when {
                sourceRefreshState is PagingSource.LoadResult.Error<*, *> -> sourceRefreshState to SOURCE_ERROR
                remoteState is PagingSource.LoadResult.Error<*, *> -> remoteState to REMOTE_ERROR
                sourceRefreshState is LoadState.NotLoading -> {
                    LoadState.NotLoading(remoteState.endOfPaginationReached) to NOT_LOADING
                }
                else -> LoadState.Loading to SOURCE_LOADING
            }
            SOURCE_ERROR -> when (sourceRefreshState) {
                is PagingSource.LoadResult.Error<*, *> -> sourceRefreshState to SOURCE_ERROR
                else -> sourceRefreshState to SOURCE_LOADING
            }
        }
    }
}
