package fr.zankia.android.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.curioustechizen.ago.RelativeTimeTextView;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private static final int TYPE_SENT = 1;

    private Listener mListener;
    private List<Message> mMessages;
    private boolean selection;
    private List<Boolean> selected;
    private User user;

    public MessageAdapter(Listener listener, List<Message> messages, User user) {
        this.mListener = listener;
        this.mMessages = messages;
        this.user = user;
        this.selection = false;
        this.selected = new ArrayList<>();
        resetSelection();
    }

    public void resetSelection() {
        selected.clear();
        for (int i = 0; i < mMessages.size(); ++i) {
            selected.add(false);
        }
    }


    public void setSelection(boolean selection) {
        this.selection = selection;
        notifyDataSetChanged();
    }

    public boolean isSelection() {
        return selection;
    }


    public void setData(List<Message> messages) {
        this.mMessages = messages;
        resetSelection();
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                viewType == TYPE_SENT ? R.layout.row_message_sent : R.layout.row_message_received,
                parent,
                false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(mMessages.get(position), selection, selected.get(position));
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mMessages.get(position).getUserEmail().equals(user.getEmail()) ? TYPE_SENT : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        private CheckBox checkBox;
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
            this.checkBox = itemView.findViewById(R.id.checkBox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    selected.set(getAdapterPosition(), b);
                }
            });
        }

        public void setData(Message m, boolean selection, boolean selected) {
            checkBox.setVisibility(selection ? View.VISIBLE : View.GONE);
            checkBox.setChecked(selected);
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
            mListener.onItemLongClick(
                    getAdapterPosition(),
                    mMessages.get(getAdapterPosition())
            );
            return true;
        }

        @Override
        public void onClick(View view) {
            selected.set(getAdapterPosition(), !checkBox.isChecked());
            notifyDataSetChanged();
        }
    }

    public interface Listener {
        void onItemLongClick(int position, Message message);
    }
}
