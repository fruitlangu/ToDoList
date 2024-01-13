package com.inavi.myapplication.model

data class ToDoList(
    val title: String="",
    val date: String="",
    val time: String="",
    val indexDb: Long=0,
    val isShow:Int=0
)
