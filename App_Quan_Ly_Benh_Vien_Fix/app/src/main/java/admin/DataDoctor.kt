package admin

import android.os.Parcel
import android.os.Parcelable

class DataDoctor(val id : String? = null, val drName : String? = null, val drAge : String? = null, val medicalDepartment : String? = null, val typeOfDesease : String? = null, val imageDr : String? = null
) : Parcelable {
    // để đưa 1 đối tượng do mình tự định nghĩa, t sử dụng thư viện Parcelable
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return  0
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeString(id)
        p0.writeString(drName)
        p0.writeString(drAge)
        p0.writeString(medicalDepartment)
        p0.writeString(typeOfDesease)
        p0.writeString(imageDr)
    }

    companion object CREATOR : Parcelable.Creator<DataDoctor> {
        override fun createFromParcel(parcel: Parcel): DataDoctor {
            return DataDoctor(parcel)
        }

        override fun newArray(size: Int): Array<DataDoctor?> {
            return arrayOfNulls(size)
        }
    }
}