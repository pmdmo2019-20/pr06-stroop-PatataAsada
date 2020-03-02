package es.iessaladillo.pedrojoya.stroop.ui.ranking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.data.model.GameWithPlayer
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.ranking_fragment_item.*

class RankingFragmentAdapter: ListAdapter<GameWithPlayer,RankingFragmentAdapter.ViewHolder>(ResultDiffCallBack) {

    var onItemClickListener: ((Int)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.player_item, parent, false)
        return ViewHolder(itemView, onItemClickListener)
    }

    fun setOnClickListener(listener: ((Int) -> Unit)?) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    object ResultDiffCallBack :DiffUtil.ItemCallback<GameWithPlayer>(){
        override fun areItemsTheSame(oldItem: GameWithPlayer, newItem: GameWithPlayer): Boolean =
            oldItem.game.id == newItem.game.id

        override fun areContentsTheSame(oldItem: GameWithPlayer, newItem: GameWithPlayer): Boolean {
            return oldItem.game == newItem.game && oldItem.player == newItem.player
        }

    }
    class ViewHolder(
        override val containerView: View,
        onItemClickListener: ((Int) -> Unit)?
    ):RecyclerView.ViewHolder(containerView),LayoutContainer {
        fun bind(gameWithPlayer: GameWithPlayer){
            img_ranking.setImageResource(gameWithPlayer.player.imgResId)
            lblName.text = gameWithPlayer.player.name
            txtCorrect.text = gameWithPlayer.game.correct.toString()
            txtGamemode.text = gameWithPlayer.game.gamemode
            txtPoints.text = gameWithPlayer.game.points.toString()
            txtTime.text = gameWithPlayer.game.time.toString()
            txtWords.text = gameWithPlayer.game.words.toString()
        }

    }
}