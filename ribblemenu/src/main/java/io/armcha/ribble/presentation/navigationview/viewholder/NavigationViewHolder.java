package io.armcha.ribble.presentation.navigationview.viewholder;

import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import io.armcha.ribble.R;
import io.armcha.ribble.presentation.navigationview.NavigationItem;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class NavigationViewHolder extends BaseViewHolder<NavigationItem> {

    private static String TAG = NavigationViewHolder.class.getSimpleName();
    private TextView itemText;
    private ImageView itemIcon;

    public NavigationViewHolder(ViewGroup parent) {
        super(parent, R.layout.layout_navigation_item);

        itemText = (TextView) $(R.id.itemText);
        itemIcon = (ImageView) $(R.id.itemIcon);
    }

    @Override
    public void setData(final NavigationItem data) {

        itemText.setText(data.getNavigationId().getValue());
        itemIcon.setImageResource(data.getIcon());

        if (data.isSelected()) {
            itemIcon.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.colorMagenta));
            itemText.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorMagenta));
        } else {
            itemIcon.setColorFilter(ContextCompat.getColor(itemView.getContext(), data.getItemIconColor()));
            itemText.setTextColor(ContextCompat.getColor(itemView.getContext(), data.getItemIconColor()));
        }
    }
}