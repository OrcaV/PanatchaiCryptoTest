package com.v.panatchai.cryptocurrency.presentation.coins.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.v.panatchai.cryptocurrency.databinding.FragmentCurrencyBinding
import com.v.panatchai.cryptocurrency.presentation.models.UiModel
import com.v.panatchai.cryptocurrency.presentation.utils.asMergedLoadStates
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn

@AndroidEntryPoint
class CurrencyFragment : Fragment() {

    @Inject
    internal lateinit var currencyListAdapter: PagingDataAdapter<UiModel, RecyclerView.ViewHolder> // <-- depends on abstraction

    private lateinit var binding: FragmentCurrencyBinding

    private val viewModel: CurrencyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrencyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupCurrencyAdapter()
        canBeReplacedByBindingAdapter()

        binding.lifecycleOwner = viewLifecycleOwner
        binding.orderBy = viewModel.orderBy
    }

    private fun setupCurrencyAdapter() {
        binding.currencyRecyclerView.adapter = currencyListAdapter.withLoadStateHeaderAndFooter(
            // Show the Loading animation while loading and Retry button when error.
            header = CurrencyListLoadStateAdapter { currencyListAdapter.retry() },
            footer = CurrencyListLoadStateAdapter { currencyListAdapter.retry() }
        )

        /**
         * Submit new [PagingData] from [Flow] to the [RecyclerView].
         */
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.currencies.collectLatest {
                currencyListAdapter.submitData(it)
            }
        }

        // Handle the Refresh animation by checking the Paging3 Loading state.
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            currencyListAdapter.loadStateFlow.collect { loadStates ->
                binding.currencySwipeRefresh.isRefreshing =
                    loadStates.mediator?.refresh is LoadState.Loading
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            currencyListAdapter.loadStateFlow
                // Use a state-machine to track LoadStates such that we only transition to
                // NotLoading from a RemoteMediator load if it was also presented to UI.
                .asMergedLoadStates()
                // Only emit when REFRESH changes, as we only want to react on loads replacing the
                // list.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .flowOn(Dispatchers.Default)
                // Scroll to top is synchronous with UI updates, even if remote load was triggered.
                .collect { binding.currencyRecyclerView.scrollToPosition(0) }
        }
    }

    private fun canBeReplacedByBindingAdapter() {
        binding.currencySwipeRefresh.setOnRefreshListener { currencyListAdapter.refresh() }
        binding.currencyFab.setOnClickListener {
            binding.currencyRecyclerView.scrollToPosition(0)
        }
        binding.currencySearchInputEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.filter(text?.toString())
        }
        binding.currencySort.setOnClickListener {
            viewModel.toggleOrderBy()
        }
    }
}
