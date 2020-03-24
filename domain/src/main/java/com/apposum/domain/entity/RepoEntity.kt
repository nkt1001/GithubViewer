package com.apposum.domain.entity

data class RepoEntity(val id: Int,
                      val name: String,
                      val fullName: String,
                      val description: String?,
                      val owner: OwnerEntity,
                      val stars: Int)