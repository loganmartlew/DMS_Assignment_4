package com.assign3.addressbook.models

data class Request(var id: Int, var status: String, var location: Location, var fromUser: User, var toUser: User, var createdAt: String)