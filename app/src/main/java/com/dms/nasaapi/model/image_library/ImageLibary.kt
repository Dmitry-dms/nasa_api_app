package com.dms.nasaapi.model.image_library


data class ImageLibrarySearchResponse(
    val collection: Collection
)

data class Collection(
    val items: List<Item>
)

data class Item(
    val data: List<Data>,
    val links: List<Link>
)

data class Data(
    val center: String,
    val date_created: String,
    val media_type: String,
    val title: String

)

data class Link(
    val href: String
)