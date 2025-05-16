package com.developbharat.yogsudhaar.ui.screens.check.components

data class PointData(
    val mean: Double,
    val std: Double,
    val coOrdinatesIds: List<Int>,
)

object AngleStatsData {
    val leftHandedHash: Map<String, PointData> = mapOf(
        Pair(
            "lH_lK_lA",
            PointData(
                mean = 29.166666666666668,
                std = 8.498365855987975,
                coOrdinatesIds = listOf(23, 25, 27)
            )
        ),
        Pair(
            "rH_rK_rA",
            PointData(
                mean = 184.16666666666666,
                std = 2.3570226039551585,
                coOrdinatesIds = listOf(24, 26, 28)
            )
        ),
        Pair(
            "rS_rH_rK",
            PointData(
                mean = 175.83333333333334,
                std = 2.3570226039551585,
                coOrdinatesIds = listOf(12, 24, 26)
            )
        ),
        Pair(
            "lS_lE_lW",
            PointData(
                mean = 209.16666666666666,
                std = 4.714045207910316,
                coOrdinatesIds = listOf(11, 13, 15)
            )
        ),
        Pair(
            "rS_rE_rW",
            PointData(
                mean = 160.83333333333334,
                std = 11.785113019775793,
                coOrdinatesIds = listOf(12, 14, 16)
            )
        ),
        Pair(
            "lS_lH_lK",
            PointData(
                mean = 250.83333333333334,
                std = 4.714045207910316,
                coOrdinatesIds = listOf(11, 23, 25)
            )
        ),
        Pair(
            "lE_lS_lH",
            PointData(
                mean = 179.16666666666666,
                std = 2.3570226039551585,
                coOrdinatesIds = listOf(13, 11, 23)
            )
        ),
        Pair(
            "rE_rS_rH",
            PointData(
                mean = 200.83333333333334,
                std = 4.714045207910316,
                coOrdinatesIds = listOf(14, 12, 24)
            )
        ),
    )

    val rightHandedHash: Map<String, PointData> = mapOf(
        Pair(
            "lH_lK_lA",
            PointData(
                mean = 169.16666666666666,
                std = 6.236095644623235,
                coOrdinatesIds = listOf(23, 25, 27)
            )
        ),
        Pair(
            "rH_rK_rA",
            PointData(
                mean = 340.27777777777777,
                std = 2.4845199749997664,
                coOrdinatesIds = listOf(24, 26, 28)
            )
        ),
        Pair(
            "rS_rH_rK",
            PointData(
                mean = 106.94444444444444,
                std = 4.969039949999532,
                coOrdinatesIds = listOf(12, 24, 26)
            )
        ),
        Pair(
            "lS_lE_lW",
            PointData(
                mean = 204.16666666666666,
                std = 19.293061504650378,
                coOrdinatesIds = listOf(11, 13, 15)
            )
        ),
        Pair(
            "rS_rE_rW",
            PointData(
                mean = 154.72222222222223,
                std = 18.72477727372524,
                coOrdinatesIds = listOf(12, 14, 16)
            )
        ),
        Pair(
            "lS_lH_lK",
            PointData(
                mean = 184.16666666666666,
                std = 4.0824829046386295,
                coOrdinatesIds = listOf(11, 23, 25)
            )
        ),
        Pair(
            "lE_lS_lH",
            PointData(
                mean = 161.94444444444446,
                std = 9.558139185602919,
                coOrdinatesIds = listOf(13, 11, 23)
            )
        ),
        Pair(
            "rE_rS_rH",
            PointData(
                mean = 178.61111111111111,
                std = 6.983225049986964,
                coOrdinatesIds = listOf(14, 12, 24)
            )
        ),
    )
}