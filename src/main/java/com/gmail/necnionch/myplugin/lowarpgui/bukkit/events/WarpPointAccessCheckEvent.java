package com.gmail.necnionch.myplugin.lowarpgui.bukkit.events;

import com.gmail.necnionch.myplugin.lowarpgui.bukkit.warp.WarpPoint;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WarpPointAccessCheckEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final WarpPoint point;
    private final Player player;
    private Event.Result result = Result.DEFAULT;
    private @Nullable WarpPoint.AccessDenied reason;

    public WarpPointAccessCheckEvent(WarpPoint point, Player player) {
        this.point = point;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public WarpPoint getPoint() {
        return point;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public @Nullable WarpPoint.AccessDenied getReason() {
        return reason;
    }

    public void setReason(@Nullable WarpPoint.AccessDenied reason) {
        this.reason = reason;
        this.result = Result.DENY;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
