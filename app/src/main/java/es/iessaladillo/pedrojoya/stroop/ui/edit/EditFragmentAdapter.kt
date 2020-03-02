package es.iessaladillo.pedrojoya.stroop.ui.edit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import es.iessaladillo.pedrojoya.stroop.R
import es.iessaladillo.pedrojoya.stroop.data.model.Avatar
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.edit_fragment_item.*

class EditFragmentAdapter :
    androidx.recyclerview.widget.ListAdapter<Avatar, EditFragmentAdapter.ViewHolder>(
        EditFragmentAdapter.AvatarDiffCallBack
    ) {

    var onItemClickListener: ((Int)->Unit)? = null

    fun getImgResId(position: Int): Int = currentList[position].imgResId

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditFragmentAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.edit_fragment_item, parent, false)
        return EditFragmentAdapter.ViewHolder(itemView, onItemClickListener)
    }

    fun setOnClickListener(listener:((Int)->Unit)?){
        onItemClickListener = listener
    }


    override fun onBindViewHolder(holder: EditFragmentAdapter.ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class ViewHolder(override val containerView: View,onItemClickListener: ((Int)->Unit)?) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        init {
            containerView.setOnClickListener { onItemClickListener?.invoke(adapterPosition) }
        }
        fun bind(avatar: Avatar){
            imgAvatar.setImageResource(avatar.imgResId)
        }
    }

    object AvatarDiffCallBack : DiffUtil.ItemCallback<Avatar>() {

        override fun areItemsTheSame(oldItem: Avatar, newItem: Avatar): Boolean = oldItem.imgResId == newItem.imgResId

        override fun areContentsTheSame(oldItem: Avatar, newItem: Avatar): Boolean = oldItem.imgResId == newItem.imgResId

    }
}


