package br.edu.ifsp.dmo1.sitesfavoritos.view.main

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.dmo1.sitesfavoritos.R
import br.edu.ifsp.dmo1.sitesfavoritos.databinding.ActivityMainBinding
import br.edu.ifsp.dmo1.sitesfavoritos.databinding.SitesDialogBinding
import br.edu.ifsp.dmo1.sitesfavoritos.view.adapters.SiteAdapter
import br.edu.ifsp.dmo1.sitesfavoritos.view.listeners.SiteItemClickListener

class MainActivity : AppCompatActivity(), SiteItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: SiteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        configListener()
        configRecyclerView()
        configObservers()
    }

    override fun clickSiteItem(position: Int) {
        val site = viewModel.getSite(position)
        val mIntent = Intent(Intent.ACTION_VIEW)
        mIntent.setData(Uri.parse("http://"+site))
        startActivity(mIntent)
    }

    override fun clickHearSiteItem(position: Int) {
        viewModel.toggleFavorite(position)
    }

    override fun clickTrashSiteItem(position: Int) {
        viewModel.removeSite(position)
    }

    private fun configListener(){
        binding.buttonAdd.setOnClickListener{handleAddSite()}
    }

    private fun handleAddSite() {
        val tela = layoutInflater.inflate(R.layout.sites_dialog,null)
        val bindingDialog:SitesDialogBinding = SitesDialogBinding.bind(tela)

        val builder = AlertDialog.Builder(this)
            .setView(tela)
            .setTitle(R.string.novo_site)
            .setPositiveButton(R.string.salvar, DialogInterface.OnClickListener{dialog, which->
                viewModel.addSite(bindingDialog.edittextApelido.text.toString(),
                                  bindingDialog.edittextUrl.text.toString())
                dialog.dismiss()
            })
            .setNegativeButton(R.string.cancelar, DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })

        builder.create().show()
    }

    private fun configRecyclerView(){
        adapter = SiteAdapter(this, mutableListOf(), this)
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.recyclerviewSites.layoutManager = layoutManager
        binding.recyclerviewSites.adapter = adapter
    }

    private fun configObservers(){
        viewModel.datasource.observe(this, Observer {
            adapter.updateDataset(it)
        })
        viewModel.favorite.observe(this, Observer {
            Toast.makeText(
                this,
                if(it){
                    R.string.add_favorite
                }else{
                    R.string.del_favorite
                },
                Toast.LENGTH_LONG
            ).show()
        })
        viewModel.removed.observe(this, Observer {
            Toast.makeText(
                this,
                R.string.success_remove,
                Toast.LENGTH_LONG
            ).show()
        })
        viewModel.added.observe(this, Observer {
            Toast.makeText(
                this,
                R.string.success_add,
                Toast.LENGTH_LONG
            ).show()
        })
    }
}