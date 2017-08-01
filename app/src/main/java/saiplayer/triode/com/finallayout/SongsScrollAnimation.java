package saiplayer.triode.com.finallayout;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;

/**
 * Created by dell on 27-Jul-17.
 */

public class SongsScrollAnimation {

    public static void animateSongList(RecyclerView.ViewHolder holder,boolean scrollState){

        AnimatorSet animatorSet=new AnimatorSet();

        ObjectAnimator translateY = ObjectAnimator.ofFloat(holder.itemView, "translationY" , scrollState==true ? 500 : -500,0);
        translateY.setDuration(500);

        /*ObjectAnimator translateX = ObjectAnimator.ofFloat(holder.itemView, "translationX" , -20,20,-10,10,-5,5,0);
        translateX.setDuration(1000);*/

        animatorSet.playTogether(translateY);
        animatorSet.start();

    }

}
