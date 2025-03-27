package com.developbharat.yogsudhaar.domain.splitters

interface IRepetitionSplitter {
    fun split(frames: List<List<Double>>): Pair<Int, Int>?
}