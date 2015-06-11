package com.shape100.gym.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.LatLngBounds.Builder;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shape100.gym.ConstVar;
import com.shape100.gym.Logger;
import com.shape100.gym.MainApplication;
import com.shape100.gym.R;
import com.shape100.gym.config.AppConfig;
import com.shape100.gym.model.ClubBean;
import com.shape100.gym.protocol.ClubJoin;
import com.shape100.gym.protocol.ClubNearby;
import com.shape100.gym.protocol.ProtocolHandler;
import com.shape100.gym.protocol.ThreadPool;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.model.Event;

public class ClubMapFragment extends BaseFragment implements LocationSource,
		AMapLocationListener, OnMarkerClickListener, OnInfoWindowClickListener,
		OnMapLoadedListener, OnClickListener, InfoWindowAdapter {

	private static final Logger log = Logger.getLogger("ClubMapFragment");

	/** Data */
	private MarkerOptions markerOption;
	private LatLng latlng = new LatLng(36.061, 103.834);
	private double mGeoLat;
	private double mGeoLng;
	private String mQuery = "";
	private List<ClubBean> mClubItems = new ArrayList<ClubBean>();
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private DisplayImageOptions options;
	private LatLng mLocationLatLng;
	private int mPosition;
	/** View */

	private Marker marker;// 有跳动效果的marker对象
	private AMap aMap;
	private MapView mapView;
	private ProgressBar mProgressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		log.d("onCreate");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		log.d("onCreateView");
		return inflater.inflate(R.layout.clubmap_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		log.d("onActivityCreated");

		View v = getView();
		mapView = (MapView) v.findViewById(R.id.map);
		mProgressBar = (ProgressBar) v.findViewById(R.id.pb_clubmap_frag_map);

		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		// init cache image options
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_club_default_logo)
				.showImageOnFail

				(R.drawable.ic_error).cacheInMemory(true).cacheOnDisk(true)
				.considerExifParams(true).bitmapConfig

				(Bitmap.Config.RGB_565).build();

		// init map
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}

	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		/** 定位 */
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
		myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// aMap.setMyLocationType()

		/** marker */
		aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式

	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	/**
	 * 方法必须重写
	 * 
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	/**
	 * 此方法已经废弃
	 */
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null && aLocation != null) {
			mListener.onLocationChanged(aLocation); // 显示系统小蓝点

			double geoLat = aLocation.getLatitude();
			double geoLng = aLocation.getLongitude();
			mLocationLatLng = new LatLng(geoLat, geoLng);
			String address = aLocation.getAddress();

			if (Logger.DBG) {
				log.d("Longitude : " + geoLng);
				log.d("Latitude : " + geoLat);
				log.d("Address : " + address);
			}

			mGeoLat = geoLat;
			mGeoLng = geoLng;
			AppConfig.getInstance().setCurrLat(mGeoLat + "");
			AppConfig.getInstance().setCurrLon(mGeoLng + "");

			// task to get json
			ThreadPool.getInstance().execute(
					new ClubNearby(new EventProtocolHandler(), geoLat, geoLng,
							1, mQuery));
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy
					.getInstance(getActivity());

			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 60 * 1000, 10, this);
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

	public double getGeoLat() {
		return mGeoLat;
	}

	public double getGeoLng() {
		return mGeoLng;
	}

	/**
	 * 在地图上添加marker
	 */
	private void addMarkersToMap() {

		for (int i = 0; i < mClubItems.size(); i++) {
			markerOption = new MarkerOptions();
			double lat = Double.parseDouble(mClubItems.get(i).getLat());
			double lng = Double.parseDouble(mClubItems.get(i).getLon());
			LatLng latLng = new LatLng(lat, lng);

			markerOption.position(latLng);
			markerOption.title(mClubItems.get(i).getName()).snippet(
					i + "_" + "地址：" + mClubItems.get(i).getAddress() + "_电话："
							+ mClubItems.get(i).getPhone() + "_"
							+ mClubItems.get(i).getLogoUrl());
			markerOption.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.ic_marker));
			marker = aMap.addMarker(markerOption);

		}

	}

	/**
	 * 对marker标注点点击响应事件
	 */
	@Override
	public boolean onMarkerClick(final Marker marker) {

		return false;
	}

	/**
	 * 监听点击infowindow窗口事件回调
	 */
	@Override
	public void onInfoWindowClick(Marker marker) {
		// Toast.makeText(getActivity(), "你点击了infoWindow窗口" + marker.getTitle(),
		// Toast.LENGTH_SHORT).show();
	}

	/**
	 * 监听amap地图加载成功事件回调
	 */
	@Override
	public void onMapLoaded() {
		// 设置所有maker显示在当前可视区域地图中

		if (getGeoLng() != 0) {
			Builder builder = new LatLngBounds.Builder()
					.include(mLocationLatLng);
			for (int i = 0; i < mClubItems.size(); i++) {
				double lat = Double.parseDouble(mClubItems.get(i).getLat());
				double lon = Double.parseDouble(mClubItems.get(i).getLon());
				LatLng latlng = new LatLng(lat, lon);
				builder.include(latlng);
			}
			LatLngBounds bounds = builder.build();
			aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
		}
	}

	/**
	 * 自定义infowinfow窗口 marker.getSnippet().split("_")[0] = mClubItems.position
	 * marker.getSnippet().split("_")[1] = address
	 * marker.getSnippet().split("_")[2] = phone
	 * marker.getSnippet().split("_")[3] = logo url
	 * 
	 */
	public void render(Marker marker, View view) {
		// position
		mPosition = Integer.parseInt(marker.getSnippet().split("_")[0]);

		// title
		TextView titleUi = (TextView) view
				.findViewById(R.id.tv_fragclub_map_name);
		titleUi.setText(marker.getTitle());

		// address
		String address = marker.getSnippet().split("_")[1];
		if (address.length() > 12) {
			address = address.substring(0, 12) + "……";
		}
		TextView addressView = (TextView) view
				.findViewById(R.id.tv_fragclub_map_address);
		addressView.setText(address);

		// phone
		String phone = marker.getSnippet().split("_")[2];
		TextView phoneView = (TextView) view
				.findViewById(R.id.tv_fragclub_map_phone);
		phoneView.setText(phone);

		// logo
		ImageView imageView = (ImageView) view
				.findViewById(R.id.iv_fragclub_map_logo);
		// imageView.setBackgroundResource(R.drawable.ic_club_default_logo);
		if (marker.getSnippet().split("_").length == 4) {
			String logo = marker.getSnippet().split("_")[3];

			ImageLoader.getInstance().displayImage(logo, imageView, options,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,

						FailReason failReason) {
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view,

								Bitmap loadedImage) {
						}
					}, new ImageLoadingProgressListener() {
						@Override
						public void onProgressUpdate(String imageUri,
								View view,

								int current, int total) {
						}
					});
		}

		// join onclick
		view.findViewById(R.id.btn_clubmap_custom_window_join)
				.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_clubmap_custom_window_join:
			// Intent intent = new Intent(this.getActivity(),
			// ClubInfoActivity.class);
			// Bundle bundle = new Bundle();
			// bundle.putSerializable("club", mClubItems.get(mPosition));
			// intent.putExtras(bundle);
			// this.startActivity(intent);
			if (AppConfig.getInstance().getUserId() != 0) {
				log.e("clubId:" + mClubItems.get(mPosition).getId());
				new ClubJoin(new JoinEventProtocolHandler(),
						mClubItems.get(mPosition)).start();
			} else {
				Toast.makeText(getActivity(), "您还没有登录，不能加入俱乐部！",
						Toast.LENGTH_SHORT).show();
			}

			break;
		default:
			break;
		}
	}

	/**
	 * 监听自定义infowindow窗口的infocontents事件回调
	 */
	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}

	/**
	 * 监听自定义infowindow窗口的infowindow事件回调
	 */
	@Override
	public View getInfoWindow(Marker marker) {
		View infoWindow = getActivity().getLayoutInflater().inflate(
				R.layout.clubmap_fragment_custom_info_window, null);
		render(marker, infoWindow);
		return infoWindow;
	}

	// Find by key word
	public void findClubByKeyWord(String query) {
		int page = 1;
		mQuery = query;
		mClubItems.clear();
		ThreadPool.getInstance().execute(
				new ClubNearby(new SearchEventProtocolHandler(), getGeoLat(),
						getGeoLng(), page, mQuery));

	}

	public class EventProtocolHandler extends ProtocolHandler {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == ConstVar.HANDLER_MSG_SUCCESS) {
				log.d("mClubItems.size():" + mClubItems.size());
				mProgressBar.setVisibility(mProgressBar.INVISIBLE);
				ArrayList<ClubBean> clubBeans = (ArrayList<ClubBean>) msg.obj;
				if (mClubItems != null) {
					mClubItems = clubBeans;
				}

				// showMarker();
				addMarkersToMap();
				onMapLoaded();
			}
		}
	}

	/**
	 * EventProtocolHandler
	 * 
	 * @author zpy zpy@98ki.com
	 * @date 2014-11-12 下午8:14:20
	 * @version: V1.0
	 */
	private class JoinEventProtocolHandler extends ProtocolHandler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ConstVar.HANDLER_MSG_SUCCESS:
				// post EventBus to FragmentCurr, refresh club and my schedule.
				EventBus.getDefault().post(
						new Event.RefreshScheduleEvent("joinClub"));
				EventBus.getDefault().post(
						new Event.ChangeFragmentEvent("ClubInfoFragment"));
				// hint
				Toast.makeText(MainApplication.sContext,
						"成功加入" + mClubItems.get(mPosition).getName() + "!",
						Toast.LENGTH_SHORT).show();
				break;
			case ConstVar.HANDLER_MSG_FAILURE:
				Toast.makeText(MainApplication.sContext,
						"加入" + mClubItems.get(mPosition).getName() + "失败!",
						Toast.LENGTH_SHORT).show();
				break;
			case ConstVar.HANDLER_MSG_ERROR:
				Toast.makeText(MainApplication.sContext, "请检查网络是否正确！",
						Toast.LENGTH_SHORT).show();
				break;

			}
		}
	}

	public class SearchEventProtocolHandler extends ProtocolHandler {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == ConstVar.HANDLER_MSG_SUCCESS) {
				log.d("mClubItems.size():" + mClubItems.size());
				aMap.clear();
				mClubItems.addAll((ArrayList<ClubBean>) msg.obj);
				if (mClubItems != null) {
					addMarkersToMap();
					onMapLoaded();
				}
			}
		}
	}
}