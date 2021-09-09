package com.example.myandroidnotes.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myandroidnotes.EditActivity;
import com.example.myandroidnotes.DB.NoteDbOpenHelper;
import com.example.myandroidnotes.Notes;
import com.example.myandroidnotes.R;


import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Notes> mList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    private NoteDbOpenHelper mNoteDbOpenHelper;

    private int viewType;

    public static int TYPE_LINEAR_LAYOUT = 0;
    public static int TYPE_GRID_LAYOUT = 1;


    public MyAdapter(Context mContext, List<Notes> mList) {
        this.mList = mList;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        mNoteDbOpenHelper = new NoteDbOpenHelper(mContext);
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_LINEAR_LAYOUT) {
            View view = mLayoutInflater.inflate(R.layout.list_item_layout, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);

            return myViewHolder;

        } else if (viewType == TYPE_GRID_LAYOUT) {
            View view = mLayoutInflater.inflate(R.layout.list_item_grid_layout, parent, false);
            MyGridViewHolder myGridViewHolder = new MyGridViewHolder(view);
            return myGridViewHolder;

        }


        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        }
        if (holder instanceof MyViewHolder) {
            bindMyViewHolder((MyViewHolder) holder, position);
        } else if (holder instanceof MyGridViewHolder) {
            bindGridViewHolder((MyGridViewHolder) holder, position);

        }
    }


    private void bindMyViewHolder(MyViewHolder holder, int position) {
        Notes note = mList.get(position);
        holder.mTvTitle.setText(note.getTitle());
        holder.mTvContent.setText(note.getContents());
        holder.mTvTime.setText(note.getTime());

        // 点击跳转到编辑界面
        holder.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditActivity.class);   // 点击条目就进入新的编辑界面
                intent.putExtra("note", note);
                mContext.startActivity(intent);
            }
        });

        // 长按
        holder.rlContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                // 弹出弹窗
                Dialog dialog = new Dialog(mContext, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);
                View dialogView = mLayoutInflater.inflate(R.layout.list_item_dialog_layout, null);

                TextView tvDelete = dialogView.findViewById(R.id.tv_delete);
                TextView tvEdit = dialogView.findViewById(R.id.tv_edit);

                // 点击删除 则从数据库中删除该笔记
                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mNoteDbOpenHelper == null) {
                            mNoteDbOpenHelper = new NoteDbOpenHelper(mContext);
                        }


                        AlertDialog.Builder dialog2 = new AlertDialog.Builder(mContext);
                        dialog2.setTitle("确定删除？");
                        dialog2.setMessage("真的删除的话，请点击确定");
                        dialog2.setCancelable(false);    //不可取消
                        dialog2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(mContext, "已删除", Toast.LENGTH_LONG).show();

                                        int row = mNoteDbOpenHelper.deleteFromDbById(note.getId());   // 从数据库删除
                                        if (row > 0) {
                                            removeData(position);
                                        }

                                        dialog.dismiss();


                                    }
                                }

                        );

                        dialog2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(mContext, "取消删除", Toast.LENGTH_LONG).show();
                            }

                        });

                        dialog2.create().show();

                        dialog.dismiss();

                    }
                });


                tvEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, EditActivity.class);
                        intent.putExtra("note", note);
                        mContext.startActivity(intent);     // 打开编辑界面
                        dialog.dismiss();

                    }
                });

                dialog.setContentView(dialogView);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                return true;

            }
        });
        
    }


    private void bindGridViewHolder(MyGridViewHolder holder, int position) {
        Notes note = mList.get(position);
        holder.mTvTitle.setText(note.getTitle());
        holder.mTvContent.setText(note.getContents());
        holder.mTvTime.setText(note.getTime());
        holder.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditActivity.class);   // 点击条目就进入新的编辑界面
                intent.putExtra("note", note);
                mContext.startActivity(intent);
            }
        });

        holder.rlContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                // 弹出弹窗
                Dialog dialog = new Dialog(mContext, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);
                View dialogView = mLayoutInflater.inflate(R.layout.list_item_dialog_layout, null);

                TextView tvDelete = dialogView.findViewById(R.id.tv_delete);
                TextView tvEdit = dialogView.findViewById(R.id.tv_edit);

                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int row = mNoteDbOpenHelper.deleteFromDbById(note.getId());
                        if (row > 0) {
                            removeData(position);
                        }
                        dialog.dismiss();
                    }
                });


                tvEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, EditActivity.class);
                        intent.putExtra("note", note);
                        mContext.startActivity(intent);
                        dialog.dismiss();

                    }
                });

                dialog.setContentView(dialogView);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                return true;

            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    // 删除数据库中对应id的条目
    // 删除后
    private void removeData(int position) {
        this.mList.remove(position);

//        if(mNoteDbOpenHelper==null)
//        {
//            mNoteDbOpenHelper=new NoteDbOpenHelper(this.mContext);
//
//        }
//
//        int result=mNoteDbOpenHelper.deleteFromDbById(position);


        notifyItemRemoved(position);


    }

    public void refreshDate(List<Notes> notes) {
        this.mList = notes;
        notifyDataSetChanged();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTvTitle;
        TextView mTvContent;
        TextView mTvTime;
        ViewGroup rlContainer;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mTvTitle = itemView.findViewById(R.id.tv_title);
            this.mTvContent = itemView.findViewById(R.id.tv_content);
            this.mTvTime = itemView.findViewById(R.id.tv_time);
            this.rlContainer = itemView.findViewById(R.id.rl_item_container);
        }
    }

    class MyGridViewHolder extends RecyclerView.ViewHolder {
        TextView mTvTitle;
        TextView mTvContent;
        TextView mTvTime;
        ViewGroup rlContainer;

        public MyGridViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mTvTitle = itemView.findViewById(R.id.grid_tv_title);
            this.mTvContent = itemView.findViewById(R.id.grid_tv_content);
            this.mTvTime = itemView.findViewById(R.id.grid_tv_time);
            this.rlContainer = itemView.findViewById(R.id.rl_item_container);
        }
    }

}
