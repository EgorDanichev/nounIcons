package com.edanichev.nounIcons.app.main.NounIconDrawer;

import android.view.View;
import android.widget.ImageView;

import com.edanichev.nounIcons.app.R;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.model.BaseViewHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

public class UrlPrimaryDrawerItem extends PrimaryDrawerItem {
    @Override
    protected void bindViewHelper(BaseViewHolder viewHolder) {
        super.bindViewHelper(viewHolder);

        ImageView imageView = viewHolder.itemView.findViewById(R.id.material_drawer_icon);
        imageView.setVisibility(View.GONE);
        if (isSelected()) {
            if (selectedIcon != null && selectedIcon.getUri() != null) {
                imageView.setVisibility(View.VISIBLE);
                ImageHolder.applyTo(selectedIcon, imageView, "customUrlItem");
            }
        } else {
            if (icon != null && icon.getUri() != null) {
                imageView.setVisibility(View.VISIBLE);
                ImageHolder.applyTo(icon, imageView, "customUrlItem");
            }
        }
    }

    public UrlPrimaryDrawerItem withIcon(String url) {
        this.icon = new ImageHolder(url);
        return this;
    }


}