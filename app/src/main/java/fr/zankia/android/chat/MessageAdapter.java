package fr.zankia.android.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.curioustechizen.ago.RelativeTimeTextView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Listener mListener;
    private List<Message> mMessages;
    private int selected = -1;

    public MessageAdapter(Listener listener, List<Message> messages) {
        this.mListener = listener;
        this.mMessages = messages;
    }



    public void setData(List<Message> messages) {
        this.mMessages = messages;
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_message,
                parent,
                false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(mMessages.get(position), position == selected);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        private TextView nickname, message;
        private ImageView image;
        private RelativeTimeTextView timestamp;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            this.nickname = itemView.findViewById(R.id.nickname);
            this.message = itemView.findViewById(R.id.message);
            this.timestamp = itemView.findViewById(R.id.timestamp);
            this.image = itemView.findViewById(R.id.userImage);
        }

        public void setData(Message m, boolean selected) {
            itemView.findViewById(R.id.card_view).setBackgroundResource(
                    selected ? R.color.colorSelected : R.color.cardview_light_background
            );
            Context context = image.getContext();
            Glide
                .with(context)
                .load(context.getString(R.string.gravatarURI) + Utils.md5(m.getUserEmail()))
                .into(image);
            nickname.setText(m.getUserName());
            message.setText(m.getContent());
            timestamp.setReferenceTime(m.getTimestamp());
        }

        @Override
        public boolean onLongClick(View view) {
            mListener.onItemClick(
                    getAdapterPosition(),
                    mMessages.get(getAdapterPosition())
            );
            return true;
        }

        @Override
        public void onClick(View view) {
            selected = getAdapterPosition();
            notifyDataSetChanged();
        }
    }

    public interface Listener {
        void onItemClick(int position, Message message);
    }
}
