package br.edu.ifsp.dmo1.sitesfavoritos.view.listeners

interface SiteItemClickListener {
    fun clickSiteItem(position: Int)
    fun clickHearSiteItem(position: Int)
    fun clickTrashSiteItem(position: Int)
}