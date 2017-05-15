package br.com.cucha.filmes.data

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * Created by eduardocucharro on 12/05/17.
 */
class Movie(val id: Int,
            val title: String,
            val release: String,
            val poster: String,
            val overview: String) : Parcelable {

    var posterConfiguration: PosterConfiguration? = null

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Movie> = object : Parcelable.Creator<Movie> {
            override fun createFromParcel(source: Parcel): Movie = Movie(source)
            override fun newArray(size: Int): Array<Movie?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(title)
        dest.writeString(release)
        dest.writeString(poster)
        dest.writeString(overview)
    }

    override fun equals(other: Any?): Boolean {
        return id == (other as Movie).id
    }
}
