package com.example.lesson_11_kislyakov

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CustomDiagramView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val ANIMATION_TIME = 1000
    private val ANIM_END_VALUE = 1.0f

    private val MAX_COLUMN_VALUE = 100

    private var mInterpolator: Interpolator

    private val MIN_COLUMN_COUNT = 1
    private val MAX_COLUMN_COUNT = 9

    private var mAnimateOnDisplay = false
    private var animationFinished = true

    private var mAnimationStartTime = 0L

    private var currentAnimValue = ArrayList<Float>()

    private val textBounds = Rect()
    private var columnWidth = 0.0f

    private var columnWidthOnScreen = 0.0f
    private var columnHeightOnScreen = 0.0f
    private var columnYStartpoint = 0.0f

    private var columnCornerRadius = 0.0f

    private lateinit var maxColumnValue: Column

    private var dateTextMargin = 0.0f
    private var valueTextMargin = 0.0f
    private var valueTextTopMargin = 0.0f

    private var valueTextSize = 0.0f
    private var dateTextSize = 0.0f

    private var columnList = ArrayList<Column>()

    private var dateColor: String?
    private var columnColor: String?

    private var textPaint: Paint
    private var datePaint: Paint

    init {
        attrs.let {
            val styleAttrs = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CustomDiagramView,
                0, 0
            )
            dateColor = if (styleAttrs.getString(R.styleable.CustomDiagramView_dateColor) != null) {
                styleAttrs.getString(R.styleable.CustomDiagramView_dateColor)
            } else {
                Color.GREEN.toString()
            }

            columnColor = if (styleAttrs.getString(R.styleable.CustomDiagramView_columnColor) != null) {
                styleAttrs.getString(R.styleable.CustomDiagramView_columnColor)
            } else {
                Color.GREEN.toString()
            }
            styleAttrs.recycle()
        }
        dateTextSize = resources.getDimension(R.dimen.dimen_date_text_size)
        valueTextSize = resources.getDimension(R.dimen.dimen_value_text_size)

        dateTextMargin = resources.getDimension(R.dimen.dimen_date_text_margin)
        valueTextMargin = resources.getDimension(R.dimen.dimen_value_text_margin)
        valueTextTopMargin = resources.getDimension(R.dimen.dimen_top_margin)

        columnCornerRadius = resources.getDimension(R.dimen.dimen_column_corner_radius)

        columnWidth = resources.getDimension(R.dimen.dimen_column_width)

        mInterpolator = AccelerateDecelerateInterpolator()

        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.color = Color.parseColor(columnColor)
        textPaint.textSize = valueTextSize

        datePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        datePaint.color = Color.parseColor(dateColor)
        datePaint.textSize = dateTextSize
    }


    override fun onDraw(canvas: Canvas) {
        Log.d("myTag", "onDraw")
        super.onDraw(canvas)
        if (mAnimationStartTime == 0L) {
            mAnimationStartTime = System.currentTimeMillis()
        }
        drawColumns(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Log.d("myTag", "onSizeChanged w = $w, h = $h")
        super.onSizeChanged(w, h, oldw, oldh)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var dateWidth: Float = textBounds.width().toFloat()
        var dateHeight: Float = textBounds.height().toFloat()

        datePaint.getTextBounds(
            convertDateToString(columnList[0].date), 0, //добавляю символ-пробел между датами
            convertDateToString(columnList[0].date).length, textBounds
        )
        dateWidth += textBounds.width()
        dateHeight += textBounds.height()
        //высота текста даты

        textPaint.getTextBounds(
            maxColumnValue.value.toString(), 0,
            maxColumnValue.value.toString().length, textBounds
        )
        dateHeight += textBounds.height() // + высота подписи столбца


        val minW = paddingLeft + paddingRight + dateWidth.toInt() * (columnList.size)// ширина даты * число столбцов

        val minH =
            paddingBottom + paddingTop + dateHeight.toInt() + resources.getDimension(R.dimen.dimen_min_column_height).toInt()

        val w = resolveSizeAndState(minW, widthMeasureSpec, 0)
        val h = resolveSizeAndState(minH, heightMeasureSpec, 0)

        setMeasuredDimension(w, h)
    }

    fun setData(columnCount: Int) {
        val rnd = Random()
        if (columnCount in MIN_COLUMN_COUNT..MAX_COLUMN_COUNT) {
            for (i in 0 until columnCount) {
                columnList.add(Column(Date(rnd.nextLong() % Date().time), rnd.nextInt(MAX_COLUMN_VALUE)))
                currentAnimValue.add(0.0f)
            }
        } else {
            throw Exception(context.getString(R.string.column_count_fail))
        }
        maxColumnValue = Collections.max(columnList, ColumnComparator())
        Log.d("myTag", "SetData completed")
    }

    fun drawColumns(canvas: Canvas) {

        val maxColumnSizeOnScreen =
            dateTextSize + valueTextSize + dateTextMargin * 2 + valueTextMargin * 2 + valueTextTopMargin
        columnYStartpoint = dateTextMargin * 2 + dateTextSize
        columnHeightOnScreen = canvas.height - columnYStartpoint
        columnWidthOnScreen = (canvas.width / columnList.size).toFloat()

        textPaint.color = Color.parseColor(columnColor)

        for (i in 0 until columnList.size) {
            val bounds = Rect()
            textPaint.getTextBounds(
                columnList[i].value.toString(), 0,
                columnList[i].value.toString().length, bounds
            )

            val textWidth = bounds.width().toFloat()
            val columnCurrentHeight =
                columnList[i].value * (canvas.height - maxColumnSizeOnScreen) * currentAnimValue[i] / maxColumnValue.value

            //draw initial circle
            canvas.drawCircle(
                (i * columnWidthOnScreen + columnWidthOnScreen / 2),
                columnHeightOnScreen, columnCornerRadius / 2.0f, textPaint
            )

            // draw columns
            canvas.drawRoundRect(
                RectF(
                    (i * columnWidthOnScreen + columnWidthOnScreen / 2 - columnWidth / 2),
                    columnHeightOnScreen - columnCurrentHeight,
                    (i * columnWidthOnScreen + columnWidthOnScreen / 2 + columnWidth / 2),
                    columnHeightOnScreen
                ),
                columnCornerRadius, columnCornerRadius, textPaint
            )

            // draw value on the top of column
            canvas.drawText(
                columnList[i].value.toString(), i * columnWidthOnScreen + columnWidthOnScreen / 2 - textWidth / 2,
                columnHeightOnScreen - valueTextMargin - columnCurrentHeight, textPaint
            )

            val dateBounds = Rect()
            datePaint.getTextBounds(
                convertDateToString(columnList[i].date), 0,
                convertDateToString(columnList[i].date).length, dateBounds
            )
            val dateWidth = dateBounds.width().toFloat()

            canvas.drawText(
                convertDateToString(columnList[i].date),
                i * columnWidthOnScreen + columnWidthOnScreen / 2 - dateWidth / 2,
                canvas.height - dateTextMargin,
                datePaint
            )

        }

        if (mAnimateOnDisplay && setCurrentAnimValue()[setCurrentAnimValue().size - 1] < ANIM_END_VALUE) {
            invalidate()
        }
        if (mAnimateOnDisplay && setCurrentAnimValue()[setCurrentAnimValue().size - 1] >= ANIM_END_VALUE) {
            invalidate()
            mAnimateOnDisplay = false
        }
    }

    private fun setCurrentAnimValue(): ArrayList<Float> {

        for (i in currentAnimValue.indices) {
            val now = System.currentTimeMillis()
            val pathGone =
                (now - mAnimationStartTime - (i * ANIMATION_TIME / columnList.size / 2).toLong()).toFloat() / ANIMATION_TIME
            when {
                pathGone < 0.0f -> currentAnimValue[i] = 0f
                pathGone < 1.0f -> {
                    val interpolatedPathGone = mInterpolator.getInterpolation(pathGone)
                    currentAnimValue[i] = interpolatedPathGone
                    //mListener.onCircleAnimation(getCurrentAnimationFrameValue(interpolatedPathGone));
                }
                else -> currentAnimValue[i] = ANIM_END_VALUE
                //mListener.onCircleAnimation(getCurrentAnimationFrameValue(1.0f));
            }
        }

        return currentAnimValue
    }

    fun showAnimation() {
        mAnimateOnDisplay = true
        animationFinished = false
        mAnimationStartTime = 0
        invalidate()
    }

    private fun convertDateToString(date: Date): String {
        val df = SimpleDateFormat("dd.MM", Locale.getDefault())
        return df.format(date)
    }

}


internal class ColumnComparator : Comparator<Column> {

    override fun compare(e1: Column, e2: Column): Int {
        return e1.value.compareTo(e2.value)
    }
}