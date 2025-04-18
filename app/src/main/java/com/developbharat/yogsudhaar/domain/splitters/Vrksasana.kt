package com.developbharat.yogsudhaar.domain.splitters

import android.util.Log
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt


class VrksasanaSplitter : IRepetitionSplitter {
    private fun findPeaks(data: List<List<Double>>): Pair<Int, Int>? {
        val targetColumn = 1 + 4 * 15 + 1
        val y = DoubleArray(data.count())
        for (i in 0..<data.count()) {
            if (targetColumn < data[i].count()) {
                y[i] = 1 - data[i][targetColumn]
            }
        }


        // Find peaks (local maxima)
        var peaks: MutableList<Int?> = ArrayList<Int?>()
        for (i in 1..<y.size - 1) {
            if (y[i] > y[i - 1] && y[i] > y[i + 1]) {
                peaks.add(i)
            }
        }


        // Calculate prominences
        var prominences = DoubleArray(peaks.size)
        val filteredPeaks: MutableList<Int?> = ArrayList<Int?>()
        val filteredProminences: MutableList<Double?> = ArrayList<Double?>()

        for (i in peaks.indices) {
            val peak: Int = peaks.get(i)!!
            val peakValue = y[peak]

            // Find minimum to left
            var leftMin = peakValue
            var leftFoundHigher = false
            for (j in peak - 1 downTo 0) {
                if (y[j] > peakValue) {
                    leftFoundHigher = true
                    break
                }
                leftMin = min(leftMin, y[j])
            }
            if (!leftFoundHigher) {
                leftMin = min(leftMin, y[0])
            }

            // Find minimum to right
            var rightMin = peakValue
            var rightFoundHigher = false
            for (j in peak + 1..<y.size) {
                if (y[j] > peakValue) {
                    rightFoundHigher = true
                    break
                }
                rightMin = min(rightMin, y[j])
            }
            if (!rightFoundHigher) {
                rightMin = min(rightMin, y[y.size - 1])
            }

            // Prominence is height of peak over highest of left/right minimum
            val prominence = peakValue - max(leftMin, rightMin)

            // Only keep peaks with prominence > 0.2
            if (prominence > 0.2) {
                prominences[filteredPeaks.size] = prominence
                filteredPeaks.add(peak)
                filteredProminences.add(prominence)
            }
        }

        peaks = filteredPeaks
        prominences = prominences.copyOf(filteredPeaks.size)

        // Calculate standard deviation of prominences
        var sum = 0.0
        for (prominence in prominences) {
            sum += prominence
        }
        val mean = sum / prominences.size

        var sumSq = 0.0
        for (prominence in prominences) {
            sumSq += (prominence - mean).pow(2.0)
        }
        val stdDev = sqrt(sumSq / prominences.size)

        // Find good peaks (prominences > 1 * stdDev)
        for (i in 0..<prominences.count()) {
            if (prominences[i] > 1 * stdDev) {
                val peak: Int = peaks.get(i)!!
                val key = y[peak] - (prominences[i] * 0.9)

                val from = findBefore(key, peak, y)
                val till = findAfter(key, peak, y)

                // Print frame number
                Log.d("frames", "(${data[from][0]}, ${data[till][0]})")

                return Pair<Int, Int>(from, till)
            }
        }

        return null;
    }

    /**
     * Find index before peak where value goes below key
     */
    private fun findBefore(key: Double, index: Int, data: DoubleArray): Int {
        var ans = index
        for (i in index downTo 0) {
            if (data[i] > key) {
                ans = i
            } else {
                break
            }
        }
        return ans
    }

    /**
     * Find index after peak where value goes below key
     */
    private fun findAfter(key: Double, index: Int, data: DoubleArray): Int {
        var ans = index
        for (i in index until data.size) {
            if (data[i] > key) {
                ans = i
            } else {
                break
            }
        }
        return ans
    }


    override fun split(frames: List<List<Double>>): Pair<Int, Int>? {
        return findPeaks(frames)
    }
}