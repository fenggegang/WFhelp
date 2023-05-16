package com.fggang.sendmsg;

import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.message.data.Message;

public class SendFriend {

    public static void sendMsg(Friend e , String s) {
        e.sendMessage(s);
    }

    public static void sendMsg(Friend e , Message s) {
        e.sendMessage(s);
    }

}
