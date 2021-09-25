package com.example.myandroidnotes.adapter;

import static com.example.myandroidnotes.util.TextParse.getPlainTextFromHTML;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
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
import com.example.myandroidnotes.util.ItemTouchHelperAdapter;
import com.example.myandroidnotes.util.ItemTouchHelperCallback;
import com.example.myandroidnotes.util.ItemTouchHelperViewHolder;
import com.example.myandroidnotes.util.TextParse;


import java.util.Collections;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {

    public static int TYPE_LINEAR_LAYOUT = 0;
    public static int TYPE_GRID_LAYOUT = 1;
    private static final String TAG = "MyAdapter";
    private List<Notes> mList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int viewType;

    private NoteDbOpenHelper mNoteDbOpenHelper;


    public MyAdapter(Context mContext, List<Notes> mList) {
        this.mList = mList;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        mNoteDbOpenHelper = new NoteDbOpenHelper(mContext);
    }


    public void setViewType(int viewType) {
        this.viewType = viewType;
    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        // 交换列表中指定位置的数据
        Notes fromNote = mList.get(fromPosition);
        mList.remove(fromNote);
        mList.add(toPosition, fromNote);
        notifyItemMoved(fromPosition, toPosition);

//        Collections.swap(mList, fromPosition, toPosition);
//        notifyItemMoved(fromPosition, toPosition);


//        notifyItemRangeChanged(Math.min(fromPosition, toPosition), Math.abs(fromPosition - toPosition) +1);

       /* if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);*/

        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        // 移除
        mList.remove(position);
        notifyItemRemoved(position);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_LINEAR_LAYOUT) {
            View view = mLayoutInflater.inflate(R.layout.list_item_layout, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            Log.d(TAG, "onCreateViewHolder: viewType == TYPE_LINEAR_LAYOUT");
            return myViewHolder;

        } else if (viewType == TYPE_GRID_LAYOUT) {
            View view = mLayoutInflater.inflate(R.layout.list_item_grid_layout, parent, false);
            MyGridViewHolder myGridViewHolder = new MyGridViewHolder(view);
            Log.d(TAG, "onCreateViewHolder: viewType == TYPE_GRID_LAYOUT");
            return myGridViewHolder;

        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        }
        Log.d(TAG, "onBindViewHolder: holder = "+holder);
        if (holder instanceof MyViewHolder) {
            Log.d(TAG, "onBindViewHolder: holder instanceof MyViewHolder");
            bindMyViewHolder((MyViewHolder) holder, position);
        } else if (holder instanceof MyGridViewHolder) {
            Log.d(TAG, "onBindViewHolder: holder instanceof MyGridViewHolder");
            bindGridViewHolder((MyGridViewHolder) holder, position);

        }
    }


    private void bindMyViewHolder(MyViewHolder holder, int position) {
        Notes note = mList.get(position);
        String parsedNoteContent = TextParse.parsePlainTextFromHTML(note.getContents());

        holder.mTvTitle.setText(note.getTitle());
        holder.mTvContent.setText(parsedNoteContent);
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

      /*  holder.rlContainer.setOnLongClickListener(new View.OnLongClickListener() {
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
        });*/
    }




    /**
     * 绑定网格布局 ViewHolder
     * @param holder
     * @param position
     */

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

     /*   holder.rlContainer.setOnLongClickListener(new View.OnLongClickListener() {
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
*/
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public int getViewType() {
        return viewType;
    }

    /**
     * @param
     * @return
     * @method removeData
     * @description 删除数据库中对应id的条目
     * @date: 2021/9/10 19:51
     * @author: wangxianwen
     */
    private void removeData(int position) {
        this.mList.remove(position);
        notifyItemRemoved(position);

    }


    /**
     * @param
     * @return
     * @method refreshDate
     * @description 刷新数据
     * @date: 2021/9/10 20:33
     * @author: wangxianwen
     */
    public void refreshDate(List<Notes> notes) {
        this.mList = notes;
        notifyDataSetChanged();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

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

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }


    }

    public class MyGridViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder{
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

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);

        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

}
