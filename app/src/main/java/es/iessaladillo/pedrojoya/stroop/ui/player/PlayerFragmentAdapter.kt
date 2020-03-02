package es.iessaladillo.pedrojoya.stroop.ui.player

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.data.model.Player
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.player_item.view.*


class PlayerFragmentAdapter :
    ListAdapter<Player, PlayerFragmentAdapter.ViewHolder>(PlayerDiffCallBack) {

    var onItemClickListener: ((Int)->Unit)? = null

    override fun getItemId(position: Int): Long = currentList[position].id

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.player_item, parent, false)
        return ViewHolder(itemView, onItemClickListener)
    }

    fun setOnClickListener(listener:((Int)->Unit)?){
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class ViewHolder(override val containerView: View, onItemClickListener: ((Int)->Unit)?) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        init {
            containerView.setOnClickListener { onItemClickListener?.invoke(adapterPosition) }
        }

        fun bind(player: Player) {
            containerView.lblName.text = player.name
            containerView.imgAvatar.setImageResource(player.imgResId)
        }

    }

    object PlayerDiffCallBack : DiffUtil.ItemCallback<Player>() {
        override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean =
            oldItem.name == newItem.name || oldItem.imgResId == newItem.imgResId

    }
}