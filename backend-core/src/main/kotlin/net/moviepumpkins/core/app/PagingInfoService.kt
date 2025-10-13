package net.moviepumpkins.core.app

import net.moviepumpkins.core.app.config.PagedProperties
import net.moviepumpkins.core.integration.models.PagingInfo
import org.springframework.stereotype.Component

@Component
class PagingInfoService(
    private val pagedProperties: PagedProperties,
) {
    fun derivePagingInfo(count: Int): PagingInfo {
        return PagingInfo(
            pageCount = pagedProperties.derivePageCount(count),
            pageSize = pagedProperties.pageSize
        )
    }
}