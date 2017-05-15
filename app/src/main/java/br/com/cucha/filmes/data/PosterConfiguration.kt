package br.com.cucha.filmes.data

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by eduardocucharro on 12/05/17.
 */
class PosterConfiguration(val baseUrl: String, val sizes: List<String>) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<PosterConfiguration> = object : Parcelable.Creator<PosterConfiguration> {
            override fun createFromParcel(source: Parcel): PosterConfiguration = PosterConfiguration(source)
            override fun newArray(size: Int): Array<PosterConfiguration?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.createStringArrayList()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(baseUrl)
        dest.writeStringList(sizes)
    }
}