package com.v.panatchai.cryptocurrency.presentation.coins.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.v.panatchai.cryptocurrency.databinding.FragmentCurrencyDetailBinding
import com.v.panatchai.cryptocurrency.di.GlideRequests
import com.v.panatchai.cryptocurrency.presentation.models.CurrencyModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A simple [Fragment] subclass to display Currency Details.
 */
@AndroidEntryPoint
class CurrencyDetailFragment : Fragment() {

    @Inject
    internal lateinit var glide: GlideRequests

    private val args: CurrencyDetailFragmentArgs by navArgs()

    private lateinit var binding: FragmentCurrencyDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrencyDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAppbar()
        initModel(args.currencyModel)
    }

    private fun initModel(model: CurrencyModel) {
        binding.model = model
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            binding.simpleCurrencyDetails.listItemCurrencyTrailingImage.visibility = View.INVISIBLE
            glide.load(model.icon)
                .centerCrop()
                .placeholder(android.R.drawable.ic_dialog_info)
                .into(binding.simpleCurrencyDetails.listItemCurrencyImage)
        }
    }

    private fun initAppbar() {
        with(binding.currencyDetailToolbar) {
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}
