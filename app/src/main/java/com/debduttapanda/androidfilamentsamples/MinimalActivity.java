package com.debduttapanda.androidfilamentsamples;

import android.app.Activity;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceView;

import com.google.android.filament.Engine;
import com.google.android.filament.Filament;
import com.google.android.filament.Renderer;
import com.google.android.filament.SwapChain;
import com.google.android.filament.View;
import com.google.android.filament.android.UiHelper;

public class MinimalActivity extends Activity {
    private UiHelper mUiHelper;
    private SurfaceView mSurfaceView;

    // Filament specific APIs
    private Engine mEngine;
    private Renderer mRenderer;
    private View mView; // com.google.android.filament.View, not android.view.View
    private SwapChain mSwapChain;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Filament.init();
        // Create a SurfaceView and add it to the activity
        mSurfaceView = new SurfaceView(this);
        setContentView(mSurfaceView);

        // Create the Filament UI helper
        mUiHelper = new UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK);

        // Attach the SurfaceView to the helper, you could do the same with a TextureView
        mUiHelper.attachTo(mSurfaceView);

        // Set a rendering callback that we will use to invoke Filament
        mUiHelper.setRenderCallback(new UiHelper.RendererCallback() {
            public void onNativeWindowChanged(Surface surface) {
                if (mSwapChain != null) mEngine.destroySwapChain(mSwapChain);
                mSwapChain = mEngine.createSwapChain(surface, mUiHelper.getSwapChainFlags());
            }

            // The native surface went away, we must stop rendering.
            public void onDetachedFromSurface() {
                if (mSwapChain != null) {
                    mEngine.destroySwapChain(mSwapChain);

                    // Required to ensure we don't return before Filament is done executing the
                    // destroySwapChain command, otherwise Android might destroy the Surface
                    // too early
                    mEngine.flushAndWait();

                    mSwapChain = null;
                }
            }

            // The native surface has changed size. This is always called at least once
            // after the surface is created (after onNativeWindowChanged() is invoked).
            public void onResized(int width, int height) {
                // Compute camera projection and set the viewport on the view
            }
        });

        mEngine = Engine.create();
        mRenderer = mEngine.createRenderer();
        mView = mEngine.createView();
        // Create scene, camera, etc.
    }

    public void onDestroy() {
        super.onDestroy();
        // Always detach the surface before destroying the engine
        mUiHelper.detach();

        mEngine.destroy();
    }

    // This is an example of a render function. You will most likely invoke this from
    // a Choreographer callback to trigger rendering at vsync.
    public void render() {
        if (mUiHelper.isReadyToRender()) {
            // If beginFrame() returns false you should skip the frame
            // This means you are sending frames too quickly to the GPU
            if (mRenderer.beginFrame(mSwapChain, System.nanoTime())) {
                mRenderer.render(mView);
                mRenderer.endFrame();
            }
        }
    }
}
