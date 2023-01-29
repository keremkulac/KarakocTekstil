package com.keremkulac.karakoctekstil.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Pattern(
    @ColumnInfo(name = "name")
    @SerializedName("name")
    var name : String?,
    @ColumnInfo(name = "width")
    @SerializedName("width")
    var width : String?,
    @ColumnInfo(name = "height")
    @SerializedName("height")
    var height : String?,
    @ColumnInfo(name = "hit")
    @SerializedName("hit")
    var hit : String?,
    @ColumnInfo(name = "pattern_url")
    @SerializedName("pattern_url")
    var pattern_url : String?) : Parcelable{

    @PrimaryKey(autoGenerate = true)
    var uuid : Int =0

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
        //   uuid = parcel.readInt()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        System.out.println("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Pattern> {
        override fun createFromParcel(parcel: Parcel): Pattern {
            return Pattern(parcel)
        }

        override fun newArray(size: Int): Array<Pattern?> {
            return arrayOfNulls(size)
        }
    }

}
