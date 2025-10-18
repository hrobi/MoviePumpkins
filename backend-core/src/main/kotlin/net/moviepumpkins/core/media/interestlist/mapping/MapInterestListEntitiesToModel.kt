package net.moviepumpkins.core.media.interestlist.mapping

import net.moviepumpkins.core.media.interestlist.entity.InterestListEntity
import net.moviepumpkins.core.media.interestlist.model.InterestListItem
import net.moviepumpkins.core.media.mediadetails.mapping.toMedia

fun InterestListEntity.toInterestListItem() = InterestListItem(
    id = id!!,
    media = media.toMedia()
)