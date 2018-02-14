package com.merlin.bright.cory.scorecard.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.merlin.bright.cory.scorecard.R;
import com.merlin.bright.cory.scorecard.gameObjects.Game;
import com.merlin.bright.cory.scorecard.ui.MainActivity;
import com.merlin.bright.cory.scorecard.ui.PlayGameActivity;
import com.merlin.bright.cory.scorecard.ui.PlayTeamGameActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cory on 12/6/17.
 */

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.ViewHolder> {
    private ArrayList<Game> mGames = new ArrayList<>();
    private Context mContext;

    public GamesAdapter(Context context, ArrayList<Game> games) {
        mGames = games;
        mContext = context;
    }

    @Override
    public GamesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GamesAdapter.ViewHolder holder, int position) {
        holder.mGameName.setText(mGames.get(position).getGameName());
        holder.mGameName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                final EditText input = new EditText(mContext);
                builder.setView(input);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = String.valueOf(input.getText());
                        mGames.get(holder.getAdapterPosition()).setGameName(name);
                        holder.mGameName.setText(name);
                        MainActivity.mGameViewModel.updateGame(mGames.get(holder.getAdapterPosition()));
                    }
                });

                builder.setTitle("Enter Name of Game");
                AlertDialog dialog = builder.create();

                dialog.show();
            }
        });
        holder.mWinnerName.setText(mGames.get(position).getWinner());
        holder.mScoreOfWinner.setText(mGames.get(position).getWinningScore() + "");
        holder.mPlayGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (mGames.get(holder.getAdapterPosition()).isTeam()) {
                    intent = new Intent(mContext, PlayTeamGameActivity.class);
                }else {
                    intent = new Intent(mContext, PlayGameActivity.class);
                }
                intent.putExtra(MainActivity.NEW_GAME_INDEX, holder.getAdapterPosition());
                mContext.startActivity(intent);
            }
        });
        holder.mPlayGameButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                deleteGame(holder);
                return true;
            }
        });
        holder.mGameName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                deleteGame(holder);
                return true;
            }
        });
    }

    private void deleteGame(final ViewHolder holder) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .create();
        alertDialog.setTitle("Deleting game");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete Game",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.mGameViewModel.deleteGame(
                                mGames.get(holder.getAdapterPosition()));
                    }
                });
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return mGames.size();
    }

    public void setGames(List<Game> games) {
        mGames = (ArrayList<Game>) games;
        MainActivity.mGames = mGames;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mGameName;
        TextView mWinnerName;
        TextView mScoreOfWinner;
        Button mPlayGameButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mGameName = itemView.findViewById(R.id.gameNameTextView);
            mWinnerName = itemView.findViewById(R.id.winnerTextView);
            mScoreOfWinner = itemView.findViewById(R.id.scoreTextView);
            mPlayGameButton = itemView.findViewById(R.id.game_button);
        }
    }
}
