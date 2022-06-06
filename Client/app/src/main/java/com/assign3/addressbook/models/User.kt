package com.assign3.addressbook.models

import java.util.*

data class User(var id: Int, var name: String, var locations: List<Location>)