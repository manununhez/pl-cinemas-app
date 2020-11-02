package today.kinema.data.api.model

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.RawValue

data class GeneralResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: @RawValue T
)