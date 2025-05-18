package com.developbharat.yogsudhaar.ui.screens.check.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.developbharat.yogsudhaar.R
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.max
import kotlin.math.min


class OverlayView(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {

    private var results: PoseLandmarkerResult? = null
    private var successPointPaint = Paint()
    private var errorPointPaint = Paint()
    private var linePaint = Paint()

    private var scaleFactor: Float = 1f
    private var imageWidth: Int = 1
    private var imageHeight: Int = 1

    private val angleTriplets: Map<String, List<Int>> = mapOf(
        Pair("lH_lK_lA", listOf(23, 25, 27)),
        Pair("rH_rK_rA", listOf(24, 26, 28)),
        Pair("rS_rH_rK", listOf(12, 24, 26)),
        Pair("lS_lE_lW", listOf(11, 13, 15)),
        Pair("rS_rE_rW", listOf(12, 14, 16)),
        Pair("lS_lH_lK", listOf(11, 23, 25)),
        Pair("lE_lS_lH", listOf(13, 11, 23)),
        Pair("rE_rS_rH", listOf(14, 12, 24)),
    )

    init {
        initPaints()
    }

    fun clear() {
        results = null
        successPointPaint.reset()
        errorPointPaint.reset()
        linePaint.reset()
        invalidate()
        initPaints()
    }

    private fun createPaint(color: Int, style: Paint.Style = Paint.Style.STROKE): Paint {
        val pointPaint = Paint()
        pointPaint.color = color
        pointPaint.strokeWidth = LANDMARK_STROKE_WIDTH
        pointPaint.style = style
        return pointPaint
    }

    private fun initPaints() {
        linePaint.color =
            ContextCompat.getColor(context!!, R.color.teal_700)
        linePaint.strokeWidth = LANDMARK_STROKE_WIDTH
        linePaint.style = Paint.Style.STROKE

        successPointPaint.color = Color.YELLOW
        successPointPaint.strokeWidth = LANDMARK_STROKE_WIDTH
        successPointPaint.style = Paint.Style.FILL
    }

    private fun vectorSubtract(a: List<Float>, b: List<Float>): List<Float> {
        val items = mutableListOf<Float>()
        for (i in 0..a.size - 1) {
            items.add(a[i] - b[i])
        }
        return items
    }

    private fun calculateAngle(
        a: NormalizedLandmark,
        b: NormalizedLandmark,
        c: NormalizedLandmark
    ): Double {
        val ba = vectorSubtract(listOf(a.x(), a.y(), a.z()), listOf(b.x(), b.y(), b.z()))
        val bc = vectorSubtract(listOf(c.x(), c.y(), c.z()), listOf(b.x(), b.y(), b.z()))

        val x1 = ba.first()
        val y1 = ba[1]

        val x2 = bc[0]
        val y2 = bc[1]

        val ang_a = atan2(y1, x1)
        val ang_b = atan2(y2, x2)

        return Math.toDegrees((ang_a - ang_b) % (2 * PI))

    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        results?.let { poseLandmarkerResult ->
            for (landmark in poseLandmarkerResult.landmarks()) {

                // Draw all landmark points
                for (normalizedLandmark in landmark) {
                    canvas.drawPoint(
                        normalizedLandmark.x() * imageWidth * scaleFactor,
                        normalizedLandmark.y() * imageHeight * scaleFactor,
                        successPointPaint
                    )
                }

                // Draw correct/incorrect point on canvas
                for ((name, points) in angleTriplets.entries) {
                    val a = landmark[points.first()]
                    val b = landmark[points[1]]
                    val c = landmark[points[2]]
                    if (a != null && b != null && c != null) {
                        val angle = calculateAngle(a, b, c)
                        val mean = AngleStatsData.rightHandedHash.get(name)?.mean
                        val std = AngleStatsData.rightHandedHash.get(name)?.std

                        if (mean == null || std == null) continue

                        val paint =
                            if (abs(angle - mean) <= (3 * std)) createPaint(
                                Color.GREEN,
                                Paint.Style.FILL
                            ) else createPaint(
                                Color.RED, Paint.Style.FILL
                            )

                        val x = b.x() * imageWidth * scaleFactor
                        val y = b.y() * imageHeight * scaleFactor
                        canvas.drawPoint(x, y, paint)
                    }
                }


                // Draw lines on canvas to connect points
                PoseLandmarker.POSE_LANDMARKS.forEach {
                    canvas.drawLine(
                        poseLandmarkerResult.landmarks().get(0).get(it!!.start())
                            .x() * imageWidth * scaleFactor,
                        poseLandmarkerResult.landmarks().get(0).get(it.start())
                            .y() * imageHeight * scaleFactor,
                        poseLandmarkerResult.landmarks().get(0).get(it.end())
                            .x() * imageWidth * scaleFactor,
                        poseLandmarkerResult.landmarks().get(0).get(it.end())
                            .y() * imageHeight * scaleFactor,
                        linePaint
                    )
                }
            }
        }
    }

    fun setResults(
        poseLandmarkerResults: PoseLandmarkerResult,
        imageHeight: Int,
        imageWidth: Int,
        runningMode: RunningMode = RunningMode.IMAGE
    ) {
        results = poseLandmarkerResults

        this.imageHeight = imageHeight
        this.imageWidth = imageWidth

        scaleFactor = when (runningMode) {
            RunningMode.IMAGE,
            RunningMode.VIDEO -> {
                min(width * 1f / imageWidth, height * 1f / imageHeight)
            }

            RunningMode.LIVE_STREAM -> {
                // PreviewView is in FILL_START mode. So we need to scale up the
                // landmarks to match with the size that the captured images will be
                // displayed.
                max(width * 1f / imageWidth, height * 1f / imageHeight)
            }
        }
        invalidate()
    }

    companion object {
        private const val LANDMARK_STROKE_WIDTH = 15F
    }
}