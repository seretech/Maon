package com.developers.serenity.maontech

class ModelClass {

    private var id: Int? = 0
    private var title: String? = "N/A"
    private var name: String? = "N/A"
    private var country: String? = "N/A"
    private var date: String? = "N/A"
    private var img: String? = "N/A"
    private var type: String? = "N/A"

    fun getId(): Int {
        return id ?: 0
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getTitles(): String {
        return title.toString()
    }

    fun setTitles(title: String) {
        this.title = title
    }

    fun getNames(): String {
        return name.toString()
    }

    fun setNames(name: String) {
        this.name = name
    }

    fun getCountries(): String {
        return country.toString()
    }

    fun setCountries(country: String) {
        this.country= country
    }

    fun getDates(): String {
        return date.toString()
    }

    fun setDates(date: String) {
        this.date = date
    }

    fun getImgs(): String {
        return img.toString()
    }

    fun setImgs(img: String) {
        this.img = img
    }

    fun getTypes(): String {
        return type.toString()
    }

    fun setTypes(type: String) {
        this.type = type
    }

}