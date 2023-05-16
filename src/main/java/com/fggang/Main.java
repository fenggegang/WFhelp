package com.fggang;


import com.fggang.market.ItemOrder;
import com.fggang.receivemsg.ReceiveFriend;
import com.fggang.sendmsg.SendFriend;
import com.fggang.sendmsg.SendGroup;
import com.fggang.worldstate.CetusCycle;
import com.fggang.worldstate.VoidTrader;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import static com.fggang.datasave.items.ItemList.itemRead;


public class Main extends SimpleListenerHost {
    @EventHandler
    private ListeningStatus onEvent(GroupMessageEvent e) {
        String msg = e.getMessage().contentToString().toLowerCase();
        if(msg.equals("#平原") ) {
            SendGroup.SendMsg(e.getGroup() , new CetusCycle().getcetusCycle());

            return ListeningStatus.LISTENING;
        }



        if(msg.indexOf("#wm") == 0) {
            SendGroup.SendMsg(e.getGroup() , ItemOrder.getItemOrder(msg.replaceAll("#wm" , "")));
            return ListeningStatus.LISTENING;
        }

        if(e.getSender().getId() == 1993949755L && msg.equals("更新物品库")) {
            itemRead();
            e.getGroup().sendMessage("更新成功");
            return ListeningStatus.LISTENING;
        }



        return ListeningStatus.LISTENING;
    }


    @EventHandler
    private ListeningStatus onEvent(FriendMessageEvent e) {
        String msg = e.getMessage().contentToString().toLowerCase();

        if(msg.indexOf("#wm") == 0) {
            e.getSender().sendMessage(ItemOrder.getItemOrder(msg.replaceAll("#wm" , "")));
            return ListeningStatus.LISTENING;
        }

        if(e.getSender().getId() == 1993949755L && msg.equals("更新物品库")) {
            itemRead();
            e.getSender().sendMessage("更新成功");
            return ListeningStatus.LISTENING;
        }

        if( msg.indexOf("更新菜单\n") == 0) {
            if (new ReceiveFriend().updateMenu(msg)) {
                SendFriend.sendMsg(e.getFriend() , "更新成功");
            } else {
                SendFriend.sendMsg(e.getFriend() , "更新失败");
            }
        }

        return ListeningStatus.LISTENING;
    }
}
