package tw.com.tcc.ultimatetictactoe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/15.
 */

public class UtttView extends View {
    protected static int Padding = 10;
    private static final int Neutral = -1;
    private static final int O = 0;
    private static final int X = 1;
    private final Paint paint = new Paint(); // 筆
    private int height;
    private int width;
    private int minSide;
    private int widthPadding;
    private int heightPadding;
    protected static List<Point> steps = new ArrayList<Point>(); // 步
    protected static int[][] zone = {
            {Neutral, Neutral, Neutral},
            {Neutral, Neutral, Neutral},
            {Neutral, Neutral, Neutral}
    }; // 佔領區塊
//    protected Point tempPoint = new Point(0, 0);
    private Context mContext;
//    public UtttView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        mContext = context;
//        this.setFocusable(true);
//        this.setFocusableInTouchMode(true);
//        init();
//    }
    public UtttView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        init();
    }

    public void init() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                zone[i][j] = Neutral;
            }
        }
        steps.clear();
        refressCanvas();
    }

    private void drawPoints(Canvas canvas) {
        for (int i = 0; i < steps.size(); i++) {
            Drawable drawable = getResources().getDrawable(i % 2 == 0 ? R.drawable.o : R.drawable.x);
            Bitmap bitmap = Bitmap.createBitmap(minSide / 9, minSide / 9, Bitmap.Config.ARGB_8888);
            Canvas mCanvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, minSide / 9, minSide / 9);
            drawable.draw(mCanvas);
            canvas.drawBitmap(bitmap, steps.get(i).x * minSide / 9 + widthPadding + Padding, steps.get(i).y * minSide / 9 + heightPadding + Padding, paint);
        }

    }
    private void drawChssboardLines(Canvas canvas) {
        // 棋盤以外背景色
        paint.setColor(Color.WHITE);
        Path rect = new Path();
        rect.addRect(0, 0, width, height, Path.Direction.CW);
        canvas.drawPath(rect, paint);
        // 棋盤背景色
        paint.setColor(steps.size() == 0 || zone[steps.get(steps.size() - 1).x % 3][steps.get(steps.size() - 1).y % 3] != Neutral ? getResources().getColor(R.color.colorLime) : Color.GRAY);
        rect = new Path();
        rect.addRect(widthPadding + Padding, heightPadding + Padding, width - widthPadding - Padding, height - heightPadding - Padding, Path.Direction.CW);
        canvas.drawPath(rect, paint);
        // 畫區塊背景
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (steps.size() > 0 && steps.get(steps.size() - 1).x % 3 == i && steps.get(steps.size() - 1).y % 3 == j || zone[i][j] != Neutral) {
                    rect = new Path();
    //                    steps.get(steps.size() - 1).x % 3
                    paint.setColor(getResources().getColor(zone[i][j] == Neutral && steps.get(steps.size() - 1).x % 3 == i && steps.get(steps.size() - 1).y % 3 == j ? R.color.colorLime : (zone[i][j] == O ? R.color.colorLightCoral : R.color.colorLightSkyBlue)));
                    rect.addRect(
                            widthPadding + Padding + i * minSide / 3,
                            heightPadding + Padding + j * minSide / 3,
                            widthPadding + Padding + (i + 1) * minSide / 3,
                            heightPadding + Padding + (j + 1) * minSide / 3,
                            Path.Direction.CW
                    );
                    canvas.drawPath(rect, paint);
                }
            }
        }
        // 畫粗的線
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        for (int i = 0; i <= 3; i++) {
            canvas.drawLine(
                    i * minSide / 3 + Padding + widthPadding,
                    Padding + heightPadding,
                    i * minSide / 3 + Padding + widthPadding,
                    minSide + Padding + heightPadding,
                    paint
            );
            canvas.drawLine(
                    Padding + widthPadding,
                    i * minSide / 3 + Padding + heightPadding,
                    minSide + Padding + widthPadding,
                    i * minSide / 3 + Padding + heightPadding,
                    paint
            );
        }
        // 畫細的線
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                paint.setStrokeWidth(1);
                for (int k = 1; k < 3; k++) {
                    canvas.drawLine(
                            i * minSide / 3 + k * minSide / 9 + Padding + widthPadding,
                            j * minSide / 3 + 2 * Padding + heightPadding,
                            i * minSide / 3 + k * minSide / 9 + Padding + widthPadding,
                            (j + 1) * minSide / 3 + heightPadding,
                            paint
                    );
                    canvas.drawLine(
                            j * minSide / 3 + 2 * Padding + widthPadding,
                            i * minSide / 3 + k * minSide / 9 + Padding + heightPadding,
                            (j + 1) * minSide / 3 + widthPadding,
                            i * minSide / 3 + k * minSide / 9 + Padding + heightPadding,
                            paint
                    );

                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        height = getMeasuredHeight();
        width = getMeasuredWidth();
        minSide = Math.min(height, width);
        widthPadding = (width - minSide) / 2;
        heightPadding = (height - minSide) / 2;
        minSide -= 2 * Padding;
        drawChssboardLines(canvas);
        // 畫點
        drawPoints(canvas);
    }
    // 刷新
    private void refressCanvas(){
        // 觸發 onDraw 函數
        UtttView.this.invalidate();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        playerRun(event);
        return true;
    }

    private synchronized void playerRun(MotionEvent event) {
        Point current = newPoint(event.getX(), event.getY());
        if (current.x >= 0 && current.x < 9) {
            if (current.y >= 0 && current.y < 9) {
                if (steps.indexOf(current) < 0) {
                    if (zone[current.x / 3][current.y / 3] == Neutral) {
                        if (steps.size() == 0
                                || (steps.get(steps.size() - 1).x % 3) == (current.x / 3) && (steps.get(steps.size() - 1).y % 3) == (current.y / 3)
                                || zone[steps.get(steps.size() - 1).x % 3][steps.get(steps.size() - 1).y % 3] != Neutral) {
                            steps.add(current);
                            if (hasWon()) {
                                zone[current.x / 3][current.y / 3] = 1 - steps.size() % 2;
                                MediaPlayer.create(mContext, hasWonWon() ? R.raw.clapping_bravo : R.raw.cat_sound).start();
                                if (hasWonWon()) {
                                    Toast.makeText(mContext, getResources().getString(R.string.you_win), Toast.LENGTH_SHORT).show();
                                }
                            }
                            refressCanvas();
                        }
                    }
                }
            }
        }
    }
    // 根據觸摸點坐標找到對應點
    private Point newPoint(Float x, Float y){
        Point p = new Point(0, 0);
        if (x >= widthPadding && x < width - widthPadding) {
            if (y >= heightPadding && y < height - heightPadding) {
                p.set((int) (x - widthPadding) * 9 / minSide, (int) (y - heightPadding) * 9 / minSide);
                return p;
            }
        }
        p.set(-1, -1);
        return p;
    }
    private boolean hasWon() {
        Point current = steps.get(steps.size() - 1);
        int camp = 1 - steps.size() % 2; // 陣營
        return steps.indexOf(new Point(current.x, current.y / 3 * 3)) % 2 == camp
                    && steps.indexOf(new Point(current.x, current.y / 3 * 3 + 1)) % 2 == camp
                    && steps.indexOf(new Point(current.x, current.y / 3 * 3 + 2)) % 2 == camp
                || steps.indexOf(new Point(current.x / 3 * 3, current.y)) % 2 == camp
                    && steps.indexOf(new Point(current.x / 3 * 3 + 1, current.y)) % 2 == camp
                    && steps.indexOf(new Point(current.x / 3 * 3 + 2, current.y)) % 2 == camp
                || current.x % 3 == current.y % 3
                    && steps.indexOf(new Point(current.x / 3 * 3, current.y / 3 * 3)) % 2 == camp
                    && steps.indexOf(new Point(current.x / 3 * 3 + 1, current.y / 3 * 3 + 1)) % 2 == camp
                    && steps.indexOf(new Point(current.x / 3 * 3 + 2, current.y / 3 * 3 + 2)) % 2 == camp
                || current.x % 3 + current.y % 3 == 2
                    && steps.indexOf(new Point(current.x / 3 * 3 + 2, current.y / 3 * 3)) % 2 == camp
                    && steps.indexOf(new Point(current.x / 3 * 3 + 1, current.y / 3 * 3 + 1)) % 2 == camp
                    && steps.indexOf(new Point(current.x / 3 * 3 , current.y / 3 * 3 + 2)) % 2 == camp;
    }
    private boolean hasWonWon() {
        Point current = steps.get(steps.size() - 1);
        int camp = zone[current.x / 3][current.y / 3]; // 陣營
        return zone[current.x / 3][0] == camp
                    && zone[current.x / 3][1] == camp
                    && zone[current.x / 3][2] == camp
                || zone[0][current.y / 3] == camp
                    && zone[1][current.y / 3] == camp
                    && zone[2][current.y / 3] == camp
                || current.x / 3 == current.y / 3
                    && zone[0][0] == camp
                    && zone[1][1] == camp
                    && zone[2][2] == camp
                || current.x / 3 + current.y / 3 == 2
                    && zone[0][2] == camp
                    && zone[1][1] == camp
                    && zone[2][0] == camp;
    }

}