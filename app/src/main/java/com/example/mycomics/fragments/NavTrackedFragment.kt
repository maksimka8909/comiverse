package com.example.mycomics.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycomics.ApiInterface
import com.example.mycomics.R
import com.example.mycomics.adapters.ComicsRecyclerAdapter
import com.example.mycomics.classes.AlertDialog
import com.example.mycomics.models.Comics
import retrofit2.Call
import retrofit2.Response


class NavTrackedFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_nav_tracked, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val recyclerView = requireActivity().findViewById<RecyclerView>(R.id.recyclerViewTracked)
        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        val retrofit = ApiInterface.create()
        val progressBar =  requireActivity().findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = ProgressBar.VISIBLE
        retrofit.getTrackedComics(requireActivity().intent.getIntExtra("userId",0)).enqueue(object : retrofit2.Callback<List<Comics>>{
            override fun onFailure(call: Call<List<Comics>>, t: Throwable) {
                progressBar.visibility = ProgressBar.INVISIBLE
                val myAlertDialog = AlertDialog()
                val manager = requireActivity().supportFragmentManager
                val transaction = manager.beginTransaction()
                var args = Bundle()
                args.putString("message","??????-???? ?????????? ???? ?????? ?????????????????? ?????????????? ?????? ???????????????????? ?? ???????????? ??????????????????")
                myAlertDialog.arguments = args
                myAlertDialog.show(transaction,"dialog")
                Log.i("LOL",t.message.toString())
            }

            override fun onResponse(call: Call<List<Comics>>, response: Response<List<Comics>>) {
                progressBar.visibility = ProgressBar.INVISIBLE
                var adapter = ComicsRecyclerAdapter(response.body().orEmpty())
                recyclerView.adapter = adapter
                adapter.setOnItemClickListener(object : ComicsRecyclerAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        val bundle = Bundle()
                        bundle.putInt("idComics", response.body()!![position].id )
                        bundle.putString("nameComics", response.body()!![position].name )
                        bundle.putString("coverComics", response.body()!![position].cover )
                        bundle.putString("dateComics", response.body()!![position].date )
                        bundle.putString("descriptionComics", response.body()!![position].description )
                        bundle.putString("authorComics", response.body()!![position].author )
                        bundle.putString("editorComics", response.body()!![position].editor )
                        val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
                        val newFragment = NavComicsDetailFragment()
                        newFragment.arguments = bundle
                        transaction.replace(R.id.navFragment, newFragment)
                        transaction.addToBackStack(null)
                        transaction.commit()
                    }

                })
            }
        })
    }
    companion object {
        @JvmStatic
        fun newInstance() = NavTrackedFragment()
    }
}