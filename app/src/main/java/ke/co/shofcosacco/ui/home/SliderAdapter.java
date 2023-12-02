package ke.co.shofcosacco.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.RowSliderHomeBinding;
import ke.co.shofcosacco.app.models.Carousel;
import ke.co.shofcosacco.app.utils.EnchantedViewPager;
import ke.co.shofcosacco.app.utils.OnClick;


public class SliderAdapter extends PagerAdapter {

    Activity activity;
    List<Carousel> sliderLists;
    LayoutInflater inflater;
    RowSliderHomeBinding viewAdapterSlider;
    OnClick onClick;

    public SliderAdapter(Activity activity, List<Carousel> sliderLists) {
        this.activity = activity;
        this.sliderLists = sliderLists;
        inflater = activity.getLayoutInflater();
    }


    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        viewAdapterSlider = RowSliderHomeBinding.inflate(inflater, container, false);
        View view = viewAdapterSlider.getRoot();

        if (!sliderLists.get(position).imageUrl.equals("")) {
            Glide.with(activity.getApplicationContext()).load(sliderLists.get(position).imageUrl)
                    .placeholder(R.drawable.chuna_bg)
                    .into(viewAdapterSlider.ivHomeFeature2);
        }

        view.setTag(EnchantedViewPager.ENCHANTED_VIEWPAGER_POSITION + position);
        container.addView(view, 0);
        return view;

    }

    @Override
    public int getCount() {
        return sliderLists.size();
    }

    public void setOnItemClickListener(OnClick clickListener) {
        this.onClick = clickListener;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        (container).removeView((View) object);
    }

}

