import androidx.recyclerview.widget.RecyclerView

class ClassifyLayoutManager(private val columns: Int, private val rows: Int) : RecyclerView.LayoutManager() {
    private val totalCount = columns * rows
    private var totalWidth = 0
    private var horizontalScrollOffset = 0
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        detachAndScrapAttachedViews(recycler)
        measuredAndLayout(recycler)
    }

    private fun measuredAndLayout(recycler: RecyclerView.Recycler) {
        var childLeft = 0
        var childTop = 0

        val everyWidth = width / columns
        val everyHeight = height / rows

        var curIndex = 0

        for (index in 0 until itemCount) {
            val view = recycler.getViewForPosition(index)
            addView(view)
            measureChildWithMargins(view, 0, 0)

            val childWidth = getDecoratedMeasuredWidth(view)
            val childHeight = getDecoratedMeasuredHeight(view)

            val widthMargin = (everyWidth / 2) - (childWidth / 2)
            val heightMargin = (everyHeight / 2) - (childHeight / 2)

            if (index != 0 && index % columns == 0) {
                childTop += everyHeight
                childLeft = curIndex * width
            }

            if (index != 0 && index % totalCount == 0) {
                childLeft += width
                curIndex += 1
                childTop = 0
            }

            layoutDecorated(
                view,
                childLeft + widthMargin,
                childTop + heightMargin,
                childLeft + widthMargin + everyWidth,
                childTop + heightMargin + everyHeight
            )

            childLeft += everyWidth

            totalWidth = (curIndex + 1) * width
        }
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        var travel = dx
        if (horizontalScrollOffset + dx < 0) {
            travel = -horizontalScrollOffset
        } else if (horizontalScrollOffset + dx > totalWidth - getHorizontalSpace()) {
            travel = totalWidth - getHorizontalSpace() - horizontalScrollOffset
        }
        horizontalScrollOffset += travel
        offsetChildrenHorizontal(-travel)
        return travel
    }

    private fun getHorizontalSpace() = width - paddingEnd - paddingStart
}
