package com.inhohyun.retrofit2basic


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.inhohyun.retrofit2basic.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


class MainActivity : AppCompatActivity() {
    private val myRecyclerViewAdapter by lazy { EmgMedAdapter() }
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = myRecyclerViewAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }


        binding.btnGet.setOnClickListener {
            retrofitWork()
        }
    }

    private fun retrofitWork() {
        val service = RetrofitApi.emgMedService

        service.getEmgMedData(getString(R.string.api_key), "json")
            .enqueue(object :  retrofit2.Callback<EmgMedResponse> {
                override fun onResponse(
                    call: Call<EmgMedResponse>,
                    response: Response<EmgMedResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("TAG", response.body().toString())
                        // head를 스킵하기 위해 index 1번을 가져옴
                        val result = response.body()?.emgMedInfo?.get(1)?.row
                        myRecyclerViewAdapter.submitList(result!!)
                    }
                }

                override fun onFailure(call: Call<EmgMedResponse>, t:  Throwable) {
                    Log.d("TAG", t.message.toString())
                }
            })
    }


}