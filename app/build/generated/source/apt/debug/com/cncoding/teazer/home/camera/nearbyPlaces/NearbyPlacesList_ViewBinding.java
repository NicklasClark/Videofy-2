// Generated code from Butter Knife. Do not modify!
package com.cncoding.teazer.home.camera.nearbyPlaces;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class NearbyPlacesList_ViewBinding implements Unbinder {
  private NearbyPlacesList target;

  private View view2131755393;

  private View view2131755391;

  @UiThread
  public NearbyPlacesList_ViewBinding(final NearbyPlacesList target, View source) {
    this.target = target;

    View view;
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.nearby_places_recycler_view, "field 'recyclerView'", RecyclerView.class);
    target.locationNotAvailableLayout = Utils.findRequiredViewAsType(source, R.id.location_not_available_layout, "field 'locationNotAvailableLayout'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.turn_on_location_btn, "field 'turnOnLocationBtn' and method 'onTurnOnLocationBtnClicked'");
    target.turnOnLocationBtn = Utils.castView(view, R.id.turn_on_location_btn, "field 'turnOnLocationBtn'", ProximaNovaSemiboldButton.class);
    view2131755393 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onTurnOnLocationBtnClicked();
      }
    });
    target.appBarLayout = Utils.findRequiredViewAsType(source, R.id.nearby_places_app_bar_layout, "field 'appBarLayout'", FrameLayout.class);
    view = Utils.findRequiredView(source, R.id.search_nearby_places, "field 'searchNearbyPlaces' and method 'launchNearbyPlaceSearch'");
    target.searchNearbyPlaces = Utils.castView(view, R.id.search_nearby_places, "field 'searchNearbyPlaces'", AppCompatImageView.class);
    view2131755391 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.launchNearbyPlaceSearch();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    NearbyPlacesList target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerView = null;
    target.locationNotAvailableLayout = null;
    target.turnOnLocationBtn = null;
    target.appBarLayout = null;
    target.searchNearbyPlaces = null;

    view2131755393.setOnClickListener(null);
    view2131755393 = null;
    view2131755391.setOnClickListener(null);
    view2131755391 = null;
  }
}
