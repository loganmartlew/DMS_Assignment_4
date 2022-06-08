package com.assign3.addressbook.models

enum class RequestStatus(val text: String) {
    PENDING("pending"),
    ACCEPTED("accepted"),
    REJECTED("rejected")
}