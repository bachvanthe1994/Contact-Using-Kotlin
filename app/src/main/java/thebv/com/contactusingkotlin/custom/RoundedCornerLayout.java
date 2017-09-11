package thebv.com.contactusingkotlin.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;

import thebv.com.contactusingkotlin.R;

/**
 * Created by thebv on 07/26/2017.
 */

public class RoundedCornerLayout extends FrameLayout {
    private float cornerRadius;
    private Paint paint;
    private Bitmap bitmap;
    private Canvas bitmapCanvas;

    public RoundedCornerLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    public RoundedCornerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public RoundedCornerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedCornerLayout);

        float defaultCornerRadius = 0;

        cornerRadius = defaultCornerRadius;

        paint = new Paint();
        paint.setAntiAlias(true);

        if (a != null) {
            cornerRadius = a.getDimension(R.styleable.RoundedCornerLayout_cornerRadius, defaultCornerRadius);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        final Path path = new Path();
        path.setFillType(Path.FillType.WINDING);
        float[] corner = new float[]{cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius};
        path.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), corner, Path.Direction.CW);

        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        bitmapCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        super.dispatchDraw(bitmapCanvas);

        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        canvas.drawPath(path, paint);
    }

    @Override
    public ViewParent invalidateChildInParent(int[] location, Rect dirty) {
        invalidate();
        return super.invalidateChildInParent(location, dirty);
    }

    @Override
    public void childDrawableStateChanged(View child) {
        invalidate();
        super.childDrawableStateChanged(child);
    }

    @Override
    public void refreshDrawableState() {
        super.refreshDrawableState();
        invalidate();
    }
}