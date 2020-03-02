package es.iessaladillo.pedrojoya.stroop.ui.edit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import es.iessaladillo.pedrojoya.stroop.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.edit_fragment_item.*

class EditFragmentAdapter :
    androidx.recyclerview.widget.ListAdapter<Int, EditFragmentAdapter.ViewHolder>(
        AvatarDiffCallBack
    ) {

    var onItemClickListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.edit_fragment_item, parent, false)
        return ViewHolder(itemView, onItemClickListener)
    }

    fun setOnClickListener(listener: ((Int) -> Unit)?) {
        onItemClickListener = listener
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class ViewHolder(override val containerView: View, onItemClickListener: ((Int) -> Unit)?) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        init {
            containerView.setOnClickListener { onItemClickListener?.invoke(adapterPosition) }
        }

        fun bind(avatar: Int) {
            imgAvatar.setImageResource(avatar)
        }
    }

    object AvatarDiffCallBack : DiffUtil.ItemCallback<Int>() {

        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean = oldItem == newItem

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean = oldItem == newItem

    }
}


