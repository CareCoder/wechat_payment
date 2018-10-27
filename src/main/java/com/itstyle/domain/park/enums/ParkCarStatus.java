package com.itstyle.domain.park.enums;

import com.google.gson.annotations.SerializedName;

public enum ParkCarStatus {
    @SerializedName("init")
    INIT,
    @SerializedName("ready")
    READY,
    @SerializedName("done")
    DONE
}
