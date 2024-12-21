package br.edu.ifsp.dmo1.sitesfavoritos.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.edu.ifsp.dmo1.sitesfavoritos.model.Site

class MainViewModel : ViewModel() {
    private val lista: ArrayList<Site> = ArrayList()

    private val _datasource = MutableLiveData<List<Site>>()
    val datasource: LiveData<List<Site>> = _datasource

    private val _favorite = MutableLiveData<Boolean>()
    val favorite :LiveData<Boolean> = _favorite

    private val _removed = MutableLiveData<Boolean>()
    val removed :LiveData<Boolean> = _removed

    private val _added = MutableLiveData<Boolean>()
    val added :LiveData<Boolean> = _added

    init {
        load()
    }

    fun getSite(position: Int):String = lista[position].url

    private fun load() {
        _datasource.value = lista
    }

    fun toggleFavorite(position: Int) {
        val site = lista[position]
        site.favorito = !site.favorito
        _favorite.value = site.favorito
        load()
    }

    fun removeSite(position: Int) {
        lista.removeAt(position)
        _removed.value = true
        load()
    }

    fun addSite(apelido: String, url: String) {
        lista.add(Site(apelido, url))
        _added.value = true
        load()
    }
}