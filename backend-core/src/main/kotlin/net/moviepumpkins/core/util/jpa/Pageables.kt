package net.moviepumpkins.core.util.jpa

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

fun relevencyPageRequest(page: Int, pageSize: Int = 5): PageRequest =
    PageRequest.of(page, pageSize, Sort.by("modifiedAt").descending())