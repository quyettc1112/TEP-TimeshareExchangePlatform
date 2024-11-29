package com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.PostingOfResortListActivity.ChildFragment.Exchange

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tep_timeshareexchangeplatform.AppConfig.BaseConfig.BaseFragment
import com.example.tep_timeshareexchangeplatform.BaseModel.DTO.CustomerDTO
import com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Customer.Profile.CustomerProfileResponse
import com.example.tep_timeshareexchangeplatform.Common.Constant
import com.example.tep_timeshareexchangeplatform.R
import com.example.tep_timeshareexchangeplatform.UI.Activity.AuthActivity.LoginActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.RequestExchangeActivity.RequestExchangeActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.ChildFragment.ExchangePostingFragment.ExchangePostingAdapter
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.ChildFragment.PublicPostingFragment.PublicPostingFragment.Companion.PAGE_SIZE
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.ExchangeDetailActivity.ExchangeDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.CommonActivity.SearchPostingActivity.PostingDetailActivity.PostingDetailActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.MemberShipActivity.MemberInfoDialog
import com.example.tep_timeshareexchangeplatform.UI.Activity.MemberShipActivity.MemberShipActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.PostingOfResortListActivity.PostingOfResortActivity
import com.example.tep_timeshareexchangeplatform.UI.Activity.ResortDetailActivity.PostingOfResortListActivity.PostingOfResortViewModel
import com.example.tep_timeshareexchangeplatform.Until.EmumClass.UserLogState
import com.example.tep_timeshareexchangeplatform.Until.Resource
import com.example.tep_timeshareexchangeplatform.Until.Status
import com.example.tep_timeshareexchangeplatform.Until.TokenManager.TokenManager
import com.example.tep_timeshareexchangeplatform.databinding.FragmentExchangePostingResortBinding

class ExchangePostingResortFragment : BaseFragment(R.layout.fragment_exchange_posting_resort) {

    private lateinit var binding: FragmentExchangePostingResortBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var adapter : ExchangePostingAdapter
    private val viewModel: PostingOfResortViewModel by activityViewModels()
    private var exchangeId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenManager = TokenManager(requireContext())
        adapter = ExchangePostingAdapter(tokenManager)
        adapter.submitList(listOf())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExchangePostingResortBinding.inflate(layoutInflater, container, false)
        setPublicPostingListUI()
        binding.rcPosting.adapter = adapter
        observeViewModel()
        return binding.root
    }

    private fun observeViewModel() {
        viewModel.publicExchangePosingList.observe(viewLifecycleOwner) { resources ->
            when (resources.status) {
                Status.SUCCESS -> {
                    binding.animLoadingMore.visibility = View.GONE
                    resources.data?.let {
                        viewModel.loadMoreExchange(it.content)
                        adapter.submitList(viewModel.getCurrentExchangeList())
                    }
                }

                Status.ERROR -> {
                    binding.animLoadingMore.visibility = View.GONE
                    (activity as PostingDetailActivity).showErrorToast("Lỗi", "Không thể tải dữ liệu")

                }

                Status.LOADING -> {
                    binding.animLoadingMore.visibility = View.VISIBLE
                }
            }
        }

        viewModel.currentExchangePage.observe(viewLifecycleOwner) {
            viewModel.getExchangePostingList(it, PAGE_SIZE, "")
        }

        viewModel.isCustomerExist.observe(viewLifecycleOwner) {
            when (it.status) {
                // Case User have Profile Info
                Status.SUCCESS -> {
                    (activity as PostingOfResortActivity).hideLoadingWaiting()
                    saveUserLogState(it)
                    // Case User is Member
                    if (it.data!!.isMember) {
                        val intent = Intent(requireActivity(), RequestExchangeActivity::class.java)
                        intent.putExtra(Constant.DEFAULT_POSTING_ID, exchangeId)
                        startActivity(intent)
                    } else {
                        intentToMemberShipActivity()
                    }
                }
                // Case User have not Profile Info
                Status.ERROR -> {
                    (activity as PostingOfResortActivity).hideLoadingWaiting()
                    if (it.message!!.contains("404")) {
                        intentToMemberShipActivity()
                        tokenManager.saveUserLogState(UserLogState.LOGGED_IN_AS_USER)
                    }
                }

                Status.LOADING -> {
                    (activity as PostingOfResortActivity).showLoadingWaiting(true)
                }
            }
        }

    }

    private fun setPublicPostingListUI() {
        binding.rcPosting.layoutManager =
            GridLayoutManager(requireActivity(), 2, LinearLayoutManager.VERTICAL, false)
        binding.rcPosting.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val lastCompletelyVisibleItem =
                    layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                val totalElementOfAPI =
                    viewModel.publicExchangePosingList.value?.data?.totalElements ?: 0
                val currentListSizeOfAdapter = adapter.differ.currentList.size


                if (lastCompletelyVisibleItem == totalItemCount - 1 && currentListSizeOfAdapter < totalElementOfAPI) {
                    viewModel.incrementCurrentExchangePage()
                }
            }
        })

        adapter.onItemClick = {
            val intent = Intent(requireActivity(), ExchangeDetailActivity::class.java)
            intent.putExtra(Constant.DEFAULT_POSTING_ID, it.exchangePostingId)
            startActivity(intent)
        }
        adapter.onExchangeButtonClick = {
            exchangeId = it.exchangePostingId
            callCheckProfileCustomer()
        }
    }

    private fun callCheckProfileCustomer() {
        if (!tokenManager.isLoggedIn()) {
            (activity as PostingOfResortActivity).showWarningToast(
                "Bạn chưa đăng nhập!",
                "Vui lòng đăng nhập để thực hiện chức năng này"
            )
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }

        if (tokenManager.getAccessToken() != null) {
            viewModel.callIsCustomerExist(tokenManager.getAccessToken().toString())
        }
    }

    private fun intentToMemberShipActivity() {
        startActivity(Intent(requireActivity(), MemberShipActivity::class.java))
    }

    private fun showCreateProfileDialog() {
        val dialogUpdateCustomer =
            MemberInfoDialog(
                requireContext(),
                object : MemberInfoDialog.ConfirmCallback {
                    override fun positiveAction(customerDTO: CustomerDTO) {
                        callCreateCustomer(customerDTO)
                    }
                })
        dialogUpdateCustomer.show()
    }

    private fun callCreateCustomer(customerDTO: CustomerDTO) {
        viewModel.callCreateCustomer(
            tokenManager.getAccessToken().toString(),
            customerDTO
        )
    }

    private fun saveUserLogState(it: Resource<CustomerProfileResponse>) {
        if (it.data!!.isMember) {
            tokenManager.saveUserLogState(UserLogState.LOGGED_IN_AS_CUSTOMER_MEMBER)
        }
        // User Is Not Member
        else {
            tokenManager.saveUserLogState(UserLogState.LOGGED_IN_AS_CUSTOMER)
        }
        tokenManager.saveProfileInfo(it.data)
    }

}