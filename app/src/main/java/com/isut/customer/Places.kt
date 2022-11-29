package com.isut.customer

/**
 * Created by Lenovo on 10/23/2017.
 */
class Places {
    var primaryText: String? = null
    var addressDescription: String? = null
    var distance: String? = null

    constructor() {}

    //CONSTRACTOR..........
    constructor(primaryText: String?, addressDescription: String?, distance: String?) {
        this.primaryText = primaryText
        this.addressDescription = addressDescription
        this.distance = distance
    }
}